package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.PlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreeShop extends ScreenEntity {
	private static final Logger LOGGER = LoggerFactory.getLogger(TreeShop.class);
	private static final String TEXTURE = "tree_shop";
	//private final static int offsetX = -3;
	//private final static int offset_y = 1;
	
	public TreeShop(float posX, float posY) {
		super(posX, posY, 1, 2f, 2f, 0, true, TEXTURE);
	}
	
	public void onClick() {
		LOGGER.info("got the shop");
		ResourceTree newTree = new ResourceTree(this.getPosX(),this.getPosY(),0);
		//GameManager.get().getWorld().removeEntity(this);
		MultiplayerManager multiplayerManager = GameManager.get().getManager(MultiplayerManager.class);
		if (!multiplayerManager.isMultiplayer() || multiplayerManager.isMaster()) {
			AbstractTree.constructTree(newTree);
		} else {
			multiplayerManager.broadcastBuildOrder(newTree);
		}
		LOGGER.info("got here");
		GameManager.get().getManager(PlayerManager.class).getPlayer().removeTreeShop();
	}
	
}
