'''
This script should be called from within blender, or from the command line
like so:
    blender -b -P sprit_batcher.py -- input_model.3ds

Note that 'output name' doesn't have a .png or anything on the end, that's
handled within the script.
All output files are put in a directory 'blender-output' for convenience.

If you're using a POSIX machine (e.g Mac, Linux) you can also call the helper
script '3ds_to_sheet' to automatically combine all the sprites onto one sheet.
A python script to achieve the same might be put together in the future.
'''

from math import pi
from math import sqrt

from sys import argv
from os import path
import ntpath

import bpy
from bpy_extras.object_utils import world_to_camera_view

INTERVALS = 8 # The number of angles around the model to render
MODEL_FILE = '' # The Model file, used if running this through blender

OBJECTS = bpy.data.objects # A list of all the objects in the scene
SCENE = bpy.context.scene # the actual scene itself
RENDER = SCENE.render # The data object associated with rendering



def get_distance(object1, object2):
    """Get the distance between two objects"""
    x_dist = (object1.location[0] - object2.location[0])
    y_dist = (object1.location[1] - object2.location[1])
    z_dist = (object1.location[2] - object2.location[2])
    return sqrt(x_dist ** 2 + y_dist ** 2 + z_dist ** 2)

def get_arg_index():
    return argv.index("--") if "--" in argv else None

def get_arg(number):
    arg_index = get_arg_index()
    if arg_index and (len(argv) - arg_index) > number:
        return argv[arg_index + number]
    else:
        return None

def import_model():
    '''import the model specified in the cli, or use a pre-loaded model'''

    model_file = get_arg(1) or MODEL_FILE

    # import the given model file, if specified on the command line
    if OBJECTS.get("Model"):
        model = OBJECTS["Model"]

    elif model_file:
        bpy.ops.import_scene.autodesk_3ds(filepath=model_file)

        # Get rid of the default cube, if it exists
        if OBJECTS.get("Cube"):
            OBJECTS.remove(OBJECTS.get("Cube"), do_unlink=True)

    # Use whatever models are selected in 3DView
    # Imported files are automatically selected
    selected = bpy.context.selected_objects
    if not len(selected):
        raise Exception("No model is selected in 3DView, or the file import" \
                + " failed")

    # merge all the newly imported models into one supermodel
    SCENE.objects.active = model = selected.pop()
    bpy.ops.object.join()

    model.name = "Model"
    model.select = True
    SCENE.objects.active = model
    return model

def get_output_name():
    if get_arg(2):
        return path.splitext(get_arg(2))[0]
    if get_arg(1):
        return path.splitext(get_arg(1))[0]
    else:
        return "model-out"

def centre_model(model):
    '''centre the model & its origin'''
    bpy.ops.object.origin_set(type='ORIGIN_GEOMETRY', center='BOUNDS')
    model.location = [0, 0, 0]
    model.rotation_euler = [0, 0, 0]


def setup_camera(camera):
    """prepare the camera for batch rendering"""
    camera.location = [0, 10, 0]
    camera.rotation_euler = [2 * pi / 6, 0, pi]

    # make sure the camera is orthographic for an isometric appearance
    camera.data.type = 'ORTHO'

    # make the lighting generically displayed across the whole system
    SCENE.world.light_settings.use_environment_light = True

    # set the render resolution
    RENDER.resolution_x = 512
    RENDER.resolution_y = 512
    RENDER.alpha_mode = 'TRANSPARENT'



def fit_model_in_frame():
    """ Arrange the camera such that the model stays in fram as it rotates"""
    # duplicate a model for all possible model positions
    for i in range(0, INTERVALS):
        bpy.ops.object.duplicate()
        bpy.context.object.rotation_euler[2] = 2 * pi * i / INTERVALS

    # align camara to fit all models
    bpy.ops.object.select_pattern(pattern="Model.0*")
    bpy.ops.view3d.camera_to_view_selected()
    bpy.ops.object.delete()


def fit_bounds_in_frame(camera, model):
    '''
    Adjust the orthographic scale of the camera such that
    all bounding boxes are within the camera's view
    '''
    for coord in model.bound_box:
        cam_coord = world_to_camera_view(SCENE, camera, model.matrix_world *
                                         coord)
        for i in range(0, 2):
            if cam_coord[i] < 0:
                camera.data.ortho_scale *= (-i * 2 + 1)
            elif cam_coord[i] > 1:
                camera.data.ortho_scale *= i


def make_tracking_constraint(camera, model):
    '''constrain the camera to always look at an object'''
    tracker = camera.constraints.get("Batch Camera") \
            or camera.constraints.new(type="TRACK_TO")
    tracker.name = "Batch Camera"
    tracker.track_axis = 'TRACK_NEGATIVE_Z'
    tracker.target = model
    tracker.up_axis = 'UP_Y' # ensure the camera is always facing up
    return tracker


def make_orbit():
    '''create an empty object that the camera orbits around'''
    if not OBJECTS.get("Orbit"):
        orbit = OBJECTS.new("Empty", None)
        SCENE.objects.link(orbit)
        orbit.name = "Orbit"
    else:
        orbit = OBJECTS["Orbit"]

    orbit.location = [0, 0, 5]
    orbit.scale = [10, 10, 1]
    orbit.rotation_euler = [0, 0, 0]
    return orbit


def tie_orbit_to_object(orbit, obj):
    '''Tie the orbit around an object'''
    obj.parent = orbit
    obj.location = [0, 1, 0]


def render_batch(model):
    '''
    Rotate around the model, rendering a number of angles as specified by
    INTERVALS
    '''
    for i in range(0, INTERVALS):
        model.rotation_euler[2] = 2 * pi * i / INTERVALS
        RENDER.filepath = "blender-output/" + get_output_name() \
                + "%03d.png" % i
        bpy.ops.render.render(write_still=True)


def main():
    camera = OBJECTS["Camera"]
    model = import_model()

    centre_model(model)
    setup_camera(camera)
    fit_model_in_frame()

    render_batch(model)

main()
