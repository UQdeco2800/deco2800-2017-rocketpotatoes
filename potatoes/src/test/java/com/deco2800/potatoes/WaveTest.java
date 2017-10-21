package com.deco2800.potatoes;

import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.WaveLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WaveTest {

    @Test

    public void createwavefromlinetest(){
        WaveLoader TestWaveLoader = new WaveLoader("");
        EnemyWave TestWave = TestWaveLoader.createwavefromline("6, 4, 2, 7, 500, 1");

        assertEquals(TestWave.calculateEnemyRatios(6, 4, 2, 7)[0],TestWave.getEnemyRatios()[0],0.1);
    }

}
