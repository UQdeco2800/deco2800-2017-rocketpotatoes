#![crate_type = "dylib"]
extern crate rand;

mod render;
mod util;
mod game;
mod keys;

use render::{RenderInfo, RenderLine, RenderRectangle, RenderObject};
use util::CallbackFunctions;
use game::Game;
use keys::{Key, KeyLogger};

// FFI entry point to start the game.
#[no_mangle]
#[allow(non_snake_case)]
pub extern "C" fn startGame(
    startDraw: extern "C" fn(),
    endDraw: extern "C" fn(),
    updateWindow: extern "C" fn(),
    isSpacePressed: extern "C" fn() -> bool,
    isCheatKeyPressed: extern "C" fn() -> u32,
    clearWindow: extern "C" fn(),
    flushWindow: extern "C" fn(),
    getWindowInfo: extern "C" fn(&RenderInfo),
    drawSprite: extern "C" fn(RenderObject),
    drawLine: extern "C" fn(RenderLine),
    drawRectangle: extern "C" fn(RenderRectangle),
) {

    run_game(CallbackFunctions {
        start_draw: startDraw,
        end_draw: endDraw,
        update_window: updateWindow,
        is_space_pressed: isSpacePressed,
        is_cheat_key_pressed: isCheatKeyPressed,
        clear_window: clearWindow,
        flush_window: flushWindow,
        get_window_info: getWindowInfo,
        draw_sprite: drawSprite,
        draw_line: drawLine,
        draw_rectangle: drawRectangle,
    });
}

/// Takes some (TODO) callbacks for rendering purposes
/// since the Rust library is not aware of the OpenGL context and it's rather hard
/// to figure out how to do that. As a result we render to a headless opengl context
/// and send back the frame to the java side to be drawn to the primary screen.
pub fn run_game(functions: CallbackFunctions) {

    let window_info = RenderInfo {
        size_x: 0,
        size_y: 0,
    };
    let mut game = Game::new();
    let mut keys = KeyLogger::new(
        &[
            Key::Up,
            Key::Left,
            Key::Right,
            Key::Down,
            Key::Up,
            Key::Left,
            Key::Right,
            Key::Down,
        ],
    );

    loop {

        // Update state TODO split callbacks into categories?
        game.update(&functions);

        // Update window state (e.g. get resize events)
        (functions.update_window)();
        // Get window statistics (size etc)
        (functions.get_window_info)(&window_info);
        // Clear window with default background color
        (functions.clear_window)();

        game.draw(&window_info, &functions);

        // Flush any render changes etc.
        (functions.flush_window)();

        if keys.key_press((functions.is_cheat_key_pressed)()) {
            break;
        }
        std::thread::sleep(std::time::Duration::from_millis(16));
    }
}
