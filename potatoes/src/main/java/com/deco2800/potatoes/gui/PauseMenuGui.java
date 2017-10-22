package com.deco2800.potatoes.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.GuiManager;
import com.deco2800.potatoes.managers.InputManager;
import com.deco2800.potatoes.managers.ProgressBarManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.screens.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.badlogic.gdx.utils.Align.center;

public class PauseMenuGui extends Gui {

    private static final Logger LOGGER = LoggerFactory.getLogger(PauseMenuGui.class);

    private GameScreen screen;
    private Stage stage;

    private TextureManager textureManager;

    // Buttons
    private Skin uiSkin;
    private Drawable resumeDrawable;
    private Drawable optionsDrawable;
    private Drawable optionsBackDrawable;
    private Drawable saveDrawable;
    private Drawable exitDrawable;

    private VerticalGroup pauseButtonGroup;
    private ImageButton resumeButton;
    private ImageButton optionsButton;
    private ImageButton saveButton;
    private ImageButton exitButton;
    private Table table;
    
    // Help
    private Drawable helpDrawable;
    private Drawable initialGameplayDrawable;
    private Drawable treesDrawable;
    private Drawable enemiesDrawable;
    private Drawable healthDrawable;
    private Drawable portalsDrawable;
    private Drawable controlsDrawable;
    private ImageButton helpButton;
    private ImageButton initialGameplayButton;
    private ImageButton treesButton;
    private ImageButton enemiesButton;
    private ImageButton healthButton;
    private ImageButton portalsButton;
    private ImageButton controlsButton;
    private TextButton helpBackButton;
    private TextButton slideBackButton;
    private VerticalGroup helpButtonGroup;
    private Image tutorialDrawable;
    private String tutorialTexture;

    // Options
    private VerticalGroup optionsButtonGroup;

    private Label optionsEffectsVolumeLabel;
    private Slider optionsEffectsVolumeSlider;
    private Label optionsMusicVolumeLabel;
    private Slider optionsMusicVolumeSlider;
    private ImageButton optionsBackButton;
    private Drawable optionsBackgroundDrawable;

    
    // Progress bar
	private Label progressBarLabel;
	private CheckBox playerProgressBarCheckBox;
	private CheckBox alliesProgressBarCheckBox;
	private CheckBox enemyProgressBarCheckBox;
	private CheckBox potatoProgressBarCheckBox;
	
	// padding for top and bottom of buttons
    private static final int PADDINGVERTICAL = 5;
    private static final int PADDINGHORIZONTAL = 10;

    // State indicator
    private enum States {
        PAUSE,
        OPTIONS,
        HELP,
        INFORMATION
    }

    private States state = States.PAUSE;

    public PauseMenuGui(Stage stage, GameScreen screen) {
        this.screen = screen;
        this.stage = stage;
        hidden = true;

        textureManager = GameManager.get().getManager(TextureManager.class);

        uiSkin = new Skin(Gdx.files.internal("menu/uiskin.json"));
        table = new Table(uiSkin);

        // Make drawables from textures
        resumeDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("resumePauseMenu")));
        optionsDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("optionsPauseMenu")));
        optionsBackDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("optionsBackButton")));
        saveDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("savePauseMenu")));
        exitDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("exitPauseMenu")));
        helpDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("tutorialPauseMenu")));

        
        // Help drawables
        initialGameplayDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("initialGameplayButton")));
        treesDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("treesButton")));
        enemiesDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("enemiesButton")));
        healthDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("healthButton")));
        portalsDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("portalsButton")));
        controlsDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("controlsButton")));
        
        // Pause State
        resumeButton = new ImageButton(resumeDrawable);
        optionsButton = new ImageButton(optionsDrawable);
        saveButton = new ImageButton(saveDrawable);
        exitButton = new ImageButton(exitDrawable);
        helpButton = new ImageButton(helpDrawable);
        
        // Help Buttons
        initialGameplayButton = new ImageButton(initialGameplayDrawable);
        treesButton = new ImageButton(treesDrawable);
        enemiesButton = new ImageButton(enemiesDrawable);
        healthButton = new ImageButton(healthDrawable);
        portalsButton = new ImageButton(portalsDrawable);
        controlsButton = new ImageButton(controlsDrawable);
        helpBackButton = new TextButton("Back", uiSkin);
        slideBackButton = new TextButton("Back", uiSkin);

        pauseButtonGroup = new VerticalGroup();
        pauseButtonGroup.addActor(resumeButton);
        pauseButtonGroup.addActor(optionsButton);
        pauseButtonGroup.addActor(saveButton);
        pauseButtonGroup.addActor(helpButton);
        pauseButtonGroup.addActor(exitButton);
        pauseButtonGroup.space(30);

        // Options State
        optionsEffectsVolumeLabel = new Label("SFX Volume", uiSkin);
        optionsEffectsVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsMusicVolumeLabel = new Label("Music Volume", uiSkin);
        optionsMusicVolumeSlider = new Slider(0f,1f,0.01f,false, uiSkin);
        optionsBackButton = new ImageButton(optionsBackDrawable);
        optionsBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("backgroundOptionsMenu")));
        
        // Help button group
		helpButtonGroup = new VerticalGroup();
		helpButtonGroup.addActor(initialGameplayButton);
		helpButtonGroup.addActor(treesButton);
		helpButtonGroup.addActor(enemiesButton);
		helpButtonGroup.addActor(healthButton);
		helpButtonGroup.addActor(portalsButton);
		helpButtonGroup.addActor(controlsButton);
		helpButtonGroup.addActor(helpBackButton);
		helpButtonGroup.space(30);
        
        // progress bar options
		progressBarLabel = new Label("Progress Bars", uiSkin);
		playerProgressBarCheckBox = new CheckBox(" Display Player Progress Bar", uiSkin);
		playerProgressBarCheckBox.setChecked(true);
		potatoProgressBarCheckBox = new CheckBox(" Display Portal Progress Bar", uiSkin);
		potatoProgressBarCheckBox.setChecked(true);
		alliesProgressBarCheckBox = new CheckBox(" Display Ally Progress Bars", uiSkin);
		alliesProgressBarCheckBox.setChecked(true);
		enemyProgressBarCheckBox = new CheckBox(" Display Enemy Progress Bars", uiSkin);
		enemyProgressBarCheckBox.setChecked(true);
		
        optionsButtonGroup = new VerticalGroup();
        optionsButtonGroup.addActor(optionsEffectsVolumeLabel);
        optionsButtonGroup.addActor(optionsEffectsVolumeSlider);
        optionsButtonGroup.addActor(optionsMusicVolumeLabel);
        optionsButtonGroup.addActor(optionsMusicVolumeSlider);
		optionsButtonGroup.addActor(progressBarLabel);
		optionsButtonGroup.addActor(playerProgressBarCheckBox);
		optionsButtonGroup.addActor(potatoProgressBarCheckBox);
		optionsButtonGroup.addActor(alliesProgressBarCheckBox);
		optionsButtonGroup.addActor(enemyProgressBarCheckBox);
        optionsEffectsVolumeSlider.setValue(screen.getEffectsVolume());
        optionsMusicVolumeSlider.setValue(screen.getMusicVolume());
        optionsButtonGroup.space(20);
        
        // Add padding to buttons
        helpBackButton.pad(PADDINGVERTICAL, PADDINGHORIZONTAL, PADDINGVERTICAL, PADDINGHORIZONTAL);
        slideBackButton.pad(PADDINGVERTICAL, PADDINGHORIZONTAL, PADDINGVERTICAL, PADDINGHORIZONTAL);
        
        setupListeners();

        resetGui(stage);

        table.add(pauseButtonGroup);
        table.setVisible(false);
        table.setWidth(500);
        table.setHeight(450);
        table.center();
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2, center);

        stage.addActor(table);

    }

    private void setupListeners() {

        GameManager.get().getManager(InputManager.class).addKeyDownListener(keycode -> {
            if (keycode == Input.Keys.ESCAPE) {
                GameManager.get().getManager(GuiManager.class).getGui(TreeShopGui.class).closeShop();
                toggle();
            }
        });

        /* Listener for the resume button. */
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                hide();
            }
        });

        /* Listener for the options button. */
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                state = States.OPTIONS;
                resetGui(stage);
            }
        });

        /* Listener for the save button. */
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //not yet implemented
            }
        });

        /* Listener for the exit button. */
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                screen.exitToMenu();
				GameManager.get().setPaused(false);
            }
        });
        
        // Help state
        
        /* Listener for the help button. */
        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	screen.menuBlipSound();
                state = States.HELP;
                resetGui(stage);
            }
        });
        
        /* Listener for back button on the help screen */
        helpBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                state = States.PAUSE;
                resetGui(stage);
            }
        });
        
        /* Listener for initial gameplay help button */
        initialGameplayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tutorialTexture = "tutorial2";
                state = States.INFORMATION;
                resetGui(stage);
            }
        });
        
        /* Listener for trees help button */
        treesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	tutorialTexture = "tutorial3";
                state = States.INFORMATION;
                resetGui(stage);
            }
        });
        
        /* Listener for enemies help button */
        enemiesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	tutorialTexture = "tutorial4";
                state = States.INFORMATION;
                resetGui(stage);
            }
        });
        
        /* Listener for health help button */
        healthButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	tutorialTexture = "tutorial5";
                state = States.INFORMATION;
                resetGui(stage);
            }
        });
        
        /* Listener for portals help button */
        portalsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	tutorialTexture = "tutorial6";
                state = States.INFORMATION;
                resetGui(stage);
            }
        });
        
        /* Listener for controls help button */
        controlsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	tutorialTexture = "controls";
                state = States.INFORMATION;
                resetGui(stage);
            }
        });
        
        /* Listener for back button on the help screen */
        slideBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                state = States.HELP;
                resetGui(stage);
            }
        });

        // Options State

        optionsEffectsVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.setEffectsVolume(optionsEffectsVolumeSlider.getValue());
            }
        });

        optionsMusicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.setMusicVolume(optionsMusicVolumeSlider.getValue());
            }
        });

        optionsBackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screen.menuBlipSound();
                state = States.PAUSE;
                resetGui(stage);
            }
        });
        
		playerProgressBarCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameManager.get().getManager(ProgressBarManager.class).togglePlayerProgress();
			}
		});
		
		alliesProgressBarCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameManager.get().getManager(ProgressBarManager.class).toggleAlliesProgress();
			}
		});
		
		enemyProgressBarCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameManager.get().getManager(ProgressBarManager.class).toggleEnemiesProgress();
			}
		});
		
		potatoProgressBarCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameManager.get().getManager(ProgressBarManager.class).togglePotatoProgress();
			}
		});
    }

    private void resetGui(Stage stage) {
        table.reset();
        table.setWidth(400);
        table.setHeight(600);
        table.center();
        table.setPosition(stage.getWidth()/2,stage.getHeight()/2, center);
        table.background((Drawable) null);


        switch (state) {
            case PAUSE:
                table.add(pauseButtonGroup).expandX().center();
                break;
            case OPTIONS:
                table.add(optionsButtonGroup).expandX().center().padTop(50);
                table.row();
                table.add(optionsBackButton).expandX().center().padTop(15);
                table.background(optionsBackgroundDrawable);
                break;
            case HELP:
            	table.add(helpButtonGroup).expandX().center();;
            	break;
            case INFORMATION:
            	tutorialDrawable = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(tutorialTexture))));
            	table.add(tutorialDrawable).size(450, 490).pad(10);
            	table.row();
            	table.add(slideBackButton).expandX().center();
            	break;
            default:
                LOGGER.error("Failed to find pause menu state.");
                break;
        }
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
