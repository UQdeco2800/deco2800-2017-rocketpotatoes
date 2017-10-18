'''
This script should be called from within blender, or from the command line
like so:
    blender -b -P sprit_batcher.py -- input_model.3ds output

Note that 'output' doesn't have a .png or anything on the end, that's
handled within the script.

If you're using a POSIX machine (e.g Mac, Linux) you can also call the helper
script '3ds_to_sheet' to automatically combine all the sprites onto one sheet.
A python script to achieve the same might be put together in the future.
'''

from math import pi
from math import sqrt

from sys import argv
from os import path
import ntpath

try:
    import tkinter
    from tkinter import filedialog
    tkinter.Tk().withdraw()
    TKINTER = True
except ImportError:
    TKINTER = False

import bpy
from bpy_extras.object_utils import world_to_camera_view

# Options
INTERVALS = 8 # The number of angles around the model to render

# You'll probably want to leave these blank and just use the provided file
# choosers. These are only here for legacy purposes.
MODEL_FILE = '' # The 3D model's file
OUTPUT_DIR = '' # Where the resulting sprites will be put

# Ambient occlusion means concave corners are darker than other parts
AMBIENT_OCCLUSION = False
SHADOWS = True

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

    model_file = get_arg(1) or MODEL_FILE \
            or filedialog.askopenfilename(filetypes=(("3DS file", "*.3ds"),
                ("All Files", ".*")), title="Select the 3D model to import")

    # import the given model file, if specified on the command line
    if OBJECTS.get("Model"):
        model = OBJECTS["Model"]
        model.select = True

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
    output = get_arg(2) \
            or filedialog.asksaveasfilename(filetypes=(("PNG image", "*.png"),
                ("All Files", ".*")), title="Select the sprite base name") \
            or "blender-output/model-out"

    return path.splitext(output)[0]

def centre_model(model):
    '''centre the model & its origin'''
    bpy.ops.object.origin_set(type='ORIGIN_CENTER_OF_VOLUME', center='BOUNDS')
    model.location = [0, 0, 0]
    model.rotation_euler = [0, 0, 0]


def setup_camera(camera):
    """prepare the camera for batch rendering"""
    camera.location = [0, 10, 0]
    camera.rotation_euler = [2 * pi / 6, 0, pi]

    # make sure the camera is orthographic for an isometric appearance
    camera.data.type = 'ORTHO'

    # set the render resolution
    RENDER.resolution_x = 512
    RENDER.resolution_y = 512
    RENDER.alpha_mode = 'TRANSPARENT'


def setup_light(light):
    # add generic lighting across the whole scene
    SCENE.world.light_settings.use_environment_light = True
    bpy.context.scene.world.light_settings.environment_energy = 0.8


    if not SHADOWS:
        light.data.shadow_method = 'NOSHADOW'

    if AMBIENT_OCCLUSION:
        # Ambient occlusion means concave corners are darker than other parts
        bpy.context.scene.world.light_settings.use_ambient_occlusion = True
        bpy.context.scene.world.light_settings.ao_blend_type = 'MULTIPLY'

    # sun type lights aren't effected by distance
    light.data.type = "SUN"
    light.data.energy = 1.3

def fit_model_in_frame(model_string):
    """ Arrange the camera such that the model stays in fram as it rotates"""
    # duplicate a model for all possible model positions
    for i in range(0, INTERVALS):
        bpy.ops.object.duplicate()
        bpy.context.object.rotation_euler[2] = 2 * pi * i / INTERVALS

    # align camara to fit all models
    bpy.ops.object.select_pattern(pattern=model_string + ".*")
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
    output_name = get_output_name()
    for i in range(0, INTERVALS):
        model.rotation_euler[2] = 2 * pi * i / INTERVALS
        RENDER.filepath = output_name + "%03d.png" % i
        bpy.ops.render.render(write_still=True)

def render_animated_batch(model):
    '''
    Rotate around the model, rendering a number of angles as specified by
    INTERVALS
    '''
    output_name = get_output_name()
    for frame in range(SCENE.frame_start, SCENE.frame_end):
        bpy.context.scene.frame_current = frame
        bpy.context.scene.frame_set(frame)

        for i in range(0, INTERVALS):
            model.rotation_euler[2] = 2 * pi * i / INTERVALS
            RENDER.filepath = output_name + "P%02dF%02d.png" % (i, frame)
            bpy.ops.render.render(write_still=True)

def main():
    camera = OBJECTS["Camera"]
    light = OBJECTS["Lamp"]

    model = OBJECTS["Model"]
    model.select = True

    bpy.context.scene.frame_current = 0
    bpy.context.scene.frame_set(0)

    centre_model(model)
    setup_camera(camera)
    setup_light(light)

    for obj in bpy.data.objects:
        obj.select = False

    model = OBJECTS["Model"]
    model.select = True
    for x in model.children:
        x.select = True

    SCENE.objects.active = model # required to interact with model's keyframes

    for i in range(SCENE.frame_start, SCENE.frame_end):
        # ensure model can be rotated later by clearing its rotation keyframes
        bpy.context.active_object.keyframe_delete('rotation_euler', frame=i)

    blob = [] # all the models of all the frames, in one big blob

    for i in range(SCENE.frame_start, SCENE.frame_end):
        # move frame along
        bpy.context.scene.frame_current = i
        bpy.context.scene.frame_set(i)



        bpy.ops.object.duplicate()
        duped_model = bpy.context.selected_objects[0]

        # remove frame data from duplicated model
        duped_model.animation_data_clear()
        for x in duped_model.children:
            x.animation_data_clear()

        # reposition new model to match the old model (which is still at the
        # keyframe)
        duped_model.location = model.location
        duped_model.rotation_euler = model.rotation_euler
        for x in range(0, len(model.children)):
            base_child = model.children[x]
            dupe_child = duped_model.children[x]

            blob.append(dupe_child)

            dupe_child.location = base_child.location
            dupe_child.rotation_euler = base_child.rotation_euler

    # merge together
    for x in blob:
        x.select = True

    # merge all the newly imported models into one supermodel
    selected = bpy.context.selected_objects
    SCENE.objects.active = merged =  selected.pop()
    bpy.ops.object.join()

    merged.name = "Merged"
    merged.select = True
    SCENE.objects.active = merged

    fit_model_in_frame("Merged")

    # delete the animation merge
    for obj in bpy.data.objects:
        obj.select = False

    merged.select = True
    for x in OBJECTS:
        if "Model." in x.name:
            x.select = True
    bpy.ops.object.delete()

    render_animated_batch(model)

main()
