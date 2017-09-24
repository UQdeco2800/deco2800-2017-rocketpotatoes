package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.BaseTest;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.worlds.World;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.mockito.Mockito.mock;

public class WaveManagerTest extends BaseTest {

    WaveManager wm;
    EnemyWave testWaveOne = new EnemyWave(1,1,1,1,750);
    EnemyWave testWaveTwo = new EnemyWave(2,1,1,2,750);
    World mockWorld;

    @Before
    public void setUp() throws Exception {
        mockWorld = mock(World.class);
        GameManager gm = GameManager.get();
        gm.setWorld(mockWorld);
        wm = new WaveManager();
        wm.addWave(testWaveOne);
        wm.addWave(testWaveTwo);
    }

    @Test
    public void addWaveTest() {
        WaveManager freshManager = new WaveManager();
        freshManager.addWave(testWaveOne);
        Assert.assertEquals("initial wave wasn't added correctly", testWaveOne, wm.getWaves().get(0));
        freshManager.addWave(testWaveTwo);
        Assert.assertEquals("Second wave wasn't added correctly", testWaveTwo, wm.getWaves().get(1));
    }

    @Test
    public void getWavesTest() {
        ArrayList<EnemyWave> expectedWaveList = new ArrayList<>();
        expectedWaveList.add(testWaveOne);
        expectedWaveList.add(testWaveTwo);
        Assert.assertEquals("wgetWaves returns incorrect list of waves", expectedWaveList, wm.getWaves());
    }

    @Test
    public void onTickTest() {
        WaveManager MockWm = mock(WaveManager.class);
        EnemyWave mockWave = mock(EnemyWave.class);
        MockWm.addWave(mockWave);
        MockWm.addWave(testWaveTwo);

        MockWm.onTick(0);
        //verify(mockWave, times(1)).tickAction();

    }

    @Test
    public void getActiveWaveTest() {
        Assert.assertTrue("there is no active wave after initialization", wm.getActiveWave()!=null);
        Assert.assertEquals("Active wave not the first wave after initialization", testWaveOne, wm.getActiveWave());
        wm.getActiveWave().setWaveState(WaveState.FINISHED);
        Assert.assertTrue("null was not returned when no active waves", wm.getActiveWave()==null);
        wm.getWaves().get(1).setWaveState(WaveState.ACTIVE);
        Assert.assertEquals("an array set to ACTIVE not returned when it should have been", testWaveTwo, wm.getActiveWave());
    }

    @Test
    public void areWavesCompletedTest() {
        Assert.assertFalse("WaveManager thinks waves are all completed when not", wm.areWavesCompleted());
        //Remove waves to simulate incrementing waveIndex
        wm.getWaves().remove(0);
        wm.getWaves().remove(0);
        Assert.assertTrue("WaveManager things waves are not completed when they are", wm.areWavesCompleted());
    }

    @Test
    public void getWaveIndexTest() {
        Assert.assertEquals("getWaveIndex returned incorrect value", wm.getWaveIndex(), 0);
    }

    @Test
    public void getTimeBeforeNextWaveTest() {
        //Time before waves is set to 800
        Assert.assertEquals("Time before next wave not correct", 800-0, wm.getTimeBeforeNextWave());
    }
}
