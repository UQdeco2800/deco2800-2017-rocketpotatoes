use std::ffi::{CStr, CString};
use std::mem;
use std::os::raw::c_char;
use std::str;
use std::time::{Instant};

/// Non windows callback
#[cfg(not(windows))]
pub struct Callback<T> {
    pub function: extern "C" fn(T),
}

/// Windows snowflake callback
#[cfg(windows)]
pub struct Callback<T> {
    pub function: extern "stdcall" fn(T),
}

/// Non windows callback
#[cfg(not(windows))]
pub struct VoidCallback {
    pub function: extern "C" fn(),
}

/// Windows snowflake callback
#[cfg(windows)]
pub struct VoidCallback {
    pub function: extern "stdcall" fn(),
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

impl VoidCallback {
    /// Calls the internal function
    pub fn call(&self) {
        (self.function)();
    }
}

/// Converts native string to rust string
pub fn to_string(pointer: *const c_char) -> String {
    let slice = unsafe { CStr::from_ptr(pointer).to_bytes() };
    str::from_utf8(slice).unwrap().to_string()
}

/// Converts rust string to native string
pub fn to_ptr(string: String) -> *const c_char {
    let cs = CString::new(string.as_bytes()).unwrap();
    let ptr = cs.as_ptr();
    // Don't destroy our string while we still have pointer
    mem::forget(cs);

    ptr
}
