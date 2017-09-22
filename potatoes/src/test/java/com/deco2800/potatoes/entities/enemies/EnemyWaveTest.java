package com.deco2800.potatoes.entities.enemies;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WaveManager;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.worlds.World;

public class EnemyWaveTest {
	EnemyWave firstWave;
	
	@Before
	public void setUp() {
		firstWave = new EnemyWave(4, 3, 2, 1, 1);
		GameManager.get().setWorld(new TestWorld());
		GameManager.get().getManager(WaveManager.class).addWave(firstWave);
	}
	
	@Test
	public void spawnEnemyToRatioTest() {
		firstWave.spawnEnemyToRatio(firstWave.getEnemyRatios());
		
	}
	
	
	
	private class TestWorld extends World {
	}
	
}
