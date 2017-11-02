package com.deco2800.potatoes.entities.trees;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.deco2800.potatoes.entities.projectiles.HomingProjectile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.PropertiesBuilder;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.animation.Animation;
import com.deco2800.potatoes.entities.animation.AnimationFactory;
import com.deco2800.potatoes.entities.effects.Effect.EffectTexture;
import com.deco2800.potatoes.entities.effects.LightningEffect;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;


public class DamageTree extends AbstractTree implements Tickable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DamageTree.class);
    /**
     * A map include ALL TREE'S ANIMATION
     */
    private static Map<String,String[]> treeStatus = new HashMap<String, String[]>();
    /**
     * A map include tree status
     */
    private static Map<String,String > status=new HashMap<String,String>();
    /**
     * setup all tree animation inculding damaging , death ,attacking and normal status
     */
    static {

        status.put("ice_tree","ice_tree-normal");
        status.put("acorn_tree1","acorn_tree1-normal");
        status.put("lightning_tree1","lightning_tree1-normal");
        status.put("fire_tree","fire_tree-normal");
        status.put("cactusTree", "cactusTree-normal");
        status.put("coralTree", "coralTree-normal");


        treeStatus.put("ice_tree-death",new String[]{
                "ice-break1",
                "ice-break2",
                "ice-break3",
                "ice-break4",
        });
        treeStatus.put("ice_tree-normal",new String[]{
                "ice_tree1",
                "ice_tree2",
                "ice_tree3",
                "ice_tree4",
                "ice_tree5",
                "ice_tree6",
                "ice_tree7",
        });


        treeStatus.put("acorn_tree1-attacking",new String[]{
                "acorn_tree_damage_being_damaged1",
                "acorn_tree_damage_being_damaged2",
                "acorn_tree_damage_being_damaged3",
                "acorn_tree_damage_being_damaged4",
                "acorn_tree_damage_being_damaged5",
        });
        treeStatus.put("acorn_tree1-death",new String[]{
                "acorn_tree_dead1",
                "acorn_tree_dead2",
                "acorn_tree_dead3",
                "acorn_tree_dead4",
                "acorn_tree_dead5",
                "acorn_tree_dead6",
                "acorn_tree_dead7",
                "acorn_tree_dead8",
                "acorn_tree_dead9",

        });
        treeStatus.put("acorn_tree1-damaged",new String[]{
                "acorn_tree_damaged1",
                "acorn_tree_damaged2",
                "acorn_tree_damaged3",
        });
        treeStatus.put("acorn_tree1-normal",new String[]{
                "acorn_tree1",
                "acorn_tree2",
                "acorn_tree3",
        });



        treeStatus.put("fire_tree-normal",new String[]{
                "fire_tree1",
                "fire_tree2",
                "fire_tree3",
                "fire_tree4",
        });

        treeStatus.put("lightning_tree1-attacking",new String[]{
                "lightning_being_damaged1",
                "lightning_being_damaged2",
                "lightning_being_damaged3",
                "lightning_being_damaged4",
                "lightning_being_damaged5",
                "lightning_being_damaged6",
                "lightning_being_damaged7",
                "lightning_being_damaged8",
                "lightning_being_damaged9",
        });
        treeStatus.put("lightning_tree1-death",new String[]{
                "lightning_dead1",
                "lightning_dead2",
                "lightning_dead3",
                "lightning_dead4",
                "lightning_dead5",
                "lightning_dead6",
                "lightning_dead7",
        });

        treeStatus.put("lightning_tree1-normal",new String[]{
                "lightning_tree1",
                "lightning_tree2",
                "lightning_tree3",
                "lightning_tree4",
                "lightning_tree5",
                "lightning_tree6",
                "lightning_tree7",
                "lightning_tree8",
                "lightning_tree9",
        });

        treeStatus.put("cactusTree-normal",new String[]{
                "cactusTree"
        });
        
        treeStatus.put("coralTree-normal",new String[]{
                "coralsTree"
        });


    }
    /**
     * Static generating ice tree
     */
    private static final List<TreeProperties> ICE_TREE_STATS = generateTree("ice_tree",animation(), BallisticProjectile.class,ProjectileTexture.ICE);
    /**
     * Static generating acorn tree
     */
	private static final List<TreeProperties> ACORN_TREE_STATS = generateTree("acorn_tree1", animation(), HomingProjectile.class,ProjectileTexture.ACORN);
    /**
     * Static generating lightning tree
     */
	private static final List<TreeProperties> LIGHTNING_TREE_STATS = generateTree("lightning_tree1", animation(),  LightningEffect.class,EffectTexture.LIGHTNING_WATER);
    /**
     * Static generating fire tree
     */
	private static final List<TreeProperties> FIRE_TREE_STATS=generateTree("fire_tree", animation(),  HomingProjectile.class,ProjectileTexture.FIRE);
	 /**
     * Static generating cactus tree
     */
	private static final List<TreeProperties> CACTUS_TREE_STATS=generateTree("cactusTree", animation(), HomingProjectile.class,ProjectileTexture.LEAVES);
	/**
     * Static generating coral tree
     */
	private static final List<TreeProperties> CORAL_TREE_STATS=generateTree("coralTree", animation(), BallisticProjectile.class,ProjectileTexture.WATER);

	/**
     * Static field to store information about upgrades
     */
    private DamageTreeType damageTreeType;


    /**
     * Default constructor for serialization
     */
    public DamageTree() {
        //Default constructor
    }
    /**
     * Base Constructor
     */
    public DamageTree(float posX, float posY, DamageTreeType texture) {
        super(posX, posY, 1f, 1f);
        damageTreeType=texture;
        updateTexture();
    }

    @Override
    public DamageTree createCopy() {
    	return new DamageTree(this.getPosX(), this.getPosY(), this.getDamageTreeType());
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
        } else if(damageTreeType instanceof CactusTreeType){
            return CACTUS_TREE_STATS;
        }else if(damageTreeType instanceof CoralTreeType){
            return CORAL_TREE_STATS;
        }
        this.setTexture("lightning_tree1");
        return LIGHTNING_TREE_STATS;
    }

    @Override
    public String getName(){
        return damageTreeType.getTypeName();
    }
    /**
     *The class will refresh cureent tree status
     * @return a map that contain the tree status and lambda function
     */
    public static Map<String,Function<AbstractTree, Animation>> animation(){
    Map<String,Function<AbstractTree, Animation> > temp=new HashMap<String,Function<AbstractTree, Animation>>();
    for(Map.Entry<String, String[]> entry : treeStatus.entrySet()){

            temp.put(entry.getKey(),x->AnimationFactory.createSimpleTimeAnimation(100,entry.getValue())) ;
    }
    return temp;

};

    /**
     * Static method to create the list of upgrades
     * Function<AbstractTree, Animation> animation
     */
	private static List<TreeProperties> generateTree(String texture,Map<String,Function<AbstractTree, Animation>> animation,
			Class<? extends AbstractEntity> fireObjectClass, Enum<?> fireObjectType) {

		List<TreeProperties> result = new LinkedList<>();

		/*
		 * UpgradeStats(Health, Shooting Time, Shooting Range, Construction/Upgrade
		 * Time, events, events, texture)
		 */

			result.add(new PropertiesBuilder<AbstractTree>().setHealth(10).setAttackRange(8f).setBuildTime(5000)
					.setBuildCost(1).setAnimation(animation.get(status.get(texture))).addEvent(new TreeProjectileShootEvent(250, fireObjectClass,fireObjectType))
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
