package com.deco2800.potatoes;

import static org.junit.Assert.*;

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
import com.deco2800.potatoes.worlds.WorldType;

public class Render3DTest {

	Render3D renderer;
	
	@Before
	public void setUp() throws Exception {
		GameManager gameManager = GameManager.get();
		gameManager.clearManagers();
		CameraManager cameraManager = new CameraManager();
		WorldManager worldManager = new WorldManager();
		GuiManager guiManager = new GuiManager();
		OrthographicCamera camera = new OrthographicCamera();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.title = "Render Tests";
		LwjglApplication game = new LwjglApplication(null, config);
		
		//guiManager.setStage(new Stage(new ScreenViewport(camera)));
		cameraManager.setCamera(camera);
		
		GameManager.get().addManager(cameraManager);
		//GameManager.get().addManager(worldManager);
		GameManager.get().addManager(guiManager);
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
        //GameManager.get().getWorld().getMap().getProperties().put("tilewidth", 5);
		//GameManager.get().getWorld().getMap().getProperties().put("tileheight", 5);
	}

	
	@Test
	public void screenAndTileTest() {
		//Todo: fix null pointer exception
		Vector2 testVector1 = Render3D.screenToTile(10, 10);
		//Vector3 testVector2 = renderer.tileToScreen(GameManager.get().getManager(GuiManager.class).getStage(), testVector1.x, testVector1.y);
		
		assertEquals(137, testVector1.angle(), 1);
		assertEquals(0.6619, testVector1.len(), 0.001);
		
		
		Render3D.screenToWorldCoordiates(10, 10, 10);
		
	}
	
	

}
