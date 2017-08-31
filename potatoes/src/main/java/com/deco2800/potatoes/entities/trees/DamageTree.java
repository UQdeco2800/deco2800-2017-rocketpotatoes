package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DamageTree extends AbstractTree implements Tickable {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(ResourceTree.class);
    private static final transient String TEXTURE = "lightning_tree";
    private static final transient  Map<String, List<String>> LOADEDTEXTURE=Texture(TEXTURE);


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
    private static List<String> damageTreeAnimation(String Type){
        List<String> result=new LinkedList<>();
        switch (Type){
            case "lightning_tree":
                for(int i=1;i<10;i++) result.add("lightning_tree"+i);
                break;
            case "lightning_being_damaged":
                for(int i=1;i<10;i++) result.add("lightning_being_damaged"+i);
                break;
            case "lightning_damaged":
                for(int i=1;i<9;i++) result.add("lightning_damaged"+i);
                break;
            case "lightning_damaged_being_damaged":
                for(int i=1;i<10;i++) result.add("lightning_damaged_being_damaged"+i);
                break;
            case "lightning_dead":
                for(int i=1;i<8;i++) result.add("lightning_dead"+i);
                break;
            default:
                for(int i=1;i<10;i++) result.add("lightning_tree"+i);
                break;

        }
        return result;
    }


    private static Map<String,List<String>> Texture(String treeType){
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        result.put(treeType,damageTreeAnimation(treeType));
        return result;
    };


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
    @Override
    public String getTexture(){
        //TODO: condition need to apply diff texture and diff status
        return LOADEDTEXTURE.get(TEXTURE).get((int)Math.round((Math.random()+0.1)*9));


    }
}
