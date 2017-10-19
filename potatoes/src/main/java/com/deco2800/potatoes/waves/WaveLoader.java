package com.deco2800.potatoes.waves;

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
        int round_number;

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

}
