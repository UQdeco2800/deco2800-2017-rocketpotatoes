package com.deco2800.potatoes.gui;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;



public class TreeShopGui extends Gui {
	private Skin uiSkin;
	private Vector2 tileCoords;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Sprite sprite;
	
	public TreeShopGui(Stage stage){
		// world coords
		
		//Vector2 tileCoords = Render3D.worldPosToTile(x, y);
		//Vector2 screenCoords = Render3D.worldToScreenCoordinates(tileCoords.x, tileCoords.y);
		
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		Image treeMenu = new Image(new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("resources/menu/tree_menu.png")))));
		/*treeMenu.setPosition(x, y);
		system.out.println("screen x: "+screenCoords.x);
		System.out.println("tile x: " + tileCoords.x);
		System.out.println("world x: " +x);
	    treeMenu.scaleBy(0.3f);
	    treeMenu.setScaleX(0.3f);
	    treeMenu.setScaleY(0.3f);*/
		
		stage.addActor(treeMenu);
		
		
		
		
		// Add listener
		// Move listener
		// Click listener
		
		// Create seperate subsections
		


	}


	
	private void createSubMenus() {
		
	}
	
	private void moveListener() {
		
	}
	
	private void clickListener() {
		
	}
	
}
