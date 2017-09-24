package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.GameTime;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;

import java.util.ArrayList;

/**
 * Wave manager acts as a controller for the games waves of enemies. It's primary
 * function is to hold a queue of individual enemy waves and schedule them to
 * create the flow of wave - break, and so on. Through wave manager you can determine
 * the progress and state of the game.
 *
 * @author craig
 */
public class WaveManager extends Manager implements TickableManager, ForWorld {

    private ArrayList<EnemyWave> waves;
    private int waveIndex = 0;
    private int timeBetweenWaves = 800;
    private int elapsedTime = 0;

    /**
     * Create a new instance of WaveManager
     */
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
    }

    /**
     * Get the list of all waves from WaveManager
     *
     * @return ArrayList of all EnemyWaves known to manager
     * */
    public ArrayList<EnemyWave> getWaves() { return waves; }

    /**
     * Every game tick check if there is an active wave and if so tell it
     * to perform it's actions. If no active waves but still waves in queue,
     * give player a small break and then activate the next wave in queue.
     *
     * @param i the current game tick
     */
    public void onTick(long i) {
        if ((getActiveWave()) != null) {
            getActiveWave().tickAction();
        } else if (getWaveIndex()+1 < getWaves().size()) {
            //there are still more waves
            incrementTime();
            if (getElapsedTime() > timeBetweenWaves) {
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
     * Activate a wave to set its intention to spawn enemies.
     * Two waves cannot be turned on at the same time.
     *
     * @param wave the wave wanted to be activated
     * */
    public void activateWave(EnemyWave wave) {
        //Prevent from activating two waves at the same time
        if (getActiveWave() == null) {
            wave.setWaveState(WaveState.ACTIVE);
        }
    }

    /***
     * Check to see if all waves given to WaveManger have been
     * completed
     *
     * @return true if all waves given to WaveManager have been completed
     */
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

    /**
     * @return the time before next wave begins
     */
    public int getTimeBeforeNextWave() { return timeBetweenWaves -  getElapsedTime(); }

    /**
     * @return the time elapsed since the start of a wave
     */
    private int getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Resets the waveManagers internal wave timer
     */
    private void resetTime() {
        elapsedTime = 0;
    }

    /**
     * Increment the waveManagers internal wave timer
     */
    private void incrementTime() {
        elapsedTime += 1;
    }
}
