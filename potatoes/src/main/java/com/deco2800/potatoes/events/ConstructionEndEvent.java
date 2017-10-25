package com.deco2800.potatoes.events;

import com.deco2800.potatoes.entities.constructables.Constructable;

public interface ConstructionEndEvent {
    void notify(Constructable constructable);
}
