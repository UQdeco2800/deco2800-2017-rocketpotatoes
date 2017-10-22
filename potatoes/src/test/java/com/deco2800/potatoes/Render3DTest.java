package com.deco2800.potatoes;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.worlds.ForestWorld;
import com.deco2800.potatoes.worlds.WorldType;

public class Render3DTest extends BaseTest {

	Render3D renderer;
	
	@Before
	public void setUp() throws Exception {
		GameManager.get().getManager(CameraManager.class).setCamera(new OrthographicCamera());
		GameManager.get().getManager(WorldManager.class).setWorld(ForestWorld.get());
	}

	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
	
	@Test
	public void screenAndTileTest() {
		//fix null pointer exception
		Vector2 testVector1 = Render3D.screenToTile(10, 10);
		//Vector3 testVector2 = renderer.tileToScreen(GameManager.get().getManager(GuiManager.class).getStage(), testVector1.x, testVector1.y);
		
		//assertEquals(137, testVector1.angle(), 1);
		//assertEquals(0.6619, testVector1.len(), 0.001);
		
		
		Render3D.screenToWorldCoordiates(10, 10);
		
	}
	
	

}
