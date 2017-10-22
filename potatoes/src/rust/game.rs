use render::{RenderInfo, RenderLine, RenderRectangle, RenderObject};
use util::CallbackFunctions;

use std::time::Instant;

use rand::{self, Rng};
use rand::distributions::{IndependentSample, Range};

const TURBOFISH_WIDTH: i32 = 274;
const TURBOFISH_HEIGHT: i32 = 86;

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
    Rustacean,
}

impl FishableType {
    fn texture(&self) -> String {
        match *self {
            FishableType::Turbofish => "turbofish".into(),
            FishableType::Rustacean => "rustacean".into(),
        }
    }
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
    water_level: i32,
    fall_rate: i32,
    fishables: Vec<Fishable>,
    time: Instant,
}

impl Game {
    pub fn new() -> Self {
        Self {
            state: GameState::Start,
            line_x: 0,
            line_depth: 50,
            water_level: 200,
            fall_rate: 0,
            fishables: Vec::new(),
            time: Instant::now(),
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
    fn update_depth(&mut self) {
        self.line_depth = self.line_depth + self.fall_rate;
    }

    /// Updates the position of all the fishables (and spawns new ones if required)
    fn update_fishables(&mut self) {
        for f in self.fishables.iter_mut() {
            f.position.0 += f.velocity.0;
            f.position.0 += f.velocity.1;
        }


        // Delete those outside bounds TODO better bounds
        self.fishables.retain(|ref f| {
            (f.position.0 >= -3000) && (f.position.0 <= 4000)
        });


        // TODO fix this gross
        let y_range = Range::new(400, 1000);
        let x_range = Range::new(-1000, 1000);
        let dir_range = Range::new(0, 2);
        let speed_range = Range::new(1, 6);
        let color_range = Range::new(1, 6);
        let mut rng = rand::thread_rng();
        if self.fishables.len() < 50 {
            let velocity: (i32, i32);
            let position: (i32, i32);
            if dir_range.ind_sample(&mut rng) == 0 {
                position = (
                    -1500 + x_range.ind_sample(&mut rng),
                    y_range.ind_sample(&mut rng),
                );
                velocity = (speed_range.ind_sample(&mut rng), 0);
            } else {
                position = (
                    3000 + x_range.ind_sample(&mut rng),
                    y_range.ind_sample(&mut rng),
                );
                velocity = (-speed_range.ind_sample(&mut rng), 0);
            }
            self.fishables.push(Fishable {
                position: position,
                scale: 0.25,
                velocity: velocity,
                category: if rng.gen_weighted_bool(2) {
                    FishableType::Turbofish
                } else {
                    FishableType::Rustacean
                },
                color: color_range.ind_sample(&mut rng),
            });
        }
    }

    /// Moves a fishable out if one is collided with
    fn check_collisions(&self) -> Option<usize> {
        let p = (self.line_x, self.line_depth);

        for f in self.fishables.iter().enumerate() {
            let tl = f.1.position;
            let br = (
                tl.0 + (TURBOFISH_WIDTH as f32 * f.1.scale) as i32,
                tl.1 + (TURBOFISH_HEIGHT as f32 * f.1.scale) as i32,
            );

            // Return index if point inside collider
            if p.0 >= tl.0 && p.1 >= tl.1 && p.0 <= br.0 && p.1 <= br.1 {
                return Some(f.0);
            }
        }

        None
    }

    pub fn update(&mut self, callbacks: &CallbackFunctions) {
        let window_info = RenderInfo {
            size_x: 0,
            size_y: 0,
        };
        (callbacks.get_window_info)(&window_info);
        self.line_x = window_info.size_x / 2;

        // Update water level
        let elapsed = self.time.elapsed();
        let real_time = f64::sin(
            elapsed.as_secs() as f64 + elapsed.subsec_nanos() as f64 * 1e-9,
        );
        self.water_level = 200 + (10.0 * f64::sin(0.5 * real_time)) as i32;

        self.update_fishables();
        self.update_depth();

        match self.state {
            GameState::Start => {
                self.start_falling();
            }

            GameState::Falling => {

                match self.check_collisions() {
                    Some(index) => {
                        self.start_caught(index);
                    }
                    None => {}
                }

                if (callbacks.is_space_pressed)() {
                    self.start_reeling();
                }
            }

            GameState::Reeling => {

                match self.check_collisions() {
                    Some(index) => {
                        self.start_caught(index);
                    }
                    None => {}
                }

                if !(callbacks.is_space_pressed)() {
                    self.start_falling();
                }
            }

            GameState::Caught(ref mut fish) => {
                fish.position.1 = self.line_depth;
            }
        }

        if self.line_depth < 50 {
            self.start_falling();
        }
    }

    pub fn draw(&self, window_info: &RenderInfo, callbacks: &CallbackFunctions) {
        // Draw line
        (callbacks.draw_line)(RenderLine::new((window_info.size_x / 2, 50), (
            window_info.size_x / 2,
            self.line_depth,
        )));

        /*
        // Debug drawing collisions
        for f in self.fishables.iter() {
            let tl = f.position;
            let tr = (tl.0 + (TURBOFISH_WIDTH as f32 * f.scale) as i32, tl.1);
            let bl = (tl.0, tl.1 + (TURBOFISH_HEIGHT as f32 * f.scale) as i32);
            let br = (tl.0 + (TURBOFISH_WIDTH as f32 * f.scale) as i32, tl.1 + (TURBOFISH_HEIGHT as f32 * f.scale) as i32);

            (callbacks.draw_line)(RenderLine::new(tl, tr));
            (callbacks.draw_line)(RenderLine::new(tl, bl));
            (callbacks.draw_line)(RenderLine::new(bl, br));
            (callbacks.draw_line)(RenderLine::new(tr, br));
        }
        */


        // Normal sprite drawing
        (callbacks.start_draw)();

        // Draw fisherman
        let b_scale = 0.3;
        (callbacks.draw_sprite)(RenderObject::new(
            "boatman".to_string(),
            (window_info.size_x / 2) - (928 as f32 * b_scale) as i32,
            50,
            0.0,
            b_scale,
            false,
            false,
            -1,
        ));

        // Draw caught
        match self.state {
            GameState::Caught(ref f) => {
                (callbacks.draw_sprite)(RenderObject::new(
                    f.category.texture(),
                    f.position.0,
                    f.position.1,
                    0.0,
                    f.scale,
                    f.velocity.0 < 0,
                    false,
                    f.color,
                ));
            }

            _ => {}
        }

        // Draw others
        for f in self.fishables.iter() {
            (callbacks.draw_sprite)(RenderObject::new(
                f.category.texture(),
                f.position.0,
                f.position.1,
                0.0,
                f.scale,
                f.velocity.0 < 0,
                false,
                f.color,
            ));
        }
        (callbacks.end_draw)();

        // Draw water
        (callbacks.draw_rectangle)(RenderRectangle::new(
            (0, self.water_level),
            (window_info.size_x, window_info.size_y),
            3,
            0.3,
        ));

        // Draw water seam
        (callbacks.draw_rectangle)(RenderRectangle::new(
            (0, self.water_level),
            (window_info.size_x, 3),
            -1,
            1.0,
        ));
    }
}
