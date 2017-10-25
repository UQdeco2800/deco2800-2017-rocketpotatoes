package com.deco2800.potatoes.events;

import com.deco2800.potatoes.entities.AbstractEntity;

public interface EntityDeathEvent {
    void notify(AbstractEntity constructable);
}
