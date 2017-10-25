package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.constructables.Constructable;
import com.deco2800.potatoes.events.ConstructionEndEvent;
import com.deco2800.potatoes.events.ConstructionStartEvent;
import com.deco2800.potatoes.events.ConstructionTickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager for all TimeEvents associated with tickable entities. <br>
 * <br>
 * If you know a better way implement this, please change
 */
public class EventManager extends Manager {
    private List<ConstructionStartEvent> constructionStartEvents  = new ArrayList<>();
    private List<ConstructionTickEvent> constructionTickEvent = new ArrayList<>();
    private List<ConstructionEndEvent> constructionEndEven = new ArrayList<>();

    // TODO stop listening?

    public void addConstructionStartListener(ConstructionStartEvent event) {
        constructionStartEvents.add(event);
    }

    public void addConstructionTickListenerent(ConstructionTickEvent event) {
        constructionTickEvent.add(event);
    }

    public void addConstructionEndListener(ConstructionEndEvent event) {
        constructionEndEven.add(event);
    }

    public void fireConstructionStartEvent(Constructable constructable) {
        constructionStartEvents.forEach(e -> e.notify(constructable));
    }

    public void fireConstructionTickEvent(Constructable constructable) {
        constructionTickEvent.forEach(e -> e.notify(constructable));
    }

    public void fireConstructionEndEvent(Constructable constructable) {
        constructionEndEven.forEach(e -> e.notify(constructable));
    }


	public void clearListeners() {
        constructionStartEvents.clear();
        constructionTickEvent.clear();
        constructionEndEven.clear();
	}
}
