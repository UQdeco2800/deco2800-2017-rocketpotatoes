package com.deco2800.potatoes.gui;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;

/**
 * Inventory GUI is to display the amount of resources hold by player and
 * potentially allow player to spend their resources into trees or health
 * 
 * @Author Minh Tram Julien Tran
 */

public class InventoryGui extends Gui {

	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	/* Create Window with skin */
    private Window window;
    
    //Scroll Pane
    private ScrollPane scrollPane;

	/* Objects to be used in the inventory gui */
	private Table inventoryTable;
	
	/* Hashmap with the resource type as key and respective Label as value */
	private HashMap<String, Label> inventoryMap = new HashMap<String, Label>();	

	/**
	 * Instantiates a table for the InventoryGui to be placed on the current
	 * stage. It requires stage is not null because the name component of it is
	 * input in the constructor.
	 * 
	 * @require stage is not null
	 */
	public InventoryGui(Stage stage) {		
		/* Set up the Table and Scroll Pane for positioning Inventory Gui */
		instantiateTable();
		instantiateScrollPane();
		
		/* set up window */
		window = new Window("Inventory", skin);
		window.add(scrollPane).width(80).height(100);
		
		/* size control and position window in the top right */
		window.setWidth(90);
		window.setHeight(100);
		window.setPosition(stage.getWidth(), stage.getHeight());

		/* add in the window finally */
		stage.addActor(window);

	}

	/**
	 * Increase Inventory
	 * 
	 * @param resource
	 *            The type of resource that was added to inventory
	 * @param amount
	 *            The amount of resource that was added to inventory
	 */
	public void increaseInventory(String resource, int amount) {
		Label resourceLabel = inventoryMap.get(resource);
		if (resourceLabel == null) {
			
			resourceLabel = new Label("0", skin);
			resourceLabel.setText(Integer.toString(amount));
			inventoryMap.put(resource, resourceLabel);
			
			updateTable(resource);
		} else {
			resourceLabel.setText(Integer.toString(amount));
		}
	}

	/**
	 * PRIVATE METHODS
	 */

	/**
	 * To be called by the constructor to upon first time to create inventory
	 * gui display
	 */
	private void instantiateTable() {
		inventoryTable = new Table();
		inventoryTable.defaults().width(20);
		inventoryTable.padRight(10);
		inventoryTable.padTop(10);
		inventoryTable.padBottom(10);
	}
	
	/**
	 * Adds a new resource to the inventory GUI.
	 * 
	 * @param resource
	 * 			The name of the resource to be added.
	 */
	private void updateTable(String resource) {
		
		/* Image linking to display sprite */
		TextureManager textureManager = GameManager.get().getManager(TextureManager.class);
		Image resourceImage = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(resource))));
			
		resourceImage.setOrigin(50, 50);
		inventoryTable.add(resourceImage).size(30, 30).pad(2);
		inventoryTable.add(inventoryMap.get(resource)).bottom().center().pad(2);
			
		inventoryTable.row();
	}

	/**
	 * To be called by the constructor to upon first time to create Scroll Pane
	 */
	private void instantiateScrollPane(){
		scrollPane = new ScrollPane(inventoryTable, skin);
		scrollPane.setForceScroll(false, true);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setScrollBarPositions(false, true);
		scrollPane.setFadeScrollBars(false);
		scrollPane.pack();
		
	}
	
}
