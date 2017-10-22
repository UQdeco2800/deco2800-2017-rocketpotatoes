package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.LargeFootstepEffect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.IceCrystalResource;
import com.deco2800.potatoes.entities.resources.ResourceEntity;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.SoundManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class to test the LargeFootstepEffect class.
 *
 * @author ryanjphelan
 */
public class LargeFootstepEffectTest extends BaseTest {

    private LargeFootstepEffect footStepEmpty;
    private LargeFootstepEffect footStep1;
    private SoundManager sound1 = new SoundManager();

    @Before
    public void setUp() throws Exception {
        footStepEmpty = new LargeFootstepEffect();
        footStep1 = new LargeFootstepEffect(MortalEntity.class, 0, 0, 1, 1);
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
        GameManager.get().addManager(sound1);
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        footStep1 = null;
        footStepEmpty = null;
    }

    /*
     * Test an emptyConstructor instance of the large footstep class
     */
    @Test
    public void emptyConstructor() {
        footStepEmpty = new LargeFootstepEffect();
        assertEquals(true, footStepEmpty.getFootstepPosition() == null);
    }

    /*
     * Test the toString method for the LargeFootstepEffect.
     * This tests accounts for the coordinate shifting done manually in the class
     */
    @Test
    public void toStringTest() {
        assertEquals("Large Footstep at (0, 0)", footStep1.toString());
    }

    /*
     * Test the onTick method when the effect is not colliding with any other entities
     */
    @Test
    public void onTickTestNoCollisions() {
        int entitiesStartSize = GameManager.get().getWorld().getEntities().values().size();
        GameManager.get().getWorld().addEntity(footStep1);
        footStep1.onTick(1);
        assertEquals(true, footStep1.getCurrentTextureIndexCount() == 0);
        for (int i = 0; i < 9; ++i) {
            footStep1.onTick(1);
        }
        assertEquals(true, footStep1.getCurrentTextureIndexCount() == 1);
        for (int i = 0; i < 10; ++i) {
            footStep1.onTick(1);
        }
        assertEquals(true, footStep1.getCurrentTextureIndexCount() == 2);
        for (int i = 0; i < 20; ++i) {
            footStep1.onTick(1);
        }
        Collection<AbstractEntity> entityValues = GameManager.get().getWorld().getEntities().values();
        assertEquals(0, entityValues.size() - entitiesStartSize);
    }

    /*
     * Test effect colliding with entities (and not colliding with various entity types).
     */
    @Test
    public void onTickTestCollisions() {
        int entitiesStartSize = GameManager.get().getWorld().getEntities().values().size();
        GameManager.get().getWorld().addEntity(footStep1);
        GameManager.get().getWorld().addEntity(new ResourceEntity(-1f,0.5f,new SeedResource()));
        GameManager.get().getWorld().addEntity(new ResourceEntity(-1f,0.5f,new FoodResource()));
        GameManager.get().getWorld().addEntity(new ResourceEntity(-5f,1f,new IceCrystalResource()));
        GameManager.get().getWorld().addEntity(new ResourceEntity(-1f,0.5f,new IceCrystalResource()));
        GameManager.get().getWorld().addEntity(new SpeedyEnemy(2,2));
        Collection<AbstractEntity> entityValues = GameManager.get().getWorld().getEntities().values();
        assertEquals(6, entityValues.size() - entitiesStartSize);
        footStep1.onTick(1);
        entityValues = GameManager.get().getWorld().getEntities().values();
        assertEquals(3, entityValues.size() - entitiesStartSize);
    }
}
