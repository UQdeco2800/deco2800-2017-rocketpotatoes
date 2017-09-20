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

	/* Labels for each resource to display their amount */
	private Label seedLabelAmount = new Label("0", skin);
	private Label foodLabelAmount = new Label("0", skin);
	

	/**
	 * Instantiates a table for the InventoryGui to be placed on the current
	 * stage. It requires stage is not null because the name component of it is
	 * input in the constructor.
	 * 
	 * @require stage is not null
	 */
	public InventoryGui(Stage stage) {
		/* Put items in Map */
		inventoryMap.put("seed", seedLabelAmount);
		inventoryMap.put("food", foodLabelAmount);
		
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
		resourceLabel.setText(Integer.toString(amount));
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
		inventoryTable.setFillParent(true);

		inventoryTable.defaults().width(20);
		inventoryTable.padRight(10);
		
		/* Each resource display */
		for (String resource: inventoryMap.keySet()){
			/* Image linking to display sprite */
			StringBuilder imageString = new StringBuilder();
			imageString.append("resources/placeholderassets/");
			imageString.append(resource);
			imageString.append(".png");
			Image resourceImage = new Image(new TextureRegionDrawable(
					new TextureRegion(new Texture(Gdx.files.internal(imageString.toString())))));
			
			resourceImage.setOrigin(50, 50);
			inventoryTable.add(resourceImage).size(30, 30);
			inventoryTable.add(inventoryMap.get(resource)).bottom().left();
			
			inventoryTable.row();
		}		
	}

	/**
	 * To be called by the constructor to upon first time to create Scroll Pane
	 */
	private void instantiateScrollPane(){
		scrollPane = new ScrollPane(inventoryTable, skin);
		scrollPane.setForceScroll(false, true);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setScrollBarPositions(false, true);
		scrollPane.setFadeScrollBars(true);
		scrollPane.pack();
		
	}
	
}
