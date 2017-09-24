package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.worlds.World;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class WaveManagerTest extends BaseTest {

    @Before
    public void setUp() throws Exception {
        World mockWorld = mock(World.class);
        GameManager gm = GameManager.get();
        gm.setWorld(mockWorld);

    }

    @Test
    public void testAddWave() {
        WaveManager wm = new WaveManager();
        EnemyWave testWave = new EnemyWave(1,1,1,1,750);

        wm.addWave(testWave);
        //assertThat(wm.getWaves())
    }



}
