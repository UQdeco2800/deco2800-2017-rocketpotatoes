package com.deco2800.potatoes.entities;

import com.deco2800.potatoes.entities.Player;
import com.deco2800.potatoes.entities.Player.PlayerState;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.WorldManager;
import com.deco2800.potatoes.worlds.World;
import com.deco2800.potatoes.worlds.WorldType;
import com.deco2800.potatoes.util.WorldUtil;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class PlayerTest {
	Player player;

	@Before
	public void setup() {
		player = new Player();
	}
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
	
	@Test
	public void directionTest() {
		player.getDirection();
	}

	@Test
	public void keysTest(){
		player.handleKeyDown(Input.Keys.W);
		player.handleKeyUp(Input.Keys.W);
		player.handleKeyDown(Input.Keys.S);
		player.handleKeyUp(Input.Keys.S);
		player.handleKeyDown(Input.Keys.A);
		player.handleKeyUp(Input.Keys.A);
		player.handleKeyDown(Input.Keys.D);
		player.handleKeyUp(Input.Keys.D);
	}

	@Test
	public void stringTest(){
		player.toString();
		player.getProgressBar();
	}
	
	@Test
	public void checkKeyDownTest() {
		player.handleKeyUp(Input.Keys.W);
	}
	
/*

    Player player;

    @Before
    public void setup() {
        player = new Player();
        player = new Player(1, 1, 0);
        GameManager.get().clearManagers();
        PlayerManager m = new PlayerManager();
		CameraManager cameraManager = new CameraManager();
		WorldManager worldManager = new WorldManager();
		OrthographicCamera camera = new OrthographicCamera();
        GameManager.get().addManager(m);
        GameManager.get().addManager(worldManager);
        GameManager.get().getManager(WorldManager.class).setWorld(WorldType.FOREST_WORLD);
        
        cameraManager.setCamera(camera);
        
        m.setPlayer(player);
    }

    @Test
    public void keysTest2() {
        player.setPlayerType("wizard");
        player.handleKeyDown(Input.Keys.SPACE);
        player.handleKeyDown(Input.Keys.SPACE);
        player.handleKeyDown(Input.Keys.SPACE);
        player.handleKeyDown(Input.Keys.E);
        player.handleKeyDown(Input.Keys.F);
        player.handleKeyDown(Input.Keys.T);
        player.handleKeyDown(Input.Keys.R);
    }

    @Test
    public void stringTest() {
        player.toString();
        player.getProgressBar();
        player.getPlayerType();
        player.setPlayerType("");

    }

    @Test
    public void updateTest() {
        player.updateSprites();
        player.setDamaged(true);
        player.setDamaged(false);
        player.onTick(2);
    }

*/
}