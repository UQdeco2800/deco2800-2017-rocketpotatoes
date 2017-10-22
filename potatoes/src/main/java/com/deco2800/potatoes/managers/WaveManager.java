package com.deco2800.potatoes.managers;

import com.deco2800.potatoes.entities.enemies.EnemyGate;
import com.deco2800.potatoes.entities.resources.*;
import com.deco2800.potatoes.util.WorldUtil;
import com.deco2800.potatoes.waves.EnemyWave;
import com.deco2800.potatoes.waves.EnemyWave.WaveState;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.worlds.terrain.Terrain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Wave manager acts as a controller for the games waves of enemies. It's primary
 * function is to hold a queue of individual enemy waves and schedule them to
 * create the flow of wave - break, and so on. Through wave manager you can determine
 * the progress and state of the game.
 *
 * @author craig
 */
public class WaveManager extends Manager implements TickableManager, ForWorld {
    public static final int EASY = 1, MEDIUM = 2, HARD = 3;

    // Default is that there will be no campaign and the difficulty will be medium
    private boolean isCampaign = false;
    private int difficulty = MEDIUM;

    private LinkedList<EnemyWave> waves;
    private int waveIndex = 0;
    private int timeBetweenWaves = 800;
    private int elapsedTime = 0;

    /**
     * Create a new instance of WaveManager
     */
    public WaveManager() {
        // most of the time waves are created on the fly, history should be preserved though
        // LinedList offers better performance for this, will be slower when loading campaigns initially
        waves = new LinkedList<EnemyWave>();
    }

    /**
     * Start a regular game where waves are randomly generated based off difficulty.
     * @param difficulty one of the difficults given in this class,
     *                   EASY, MEDIUM or HARD.
     */
    public void regularGame(int difficulty) {
        isCampaign = false;
        if (!(EASY <= difficulty && difficulty <= HARD)) {
            throw new IndexOutOfBoundsException("Difficult is not defined in range!");
        }
        addWave(new EnemyWave(1500)); // pause wave to bootstrap startup
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
    public List<EnemyWave> getWaves() { return waves; }

    /**
     * Every game tick check if there is an active wave and if so tell it
     * to perform it's actions. If no active waves but still waves in queue,
     * give player a small break and then activate the next wave in queue.
     *
     * @param i the current game tick
     */
    @Override
	public void onTick(long i) {
        if (getActiveWave() != null) { // active wave
            getActiveWave().tickAction();
        } else if (waveIndex < waves.size() - 1) {
            //there are still more waves
            waveIndex++;
            activateWave(waves.get(waveIndex));
            resetTime();
            
            // Add resources to other worlds
            addResources();
        } else if (!isCampaign && WorldUtil.getClosestEntityOfClass(EnemyGate.class, 10, 10).isPresent()) { // no active wave, not campaign, make new wave
            generateNextWave();
        }
    }
    
    /**
     * Adds resources to random locations in the worlds
     */
    private void addResources() {
    	// World manager
    	WorldManager worldManager = GameManager.get().getManager(WorldManager.class);
    	
    	// The different world types
    	World worlds[] = {worldManager.getWorld(WorldType.DESERT_WORLD),
    			worldManager.getWorld(WorldType.ICE_WORLD),
    			worldManager.getWorld(WorldType.VOLCANO_WORLD),
    			worldManager.getWorld(WorldType.OCEAN_WORLD)};
    	
    	// Array of resources to add
    	Resource resources[] = {new CactusThornResource(), new PricklyPearResource(),
    			new TumbleweedResource(), new IceCrystalResource(), new SnowBallResource(),
    			new SealSkinResource(), new BonesResource(), new CoalResource(),
    			new ObsidianResource(), new FishMeatResource(), new PearlResource(),
    			new TreasureResource()};

    	// locations to add the resources
    	int xPos;
    	int yPos;
    	
    	// Terrain to add the resource to
    	Terrain terrain;
    	
    	// Iterate over the three different resources
    	for (int i = 0; i < 3; i ++) {
    		// Attempt to add 10 of each resource
    		for (int j = 0; j < 10; j++) {
    			// Generate random location
    			xPos = (int) (Math.random() * 40) + 10;
    			yPos = (int) (Math.random() * 40) + 10;
    			
    			// Iterate over the 4 worlds
    			for (int k = 0; k < 4; k++) {
    				// Get the terrain type at the random location
        			terrain = worlds[k].getTerrain(xPos, yPos);
        			
        			// Only add the resource if it isn't over water
        			if (terrain.getTexture() != "water_tile_1") {
        				// Add the resource to the world. Offset the resource array index to the
        				// resources relevant to each world.
        				worlds[k].addEntity(new ResourceEntity(xPos, yPos, resources[i + 3 * k]));
        			}
        			
    			}			  			    			
    		
    		}
    	
    	}
    	
    }

    /**
     * Creates next wave based off the wave number, difficulty previous wave in set.
     */
    private void generateNextWave() {
        int roundTime = 750 + waveIndex * 75;
        int spawnRates = 1  + (waveIndex % 4) * difficulty;
        switch (waveIndex % 4) {
            case 0:
                addWave(new EnemyWave(spawnRates, 0, 0, 0, roundTime, waveIndex));
                break;
            case 1:
                addWave(new EnemyWave(0, spawnRates, 0, 0, roundTime, waveIndex));
                break;
            case 2:
                addWave(new EnemyWave(600 + waveIndex * 75)); // pause wave
                break;
            case 3:
                addWave(new EnemyWave(spawnRates, spawnRates, spawnRates, spawnRates, roundTime,
                        waveIndex));
                break;
            default: // this should not be reached
                addWave(new EnemyWave(1500)); // pause wave
                break;
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
            WaveState waveState = wave.getWaveState();
            if (waveState == WaveState.ACTIVE) {
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
        return !(getWaveIndex()+1 < getWaves().size());
    };

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
