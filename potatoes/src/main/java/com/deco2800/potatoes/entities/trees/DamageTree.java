package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;

import com.deco2800.potatoes.entities.AcornTree;
import com.deco2800.potatoes.entities.Damage;
import com.deco2800.potatoes.entities.IceTree;
import com.deco2800.potatoes.entities.LightningTree;
import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.Tickable;


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
    public List<TreeStatistics> getAllUpgradeStats() {

        if(damageTreeType instanceof IceTree)
            return generateTree("ice_basic_tree");
        else if(damageTreeType instanceof AcornTree)
            return generateTree("acorn_tree");
        return generateTree("lightning_tree1");
    }

    /**
     * Static method to create the list of upgrades
     */
    private static List<TreeStatistics> generateTree(String texture) {
        List<TreeStatistics> result = new LinkedList<>();
		/* UpgradeStats(Health, Shooting Time, Shooting Range, Construction/Upgrade Time, events, events, texture) */
        result.add(new StatisticsBuilder<AbstractTree>().setHealth(10).setAttackRange(8f).setBuildTime(5000)
				.setBuildCost(1).setTexture(texture).addEvent(new TreeProjectileShootEvent(3000))
				.createTreeStatistics());
		result.add(new StatisticsBuilder<AbstractTree>().setHealth(20).setAttackRange(8f).setBuildTime(2000)
				.setBuildCost(1).setTexture(texture).addEvent(new TreeProjectileShootEvent(2500))
				.createTreeStatistics());
		result.add(new StatisticsBuilder<AbstractTree>().setHealth(30).setAttackRange(8f).setBuildTime(2000)
				.setBuildCost(1).setTexture(texture).addEvent(new TreeProjectileShootEvent(1500))
				.createTreeStatistics());

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
