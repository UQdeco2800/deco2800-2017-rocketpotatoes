use std::collections::VecDeque;

#[derive(Debug, PartialEq, Eq, Copy, Clone)]
pub enum Key {
    /// 0
    Left,
    /// 1
    Up,
    /// 2
    Right,
    /// 3
    Down,
    /// 4
    A,
    /// 5
    B,
    /// 6
    Start,
    /// 7
    Select,
}

impl Key {
    fn from_int(input: u32) -> Option<Self> {
        match input {
            0 => Some(Key::Left),
            1 => Some(Key::Up),
            2 => Some(Key::Right),
            3 => Some(Key::Down),
            4 => Some(Key::A),
            5 => Some(Key::B),
            6 => Some(Key::Start),
            7 => Some(Key::Select),
            _ => None,
        }
    }
}

#[derive(Debug, Default)]
pub struct KeyLogger {
    target: &'static [Key],
    log: VecDeque<Key>,
    current: Option<Key>,
}

impl KeyLogger {
    pub fn new(target: &'static [Key]) -> Self {
        KeyLogger {
            target,
            ..Default::default()
        }
    }

    /// Takes a key press integer. Returns true if it's (mini)game over.
    pub fn key_press(&mut self, input: u32) -> bool {
        match (Key::from_int(input), self.current) {
            (Some(key), None) => {
                self.current = Some(key);
                self.log.push_back(key);
                while self.log.len() > self.target.len() {
                    self.log.pop_front();
                }

                if self.log.len() == self.target.len() {
                    self.log.iter().zip(self.target).all(|(target, pressed)| {
                        target == pressed
                    })
                } else {
                    false
                }
            }
            (None, Some(_)) => {
                self.current = None;
                false
            }
            _ => false,
        }
    }
}
