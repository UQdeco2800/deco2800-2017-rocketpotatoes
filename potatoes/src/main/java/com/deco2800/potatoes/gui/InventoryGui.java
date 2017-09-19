package com.deco2800.potatoes.gui;

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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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

	private int seedAmount = 0;
	private Image seedImage;
	private Label seedLabelAmount = new Label(Integer.toString(seedAmount), skin);

	private int foodAmount = 0;
	private Image foodImage;
	private Label foodLabelAmount = new Label(Integer.toString(foodAmount), skin);

	/**
	 * Instantiates a table for the InventoryGui to be placed on the current
	 * stage. It requires stage is not null because the name component of it is
	 * input in the constructor.
	 * 
	 * @require stage is not null
	 */
	public InventoryGui(Stage stage) {
		/* Set up the table for positioning Inventory Gui */
		instantiateTable();
		instantiateScrollPane();

		/* position table in the top right */
		//inventoryTable.right().top();

		/* set up window */
		window = new Window("Inventory", skin);
		window.add(scrollPane).width(100).height(80);
		
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

		if (resource == "seed") {
			seedLabelAmount.setText(Integer.toString(amount));
		}

		if (resource == "food") {
			foodLabelAmount.setText(Integer.toString(amount));
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
		inventoryTable.setFillParent(true);

		inventoryTable.defaults().width(20);
		inventoryTable.padTop(10);
		inventoryTable.padRight(10);
		
		//buttons

		seedImage = new Image(new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("resources/placeholderassets/seed.png")))));
		seedImage.setOrigin(50, 50);
		inventoryTable.add(seedImage).size(45, 45);
		inventoryTable.add(seedLabelAmount).bottom().left();

		/* next row */
		inventoryTable.row();
		foodImage = new Image(new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("resources/placeholderassets/food.png")))));
		foodImage.setOrigin(50, 50);
		inventoryTable.add(foodImage).size(40, 40);
		inventoryTable.add(foodLabelAmount).bottom().left();
	}

	
	private void instantiateScrollPane(){
		scrollPane = new ScrollPane(inventoryTable, skin);
		scrollPane.setForceScroll(false, true);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setScrollBarPositions(false, true);
		scrollPane.setFadeScrollBars(true);
		scrollPane.pack();
		
	}
	
}
