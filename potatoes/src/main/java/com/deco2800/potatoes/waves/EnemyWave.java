package com.deco2800.potatoes.waves;

import com.deco2800.potatoes.entities.enemies.Moose;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.enemies.TankEnemy;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.World;

import java.util.*;

public class EnemyWave {

    private float[] enemyRatios;    //Length of wave in .001 of seconds
    private int waveLength;     //The current time of the wave
    private int waveTime = 0;   //Spawn rate (100 = 1 second)
    private int spawnRate = 110;     //Time counting down for gui
    private int[] enemyCounts = {0, 0, 0, 0};   //counter for squirrle:speedy:tank:moose added to wave
    private static int totalAmount = 0;
    private int roundNumber;
    private boolean isPauseWave;

    public enum WaveState {
        WAITING, ACTIVE, PAUSE, FINISHED
    }

    private WaveState waveState = WaveState.WAITING;

    /**
     * Create an EnemyWave. The rates for each enemy are relative, i.e if given 5,1,1,1 for each
     * enemyRate parameter, the relative rates of spawning for each type will be 5/8, 1/8, 1/8, 1/8.
     *
     * @param squirrelRate
     * @param speedyRate
     * @param tankRate
     * @param mooseRate
     * --can be changed to simply individual args for each enemy type - might actually be better
     * @param waveLength the length in minutes and seconds of wave (1.30) is 1 minute 30 seconds.
     * @param round_number
     * */
    public EnemyWave(int squirrelRate, int speedyRate, int tankRate, int mooseRate, int waveLength, int roundNumber) {
        this.enemyRatios = calculateEnemyRatios(squirrelRate, speedyRate, tankRate, mooseRate);
        this.waveLength = waveLength;
        this.roundNumber = roundNumber;
    }

    // Used for testing when waves not in use
    /**
     * Create an EnemyWave. The rates for each enemy are relative, i.e if given 5,1,1,1 for each
     * enemyRate parameter, the relative rates of spawning for each type will be 5/8, 1/8, 1/8, 1/8.
     *
     * @param squirrelRate
     * @param speedyRate
     * @param tankRate
     * @param mooseRate
     * --can be changed to simply individual args for each enemy type - might actually be better
     * @param waveLength the length in minutes and seconds of wave (1.30) is 1 minute 30 seconds.
     * */
    public EnemyWave(int squirrelRate, int speedyRate, int tankRate, int mooseRate, int waveLength) {
        this.enemyRatios = calculateEnemyRatios(squirrelRate, speedyRate, tankRate, mooseRate);
        this.waveLength = waveLength;
    }

    /**
     * Pause wave constructor, no enemies.
     */
    public EnemyWave(int waveLength) {
        this.enemyRatios = calculateEnemyRatios(0,0,0,0);
        this.waveLength = waveLength;
        this.isPauseWave = true;
    }

    /**
     * Create an array of floats between 0-1, where squirrelRate < speedyRate < tankRate < mooseRate.
     * The span of each value from itself to the immediate next highest ratio indicates its actual
     * ratio, i.e. if speedy is .50 and tank is .75, actual ratio is .25.
     *
     * @return an array of floats representing the ratio of enemy rates provided
     * */
    public float[] calculateEnemyRatios(float squirrelRate, float speedyRate, float tankRate, float mooseRate) {
        float total = squirrelRate + speedyRate + tankRate + mooseRate;
        // Ratios are the total spans of each; i.e. if speedyRatio is .50 and tank is .75, actual ratio is .25.
        float squirrelRatio = squirrelRate/total;
        float speedyRatio = squirrelRatio + speedyRate/total;
        float tankRatio = speedyRatio + tankRate/total;
        float mooseRatio = tankRatio + mooseRate/total;
        float[] ratios = {squirrelRatio, speedyRatio, tankRatio, mooseRatio};
        return ratios;
    }

    /***
     * Spawn enemies according to the ratio described by the output of calculateEnemyRatios. Adds enemies to enemy
     * counts
     *
     * @param enemyRatios Array of ratios of each enemy type in game where enemyRatio[i] is < enemyRatio[i+1] and
     *                    sum to 1.
     */
    public void spawnEnemyToRatio(float[] enemyRatios) {
        Random random = new Random();
            float randomFloat = random.nextFloat();
            if (randomFloat < enemyRatios[0]) {
                addSquirrel();
                enemyCounts[0]++;
            } else if (randomFloat < enemyRatios[1]) {
                addSpeedy();
                enemyCounts[1]++;
            } else if (randomFloat < enemyRatios[2]) {
                addTank();
                enemyCounts[2]++;
            } else if (randomFloat < enemyRatios[3]) {
                addMoose();
                enemyCounts[3]++;
            }
    }

    /*Likely to be expanded along actions for waiting, paused and finished at a later point*/
    /***
     * Perform actions depending on the current state of the wave.
     * Currently do nothing unless in ACTIVE state.
     *
     */
    public void tickAction() {
        switch (getWaveState()) {
/*            case WAITING:
                //Do nothing*/
            case PAUSE:
                break;
            case ACTIVE:
                setCurrentWaveTime(elapsedWaveTime() + 1);
                if (elapsedWaveTime() > getWaveLength()) {
                    setWaveState(WaveState.FINISHED);
                } else if (elapsedWaveTime() % spawnRate == 0) {
                    spawnEnemyToRatio(getEnemyRatios());
                }
                //Check to see if wave is paused for some reason
                break;
        }
    }

    /**
     * @return if the wave is a 'pause' wave
     */
    public boolean isPauseWave() { return this.isPauseWave; }

    /**
     * @return the time elapsed since wave started in 0.001 seconds
     */
    public int elapsedWaveTime() { return waveTime; }

    /**
     * Sets the current wave Time.
     * @param CurrentTime
     */
    public void setCurrentWaveTime(int currentTime) { this.waveTime = currentTime; }

    /**
     * @return Amount of time left in current wave
     */
    public int getTimeToEnd() { return getWaveLength()-elapsedWaveTime(); }

    /**
     * 
     * @return total amount of enemies in this wave
     */
    public static int getTotalEnemies() {
    	return totalAmount;
    }
    
    public static void reduceTotalEnemiesByOne(){
    	totalAmount--;	
    }
    
    /**
     * Add a squirrel to the world
     */
    private static void addSquirrel() {
        GameManager.get().getWorld().addEntity(new Squirrel(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE /
                8f));
        GameManager.get().getWorld().addEntity(new Squirrel(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE
                - WorldManager.WORLD_SIZE / 8f));
        GameManager.get().getWorld().addEntity(new Squirrel(WorldManager.WORLD_SIZE / 8f, WorldManager
                .WORLD_SIZE / 2f));
        GameManager.get().getWorld().addEntity(new Squirrel(WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8f,
                WorldManager.WORLD_SIZE / 2f));
        totalAmount += 4;
    }

    /**
     * Add a tank (bear) enemy to the world
     */
    private static void addTank() {
        GameManager.get().getWorld().addEntity(new TankEnemy(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE /
                8f));
        GameManager.get().getWorld().addEntity(new TankEnemy(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE
                - WorldManager.WORLD_SIZE / 8f));
        GameManager.get().getWorld().addEntity(new TankEnemy(WorldManager.WORLD_SIZE / 8f, WorldManager
                .WORLD_SIZE / 2f));
        GameManager.get().getWorld().addEntity(new TankEnemy(WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8f,
                WorldManager.WORLD_SIZE / 2f));
        totalAmount += 4;
    }

    /**
     * Add a speedy (raccoon) enemy to the world
     */
    private static void addSpeedy() {
        GameManager.get().getWorld().addEntity(new SpeedyEnemy(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE /
                8f));
        GameManager.get().getWorld().addEntity(new SpeedyEnemy(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE
                - WorldManager.WORLD_SIZE / 8f));
        GameManager.get().getWorld().addEntity(new SpeedyEnemy(WorldManager.WORLD_SIZE / 8f, WorldManager
                .WORLD_SIZE / 2f));
        GameManager.get().getWorld().addEntity(new SpeedyEnemy(WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8f,
                WorldManager.WORLD_SIZE / 2f));
        totalAmount += 4;

    }

    /**
     * Add a moose to the world
     */
    private static void addMoose() {
        GameManager.get().getWorld().addEntity(new Moose(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE /
                8f));
        GameManager.get().getWorld().addEntity(new Moose(WorldManager.WORLD_SIZE / 2f, WorldManager.WORLD_SIZE
                - WorldManager.WORLD_SIZE / 8f));
        GameManager.get().getWorld().addEntity(new Moose(WorldManager.WORLD_SIZE / 8f, WorldManager
                .WORLD_SIZE / 2f));
        GameManager.get().getWorld().addEntity(new Moose(WorldManager.WORLD_SIZE - WorldManager.WORLD_SIZE / 8f,
                WorldManager.WORLD_SIZE / 2f));
        totalAmount += 4;
    }

    /**
     * @return The total time length of this wave in approx 1/10s of a second
     */
    public int getWaveLength() { return waveLength; }

    /**
     * Set the total time length in unit 1/10s of a second of this wave
     *
     * @param waveLength time
     */
    public void setWaveLength(int waveLength) { this.waveLength = waveLength; }

    /**
     * Get the WaveState describing the wave, i.e. WAITING, PAUSED, ACTIVE or FINISHED.
     * @return
     */
    public WaveState getWaveState() { return waveState; }

    /**
     * Set the WaveState of the wave according to the WaveState type
     *
     * @param state WaveState value; WAITING, PAUSED, ACTIVE or FINISHED
     */
    public void setWaveState(WaveState state) { this.waveState = state; }

    /***
     *
     * @return the array of enemyRatios that describe the composition of the wave
     */
    public float[] getEnemyRatios(){ return this.enemyRatios;}

    /**
     * @return the rate in which enemies spawn in this wave
     */
    public int getSpawnRate() { return this.spawnRate; }

    /**
     * @return an array of the counts of squirrels : speedy : tank : moose enemies held by this wave
     */
    public int[] getEnemyCounts() { return this.enemyCounts; }
}

