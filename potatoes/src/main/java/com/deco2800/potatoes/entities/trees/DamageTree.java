package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


public class DamageTree extends AbstractTree implements Tickable {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceTree.class);
    private static final transient String TEXTURE = "damage_tree";

    /**
     * Static field to store information about upgrades
     */
    public static final List<UpgradeStats> STATS = initStats();

    /**
     * Default constructor for serialization
     */
    public DamageTree() {
    }


    /**
     * Base Constructor
     */
    public DamageTree(float posX, float posY, float posZ, String texture, float maxHealth,float demage) {
        super(posX, posY, posZ, 1f, 1f, 1f, texture);
    }

    /**
     * Returns the list of upgrades for the tree
     */
    @Override
    public List<UpgradeStats> getAllUpgradeStats() {
        return STATS;
    }

    /**
     * Static method to create the list of upgrades
     */
    private static List<UpgradeStats> initStats() {
        List<UpgradeStats> result = new LinkedList<>();
        List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
        List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();

		/* UpgradeStats(Health, Shooting Time, Shooting Range, Construction/Upgrade Time, events, events, texture) */
        result.add(new UpgradeStats(10, 1000, 8f, 5000,100, normalEvents, constructionEvents, TEXTURE));
        result.add(new UpgradeStats(20, 600, 8f, 2000,100,normalEvents, constructionEvents, TEXTURE));
        result.add(new UpgradeStats(30, 100, 8f, 2000,100, normalEvents, constructionEvents, TEXTURE));

        for (UpgradeStats upgradeStats : result) {
            upgradeStats.getNormalEventsReference().add(new TreeProjectileShootEvent(upgradeStats.getSpeed()));
        }

        return result;
    }
}
