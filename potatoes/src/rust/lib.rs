#![crate_type = "dylib"]
mod render;
mod util;
mod game;

use std::ffi::{CStr, CString};
use std::mem;
use std::os::raw::c_char;
use std::str;
use std::time::{Instant};
use render::{RenderInfo, RenderLine, RenderObject};
use util::CallbackFunctions;
use game::Game;

// FFI entry point to start the game.
#[no_mangle]
#[allow(non_snake_case)]
pub extern fn startGame(
    startDraw: extern "C" fn(),
    endDraw: extern "C" fn(), 
    updateWindow: extern "C" fn(),
    isSpacePressed: extern "C" fn() -> bool,
    clearWindow: extern "C" fn(),
    flushWindow: extern "C" fn(),
    getWindowInfo: extern "C" fn(&RenderInfo), 
    drawSprite: extern "C" fn(RenderObject),
    drawLine: extern "C" fn(RenderLine)) {

    run_game(CallbackFunctions { 
        start_draw: startDraw,
        end_draw: endDraw,
        update_window: updateWindow,
        is_space_pressed: isSpacePressed,
        clear_window: clearWindow,
        flush_window: flushWindow,
        get_window_info: getWindowInfo,
        draw_sprite: drawSprite,
        draw_line: drawLine});
}

/// Takes some (TODO) callbacks for rendering purposes
/// since the Rust library is not aware of the OpenGL context and it's rather hard
/// to figure out how to do that. As a result we render to a headless opengl context
/// and send back the frame to the java side to be drawn to the primary screen.
pub fn run_game(functions: CallbackFunctions) {

    let window_info = RenderInfo { size_x: 0, size_y: 0 };
    let timer = Instant::now();
    let mut game = Game::new();
    let mut prev_time: f64 = 0.0;

    loop {
        let elapsed = timer.elapsed();
        let real_time = f64::sin(elapsed.as_secs() as f64 + elapsed.subsec_nanos() as f64 * 1e-9);
        let delta = real_time - prev_time;
        prev_time = real_time;

        // Update state TODO split callbacks into categories?
        game.update(delta, &functions);

        // Update window state (e.g. get resize events)
        (functions.update_window)();
        // Get window statistics (size etc)
        (functions.get_window_info)(&window_info);
        // Clear window with default background color
        (functions.clear_window)();

        game.draw(delta, &window_info, &functions);

        // Flush any render changes etc.
        (functions.flush_window)();

        std::thread::sleep(std::time::Duration::from_millis(16));
    }
}
