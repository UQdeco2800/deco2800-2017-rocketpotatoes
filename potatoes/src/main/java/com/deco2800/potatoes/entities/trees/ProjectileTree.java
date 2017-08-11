package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.Tickable;

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
public class ProjectileTree extends AbstractTree implements Tickable{
    public int level;
    public int hp;
    public int speed;
    
    private UpgradeStats level1 = new UpgradeStats(4,4);
    //public whateveraprojectileis projectile;
    
    
    public ProjectileTree(float posX, float posY, float posZ, String texture, int reloadTime, float range) {
        super(posX, posY, posZ, 1f, 1f, 1f, texture);
        
        registerTimeEvent(new TreeProjectileShootEvent(this, reloadTime, range));
    }

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





