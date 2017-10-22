use std::ffi::CString;
use std::mem;
use std::os::raw::c_char;
use render::{RenderInfo, RenderLine, RenderRectangle, RenderObject};

pub struct CallbackFunctions {
    pub start_draw: extern "C" fn(),
    pub end_draw: extern "C" fn(),
    pub update_window: extern "C" fn(),
    pub is_space_pressed: extern "C" fn() -> bool,
    pub is_cheat_key_pressed: extern "C" fn() -> u32,
    pub clear_window: extern "C" fn(),
    pub flush_window: extern "C" fn(),
    pub get_window_info: extern "C" fn(&RenderInfo),
    pub draw_sprite: extern "C" fn(RenderObject),
    pub draw_line: extern "C" fn(RenderLine),
    pub draw_rectangle: extern "C" fn(RenderRectangle),
}


/// Converts rust string to native string
pub fn to_ptr(string: String) -> *const c_char {
    let cs = CString::new(string.as_bytes()).unwrap();
    let ptr = cs.as_ptr();
    // Don't destroy our string while we still have pointer
    mem::forget(cs);

    ptr
}
