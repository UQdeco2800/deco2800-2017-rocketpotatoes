use render::{RenderInfo, RenderObject, Color}; 
use util::CallbackFunctions;

extern crate rand;
use self::rand::distributions::{IndependentSample, Range};

/// GameState machine!
///
/// Start - press space to start playing
/// Falling - line is falling (player is not holding space)
/// Reeling - line is reeling up (player is holding space)
/// Caught(i32) - something is caught, reel it in (the caught index is stored with in)
#[derive(Debug)]
enum GameState {
    Start,
    Falling,
    Reeling,
    Caught(i32),
}

/// Different types of things you can fish out. Have different behaviour and look
#[derive(Debug)]
enum FishableType {
    Turbofish,
    Rustcrab,
}

/// Represents a single object that can be fished out of the water
#[derive(Debug)]
struct Fishable {
    position: (i32, i32),
    size: (i32, i32),
    velocity: (i32, i32),
    category: FishableType,
    color: i32,
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
        // TODO delta
        for f in self.fishables.iter_mut() {
            f.position.0 += f.velocity.0;
            f.position.0 += f.velocity.1;
        }


        // Delete those outside bounds TODO better bounds
        self.fishables.retain(|ref f| (f.position.0 >= -3000) && (f.position.0 <= 4000));


        // TODO fix this gross
        let y_range = Range::new(10, 1000);
        let x_range = Range::new(-1000, 1000);
        let dir_range = Range::new(0, 2);
        let speed_range = Range::new(1, 6);
        let color_range = Range::new(1, 6);
        let mut rng = rand::thread_rng();
        if self.fishables.len() < 50 {
            let velocity: (i32, i32);
            let position: (i32, i32);
            if dir_range.ind_sample(&mut rng) == 0 {
                position = (-1500 + x_range.ind_sample(&mut rng)
                            , y_range.ind_sample(&mut rng));
                velocity = (speed_range.ind_sample(&mut rng), 0);
            }
            else {
                position = (3000 + x_range.ind_sample(&mut rng)
                            , y_range.ind_sample(&mut rng));
                velocity = (-speed_range.ind_sample(&mut rng), 0);
            }
            self.fishables.push(Fishable {
                position: position,
                size: (0, 30),
                velocity: velocity,
                category: FishableType::Turbofish,
                color: color_range.ind_sample(&mut rng),
            });
        }
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

                //self.start_falling();
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
        for f in self.fishables.iter() {
            (callbacks.draw_sprite)(RenderObject::new("turbofish".to_string(), 
                                                      f.position.0, f.position.1, 0.0, 0.25, 
                                                      f.velocity.0 < 0, false, 
                                                      f.color));
        }
    }
}
