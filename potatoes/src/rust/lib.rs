#![crate_type = "dylib"]

extern crate piston_window;
use piston_window::{PistonWindow, WindowSettings};

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

/// Runs the game (Linux/MacOS specific)
#[no_mangle]
#[cfg(not(windows))]
#[allow(non_snake_case)]
pub extern fn startGame(render: extern "C" fn(*const c_char)) {
    println!("Running game Linux");
    run_game(Callback { function: render });
    println!("Game finished");
}

/// Runs the game (Linux/MacOS specific)
#[no_mangle]
#[cfg(windows)]
#[allow(non_snake_case)]
pub extern fn startGame(render: extern "stdcall" fn(*const c_char)) {
    println!("Running game Win");
    run_game(Callback { function: render });
    println!("Game finished");
}

/// Takes some (TODO) callbacks for rendering purposes
/// since the Rust library is not aware of the OpenGL context and it's rather hard
/// to figure out how to do that. As a result we render to a headless opengl context
/// and send back the frame to the java side to be drawn to the primary screen.
pub fn run_game(render: Callback<*const c_char>) {
    render.call(to_ptr("Hey java".to_string()));

    let mut window: PistonWindow = WindowSettings::new("Rustyfish", [1024, 784])
        .exit_on_esc(true)
        .build()
        .unwrap();


    let timer = Instant::now();
    while let Some(event) = window.next() {
        window.draw_2d(&event, |context, graphics| {

            piston_window::clear([1.0; 4], graphics);

            let width = 100.0;
            let height = 100.0;
            let elapsed = timer.elapsed();
            let x = (context.get_view_size()[0] as f64 - width) / 2.0
                * (1.0 + f64::sin(elapsed.as_secs() as f64 +
                           elapsed.subsec_nanos() as f64 * 1e-9));
            let y = context.get_view_size()[1] as f64 / 2.0;
            piston_window::rectangle(
                [1.0, 0.0, 0.0, 1.0],
                [x, y, width, height],
                context.transform,
                graphics);
        });
    }
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
