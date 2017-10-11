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
    scale: f32,
    flip_x: bool,
    flip_y: bool,
}

impl RenderObject {
    pub fn new(name: String, x: i32, y: i32, rotation: f32, scale: f32, flip_x: bool, flip_y: bool) -> Self {
        Self {
            asset: to_ptr(name),
            x: x,
            y: y,
            rotation: rotation,
            scale: scale,
            flip_x: flip_y,
            flip_y: flip_x,
        }
    }
}

#[derive(Debug)]
#[repr(C)]
pub struct RenderInfo {
    pub size_x: i32,
    pub size_y: i32,
}

