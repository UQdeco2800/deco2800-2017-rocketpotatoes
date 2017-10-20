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


class GetExportFile(Operator, ExportHelper):
    # important since its how bpy.ops.import_test.some_data is constructed
    bl_idname = "filename.export"
    bl_label = "Choose Export Base-name"

    # ExportHelper mixin class uses this
    filename_ext = ""

    filter_glob = StringProperty(
            default="*",
            options={'HIDDEN'},
            maxlen=255,  # Max internal buffer length, longer would be clamped.
            )

    def execute(self, context):
        bpy.context.scene['export_name'] = self.properties.filepath
        return {'FINISHED'}

bpy.utils.register_class(GetExportFile)
bpy.ops.filename.export('INVOKE_DEFAULT')

