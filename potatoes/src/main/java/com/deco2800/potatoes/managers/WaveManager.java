package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.GameTime;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;

import java.util.ArrayList;

public class WaveManager extends Manager implements TickableManager {

    private ArrayList<EnemyWave> waves;
    private EnemyWave activeWave;
    private int waveIndex = 0;
    private int timeBetweenWaves = 800;
    private int elapsedTime = 0;

    public WaveManager() {
        waves = new ArrayList<>();
    }

    /**
     * Add a wave to the list of waves held by WaveManager
     * */
    public void addWave(EnemyWave wave) {
        waves.add(wave);
        //Activate first wave - there is a better way to do this
        if (getWaves().size() == 1) {
            activateWave(waves.get(0));
        }
        activeWave = getActiveWave();
    }

    /**
     * Get the list of all waves from WaveManager
     *
     * @return ArrayList of all EnemyWaves known to manager
     * */
    public ArrayList<EnemyWave> getWaves() { return waves; }

    public void onTick(long i) {
        //activeWave.onTick(i);
        if ((getActiveWave()) != null) {
            //System.err.println(getWaves().get(getWaveIndex()).getWaveState());
            getActiveWave().onTick(i);
        } else if (getWaveIndex()+1 < getWaves().size()) {
            //there are still more waves
            countTime();
            //System.err.println("waiting time: " + getElapsedTime());
            if (getElapsedTime() > timeBetweenWaves) {
                ///System.err.println("Made it past time between waves: " + getElapsedTime());
                waveIndex++;
                activateWave(waves.get(getWaveIndex()));
                resetTime();
            }
        }
    }

    /**
     * Gets the active wave in set of waves added to the manager.
     * Only one wave can be active at a single time. Returns null if no active waves.
     *
     * @return the active EnemyWave
     * */
    public EnemyWave getActiveWave() {
        for (EnemyWave wave: waves) {
            if (wave.getWaveState() == WaveState.ACTIVE) {
                return wave;
            }
        }
        //No active waves
        return null;
    }

    /**
     * Start a wave spawn enemies. Two waves cannot be turned on at the same
     * time.
     *
     * @param wave the wave wanted to be activated
     * */
    public void activateWave(EnemyWave wave) {
        //Prevent from activating two waves at the same time
        if (getActiveWave() == null) {
            wave.setWaveState(WaveState.ACTIVE);
        }
    }

    public boolean areWavesCompleted() {
        if ((getWaveIndex()+1) < getWaves().size()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get the position of currently active or (if no current active waves) the last active
     * wave in wavelist
     *
     * @return waveIndex the most recently/currently active wave*/
    public int getWaveIndex() { return waveIndex; }


    public int getTimeBeforeNextWave() { return timeBetweenWaves -  getElapsedTime(); }

    /**
     * Set a wave to its finished state - no more enemies.
     * */
    public void finishActiveWave() {
        getActiveWave().setWaveState(WaveState.FINISHED);
    }

    /**
     * Set a wave to be paused - allows to resume from the middle of a wave.
     * */
    public void pauseActiveWave() {
        getActiveWave().setWaveState(WaveState.PAUSED);
    }

    private int getElapsedTime() {
        return elapsedTime;
    }

    private void resetTime() {
        elapsedTime = 0;
    }

    private void countTime() {
        elapsedTime += 1;
    }
}
