package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.renderering.Renderable;

public class TreeShopGui extends Gui{
	private Skin uiSkin;
	
	public TreeShopGui(Stage stage, float x, float y){
		//x += 220;
		//y += 440;
		
		
		
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		Button testTree = new TextButton("test",uiSkin);
		testTree.setPosition(x, y);
		Image seedImage = new Image(new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("resources/menu/tree_menu.png")))));
		seedImage.setPosition(x, y);
		//seedImage.scaleBy(0.3f);
		seedImage.setScaleX(0.3f);
		seedImage.setScaleY(0.3f);
		stage.addActor(seedImage);
		//stage.addActor(testTree);
	}
}
