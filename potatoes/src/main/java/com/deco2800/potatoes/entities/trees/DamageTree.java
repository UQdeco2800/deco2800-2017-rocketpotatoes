package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class DamageTree extends AbstractTree implements Tickable {
    public static transient String TEXTURE ;
    private Damage damageTreeType;
    /**
     * Static field to store information about upgrades
     */


    /**
     * Default constructor for serialization
     */
    public DamageTree() {
        //Default constructor
    }
    /**
     * Base Constructor
     */

    public DamageTree(float posX, float posY, float posZ) {

        super(posX, posY, posZ, 1f, 1f, 1f, null);

        damageTreeType=new LightningTree();


        this.resetStats();

    }

    public DamageTree(float posX, float posY, float posZ, Damage texture) {
        super(posX, posY, posZ, 1f, 1f, 1f, null);
        if(null==texture){
            damageTreeType=new LightningTree();
        }else{
            damageTreeType=texture;
        }


        this.resetStats();

    }


    public DamageTree(float posX, float posY, float posZ, Damage texture, float maxHealth,float demage) {
        super(posX, posY, posZ, 1f, 1f, 1f, null);
    }






    @Override
    public List<UpgradeStats> getAllUpgradeStats() {

        if(damageTreeType instanceof IceTree)
            return generateTree("ice_basic_tree");
        else if(damageTreeType instanceof AcornTree)
            return generateTree("acorn_tree");
        return generateTree("lightning_tree1");
    }

    /**
     * Static method to create the list of upgrades
     */
    private static List<UpgradeStats> generateTree(String texture) {
        List<UpgradeStats> result = new LinkedList<>();
        List<TimeEvent<AbstractTree>> normalEvents = new LinkedList<>();
        List<TimeEvent<AbstractTree>> constructionEvents = new LinkedList<>();
		/* UpgradeStats(Health, Shooting Time, Shooting Range, Construction/Upgrade Time, events, events, texture) */
        result.add(new UpgradeStats(10, 1000, 8f, 5000,100, normalEvents, constructionEvents, texture));
        result.add(new UpgradeStats(20, 600, 8f, 2000,100,normalEvents, constructionEvents, texture));
        result.add(new UpgradeStats(30, 100, 8f, 2000,100, normalEvents, constructionEvents, texture));

        for (UpgradeStats upgradeStats : result) {
            upgradeStats.getNormalEventsReference().add(new TreeProjectileShootEvent(upgradeStats.getSpeed()));
        }

        return result;
    }

    /**
     * test purpose only
     * @return Damage
     */
    public Damage getDamageTreeType(){
        return damageTreeType;
    }


}
