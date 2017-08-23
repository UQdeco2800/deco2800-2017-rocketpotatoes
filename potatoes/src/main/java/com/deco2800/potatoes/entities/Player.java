package com.deco2800.potatoes.entities;

import java.util.HashSet;
import java.util.List;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.potatoes.managers.InputManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tickable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.util.Box3D;
import com.deco2800.potatoes.managers.Inventory;
import com.deco2800.potatoes.entities.Resource;

/**
 * Entity for the playable character.
 *
 * @author leggy
 *
 */
public class Player extends MortalEntity implements Tickable {

	private static final transient Logger LOGGER = LoggerFactory.getLogger(Player.class);

	private final static transient float HEALTH = 100f;
	private static final transient String TEXTURE_RIGHT = "spacman_blue";
	private static final transient String TEXTURE_LEFT = "spacman_blue_2";

	private float movementSpeed;
	private float speedx;
	private float speedy;
	
	private Inventory inventory;

	/**
	 * Default constructor for the purposes of serialization
	 */
	public Player() {
		super(0, 0, 0, 1, 1, 1, TEXTURE_RIGHT, HEALTH);
	}

	/**
	 * Creates a new Player instance.
	 *
	 * @param posX
	 *            The x-coordinate.
	 * @param posY
	 *            The y-coordinate.
	 * @param posZ
	 *            The z-coordinate.
	 */
	public Player(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 1, 1, 1, TEXTURE_RIGHT, HEALTH);
		movementSpeed = 0.1f;
		this.speedx = 0.0f;
		this.speedy = 0.0f;
		
		HashSet<Resource> startingResources = new HashSet<Resource>();
		startingResources.add(new SeedResource());
		startingResources.add(new FoodResource());
		this.inventory = new Inventory(startingResources);

		//this.setTexture("spacman_blue");
	}

	public Inventory getInventory() {
		return this.inventory;
	}
	
	//* GUI display for the inventory menu
	public void inventoryMenu (Stage stage) {
		
		//Create inventory
		//Inventory inventory = new Inventory();
		
		//Get inventory number (getQuantity from Inventory)
		int seedNo = 0;
		int foodNo = 0;
		
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		//Set up a table for the inventory menu

		Label seedLabel = new Label("Seed", skin);
	    TextField seedNumber = new TextField(Integer.toString(foodNo), skin);
	    Label foodLabel = new Label("Food:", skin);
	    TextField foodNumber = new TextField(Integer.toString(seedNo), skin);
	    
	    Table table = new Table();
	    table.setFillParent(true);
	    table.add(seedLabel);
	    table.add(seedNumber).width(30);
	    table.row();
	    table.add(foodLabel);
	    table.add(foodNumber).width(30);
	    
	    table.left().bottom();
		 
		stage.addActor(table);
	}
	
	
	@Override
	public void onTick(long arg0) {
		float newPosX = this.getPosX();
		float newPosY = this.getPosY();

		newPosX += speedx;
		newPosY += speedy;

		Box3D newPos = getBox3D();
		newPos.setX(newPosX);
		newPos.setY(newPosY);

		Map<Integer, AbstractEntity> entities = GameManager.get().getWorld().getEntities();
		boolean collided = false;
		for (AbstractEntity entity : entities.values()) {
			if (!this.equals(entity) && !(entity instanceof Squirrel) && newPos.overlaps(entity.getBox3D())) {
				LOGGER.info(this + " colliding with " + entity);
				//wSystem.out.println(this + " colliding with " + entity);
				collided = true;

			}
		}

		if (!collided) {
			this.setPosX(newPosX);
			this.setPosY(newPosY);
		}
	}

	/**
	 * Handle movement when wasd keys are pressed down
	 *
	 * @param keycode
	 */
	public void handleKeyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.W:
			speedy -= movementSpeed;
			speedx += movementSpeed;
			break;
		case Input.Keys.S:
			speedy += movementSpeed;
			speedx -= movementSpeed;
			break;
		case Input.Keys.A:
			//changes the sprite so that the character is facing left
			this.setTexture(TEXTURE_LEFT);
			speedx -= movementSpeed;
			speedy -= movementSpeed;
			break;
		case Input.Keys.D:
		//changes the sprite so that the character is facing right
		this.setTexture(TEXTURE_RIGHT);
			speedx += movementSpeed;
			speedy += movementSpeed;
		default:
			break;
		}
	}

	/**
	 * Handle movement when wasd keys are released
	 *
	 * @param keycode
	 */
	public void handleKeyUp(int keycode) {
		switch (keycode) {
		case Input.Keys.W:
			speedy += movementSpeed;
			speedx -= movementSpeed;
			break;
		case Input.Keys.S:
			speedy -= movementSpeed;
			speedx += movementSpeed;
			break;
		case Input.Keys.A:
			speedx += movementSpeed;
			speedy += movementSpeed;
			break;
		case Input.Keys.D:
			speedx -= movementSpeed;
			speedy -= movementSpeed;
			break;
		default:
			break;
		}
	}

	@Override
	public String toString() {
		return "The player";
	}

}
