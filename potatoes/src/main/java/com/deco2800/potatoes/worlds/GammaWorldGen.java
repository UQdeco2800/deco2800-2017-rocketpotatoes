package com.deco2800.potatoes.worlds;

import java.util.Arrays;
import java.util.Random;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.deco2800.potatoes.entities.Selectable;
import com.deco2800.potatoes.renderering.Renderable;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.*;
import com.badlogic.gdx.maps.MapLayer.*;
import com.badlogic.gdx.maps.MapLayers;
import com.deco2800.potatoes.managers.*;
/**
 * Initial world using preset world file.
 *
 * @author leggy
 *
 */
public class GammaWorldGen extends AbstractWorld {

	/**
	 * Constructor for InitialWorld
	 */
	private TextureManager textureManager;
	private int rando = 0;
	public GammaWorldGen() {
		textureManager = GameManager.get().getManager(TextureManager.class);
		/* Load up the map for this world */
		this.setWidth(25);
		this.setLength(25);
		this.map = new TiledMap();
		map.getProperties().put("tilewidth",55);
		map.getProperties().put("tileheight",32);
		MapLayers layers = map.getLayers();
		TiledMapTileLayer layer = new TiledMapTileLayer(25, 25, 55, 32);

		Cell[] cells = {new Cell(), new Cell(), new Cell()};
		cells[0].setTile(new StaticTiledMapTile(new TextureRegion(textureManager.getTexture("grass"))));
		cells[1].setTile(new StaticTiledMapTile(new TextureRegion(textureManager.getTexture("ground_1"))));
		cells[2].setTile(new StaticTiledMapTile(new TextureRegion(textureManager.getTexture("w1"))));
		// Random tile choice
		float[][] randomTiles = new float[25][25];
		for (int x = 0; x < randomTiles.length; x++) {
			for (int y = 0; y < randomTiles[x].length; y++) {
				if (x<1){
					if (y<1){
						randomTiles[x][y] = (float)Math.random()*3;
					} else {
						randomTiles[x][y] = randomTiles[x][y - 1] + (float)(Math.round(Math.random())*(Math.random()+1));
					}
				} else 	if (y<1){
					if (x<1){
						randomTiles[x][y] = (float)Math.random()*3;
					} else {
						randomTiles[x][y] = randomTiles[x - 1][y] + (float)(Math.round(Math.random())*(Math.random()+1));
					}
				} else{
					rando = (int)Math.round(Math.random());
					randomTiles[x][y] = randomTiles[x - rando][y-(1-rando)]+ (float)(Math.round(Math.random())*(Math.random()+1));
				}
			}
		}
		// random array for tile choice should be 1/2 size and smoothed instead
		//RandomWorldGeneration.diamondSquareAlgorithm(randomTiles, 25, 0.5f, 0.9f);
		for(int i = 0; i < 25; i++) {
			for(int j = 0; j < 25; j++) {
				layer.setCell(i, j, cells[Math.round(randomTiles[i][j])%3]);
			}
		}
		layers.add(layer);

		Random random = new Random();
		for(int i = 0; i < 5; i++) {
			//this.addEntity(new Squirrel(10 + random.nextFloat() * 10, 10 + random.nextFloat() * 10, 0));
		}

	}

	/**
	 * Deselects all entities.
	 */
	public void deSelectAll() {
		for (Renderable r : this.getEntities().values()) {
			if (r instanceof Selectable) {
				((Selectable) r).deselect();
			}
		}
	}
}
