package com.deco2800.potatoes.managers;

public class ProgressBarManager extends Manager {
	private boolean showPlayerProgress = true;
	private boolean showPotatoProgress = true;
	private boolean showAlliesProgress = true;
	private boolean showEnemyProgress = true;

	public void togglePlayerProgress() {
		showPlayerProgress = !showPlayerProgress;
	}

	public void togglePotatoProgress() {
		showPotatoProgress = !showPotatoProgress;
	}

	public void toggleAlliesProgress() {
		showAlliesProgress = !showAlliesProgress;
	}

	public void toggleEnemiesProgress() {
		showEnemyProgress = !showEnemyProgress;
	}

	public boolean showPlayerProgress() {
		return showPlayerProgress;
	}

	public boolean showPotatoProgress() {
		return showPotatoProgress;
	}

	public boolean showAlliesProgress() {
		return showAlliesProgress;
	}

	public boolean showEnemiesProgress() {
		return showEnemyProgress;
	}

}
