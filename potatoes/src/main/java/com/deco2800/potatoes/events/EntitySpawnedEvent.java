package com.deco2800.potatoes.events;

import com.deco2800.potatoes.entities.AbstractEntity;

public interface EntitySpawnedEvent {
    void notify(AbstractEntity entity);
}
