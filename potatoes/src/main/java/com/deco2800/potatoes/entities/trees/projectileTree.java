package com.deco2800.potatoes.entities.trees;

import java.util.Optional;
import com.badlogic.gdx.utils.TimeUtils;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.entities.TimeEvent;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.*;

import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.WorldUtil;

class UpgradeStats {
    private int hp;
    private int speed;
    public UpgradeStats(int hp, int speed) {
        hp = hp;
        speed = speed;
    }
    public int getHp(){
        return hp;
    }
    public int getSpeed(){
        return speed;
    }
}
public class projectileTree extends AbstractTree implements Tickable{
    public int level;
    public int hp;
    public int speed;

    private UpgradeStats level1 = new UpgradeStats(4,4);
    //public whateveraprojectileis projectile;
    public projectileTree(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                          String texture) {
        super(posX, posY, posZ, xLength, yLength, zLength, texture);
        //more tower//
        this.lastFireTime = 0;
        this.reloadTime = 1000;
        // TODO Auto-generated constructor stub//
    }

    //stuff stolen from towwer//
    private int reloadTime;
    private long lastFireTime;

    private float range = 8f;

    private Optional<AbstractEntity> target = Optional.empty();
    @Override
    public void onTick(long i) {
        long time = TimeUtils.millis();
        if(!(lastFireTime + reloadTime < time)) {
            return;
        }

        lastFireTime = time;
        this.target = WorldUtil.getClosestEntityOfClass(Squirrel.class, getPosX(), getPosY());

        if(!target.isPresent()) {
            return;
        }
        System.out.println("FiRiNg Mi LaZoRs " + i);

        GameManager.get().getWorld().addEntity(new BallisticProjectile(getPosX(), getPosY(), getPosZ(), target.get().getPosX(), target.get().getPosY(), getPosZ(), range));


    }

    //===================================//


    public void init() {
        level=1;
        hp=1;
        speed=1;
    }
    public void upgrade() {
        // add upgrade numbers later
        switch (level) {
            case 1:
                hp = level1.getHp();
                speed = level1.getSpeed();
                break;
            case 2:
                hp = 10;
                break;
        }
        level++;
    }



}





