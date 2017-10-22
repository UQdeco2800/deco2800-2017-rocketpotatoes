package com.deco2800.potatoes.entities.trees;

import com.deco2800.potatoes.entities.PropertiesBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for defensive trees that don't attack but act as a wall.
 * 
 * @author Jordan Holder
 *
 */
public class DefenseTree extends AbstractTree {
	
	/*
	 * Texture to render the tree with.
	 */
	private static String TEXTURE = "defenseTree";
	
	/**
	 * Constructor for creating a defense tree at the given position.
	 * 
	 * @param posX
	 * 			The X position of the defense tree.
	 * @param posY
	 * 			The Y position of the defense tree.
	 */
	public DefenseTree(float posX, float posY) {		
		super(posX, posY, 1f, 1f);
		
		this.setTexture(TEXTURE);
	}
	
	@Override
	public AbstractTree createCopy() {
		return new DefenseTree(this.getPosX(), this.getPosY());
	}

	@Override
	public String getTexture() {
		return TEXTURE;
	}

	@Override
	public List<TreeProperties> getAllUpgradeStats() {
		List<TreeProperties> result = new LinkedList<>();

		result.add(new PropertiesBuilder<DefenseTree>().setHealth(50).setBuildTime(5000)
				.setBuildCost(1).setTexture(TEXTURE).createTreeStatistics());

		return result;
	}
	
	@Override
	public String getName() {
		return "Defense Tree";
	}

}
