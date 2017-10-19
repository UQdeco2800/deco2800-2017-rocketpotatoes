package com.deco2800.potatoes.waves;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.WaveManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class WaveLoader {

    private String filename;
    public WaveLoader(String filename){
        this.filename = filename;
    }

    public EnemyWave createwavefromline(String line){

        int squirrelRate = 0;
        int speedyRate = 0;
        int tankRate = 0;
        int mooseRate = 0;
        int waveLength = 0;
        int round_number = 0;

        // Extract value for each variable from string
        String[] WaveArray = line.split(", ");
        squirrelRate = Integer.parseInt(WaveArray[0]);
        speedyRate = Integer.parseInt(WaveArray[1]);
        tankRate = Integer.parseInt(WaveArray[2]);
        mooseRate = Integer.parseInt(WaveArray[3]);
        waveLength = Integer.parseInt(WaveArray[4]);
        round_number = Integer.parseInt(WaveArray[5]);
        
        EnemyWave returnWave = new EnemyWave(squirrelRate, speedyRate, tankRate, mooseRate, waveLength, round_number);
        return returnWave;
    }

    public void LoadWave(){

        File f = new File(filename);

        try {
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readline = "";

            while ((readline = b.readLine()) != null){
            GameManager.get().getManager(WaveManager.class).addWave(createwavefromline(readline));
            }
        } catch (java.io.IOException e) {
            System.out.println("Error reading file");
        }




    }

}
