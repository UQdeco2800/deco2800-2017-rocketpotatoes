use std::os::raw::c_char;

use util::*;

#[repr(C)]
pub struct RenderRectangle {
    x: i32,
    y: i32,
    w: i32,
    h: i32,
    color: i32,
    alpha: f32,
}

impl RenderRectangle {
    pub fn new(p: (i32, i32), size: (i32, i32), color: i32, alpha: f32) -> Self {
        Self {
            x: p.0,
            y: p.1,
            w: size.0,
            h: size.1,
            color: color,
            alpha: alpha,
        }
    }
}

#[repr(C)]
pub struct RenderLine {
    src_x: i32,
    src_y: i32,
    dst_x: i32,
    dst_y: i32,
}

impl RenderLine {
    pub fn new(src: (i32, i32), dst: (i32, i32)) -> Self {
        Self {
            src_x: src.0,
            src_y: src.1,
            dst_x: dst.0,
            dst_y: dst.1,
        }
    }
}

/// Object representing something to render
///
/// Note: booleans are treated as i32 since rust bool != java bool (I think?)
#[repr(C)]
pub struct RenderObject {
    asset: *const c_char,
    x: i32,
    y: i32,
    rotation: f32,
    scale: f32,
    flip_x: i32,
    flip_y: i32,
    col: i32,
}

impl RenderObject {
    pub fn new(
        name: String,
        x: i32,
        y: i32,
        rotation: f32,
        scale: f32,
        flip_x: bool,
        flip_y: bool,
        col: i32,
    ) -> Self {
        Self {
            asset: to_ptr(name),
            x: x,
            y: y,
            rotation: rotation,
            scale: scale,
            flip_x: flip_x as i32,
            flip_y: flip_y as i32,
            col: col,
        }
    }
}

#[derive(Debug)]
#[repr(C)]
pub struct RenderInfo {
    pub size_x: i32,
    pub size_y: i32,
}
