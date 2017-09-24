package com.deco2800.potatoes.entities.enemies;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WaveManager;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;
import com.deco2800.potatoes.worlds.World;



public class EnemyWaveTest {
	EnemyWave firstWave;
	
	@Before
	public void setUp() {
		firstWave = new EnemyWave(4, 3, 2, 1, 3);
		GameManager.get().setWorld(new TestWorld());
		GameManager.get().getManager(WaveManager.class).addWave(firstWave);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
	
	@Test
	public void spawnEnemyToRatioTest() {
		firstWave.spawnEnemyToRatio(firstWave.getEnemyRatios());
		
	}
	
	@Test
	public void elepsedWaveTimeTest() {
		Assert.assertEquals("elapsedWaveTime is not 0 ", 0, firstWave.elapsedWaveTime());
	}
	
	@Test
	public void setCurrentWaveTimeTest() {
		firstWave.setCurrentWaveTime(4);
		Assert.assertEquals("wave time was not set correctly", 4, firstWave.elapsedWaveTime());
	}
	
	@Test
	public void getTimeToEnd() {
		firstWave.setCurrentWaveTime(2);
		Assert.assertEquals("time to end of wave is not correct", 1, firstWave.getTimeToEnd());
	}
	
	@Test
	public void getWavelengthTest() {
		Assert.assertEquals("Wave length is not correct", 3, firstWave.getWaveLength());
	}
	
	@Test
	public void setWaveLengthTest() {
		firstWave.setWaveLength(5);
		Assert.assertEquals("Wave lenth was not set correctly", 5, firstWave.getWaveLength());
	}
	
	@Test
	public void getWaveStateTest() {
		Assert.assertEquals("wave state is wrong", WaveState.ACTIVE,firstWave.getWaveState() );
	}
	

	@Test
	public void setWaveStateTest() {
		firstWave.setWaveState(WaveState.PAUSED);
		Assert.assertEquals("wave state was not set correctly", WaveState.PAUSED,firstWave.getWaveState() );
	}
	
	private class TestWorld extends World {
	}
	
}
