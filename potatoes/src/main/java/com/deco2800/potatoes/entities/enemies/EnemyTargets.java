package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.AbstractEntity;

import java.util.ArrayList;

public class EnemyTargets {

    private static ArrayList<Class> mainTargets;
    private static ArrayList<Class> sightAggroTargets;
    private static ArrayList<Class> damageAggroTargets;

    public EnemyTargets(ArrayList<Class> mainTargets, ArrayList<Class> sightAggroTargets,
                        ArrayList<Class> damageAggroTargets) {
        this.mainTargets = mainTargets;
        this.sightAggroTargets = sightAggroTargets;
        this.damageAggroTargets = damageAggroTargets;
    }

    public ArrayList<Class> getMainTargets() { return mainTargets;  }

    public ArrayList<Class> getSightAggroTargets() { return sightAggroTargets; }

    public ArrayList<Class> getDamageAggroTargets() { return damageAggroTargets; }


    /*public void addMainTarget(AbstractEntity mainTarget) {
        mainTargets.add(mainTarget);
    }

    public ArrayList<AbstractEntity> getAggroableTargets() {
        return aggroableTargets;
    }

    public void addAggroableTargets(AbstractEntity aggroableTarget) {
        aggroableTargets.add(aggroableTarget);
    }*/
}
