package com.deco2800.potatoes;

import com.deco2800.potatoes.entities.Enemies.MeleeAttackEvent;
import com.deco2800.potatoes.entities.TankEnemy;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MeleeAttackEventTest {
    MeleeAttackEvent testEvent = new MeleeAttackEvent(20);
    TankEnemy testMeleeEnemy = new TankEnemy(12, 12, 0);

    @Test
    public void stringTest() {
        assertEquals("string mismatch", "Melee attack with 20 attackspeed", testEvent.toString());
    }
}
