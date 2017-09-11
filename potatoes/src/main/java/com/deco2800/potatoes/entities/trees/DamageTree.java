package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.Damage;
import com.deco2800.potatoes.entities.StatisticsBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;


public class DamageTree extends AbstractTree implements Tickable {
    public static transient String TEXTURE ;
    private Damage damageTreeType;

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
            return generateTree("ice_basic_tree",x-> AnimationFactory.createSimpleTimeAnimation(100,new String[] {"ice_basic_tree"}));
        else if(damageTreeType instanceof AcornTree)
            return generateTree("acorn_tree",x-> AnimationFactory.createSimpleTimeAnimation(100,new String[] {"acorn_tree"}));
        return generateTree("lightning_tree1",x-> AnimationFactory.createSimpleTimeAnimation(100,new String[] {
                "lightning_tree1",
                "lightning_tree2",
                "lightning_tree3",
                "lightning_tree4",
                "lightning_tree5",
                "lightning_tree6",
                "lightning_tree7",
                "lightning_tree8",
                "lightning_tree9",
        }));
    }

    /**
     * Static method to create the list of upgrades
     * @param texture the tree texture
     * @param animation the runtime animation texture
     * @return
     */
    private static List<TreeStatistics> generateTree(String texture,Function<AbstractTree,Animation> animation) {
        List<TreeStatistics> result = new LinkedList<>();
		/* UpgradeStats(Health, Shooting Time, Shooting Range, Construction/Upgrade Time, events, events, texture) */
        result.add(new StatisticsBuilder<AbstractTree>().setHealth(10).setAttackRange(8f).setBuildTime(5000)
				.setBuildCost(1).setTexture(texture).setAnimation(animation).addEvent(new TreeProjectileShootEvent(3000))
				.createTreeStatistics());
		result.add(new StatisticsBuilder<AbstractTree>().setHealth(20).setAttackRange(8f).setBuildTime(2000)
				.setBuildCost(1).setTexture(texture).setAnimation(animation).addEvent(new TreeProjectileShootEvent(2500))
				.createTreeStatistics());
		result.add(new StatisticsBuilder<AbstractTree>().setHealth(30).setAttackRange(8f).setBuildTime(2000)
				.setBuildCost(1).setTexture(texture).setAnimation(animation).addEvent(new TreeProjectileShootEvent(1500))
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
