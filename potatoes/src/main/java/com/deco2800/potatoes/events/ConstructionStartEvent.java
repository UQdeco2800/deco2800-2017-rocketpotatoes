package com.deco2800.potatoes.events;

import com.deco2800.potatoes.entities.constructables.Constructable;

public interface ConstructionStartEvent {
    void notify(Constructable constructable);
}
