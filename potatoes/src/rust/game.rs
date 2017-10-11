use render::{RenderInfo}; 
use util::CallbackFunctions;

/// GameState machine!
///
/// Start - press space to start playing
/// Falling - line is falling (player is not holding space)
/// Reeling - line is reeling up (player is holding space)
/// Caught(i32) - something is caught, reel it in (the caught index is stored with in)
enum GameState {
    Start,
    Falling,
    Reeling,
    Caught(i32),
}

/// Different types of things you can fish out. Have different behaviour and look
enum FishableType {
    Turbofish,
    Rustcrab,
}

/// Represents a single object that can be fished out of the water
struct Fishable {
    position: (i32, i32),
    size: (i32, i32),
    velocity: (i32, i32),
    category: FishableType,
}


pub struct Game {
    state: GameState,
    line_depth: i32,
    fall_rate: i32,
    fishables: Vec<Fishable>,
}

impl Game {
    pub fn new() -> Self {
        Self {
            state: GameState::Start, 
            line_depth: 0,
            fall_rate: 0,
            fishables: Vec::new(),
        }
    }

    /// Change state to playing
    fn start_falling(&mut self) {
        self.state = GameState::Falling;
        self.fall_rate = 5;
    }

    /// Switches to the reeling state
    fn start_reeling(&mut self) {
        self.state = GameState::Reeling;
        self.fall_rate = -5;
    }

    /// Switches to the caught state with the given fishable index
    fn start_caught(&mut self, index: i32) {
        self.state = GameState::Caught(index);
        self.fall_rate = -5;
    }

    /// Updates the position the line's depth
    fn update_depth(&mut self, delta_time: f64) {
        self.line_depth = (((self.line_depth + self.fall_rate) as f64) * delta_time) as i32;
    }

    /// Updates the position of all the fishables (and spawns new ones if required)
    fn update_fishables(&mut self, delta_time: f64) {

    }

    /// Return's an index of a fishable if the line is colliding with it
    fn check_collisions(&self) -> Option<i32> {
        None
    }

    pub fn update(&mut self, delta_time: f64, callbacks: &CallbackFunctions) {

        // Always update our fishables!
        self.update_fishables(delta_time);
        match self.state {
            GameState::Start => {
            },

            GameState::Falling => {
                self.update_depth(delta_time);

                match self.check_collisions() {
                    Some(index) => {
                        self.start_caught(index);
                    },
                    None => { },
                }

                if (callbacks.is_space_pressed)() {
                    self.start_reeling();
                }
            },

            GameState::Reeling => {
                self.update_depth(delta_time);

                match self.check_collisions() {
                    Some(index) => {
                        self.start_caught(index);
                    },
                    None => { },
                }

                if !(callbacks.is_space_pressed)() {
                    self.start_falling();
                }
            },

            GameState::Caught(index) => {
                self.update_depth(delta_time);

                // Check depth <= 0 TODO
            },
        }
    }

    pub fn draw(&self, delta_time: f64, window_info: &RenderInfo, callbacks: &CallbackFunctions) {
        /*
        let elapsed = timer.elapsed();
        let real_time = f64::sin(elapsed.as_secs() as f64 + elapsed.subsec_nanos() as f64 * 1e-9);
        let width = window_info.size_x as f64;
        let height = window_info.size_y as f64;
        let x = width / 2.0
            * (1.0 + real_time);
        let y = height as f64 / 2.0;
        */

        //(functions.draw_sprite)(RenderObject::new("rustyfish_test".to_string(), 0 as i32, 0 as i32, 0));

    }
}
