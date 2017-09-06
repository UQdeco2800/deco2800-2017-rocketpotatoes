package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.renderering.Renderable;

public class TreeShopGui extends Gui{
	private Skin uiSkin;
	private Vector2 tileCoords;
	
	public TreeShopGui(Stage stage, float x, float y){
		// world coords
		
		Vector2 tileCoords = Render3D.worldPosToTile(x, y);
		Vector2 screenCoords = Render3D.worldToScreenCoordinates(tileCoords.x, tileCoords.y);
		
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		Image treeMenu = new Image(new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("resources/menu/tree_menu.png")))));
		treeMenu.setPosition(x, y);
		System.out.println("screen x: "+screenCoords.x);
		System.out.println("tile x: " + tileCoords.x);
		System.out.println("world x: " +x);
	    treeMenu.scaleBy(0.3f);
	    treeMenu.setScaleX(0.3f);
	    treeMenu.setScaleY(0.3f);
		stage.addActor(treeMenu);
		//stage.addActor(testTree);
	}
}
