#![crate_type = "dylib"]

use std::ffi::{CStr, CString};
use std::mem;
use std::os::raw::c_char;
use std::str;
use std::time::{Instant};

/// Non windows callback
#[cfg(not(windows))]
pub struct Callback<T> {
    function: extern "C" fn(T),
}

/// Windows snowflake callback
#[cfg(windows)]
pub struct Callback<T> {
    function: extern "stdcall" fn(T),
}

impl<T> Callback<T> {
    /// Calls the internal function with the parameter
    ///
    /// Would be nice to have some way to nicely convert between
    /// Rust -> Native data structures. I.e. I could pass in a rust string
    /// and it would automagically convert it to a native string
    pub fn call(&self, param: T) {
        (self.function)(param);
    }
}

#[repr(C)]
pub struct RenderObject {
    asset: *const c_char,
}

impl RenderObject {
    fn new(name: String, x: i32, y: i32, rotation: f32) -> Self {
        Self {
            asset: to_ptr(name),
        }
    }
}

impl Drop for RenderObject {
    fn drop(&mut self) { }
}

pub struct RenderFunctions {
    pub start_draw: Callback<()>,
    pub end_draw: Callback<()>,
    pub update_window: Callback<()>,
    pub get_window_info: Callback<()>,
    pub draw_sprite: Callback<()>,
}

/// Runs the game (Linux/MacOS specific)
#[no_mangle]
#[cfg(not(windows))]
#[allow(non_snake_case)]
pub extern fn startGame(
    startDraw: extern "C" fn(()),
    endDraw: extern "C" fn(()), 
    updateWindow: extern "C" fn(()), 
    getWindowInfo: extern "C" fn(()), 
    drawSprite: extern "C" fn(())) {

    println!("Running game Linux");
    run_game(RenderFunctions { 
        start_draw: Callback { function: startDraw },
        end_draw: Callback { function: endDraw },
        update_window: Callback { function: updateWindow },
        get_window_info: Callback { function: getWindowInfo },
        draw_sprite: Callback { function: drawSprite },
    });
    println!("Game finished");
}

/// Runs the game (Windows specific)
#[no_mangle]
#[cfg(windows)]
#[allow(non_snake_case)]
pub extern fn startGame(
    startDraw: extern "stdcall" fn(()),
    endDraw: extern "stdcall" fn(()), 
    updateWindow: extern "stdcall" fn(()), 
    getWindowInfo: extern "stdcall" fn(()), 
    drawSprite: extern "stdcall" fn(())) {

    println!("Running game Linux");
    run_game(RenderFunctions { 
        start_draw: Callback { function: startDraw },
        end_draw: Callback { function: endDraw },
        update_window: Callback { function: updateWindow },
        get_window_info: Callback { function: getWindowInfo },
        draw_sprite: Callback { function: drawSprite },
    });
    println!("Game finished");
}

/// Takes some (TODO) callbacks for rendering purposes
/// since the Rust library is not aware of the OpenGL context and it's rather hard
/// to figure out how to do that. As a result we render to a headless opengl context
/// and send back the frame to the java side to be drawn to the primary screen.
pub fn run_game(render_functions: RenderFunctions) {
    render_functions.start_draw.call(());
    render_functions.end_draw.call(());
    render_functions.update_window.call(());
    render_functions.get_window_info.call(());
    render_functions.draw_sprite.call(());

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
}

/// Converts native string to rust string
fn to_string(pointer: *const c_char) -> String {
    let slice = unsafe { CStr::from_ptr(pointer).to_bytes() };
    str::from_utf8(slice).unwrap().to_string()
}

/// Converts rust string to native string
fn to_ptr(string: String) -> *const c_char {
    let cs = CString::new(string.as_bytes()).unwrap();
    let ptr = cs.as_ptr();
    // Don't destroy our string while we still have pointer
    mem::forget(cs);

    ptr
}
