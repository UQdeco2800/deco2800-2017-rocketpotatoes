package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.GameTime;
import com.deco2800.potatoes.waves.EnemyWave;

import java.util.ArrayList;

public class WaveManager extends Manager implements TickableManager {

    private ArrayList<EnemyWave> waves;
    private EnemyWave currentWave;
    private float CurrentTime = 0;

    public WaveManager() {
        waves = new ArrayList<>();
    }

    public void addWave(EnemyWave wave) {
        waves.add(wave);
        currentWave = wave;
    }

    public ArrayList<EnemyWave> getWaves() { return waves; }

    public void onTick(long i) {
        //Tick the current wave (no concept of current wave yet)
        currentWave.onTick(i);
    }

}
