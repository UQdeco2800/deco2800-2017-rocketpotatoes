package com.deco2800.potatoes.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.player.Player;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.After;

public class CameraManagerTest {
	
	@After
    public void cleanUp() {
    	GameManager.get().clearManagers();
    }
	
    @Test
    public void hasTarget() {
        CameraManager m = new CameraManager();
        AbstractEntity e = new Player();

        assertEquals(false, m.hasTarget());
        m.setTarget(e);
        assertEquals(true, m.hasTarget());
    }

    @Test
    public void setCamera() {
        CameraManager m = new CameraManager();
        OrthographicCamera c = new OrthographicCamera();

        assertEquals(null, m.getCamera());
        m.setCamera(c);
        assertEquals(c, m.getCamera());
    }

}