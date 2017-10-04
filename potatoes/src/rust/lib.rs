#![crate_type = "dylib"]

use std::ffi::{CStr, CString};
use std::str;
use std::mem;
use std::os::raw::c_char;

#[no_mangle]
pub extern fn print_stuff(stuff: *const c_char) {
    let stuff = to_string(stuff);
    println!("{}", stuff);
}

fn to_string(pointer: *const c_char) -> String {
    let slice = unsafe { CStr::from_ptr(pointer).to_bytes() };
    str::from_utf8(slice).unwrap().to_string()
}
