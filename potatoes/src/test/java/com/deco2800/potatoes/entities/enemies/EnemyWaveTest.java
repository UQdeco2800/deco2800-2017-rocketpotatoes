package com.deco2800.potatoes.entities.enemies;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WaveManager;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;
import com.deco2800.potatoes.worlds.World;
import static org.mockito.Mockito.*;

public class EnemyWaveTest {
	EnemyWave firstWave;
	int waveLength = 300;

	@Before
	public void setUp() throws Exception {
		firstWave = new EnemyWave(4, 3, 2, 1, waveLength);
		WaveManager waveManager = new WaveManager();

		World mockWorld = mock(World.class);
		GameManager gameManager = GameManager.get();
		gameManager.setWorld(mockWorld);
		gameManager.addManager(waveManager);
		gameManager.get().getManager(WaveManager.class).addWave(firstWave);
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    	firstWave = null;
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
		EnemyWave spawnWave = new EnemyWave(1,1,100,1,750);
		int numberOfEnemies = 250;	//THIS IS SAMPLE SIZE - NEEDS TO BE SUFFICIENTLY LARGE
		int errorRate = 10;
		int rangeBoundary = numberOfEnemies/errorRate;
		for (int i = 0; i < numberOfEnemies; i++) {
			spawnWave.spawnEnemyToRatio(spawnWave.getEnemyRatios());
		}
		float expectedSquirrels = (spawnWave.getEnemyRatios()[0])*numberOfEnemies;
		float expectedRaccoons = (spawnWave.getEnemyRatios()[1]-spawnWave.getEnemyRatios()[0])*numberOfEnemies;
		float expectedBears = (spawnWave.getEnemyRatios()[2]-spawnWave.getEnemyRatios()[1])*numberOfEnemies;
		float expectedMoose = (spawnWave.getEnemyRatios()[3]-spawnWave.getEnemyRatios()[2])*numberOfEnemies;

		/*See enemies produced are +/-errorRate% of totalEnemies as an indicator of enemies not being produced
		according to ratio correctly.*/
		Assert.assertTrue("number of squirrels exceeds their defined ratio by a very large margin",
				(expectedSquirrels-rangeBoundary < spawnWave.getEnemyCounts()[0])
						&& (spawnWave.getEnemyCounts()[0] < expectedSquirrels+rangeBoundary));
		Assert.assertTrue("number of squirrels exceeds their defined ratio by a very large margin",
				(expectedRaccoons-rangeBoundary < spawnWave.getEnemyCounts()[1])
						&& (spawnWave.getEnemyCounts()[1] < expectedRaccoons+rangeBoundary));
		Assert.assertTrue("number of squirrels exceeds their defined ratio by a very large margin",
				(expectedBears-rangeBoundary < spawnWave.getEnemyCounts()[2])
						&& (spawnWave.getEnemyCounts()[2] < expectedBears+rangeBoundary));
		Assert.assertTrue("number of squirrels exceeds their defined ratio by a very large margin",
				(expectedMoose-rangeBoundary < spawnWave.getEnemyCounts()[3])
						&& (spawnWave.getEnemyCounts()[3] < expectedMoose+rangeBoundary));

		}

	@Test
	public void tickAction() {
		int[] beforeEnemyCount = {0, 0, 0, 0};
		firstWave.setWaveState(WaveState.ACTIVE);
		firstWave.setCurrentWaveTime(waveLength+1); //current time exceeds maximum wave time
		firstWave.tickAction(); 	//do a tick action
		Assert.assertEquals("WaveState not finished after elapse time has exceeded wave length", WaveState.FINISHED, firstWave.getWaveState());

		firstWave.setWaveState(WaveState.ACTIVE);
		firstWave.setCurrentWaveTime(149);		//set the time to be spawning time
		firstWave.tickAction();
		//Assert that an enemy has been added
		//Assert.assertThat(firstWave.getEnemyCounts(), IsNot.not(IsEqual.equalTo(beforeEnemyCount)))
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
		int timeChange = 2;
		firstWave.setCurrentWaveTime(timeChange);
		Assert.assertEquals("time to end of wave is not correct", waveLength-timeChange, firstWave.getTimeToEnd());
	}
	
	@Test
	public void getWavelengthTest() {
		Assert.assertEquals("Wave length is not correct", waveLength, firstWave.getWaveLength());
	}
	
	@Test
	public void setWaveLengthTest() {
		firstWave.setWaveLength(5);
		Assert.assertEquals("Wave length was not set correctly", 5, firstWave.getWaveLength());
	}

	@Test
	public void getSpawnRateTest() {
		//Assert.assertEquals("Spawn rate not returning correct value", 75, firstWave.getSpawnRate());
	}
	
	@Test
	public void getWaveStateTest() {
		Assert.assertEquals("wave state is wrong", WaveState.ACTIVE, firstWave.getWaveState() );
	}

	@Test
	public void setWaveStateTest() {
		firstWave.setWaveState(WaveState.PAUSE);
		Assert.assertEquals("wave state was not set correctly", WaveState.PAUSE,firstWave.getWaveState() );
	}
}
