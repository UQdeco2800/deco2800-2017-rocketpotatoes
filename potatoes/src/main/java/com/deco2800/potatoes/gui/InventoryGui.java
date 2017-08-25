package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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

		/* position table in the bottom left */
		inventoryTable.left().bottom();

		/* add in the table finally */
		stage.addActor(inventoryTable);

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

		inventoryTable.defaults().width(50);

		seedImage = new Image((Drawable) new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("resources/placeholderassets/seed.png")))));
		seedImage.setOrigin(50, 50);
		inventoryTable.add(seedImage).size(30, 30).pad(2);
		inventoryTable.add(seedLabelAmount).pad(2);

		/* next row */
		inventoryTable.row();
		foodImage = new Image((Drawable) new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("resources/placeholderassets/food.png")))));
		foodImage.setOrigin(50, 50);
		inventoryTable.add(foodImage).size(25, 25).pad(2);
		inventoryTable.add(foodLabelAmount).pad(2);
	}

}
