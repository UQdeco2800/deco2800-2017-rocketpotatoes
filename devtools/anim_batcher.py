'''
This script takes an animated blender file and renders each frame of animation
across the cardinal and ordinal compas points.
ordinal compass points.

See https://github.com/UQdeco2800/deco2800-2017-rocketpotatoes/wiki/Converting-3D-models-to-Sprites
for a detailed explanation on its use.
'''
from math import pi

from sys import path as syspath
import os.path

import bpy
from bpy_extras.object_utils import world_to_camera_view

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


def render_intervals(model, intervals):
    '''
    Rotate around the model, rendering a number of angles as specified by
    intervals
    '''
    output_name = get_output_name()
    for frame in range(SCENE.frame_start, SCENE.frame_end):
        bpy.context.scene.frame_current = frame
        bpy.context.scene.frame_set(frame)

        for i in range(0, intervals):
            model.rotation_euler[2] = 2 * pi * i / intervals
            RENDER.filepath = output_name + "P%02dF%02d.png" % (i, frame)
            bpy.ops.render.render(write_still=True)


def render_compass_points(model, output_name=get_output_name()):
    '''
    Rotate around the model, rendering a number of angles as specified by
    len(direction)
    '''
    direction = ['_S_', '_SE_', '_E_', '_NE_', '_N_', '_NW_', '_W_', '_SW_']

    for frame in range(SCENE.frame_start, SCENE.frame_end):
        bpy.context.scene.frame_current = frame
        bpy.context.scene.frame_set(frame)

        for i in range(0, len(direction)):
            model.rotation_euler[2] = 2 * pi * i / len(direction)
            RENDER.filepath = output_name + direction[i] + "%d.png" % frame
            bpy.ops.render.render(write_still=True)


def main():
    camera = OBJECTS["Camera"]
    light = OBJECTS["Lamp"]

    model = OBJECTS["Model"]
    model.select = True

    bpy.context.scene.frame_current = 0
    bpy.context.scene.frame_set(0)

    centre_origin()
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
        try:
            bpy.context.active_object.keyframe_delete('rotation_euler', frame=i)
        except RuntimeError:
            pass

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
    SCENE.objects.active = merged = selected.pop()
    bpy.ops.object.join()

    merged.name = "Merged"
    merged.select = True
    SCENE.objects.active = merged

    fit_model_in_frame("Merged", INTERVALS)

    # delete the animation merge
    for obj in bpy.data.objects:
        obj.select = False

    merged.select = True
    for x in OBJECTS:
        if "Model." in x.name:
            x.select = True
    bpy.ops.object.delete()

    render_compass_points(model)

main()
