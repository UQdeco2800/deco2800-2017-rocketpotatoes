package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.screens.GameScreen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.utils.Align.center;

/**
 * Game over screen Gui
 * A screen that displays after the goal potato has been destroyed.
 *
 *@author Goh Zen Hao
 */
public class GameOverGui extends Gui {
	private GameScreen screen;
//	private GameOverScreen gameOverScreen;

    private static final Logger LOGGER = LoggerFactory.getLogger(GameOverGui.class);

    private Stage stage;
    private Skin uiSkin;

    private Table table;
    // Buttons
//    private Drawable pauseMenuDrawable;
    private VerticalGroup primaryButtons;
    private Drawable startDrawable;
    private Drawable exitDrawable;
    private ImageButton startButton;
    private ImageButton exitButton;

    public GameOverGui(Stage stage, GameScreen screen) {
    	this.stage = stage;
    	this.screen = screen;

        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table(uiSkin);

        // Make drawables from textures
        startDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gameOverRestart")));
        exitDrawable = new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gameOverExit")));
        startButton = new ImageButton(startDrawable);
        exitButton = new ImageButton(exitDrawable);

        primaryButtons = new VerticalGroup();
        primaryButtons.addActor(startButton);
        primaryButtons.addActor(exitButton);
        
     // padding for top and bottom of buttons
        final int PADDINGVERTICAL = 15;
        final int PADDINGHORIZONTAL = 10;

     // Add padding to buttons
        startButton.pad(PADDINGVERTICAL, PADDINGHORIZONTAL, PADDINGVERTICAL, PADDINGHORIZONTAL);
        exitButton.pad(PADDINGVERTICAL, PADDINGHORIZONTAL, PADDINGVERTICAL, PADDINGHORIZONTAL);
        setupListeners();

        table.setBackground(new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gameOverScreen"))));
        table.add(primaryButtons).expandX().center().padTop(230);
        table.setVisible(false);
        table.setWidth(stage.getWidth());
        table.setHeight(stage.getHeight());
        table.center();
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2, center);

        stage.addActor(table);

    }

    private void setupListeners() {
        // Primary state

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                screen.exitToMenu();;
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                // Todo nicer exit?
                System.exit(0);
            }
        });

    }



    @Override
	public void show() {
        table.setVisible(true);

        stage.addActor(table);
        hidden = false;
    }

    @Override
	public void hide() {
        table.setVisible(false);
        hidden = true;
    }
    
    /**
     * Toggles whether the menu is shown or hidden, using the hide() and show() methods.
     */
    public void toggle() {
        if (hidden) {
            show();
        } else {
            hide();
        }
    }

}
