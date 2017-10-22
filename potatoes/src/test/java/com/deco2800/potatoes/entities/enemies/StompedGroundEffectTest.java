package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.effects.StompedGroundEffect;
import com.deco2800.potatoes.entities.health.MortalEntity;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the StompedGroundEffect class.
 *
 * @author ryanjphelan
 */
public class StompedGroundEffectTest extends BaseTest {

    private StompedGroundEffect stompEmpty;
    private StompedGroundEffect stomp1;

    @Before
    public void setUp() throws Exception {
        stompEmpty = new StompedGroundEffect();
        stomp1 = new StompedGroundEffect(MortalEntity.class, 0, 0, true, 1, 1);
        GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
    }

    @After
    public void cleanUp() {
        GameManager.get().clearManagers();
        stomp1 = null;
        stompEmpty = null;
    }

    /*
     * Test an emptyConstructor instance of the stomped ground effect
     */
    @Test
    public void emptyConstructor() {
        assertEquals(true, stompEmpty.getStompedGroundPosition() == null);
    }

    /*
     * Test the toString method for the StompedGroundEffect.
     */
    @Test
    public void toStringTest() {
        assertEquals("Stomped Ground at (0, 0)", stomp1.toString());
    }

    /*
     * Test the onTick method
     */
    @Test
    public void onTickTest() {
        int entitiesStartSize = GameManager.get().getWorld().getEntities().values().size();
        GameManager.get().getWorld().addEntity(stomp1);
        stomp1.onTick(1);
        assertEquals(true, stomp1.getCurrentTextureIndex() == 0);
        for (int i = 0; i < 200; ++i) {
            stomp1.onTick(1);
        }
        assertEquals(true, stomp1.getCurrentTextureIndex() == 1);
        for (int i = 0; i < 200; ++i) {
            stomp1.onTick(1);
        }
        assertEquals(true, stomp1.getCurrentTextureIndex() == 2);
        for (int i = 0; i < 400; ++i) {
            stomp1.onTick(1);
        }
        Collection<AbstractEntity> entityValues = GameManager.get().getWorld().getEntities().values();
        assertEquals(0, entityValues.size() - entitiesStartSize);
    }
}
