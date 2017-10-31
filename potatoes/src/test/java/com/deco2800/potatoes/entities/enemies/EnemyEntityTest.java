package com.deco2800.potatoes.entities.enemies;



import com.badlogic.gdx.math.Vector3;
import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.GoalPotate;
import com.deco2800.potatoes.entities.effects.Effect;
import com.deco2800.potatoes.entities.effects.LargeFootstepEffect;
import com.deco2800.potatoes.entities.effects.StompedGroundEffect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.player.Player;
import com.deco2800.potatoes.entities.projectiles.BallisticProjectile;
import com.deco2800.potatoes.entities.projectiles.Projectile;
import com.deco2800.potatoes.entities.projectiles.Projectile.ProjectileTexture;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test class to test the EnemyEntity general class.
 *
 * @author craig & ryanjphelan
 */
public class EnemyEntityTest extends BaseTest {

    Squirrel squirrelEmpty;
    TankEnemy tank1;
    ResourceTree resourceTree;
    GoalPotate goalPotato;
    Player playerTest;


    @Before
    public void setUp() throws Exception {
        squirrelEmpty = new Squirrel();
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        squirrelEmpty = null;
        tank1 = null;
        resourceTree = null;
        goalPotato = null;
        playerTest = null;
    }

    @Test
    public void emptyConstructor() {
        assertEquals(null, squirrelEmpty.getFacing());
    }

    @Test
    public void nonEmptyConstructor() {
        tank1 = new TankEnemy(7, 7);
    }

    @Test
    public void onTickTest() {
        tank1 = new TankEnemy(7, 7);
        resourceTree = new ResourceTree(3, 3);
        goalPotato = new GoalPotate(0, 0);
        playerTest = new Player(1, 1);
        GameManager.get().getWorld().addEntity(tank1);
        GameManager.get().getWorld().addEntity(resourceTree);
        tank1.onTick(1);
        resourceTree = new ResourceTree(7, 7);
        GameManager.get().getWorld().addEntity(resourceTree);
        tank1.onTick(1);
        tank1.getGoal();
        for (int i = 0; i < 100; ++i) {
            tank1.onTick(1);
        }
    }

    @Test
    public void onTickTestPlayer() {
        tank1 = new TankEnemy(7, 7);
        playerTest = new Player(1, 1);
        ProjectileTree projectileTree = new ProjectileTree(10, 10);
        GameManager.get().getWorld().addEntity(playerTest);
        GameManager.get().getWorld().addEntity(projectileTree);
        GameManager.get().getManager(PlayerManager.class).setPlayer(playerTest);
        tank1.onTick(1);
        playerTest = new Player(7, 7);
        GameManager.get().getWorld().addEntity(playerTest);
        GameManager.get().getManager(PlayerManager.class).setPlayer(playerTest);
        tank1.onTick(1);
        Effect stomp = new StompedGroundEffect(MortalEntity.class, 7, 7, true, 1, 1);
        ResourceEntity food = new ResourceEntity(7, 7, new FoodResource());
        GameManager.get().getWorld().addEntity(stomp);
        GameManager.get().getWorld().addEntity(food);
        tank1.onTick(1);
    }

    @Test
    public void setSpeedTest() {
        tank1 = new TankEnemy(7, 7);
        tank1.setSpeed(3f);
        Assert.assertEquals("Failed to set Speed", 3f, tank1.getSpeed(), 0f);
    }

    @Test
    public void setGoalTest() {
        tank1 = new TankEnemy(7, 7);
        tank1.setGoal(ProjectileTree.class);
        Assert.assertEquals("Failed to set Goal", ProjectileTree.class, tank1.getGoal());
    }

    @Test
    public void getShotProjectileTest() {

        tank1 = new TankEnemy(7, 7);
//        Projectile proj =new BallisticProjectile(null,new Vector3(0,0,0), new Vector3(1,1,1), 8, 10,
//                            Projectile.ProjectileType.ROCKET, null, null);
        Projectile proj=new BallisticProjectile(null,new Vector3(0,0,0), new Vector3(1,1,1), 8, 10, ProjectileTexture.ROCKET, null,
                null);

        tank1.getShot(proj);
        Assert.assertTrue("enemy failed to getShot()", tank1.getHealth() < tank1.getMaxHealth());
    }

    @Test
    public void getShotEffectTest() {
        tank1 = new TankEnemy(7, 7);
        Effect foot = new LargeFootstepEffect(MortalEntity.class, 5, 5, 1, 1);
        tank1.getShot(foot);
        Assert.assertTrue("enemy failed to getShot()", tank1.getHealth() < tank1.getMaxHealth());
    }

    @Test
    public void getProgressRatioTest() {
        tank1 = new TankEnemy(7, 7);
        Assert.assertTrue("Ratio was not 1", tank1.getProgressRatio() == 1.0);
    }

    @Test
    public void getMaxProgressTest() {
        tank1 = new TankEnemy(7, 7);
        //Assert.assertTrue("Ratio was not 1", tank1.getMaxProgress() == 2000);
    }

    @Test
    public void dyingHandlerTest() {
        tank1 = new TankEnemy(7, 7);
        tank1.dyingHandler();
    }

    @Test
    public void deathHandlerTest() {
        tank1 = new TankEnemy(7, 7);
        tank1.deathHandler();
    }

//    @Test
//    public void delayTest(){
//        tank1 = new TankEnemy(7, 7);
//        Assert.assertEquals(tank1.delay(10,tank1.getEnemyType().length),0);
//    }

    @Test
    public void channelTest() {
        tank1 = new TankEnemy(7, 7);
        tank1.getChannelTimer();
        tank1.setChannellingTimer(3);
        tank1.getProgressBar();
    }

}
