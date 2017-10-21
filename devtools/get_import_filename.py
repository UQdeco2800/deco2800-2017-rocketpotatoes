'''
This script was originally from the blender templates, including all comments,
but has been modified to suit purpose
'''
import bpy 

# ExportHelper is a helper class, defines filename and
# invoke() function which calls the file selector.
from bpy_extras.io_utils import ExportHelper, ImportHelper
from bpy.props import StringProperty
from bpy.types import Operator


class GetImportFile(Operator, ImportHelper):
    # important since its how bpy.ops.import_test.some_data is constructed
    bl_idname = "filename.importfile"
    bl_label = "Choose 3DS file"

    # ImportHelper mixin class uses this
    filename_ext = ".3ds"

    filter_glob = StringProperty(
            default="*",
            options={'HIDDEN'},
            maxlen=255,  # Max internal buffer length, longer would be clamped.
            )

    def execute(self, context):
        bpy.context.scene['import_name'] = self.properties.filepath
        return {'FINISHED'}

bpy.utils.register_class(GetImportFile)
bpy.ops.filename.importfile('INVOKE_DEFAULT')
