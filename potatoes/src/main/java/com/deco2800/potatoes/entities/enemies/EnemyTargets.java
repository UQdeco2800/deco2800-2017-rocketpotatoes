package com.deco2800.potatoes.entities.enemies;

import java.util.ArrayList;
import java.util.List;

public class EnemyTargets {

    private ArrayList<Class> mainTargets;
    private ArrayList<Class> sightAggroTargets;
    private ArrayList<Class> damageAggroTargets;

    public EnemyTargets(ArrayList<Class> mainTargets, ArrayList<Class> sightAggroTargets) {
        this.mainTargets = mainTargets;
        this.sightAggroTargets = sightAggroTargets;
        this.damageAggroTargets = damageAggroTargets;
    }

    public List<Class> getMainTargets() { return mainTargets;  }

    public List<Class> getSightAggroTargets() { return sightAggroTargets; }

}
