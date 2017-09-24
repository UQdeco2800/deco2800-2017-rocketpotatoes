package com.deco2800.potatoes.entities.trees;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.animation.SingleFrameAnimation;



public class DamageTree extends AbstractTree implements Tickable {
	private static final List<TreeProperties> ICE_TREE_STATS = generateTree("ice_tree",
			x -> AnimationFactory.createSimpleTimeAnimation(100,
                    new String[]{
			        "ice_tree1",
			        "ice_tree2",
			        "ice_tree3",
			        "ice_tree4",
			        "ice_tree5",
			        "ice_tree6",
			        "ice_tree7",
    }));
	private static final List<TreeProperties> ACORN_TREE_STATS = generateTree("acorn_tree1",
            x -> AnimationFactory.createSimpleTimeAnimation(100,
                    new String[] {
                            "acorn_tree1",
                            "acorn_tree2",
                            "acorn_tree3",
                    }));
	private static final List<TreeProperties> LIGHTNING_TREE_STATS = generateTree("lightning_tree1",
			x -> AnimationFactory.createSimpleTimeAnimation(500,
					new String[] {
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
	
	private static final List<TreeProperties> FIRE_TREE_STATS=generateTree("fire_tree",
            x->AnimationFactory.createSimpleTimeAnimation(500,new String[]{
			       "fire_tree1",
			       "fire_tree2",
			       "fire_tree3",
			       "fire_tree4",
			 
			 }));
    private DamageTreeType damageTreeType;
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

        super(posX, posY, posZ, 1f, 1f, 1f);

        damageTreeType=new LightningTreeType();


        this.resetStats();

    }

    public DamageTree(float posX, float posY, float posZ, DamageTreeType texture) {
        super(posX, posY, posZ, 1f, 1f, 1f);
        if(null==texture){
            damageTreeType=new LightningTreeType();
        }else{
            damageTreeType=texture;
        }


        this.resetStats();

    }


    public DamageTree(float posX, float posY, float posZ, DamageTreeType texture, float maxHealth,float demage) {
        super(posX, posY, posZ, 1f, 1f, 1f);
    }

    @Override
    public DamageTree clone() {
    	return new DamageTree(this.getPosX(), this.getPosY(), this.getPosZ(), this.getDamageTreeType());
    }



    @Override
    public List<TreeProperties> getAllUpgradeStats() {
        if(damageTreeType instanceof IceTreeType){
            this.setTexture("ice_basic_tree");
            return ICE_TREE_STATS;
        }else if(damageTreeType instanceof AcornTreeType){
            this.setTexture("acorn_tree");
            return ACORN_TREE_STATS;
        } else if(damageTreeType instanceof FireTreeType){
            this.setTexture("fire_tree");
            return FIRE_TREE_STATS;
        }
        this.setTexture("lightning_tree1");
        return LIGHTNING_TREE_STATS;
    }

    /**
     * Static method to create the list of upgrades
     */
	private static List<TreeProperties> generateTree(String texture, Function<AbstractTree, Animation> animation) {
		List<TreeProperties> result = new LinkedList<>();
		/*
		 * UpgradeStats(Health, Shooting Time, Shooting Range, Construction/Upgrade
		 * Time, events, events, texture)
		 */

			result.add(new PropertiesBuilder<AbstractTree>().setHealth(10).setAttackRange(8f).setBuildTime(5000)
					.setBuildCost(1).setAnimation(animation).addEvent(new LightningShootEvent(250))
					.createTreeStatistics());



		return result;
	}

    /**
     * test purpose only
     * @return Damage
     */
    public DamageTreeType getDamageTreeType(){
        return damageTreeType;
    }



}
