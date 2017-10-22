package com.deco2800.potatoes.waves;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WaveManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class WaveLoader {

    private String filename;
    public WaveLoader(String filename){
        this.filename = filename;
    }

    public EnemyWave createwavefromline(String line){

        int squirrelRate;
        int speedyRate;
        int tankRate;
        int mooseRate;
        int waveLength;
        int roundNumber;

        // Extract value for each variable from string
        String[] WaveArray = line.split(", ");
        squirrelRate = Integer.parseInt(WaveArray[0]);
        speedyRate = Integer.parseInt(WaveArray[1]);
        tankRate = Integer.parseInt(WaveArray[2]);
        mooseRate = Integer.parseInt(WaveArray[3]);
        waveLength = Integer.parseInt(WaveArray[4]);
        roundNumber = Integer.parseInt(WaveArray[5]);

        return new EnemyWave(squirrelRate, speedyRate, tankRate, mooseRate, waveLength, roundNumber);
    }

    public void loadWave(){

        File f = new File(filename);

        try {
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readline = "";

            while ((readline = b.readLine()) != null){
            GameManager.get().getManager(WaveManager.class).addWave(createwavefromline(readline));
            }
            b.close();
        } catch (java.io.IOException e) {
            System.out.println("Error reading file");
        }




    }

}
