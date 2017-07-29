package com.deco2800.potatoes.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.moos.entities.Tickable;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.WorldEntity;

import java.util.Random;

public class Base extends WorldEntity implements Clickable, Tickable, Selectable {


	boolean selected = false;

	/**
	 * Constructor for the base
	 * @param world
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Base(AbstractWorld world, float posX, float posY, float posZ) {
		super(world, posX, posY, posZ, 1, 1, 1);
		this.setTexture("selected");
	}

	/*public void giveAction(DecoAction action) {
		if (!currentAction.isPresent()) {
			currentAction = Optional.of(action);
		}
	}*/

	/**
	 * On click handler
	 */
	@Override
	public void onClick() {
		System.out.println("Base got clicked");

		if (!selected) {
			selected = true;
		}
	}

	/**
	 * On Tick handler
	 * @param i time since last tick
	 */
	@Override
	public void onTick(int i) {

		if (selected) {
			this.setTexture("tree_selected");
		} else {
			this.setTexture("selected");
		}

		/*if (currentAction.isPresent()) {
			currentAction.get().doAction();

			if (currentAction.get().completed()) {
				currentAction = Optional.empty();
			}
		}*/
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void deselect() {
		selected = false;
	}

	@Override
	public Button getButton() {
		Button button = new TextButton("Make Peon", new Skin(Gdx.files.internal("uiskin.json")));
		button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				buttonWasPressed();
			}
		});
		return button;
	}

	@Override
	public void buttonWasPressed() {
		Random rand = new Random();
		/* We probably don't want these in random spots */
		//currentAction = Optional.of(new GenerateAction(new Peon(this.getParent(), rand.nextInt(24), rand.nextInt(24), 0), this.getParent()));
	}
}
