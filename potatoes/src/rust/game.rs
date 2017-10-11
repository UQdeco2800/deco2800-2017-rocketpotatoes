use render::{RenderInfo, RenderObject, RenderLine, Color}; 
use util::CallbackFunctions;

extern crate rand;
use self::rand::distributions::{IndependentSample, Range};

const turbofish_width: i32 = 274;
const turbofish_height: i32 = 86;

/// GameState machine!
///
/// Start - press space to start playing
/// Falling - line is falling (player is not holding space)
/// Reeling - line is reeling up (player is holding space)
/// Caught(usize) - something is caught, reel it in (the caught index is stored with in)
#[derive(Debug)]
enum GameState {
    Start,
    Falling,
    Reeling,
    Caught(Fishable),
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
    scale: f32,
    velocity: (i32, i32),
    category: FishableType,
    color: i32,
}


pub struct Game {
    state: GameState,
    line_x: i32,
    line_depth: i32,
    fall_rate: i32,
    fishables: Vec<Fishable>,
}

impl Game {
    pub fn new() -> Self {
        Self {
            state: GameState::Start, 
            line_x: 0,
            line_depth: 200,
            fall_rate: 0,
            fishables: Vec::new(),
        }
    }

    /// Change state to playing
    fn start_falling(&mut self) {
        self.state = GameState::Falling;
        self.fall_rate = 3;
    }

    /// Switches to the reeling state
    fn start_reeling(&mut self) {
        self.state = GameState::Reeling;
        self.fall_rate = -3;
    }

    /// Switches to the caught state with the given fishable index
    fn start_caught(&mut self, index: usize) {
        self.state = GameState::Caught(self.fishables.swap_remove(index));
        self.fall_rate = -3;
    }

    /// Updates the position the line's depth
    fn update_depth(&mut self, delta_time: f64) {
        self.line_depth = self.line_depth + self.fall_rate;
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
                scale: 0.25,
                velocity: velocity,
                category: FishableType::Turbofish,
                color: color_range.ind_sample(&mut rng),
            });
        }
    }

    /// Moves a fishable out if one is collided with
    fn check_collisions(&self) -> Option<usize> {
        let p = (self.line_x, self.line_depth);

        for f in self.fishables.iter().enumerate() {
            let tl = f.1.position;
            let br = (tl.0 + (turbofish_width as f32 * f.1.scale) as i32, tl.1 + (turbofish_height as f32 * f.1.scale) as i32);

            // Return index if point inside collider
            if p.0 >= tl.0 && p.1 >= tl.1 && p.0 <= br.0 && p.1 <= br.1 {
                return Some(f.0);
            }
        }

        None
    }

    pub fn update(&mut self, delta_time: f64, callbacks: &CallbackFunctions) {
        let window_info = RenderInfo { size_x: 0, size_y: 0 };
        (callbacks.get_window_info)(&window_info);
        self.line_x = window_info.size_x / 2;

        self.update_fishables(delta_time);
        self.update_depth(delta_time);
        
        match self.state {
            GameState::Start => {
                self.start_falling();
            },

            GameState::Falling => {

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

            GameState::Caught(ref mut fish) => {
                fish.position.1 = self.line_depth;
            },
        }

        if self.line_depth < 0 {
            self.start_falling();
        }
    }

    pub fn draw(&self, delta_time: f64, window_info: &RenderInfo, callbacks: &CallbackFunctions) {
        (callbacks.draw_line)(RenderLine::new((window_info.size_x / 2, -10), (window_info.size_x / 2, self.line_depth)));

        /*
        // Debug drawing collisions
        for f in self.fishables.iter() {
            let tl = f.position;
            let tr = (tl.0 + (turbofish_width as f32 * f.scale) as i32, tl.1);
            let bl = (tl.0, tl.1 + (turbofish_height as f32 * f.scale) as i32);
            let br = (tl.0 + (turbofish_width as f32 * f.scale) as i32, tl.1 + (turbofish_height as f32 * f.scale) as i32);

            (callbacks.draw_line)(RenderLine::new(tl, tr));
            (callbacks.draw_line)(RenderLine::new(tl, bl));
            (callbacks.draw_line)(RenderLine::new(bl, br));
            (callbacks.draw_line)(RenderLine::new(tr, br));
        }
        */

        (callbacks.start_draw)();

        match self.state {
            GameState::Caught(ref f) => {
                (callbacks.draw_sprite)(RenderObject::new("turbofish".to_string(), 
                                                          f.position.0, f.position.1, 0.0, f.scale, 
                                                          f.velocity.0 < 0, false, 
                                                          f.color));
            },

            _ => { },
        }

        for f in self.fishables.iter() {
            (callbacks.draw_sprite)(RenderObject::new("turbofish".to_string(), 
                                                      f.position.0, f.position.1, 0.0, f.scale, 
                                                      f.velocity.0 < 0, false, 
                                                      f.color));
        }
        (callbacks.end_draw)();
    }
}
