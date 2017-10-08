use std::ffi::{CStr, CString};
use std::mem;
use std::os::raw::c_char;
use std::str;
use std::time::{Instant};

use util::*;


#[repr(C)]
pub struct RenderObject {
    asset: *const c_char,
    x: i32,
    y: i32,
    rotation: f32,
}

impl RenderObject {
    pub fn new(name: String, x: i32, y: i32, rotation: f32) -> Self {
        Self {
            asset: to_ptr(name),
            x: x,
            y: y,
            rotation: rotation,
        }
    }
}

#[derive(Debug)]
#[repr(C)]
pub struct RenderInfo {
    pub size_x: i32,
    pub size_y: i32,
}

pub struct RenderFunctions {
    pub start_draw: extern "C" fn(),
    pub end_draw: extern "C" fn(),
    pub update_window: extern "C" fn(),
    pub clear_window: extern "C" fn(),
    pub flush_window: extern "C" fn(),
    pub get_window_info: extern "C" fn(&RenderInfo),
    pub draw_sprite: extern "C" fn(RenderObject),
}

