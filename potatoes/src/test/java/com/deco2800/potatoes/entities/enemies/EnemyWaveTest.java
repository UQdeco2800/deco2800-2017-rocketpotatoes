package com.deco2800.potatoes.entities.enemies;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WaveManager;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;
import com.deco2800.potatoes.worlds.World;
import static org.mockito.Mockito.*;

public class EnemyWaveTest {
	EnemyWave firstWave;

	@Before
	public void setUp() {
		firstWave = new EnemyWave(4, 3, 2, 1, 3);
		WaveManager waveManager = new WaveManager();

		World mockWorld = mock(World.class);
		GameManager gameManager = GameManager.get();
		gameManager.setWorld(mockWorld);
		gameManager.addManager(waveManager);
		gameManager.get().getManager(WaveManager.class).addWave(firstWave);
	}
	
	@Test
	public void calculateEnemyRatiosTest() {
		float [] testRatio;
		EnemyWave testWave = new EnemyWave(1,1,1,1,3);
		testRatio = testWave.getEnemyRatios();
		float[] expectedArray = {0.25f, 0.50f, 0.75f, 1f};
		Assert.assertArrayEquals("enemy ratio is incorrect", expectedArray, (testRatio), 0);
		//Assert that ratios accumulate to 1f.
		Assert.assertEquals("enemy ratios don't accumulate to 1f", 1f,
				((testRatio[3]-testRatio[2]) + (testRatio[2]-testRatio[1]) + (testRatio[1]-testRatio[0]) + (testRatio[0]-0)), 0);

		//Assert that ratios follow pattern squirrel <= speedy <= tank <= moose.
		EnemyWave secondTestWave = new EnemyWave(8,2,33,99,3);
		testRatio = secondTestWave.getEnemyRatios();
		Assert.assertTrue("less than or equal to relationship for i++ doesn't occur",
				((testRatio[0] <= testRatio[1]) && (testRatio[1] <= testRatio[2]) &&
						(testRatio[2] <= testRatio[3])));
	}

	@Test
	public void spawnEnemyToRatioTest() {
		//firstWave.setWaveState(WaveState.ACTIVE);
	}


/*	@Test
	public void tickAction() {
		firstWave.setWaveState(WaveState.ACTIVE);
		if (firstWave.getWaveState() == WaveState.ACTIVE) {
			if (firstWave.elapsedWaveTime()> firstWave.getWaveLength()) {
				Assert.assertEquals("WaveState not finished after elapsed time has increments over wave length",
						firstWave.getWaveState(), WaveState.FINISHED);
			} else {
				//verify(firstWave.spawnEnemyToRatio();
			}
		}
	}
*/

	@Test
	public void tickAction() {
		firstWave.setWaveState(WaveState.ACTIVE);
		firstWave.setCurrentWaveTime(10000000); //exceeds wave time
		firstWave.tickAction(); 	//do a tick action
		Assert.assertEquals("WaveState not finished after elapse time has exceeded wave length", WaveState.FINISHED, firstWave.getWaveState());

		int[] beforeEnemyCount = firstWave.getEnemyCounts();
		firstWave.setWaveState(WaveState.ACTIVE);
		firstWave.setCurrentWaveTime(150);		//set the time to be spawning time
		firstWave.tickAction();
//		Assert.assertNotEquals("An enemy wasn't spawned when it should have been", beforeEnemyCount, firstWave.getEnemyCounts());

		//Assert.assertThat(firstWave.getEnemyCounts(), IsNot.not(IsEqual.equalTo(beforeEnemyCount)));
	}


	@Test
	public void elapsedWaveTimeTest() {
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
		Assert.assertEquals("Wave length was not set correctly", 5, firstWave.getWaveLength());
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
