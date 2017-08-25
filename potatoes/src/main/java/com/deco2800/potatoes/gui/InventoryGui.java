package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.potatoes.entities.FoodResource;
import com.deco2800.potatoes.entities.SeedResource;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.managers.PlayerManager;

/**
 * Inventory GUI is to display the amount of resources hold by player
 * and potentially allow player to spend their resources into
 * trees or health
 * @Author Minh Tram Julien Tran
 */

public class InventoryGui extends Gui {
	
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	
	//Get the player's inventory
	private Inventory inventory;
	
	//Set up a table for the inventory menu
	private Table inventoryTable;
	
	private int seedAmount = 0;
	private Label seedLabel = new Label("Seed", skin);;
	private Label seedLabelAmount = new Label(Integer.toString(seedAmount), skin);;
	private Image seedImage;
	
	private int foodAmount = 0;
	private Label foodLabel = new Label("Food", skin);;
	private Label foodLabelAmount = new Label(Integer.toString(foodAmount), skin);;
	private Image foodImage;
	
	//Constructor takes in playerManager (for the inventory) and takes in stage to set up
	public InventoryGui(Stage stage){
		//get inventory
		//inventory = playerManager.getPlayer().getInventory();
		//get amount of food/seed
		//seedAmount = inventory.getQuantity(new SeedResource());
		//foodAmount = inventory.getQuantity(new FoodResource());
		
		//add labels
		//Label seedLabel = new Label("Seed", skin);
		//Label foodLabel = new Label("Food", skin);
		//seedLabelAmount = new Label(Integer.toString(seedAmount), skin);
		//foodLabelAmount = new Label(Integer.toString(foodAmount), skin);
		
		//instantiate table
		inventoryTable = new Table();
		inventoryTable.setFillParent(true);
		inventoryTable.add(seedLabel);
		inventoryTable.add(seedLabelAmount).width(30);
		
		//seedImage = new Image(new Texture());
	    //table.add(seedNumber).width(30);
		
		inventoryTable.row();
		
		inventoryTable.add(foodLabel);
		inventoryTable.add(foodLabelAmount).width(30);
	    //table.add(foodNumber).width(30);
		
		//position table in the bottom left
		inventoryTable.left().bottom();
		
		//add in the table finally
		stage.addActor(inventoryTable);
		
	}
	
	//set stage
	
	//take in player's inventory to set inventory display
	public void setInventory(Inventory playerInventory){
		inventory = playerInventory;
	}
	
}
