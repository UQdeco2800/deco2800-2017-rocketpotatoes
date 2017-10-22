import bpy

from math import pi
from math import sqrt

from sys import argv
import os.path

from bpy_extras.object_utils import world_to_camera_view

try:
    import tkinter
    from tkinter import filedialog
    tkinter.Tk().withdraw()
    TKINTER = True
except ImportError:
    TKINTER = False

SCENE = bpy.context.scene # the actual scene itself
RENDER = SCENE.render # The data object associated with rendering


def get_arg_index():
    return argv.index("--") if "--" in argv else None


def get_arg(number):
    arg_index = get_arg_index()
    if arg_index and (len(argv) - arg_index) > number:
        return argv[arg_index + number]
    else:
        return None


def get_distance(object1, object2):
    """Get the distance between two objects"""
    x_dist = (object1.location[0] - object2.location[0])
    y_dist = (object1.location[1] - object2.location[1])
    z_dist = (object1.location[2] - object2.location[2])
    return sqrt(x_dist ** 2 + y_dist ** 2 + z_dist ** 2)


def get_output_name(arg_num):
    output = get_arg(arg_num)
    if (not output) and TKINTER:
       output = filedialog.asksaveasfilename(filetypes=(("PNG image", "*.png"),
           ("All Files", ".*")), title="Select the sprite base name")

    if not output:
        if 'export_name' in SCENE:
            output = SCENE['export_name']
        else:
            output = "blender-output/model-out"

    return os.path.splitext(output)[0]

def deselect_all():
    for obj in bpy.data.objects:
        obj.select = False


def centre_origin():
    bpy.ops.object.origin_set(type='ORIGIN_CENTER_OF_VOLUME', center='BOUNDS')


def centre_model(model):
    '''centre the model & its origin'''
    centre_origin()
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

def fit_model_in_frame(model_string, intervals):
    """ Arrange the camera such that the model stays in fram as it rotates"""
    # duplicate a model for all possible model positions
    for i in range(0, intervals):
        bpy.ops.object.duplicate()
        bpy.context.object.rotation_euler[2] = 2 * pi * i / intervals

    # align camara to fit all models
    bpy.ops.object.select_pattern(pattern=model_string + ".*")
    bpy.ops.view3d.camera_to_view_selected()
    bpy.ops.object.delete()


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


def setup_light(light, ambient_occlusion=False, shadows=True):
    # add generic lighting across the whole scene
    SCENE.world.light_settings.use_environment_light = True
    bpy.context.scene.world.light_settings.environment_energy = 0.8

    if not shadows:
        light.data.shadow_method = 'NOSHADOW'

    if ambient_occlusion:
        # Ambient occlusion means concave corners are darker than other parts
        bpy.context.scene.world.light_settings.use_ambient_occlusion = True
        bpy.context.scene.world.light_settings.ao_blend_type = 'MULTIPLY'

    # sun type lights aren't effected by distance
    light.data.type = "SUN"
    light.data.energy = 1.3


print("loaded batcher_common")
