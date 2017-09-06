package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.PlayerManager;

public class TreeShop extends ScreenEntity {
	private final static String TEXTURE = "tree_shop";
	//private final static int offsetX = -3;
	//private final static int offset_y = 1;
	
	public TreeShop(float posX, float posY) {
		super(posX, posY, 1, 2f, 2f, 0, true, TEXTURE);
	}
	
	public void onClick() {
		System.out.println("got the shop");
		ResourceTree newTree = new ResourceTree(this.getPosX(),this.getPosY(),0);
		//GameManager.get().getWorld().removeEntity(this);
		MultiplayerManager multiplayerManager = GameManager.get().getManager(MultiplayerManager.class);
		if (!multiplayerManager.isMultiplayer() || multiplayerManager.isMaster()) {
			AbstractTree.constructTree(newTree);
		} else {
			multiplayerManager.broadcastBuildOrder(newTree);
		}
		System.out.println("got here");
		GameManager.get().getManager(PlayerManager.class).getPlayer().removeTreeShop();
	}
	
}
