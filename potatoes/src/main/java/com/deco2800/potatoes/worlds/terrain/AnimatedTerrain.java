package com.deco2800.potatoes.worlds.terrain;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.TextureManager;

import java.util.Map;

public class AnimatedTerrain extends Terrain {
	public static final AnimatedTerrain SHALLOW_WATER = new AnimatedTerrain(new String[]{"water_shallow1",
			"water_shallow2", "water_shallow3", "water_shallow4", "water_shallow5", "water_shallow6", "water_shallow7", "water_shallow8", "water_shallow9", "water_shallow10", "water_shallow11"}, 0.75f,false, 0.1f);

	private final String[] textures;
	private final float frameLength;

	public AnimatedTerrain(String[] textures, float moveScale, boolean plantable, float frameLength) {
		super(textures[0], moveScale, plantable);
		this.textures = textures;
		this.frameLength = frameLength;
	}

	@Override
	public void putCell(Map<String, Cell> m) {
		Array<StaticTiledMapTile> tiles = new Array<>();
		for (String texture : textures) {
			tiles.add(new StaticTiledMapTile(GameManager.get().getManager(TextureManager.class).getTextureRegion(texture)));
		}
		m.put(getTexture(), new Cell().setTile(new AnimatedTiledMapTile(frameLength, tiles)));
	}

	public String[] getTextures() {
		return textures;
	}

	public float getFrameLength() {
		return frameLength;
	}
}
