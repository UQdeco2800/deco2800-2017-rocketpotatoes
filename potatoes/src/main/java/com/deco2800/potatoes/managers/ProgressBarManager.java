package com.deco2800.potatoes.managers;

import java.util.ArrayList;

public class ProgressBarManager extends Manager {
	private boolean showPlayerProgress = true;
	private boolean showPotatoProgress = true;
	private boolean showAlliesProgress = true;
	private boolean showEnemyProgress = true;

	private ArrayList<Boolean> progressValues = new ArrayList<>();

	public void togglePlayerProgress() {
		showPlayerProgress = !showPlayerProgress;
	}

	public void togglePotatoProgress() {
		showPotatoProgress = !showPotatoProgress;
	}

	public void toggleAlliesProgress() {
		showAlliesProgress = !showAlliesProgress;
	}

	public void toggleEnemyProgress() {
		showEnemyProgress = !showEnemyProgress;
	}

	public ArrayList<Boolean> getProgressValues() {
		progressValues.clear();
		progressValues.add(showPlayerProgress);
		progressValues.add(showPotatoProgress);
		progressValues.add(showAlliesProgress);
		progressValues.add(showEnemyProgress);

		return new ArrayList<>(progressValues);
	}

}
