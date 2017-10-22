package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.resources.*;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.WaveLoader;
import com.deco2800.potatoes.worlds.DesertWorld;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;



public class WaveManagerTest {

    WaveManager wm;
    WaveLoader wl;
    EnemyWave testWaveOne = new EnemyWave(1,1,1,1,750);
    EnemyWave testWaveTwo = new EnemyWave(2,1,1,2,750);
    World mockWorld;
    GameManager gm;

    @Before
    public void setUp() throws Exception {
        mockWorld = mock(World.class);
        gm = GameManager.get();
        gm.setWorld(mockWorld);
        wm = gm.getManager(WaveManager.class);
        wm.addWave(testWaveOne);
        wm.addWave(testWaveTwo);
    }

    @After
    public void tearDown() {

        mockWorld = null;
        gm.clearManagers();
        gm = null;
        testWaveOne = null;
        testWaveTwo = null;
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
    
    @Test
    public void addResourceTest() {
    	// Variable that holds if a resources if found in a world
    	boolean desertResources = false;
    	boolean iceResources = false;
    	boolean volcanoResources = false;
    	boolean oceanResources = false;
    	
    	WorldManager worldManager = gm.getManager(WorldManager.class);
    	
    	// The worlds to check
    	World desertWorld = worldManager.getWorld(DesertWorld.get());
    	World iceWorld = worldManager.getWorld(DesertWorld.get());
    	World volcanoWorld = worldManager.getWorld(DesertWorld.get());
    	World oceanWorld = worldManager.getWorld(DesertWorld.get());
    	
    	// Shorter waves to add to the wave manager
    	EnemyWave waveOne = new EnemyWave(1,1,1,1,1);
        EnemyWave waveTwo = new EnemyWave(2,1,1,2,1);

        // Remove old waves
        wm.getWaves().remove(0);
        wm.getWaves().remove(0);
        
        // Add new waves
        waveOne.setCurrentWaveTime(2);
        wm.addWave(waveOne);
        wm.addWave(waveTwo);
        
        // Simulate game ticks so the wave changes
        wm.onTick(1);
        wm.onTick(1);
        
        // Check if resources have been added to each world
        desertResources = checkContains(desertWorld.getEntities().values());
        iceResources = checkContains(iceWorld.getEntities().values());
        volcanoResources = checkContains(volcanoWorld.getEntities().values());
        oceanResources = checkContains(oceanWorld.getEntities().values());
        
        assertTrue(desertResources);
        assertTrue(iceResources);
        assertTrue(volcanoResources);
        assertTrue(oceanResources);
    }
    
    /**
     * Checks if resources are part of a collection
     * @param values
     * 			The collection of abstract entities to check
     * @return
     * 			True if a resource is in the collection, otherwise false
     */
    private boolean checkContains(Collection<AbstractEntity> values) {
    	for (AbstractEntity entity : values) {
     	   if (entity.getClass() == ResourceEntity.class) {
     		   return true;
     	   }
     	   
        }
    	
    	return false;
    	
    }

    @Test
    public void extraTest() {
        testWaveOne = new EnemyWave(1,2,3,4,5,6);
        testWaveOne = new EnemyWave(6) ;
        wm.regularGame(2);
        wm.onTick(1);
        wm.onTick(1);
        wm.onTick(1);
    }

    @Test
    public void loaderTest() {
        wl = new WaveLoader("filename");
        wl.createwavefromline("1, 2, 3, 4, 5, 6, 7");
    }
}
