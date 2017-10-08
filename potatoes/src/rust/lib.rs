#![crate_type = "dylib"]
mod render;
mod util;

use std::ffi::{CStr, CString};
use std::mem;
use std::os::raw::c_char;
use std::str;
use std::time::{Instant};
use render::{RenderFunctions, RenderInfo, RenderObject};

// FFI entry point to start the game.
#[no_mangle]
#[allow(non_snake_case)]
pub extern fn startGame(
    startDraw: extern "C" fn(),
    endDraw: extern "C" fn(), 
    updateWindow: extern "C" fn(), 
    clearWindow: extern "C" fn(),
    flushWindow: extern "C" fn(),
    getWindowInfo: extern "C" fn(&RenderInfo), 
    drawSprite: extern "C" fn(RenderObject)) {

    run_game(RenderFunctions { 
        start_draw: startDraw,
        end_draw: endDraw,
        update_window: updateWindow,
        clear_window: clearWindow,
        flush_window: flushWindow,
        get_window_info: getWindowInfo,
        draw_sprite: drawSprite });
}

/// Takes some (TODO) callbacks for rendering purposes
/// since the Rust library is not aware of the OpenGL context and it's rather hard
/// to figure out how to do that. As a result we render to a headless opengl context
/// and send back the frame to the java side to be drawn to the primary screen.
pub fn run_game(functions: RenderFunctions){

    let window_info = RenderInfo { size_x: 0, size_y: 0 };
    let timer = Instant::now();
    loop {

        // Get input/process resize events etc.
        (functions.update_window)();

        // Get window statistics (size etc)
        (functions.get_window_info)(&window_info);

        // Clear window with default background color
        (functions.clear_window)();

        (functions.start_draw)();

        let elapsed = timer.elapsed();
        let width = window_info.size_x as f64;
        let height = window_info.size_y as f64;
        let x = width / 2.0
            * (1.0 + f64::sin(elapsed.as_secs() as f64 +
                              elapsed.subsec_nanos() as f64 * 1e-9));
        let y = height as f64 / 2.0;

        (functions.draw_sprite)(RenderObject::new("potate".to_string(), x as i32, y as i32, 0.0));

        (functions.end_draw)();

        // Flush any render changes etc.
        (functions.flush_window)();

        std::thread::sleep(std::time::Duration::from_millis(16));
    }
}

    /*
    let timer = Instant::now();
    let width = 100.0;
    let height = 100.0;
    let elapsed = timer.elapsed();
    let x = (context.get_view_size()[0] as f64 - width) / 2.0
        * (1.0 + f64::sin(elapsed.as_secs() as f64 +
                          elapsed.subsec_nanos() as f64 * 1e-9));
    let y = context.get_view_size()[1] as f64 / 2.0;
    */
