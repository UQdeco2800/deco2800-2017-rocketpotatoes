#![crate_type = "dylib"]
mod render;
mod callback;

use std::ffi::{CStr, CString};
use std::mem;
use std::os::raw::c_char;
use std::str;
use std::time::{Instant};

use callback::*;
use render::{RenderFunctions, RenderInfo, RenderObject};
/// Runs the game (Linux/MacOS specific)
#[no_mangle]
#[cfg(not(windows))]
#[allow(non_snake_case)]
pub extern fn startGame(
    startDraw: extern "C" fn(),
    endDraw: extern "C" fn(), 
    updateWindow: extern "C" fn(), 
    getWindowInfo: extern "C" fn(&RenderInfo), 
    drawSprite: extern "C" fn(())) {

    run_game(&Callback { function: getWindowInfo} );


    /*
    println!("Running game Linux");
    run_game(RenderFunctions { 
        start_draw: VoidCallback { function: startDraw },
        end_draw: VoidCallback { function: endDraw },
        update_window: VoidCallback { function: updateWindow },
        get_window_info: Callback { function: getWindowInfo },
        draw_sprite: Callback { function: drawSprite },
    });
    println!("Game finished");
    */
}


/// Runs the game (Windows specific)
#[no_mangle]
#[cfg(windows)]
#[allow(non_snake_case)]
pub extern fn startGame(
    startDraw: extern "stdcall" fn(),
    endDraw: extern "stdcall" fn(), 
    updateWindow: extern "stdcall" fn(), 
    getWindowInfo: extern "stdcall" fn(&RenderInfo), 
    drawSprite: extern "stdcall" fn(())) {

    /*
    println!("Running game Linux");
    run_game(RenderFunctions { 
        start_draw: VoidCallback { function: startDraw },
        end_draw: VoidCallback { function: endDraw },
        update_window: VoidCallback { function: updateWindow },
        get_window_info: Callback { function: getWindowInfo },
        draw_sprite: Callback { function: drawSprite },
    });
    println!("Game finished");
    */
}

/// Takes some (TODO) callbacks for rendering purposes
/// since the Rust library is not aware of the OpenGL context and it's rather hard
/// to figure out how to do that. As a result we render to a headless opengl context
/// and send back the frame to the java side to be drawn to the primary screen.
//pub fn run_game(render_functions: RenderFunctions) {
pub fn run_game(get_window_info: &Callback<&RenderInfo>) {
    let x = RenderInfo { size_x: 0, size_y: 0 };
    
    {
        get_window_info.call(&x);
    }

    println!("{:?}", x);
    /*
    let x = RenderInfo { size_x: 0, size_y: 0 };
    
    render_functions.start_draw.call();
    render_functions.end_draw.call();
    render_functions.update_window.call();
    render_functions.get_window_info.call(&x);
    render_functions.draw_sprite.call(());
    */

    //println!("{:?}", x);
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
