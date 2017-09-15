package com.deco2800.potatoes.waves;

import com.deco2800.potatoes.entities.enemies.Moose;
import com.deco2800.potatoes.entities.enemies.SpeedyEnemy;
import com.deco2800.potatoes.entities.enemies.Squirrel;
import com.deco2800.potatoes.entities.enemies.TankEnemy;
import com.deco2800.potatoes.managers.GameManager;
import java.util.*;

import static com.badlogic.gdx.math.MathUtils.random;

public class EnemyWave {

    //The current elapsed time into the awave
    private int elapsedTime;
    private float[] enemyRatios;

    /**
     * Create the enemy wave.
     *
     *
     * @param squirrelRate
     * @param speedyRate
     * @param tankRate
     * @param mooseRate
     * --can be changed to simply individual args for each enemy type - might actually be better
     * @param waveTime the length in minutes and seconds of wave (1.30) is 1 minute 30 seconds.
     * */
    public EnemyWave(int squirrelRate, int speedyRate, int tankRate, int mooseRate, float waveTime) {
        //addSquirrel();
        //addSquirrel();
        this.enemyRatios = getEnemyRatio(squirrelRate, speedyRate, tankRate, mooseRate);
        spawnEnemyToRatio(enemyRatios);

    }

    /**
     * Spawn enemies at ratio given in enemy ratio
     * */
    private float[] getEnemyRatio(float squirrelRate, float speedyRate, float tankRate, float mooseRate) {
        float total = squirrelRate + speedyRate + tankRate + mooseRate;
        // These are 'positional ratios'
        // Ratios are the spans of each; i.e. if speedyRatio is .50 and tank is .75, actual ratio is .25.
        float squirrelRatio = squirrelRate/total;
        float speedyRatio = squirrelRatio + speedyRate/total;
        float tankRatio = speedyRatio + tankRate/total;
        float mooseRatio = tankRatio + mooseRate/total;
        float[] enemyRatios = {squirrelRatio, speedyRatio, tankRatio, mooseRatio};
        return enemyRatios;
    }


    /*Right now just makes 10 enemies according to the rate, but want to incorporate game time
    to this. So maybe in the future take away the for loop and just have it generate one enemy.
    then we give this to the gametime counter (or onTick method) and run this method every x
    amount of time until the game wave is over.
    */
    /**
     * Spawn enemies according to the ratio defined in the constructor
     * */
    private void spawnEnemyToRatio(float[] enemyRatios) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            float randomFloat = random.nextFloat();

            if (randomFloat < enemyRatios[0]) {
                addSquirrel();
            } else if (randomFloat < enemyRatios[1]) {
                addSpeedy();
            } else if (randomFloat < enemyRatios[2]) {
                addTank();
            } else if (randomFloat < enemyRatios[3]) {
                addMoose();
            }

        }
    }



    private void addSquirrel() {
        GameManager.get().getWorld().addEntity(new Squirrel(
                    10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
    }

    private void addTank() {
        GameManager.get().getWorld().addEntity(
                new TankEnemy(15 + random.nextFloat()*10, 20 + random.nextFloat()*10, 0));
    }

    private void addSpeedy() {
        GameManager.get().getWorld().addEntity(
                new SpeedyEnemy(24+random.nextFloat()*10, 20+random.nextFloat()*10, 0));

    }

    private void addMoose() {
        GameManager.get().getWorld().addEntity(new Moose(
                10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
    }

    private void addMultipleSquirrels(int squirrelCount) {
            for (int i = 0; i < squirrelCount; i++) {
                GameManager.get().getWorld().addEntity(new Squirrel(
                        10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
            }
        }

    private void addMultipleTanks(int tankCount) {
        for (int i = 0; i < tankCount; i++) {
            GameManager.get().getWorld().addEntity(
                    new TankEnemy(15 + random.nextFloat()*10, 20 + random.nextFloat()*10, 0));
        }
    }

    private void addMultipleMoose(int mooseCount) {
        for (int i = 0; i < mooseCount; ++i) {
            GameManager.get().getWorld().addEntity(new Moose(
                    10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
        }
    }

    private void addMultipleSpeedy(int speedyCount) {
        for(int i=0 ; i<speedyCount ; i++) {
            GameManager.get().getWorld().addEntity(
                    new SpeedyEnemy(24+random.nextFloat()*10, 20+random.nextFloat()*10, 0));
        }
    }

}

