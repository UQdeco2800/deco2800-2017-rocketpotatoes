'''
This script takes an unanimated model and captures it across the cardinal and
ordinal compass points.

If you want to export animations, this is the wrong script. Please use
anim_batcher.py.

See https://github.com/UQdeco2800/deco2800-2017-rocketpotatoes/wiki/Converting-3D-models-to-Sprites
for a detailed explanation on its use.
'''

from math import pi

from sys import argv
from sys import path as syspath
import os.path

import bpy

for x in argv:
    if ".py" in x:
        script_dir = os.path.dirname(x)
        break

if not script_dir:
    script_dir = os.path.dirname(bpy.context.space_data.text.filepath)

syspath.append(script_dir)
from batcher_common import *

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

def import_model():
    '''import the model specified in the cli, or use a pre-loaded model'''

    model_file = get_arg(1)
    if (not model_file) and TKINTER:
        model_file = filedialog.askopenfilename(filetypes=(("3DS file", "*.3ds"),
            ("All Files", ".*")), title="Select the 3D model to import")

    if not model_file:
        if 'export_name' in SCENE:
            model_file = MODEL_FILE

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


def render_intervals(model, intervals):
    '''
    Rotate around the model, rendering a number of angles as specified by
    INTERVALS
    '''
    output_name = get_output_name(2)
    for i in range(0, intervals):
        model.rotation_euler[2] = 2 * pi * i / intervals
        RENDER.filepath = output_name + "%03d.png" % i
        bpy.ops.render.render(write_still=True)


def render_compass_points(model):
    '''
    Rotate around the model, rendering a number of angles as specified by
    INTERVALS
    '''
    direction = ['_S', '_SE', '_E', '_NE', '_N', '_NW', '_W', '_SW']

    output_name = get_output_name(2)
    for i in range(0, len(direction)):
        model.rotation_euler[2] = 2 * pi * i / len(direction)
        RENDER.filepath = output_name + direction[i]
        bpy.ops.render.render(write_still=True)


def main():
    camera = OBJECTS["Camera"]
    light = OBJECTS["Lamp"]
    model = import_model()

    centre_model(model)
    setup_camera(camera)
    setup_light(light)
    fit_model_in_frame("Model", INTERVALS)

    render_compass_points(model)

main()
