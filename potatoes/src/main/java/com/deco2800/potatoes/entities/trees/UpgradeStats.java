package com.deco2800.potatoes.entities.trees;

public class UpgradeStats {

	private int hp = 0;
	private int speed = 0;

	public UpgradeStats() {
	}

	public UpgradeStats(int hp, int speed) {
		this.hp = hp;
		this.speed = speed;
	}

	public int getHp() {
		return hp;
	}

	public int getSpeed() {
		return speed;
	}
}