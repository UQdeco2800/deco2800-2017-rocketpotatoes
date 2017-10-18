package com.deco2800.potatoes.entities.enemies;

import com.deco2800.potatoes.entities.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

public class EnemyTargets {

    private static List<Class> mainTargets;
    private static List<Class> sightAggroTargets;
    private static List<Class> damageAggroTargets;

    public EnemyTargets(List<Class> mainTargets, List<Class> sightAggroTargets,
                        List<Class> damageAggroTargets) {
        this.mainTargets = mainTargets;
        this.sightAggroTargets = sightAggroTargets;
        this.damageAggroTargets = damageAggroTargets;
    }

    public List<Class> getMainTargets() { return mainTargets;  }

    public List<Class> getSightAggroTargets() { return sightAggroTargets; }

    public List<Class> getDamageAggroTargets() { return damageAggroTargets; }


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
