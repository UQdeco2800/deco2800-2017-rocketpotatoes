package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.screens.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.utils.Align.center;

/**
 * Gui for the tutorial at the start of the game.
 * 
 * @author Jordan Holder
 *
 */
public class TutorialGui extends Gui {

    private static final Logger LOGGER = LoggerFactory.getLogger(TutorialGui.class);

    private GameScreen screen;
    private Stage stage;
    private Skin uiSkin;
    private TextureManager textureManager;

    // Buttons
    private Image tutorialDrawable;
    private TextButton backButton;
    private TextButton skipButton;
    private TextButton nextButton;
    
    // Table to hold buttons
    private Table table;

    // Groups
    private HorizontalGroup tutorialButtonGroup;
    
    // Array of all the slides to show
    private String[] slides = {"backgroundMainMenu", "forest_portal", "desert_portal",
    		"iceland_portal", "sea_portal", "volcano_portal"};
    // Current slide being displayed
    private int position;

    public TutorialGui(Stage stage, GameScreen screen) {
        this.screen = screen;
        this.stage = stage;
        hidden = true;

        textureManager = GameManager.get().getManager(TextureManager.class);

        uiSkin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        table = new Table(uiSkin);

        // set current slide shown to be 0
        position = 0;

        // Assign text to buttons
        backButton = new TextButton("Previous", uiSkin);
        skipButton = new TextButton("Skip", uiSkin);
        nextButton = new TextButton("Next", uiSkin);

        // Add actors
        tutorialButtonGroup = new HorizontalGroup();
        tutorialButtonGroup.addActor(backButton);
        tutorialButtonGroup.addActor(skipButton);
        tutorialButtonGroup.addActor(nextButton);
        tutorialButtonGroup.space(100);

        // Add listeners to buttons
        setupListeners();
        
        // Set up table
        resetGui(stage);
        table.setVisible(false);
        stage.addActor(table);

    }

    private void setupListeners() {

        /* Listener for the resume button. */
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // ToDo: restart game state.
                screen.menuBlipSound();
                hide();
            }
        });

        /* Listener for the options button. */
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (position != 0) {
                	position--;
                	resetGui(stage);
                	
                	LOGGER.info("Changing to previous tutorial page.");
                }
            }
        });

        /* Listener for the save button. */
        nextButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (position == slides.length - 1) {
                	hide();
                } else {
                	// Increment position
                    position++;
                	resetGui(stage);
                	
                	LOGGER.info("Changing to next tutorial page.");
                }
            }
        });
        
    }

    private void resetGui(Stage stage) {
    	
    	// Make drawables from textures
        tutorialDrawable = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(slides[position]))));
    	
        // set size and position of table
        table.reset();
        table.setWidth(500);
        table.setHeight(450);
        table.center();
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2, center);

        // add elements to table
        table.add(tutorialDrawable).size(400, 400).pad(10);
        table.row();
        table.add(tutorialButtonGroup).expandX().center();
         
    }

    /**
     * Adjusts this gui's position to correct for any resize event.
     *
     * @param stage
     */
    @Override
    public void resize(Stage stage) {
        super.resize(stage);

        resetGui(stage);
    }

    @Override
	public void show() {
        table.setVisible(true);

        stage.addActor(table);
        hidden = false;

        GameManager.get().setPaused(true);
    }

    @Override
	public void hide() {
        table.setVisible(false);
        hidden = true;
        GameManager.get().setPaused(false);
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
