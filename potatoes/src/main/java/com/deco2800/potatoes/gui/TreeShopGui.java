package com.deco2800.potatoes.gui;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.Tower;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.AbstractTree;
import com.deco2800.potatoes.entities.trees.DamageTree;
import com.deco2800.potatoes.entities.trees.ProjectileTree;
import com.deco2800.potatoes.entities.trees.ResourceTree;
import com.deco2800.potatoes.managers.CameraManager;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;

public class TreeShopGui extends Gui implements SceneGui {
	private Circle shopShape;
	private int selectedSegment;
	private LinkedHashMap<AbstractEntity, Color> items;
	private boolean mouseIn;
	private boolean initiated;
	private Stage stage;
	private int shopX;
	private int shopY;
	private int shopTileX;
	private int shopTileY;
	private int treeX;
	private int treeY;
	private TextureManager textureManager;
	private WidgetGroup container;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	final private float UNSELECTED_ALPHA = 0.2f;
	final private float SELECTED_ALPHA = 0.5f;

	public TreeShopGui(Stage stage) {
		// Render menu
		this.stage = stage;
		shopX = 300;
		shopY = 300;
		initiated = false;
		items = new LinkedHashMap<AbstractEntity, Color>();
		items.put(new ResourceTree(treeX, treeY, 0, new SeedResource(), 2), Color.RED);
		items.put(new ResourceTree(treeX, treeY, 0, new FoodResource(), 8), Color.BLUE);
		items.put(new Tower(treeX, treeY, 0), Color.YELLOW);
		textureManager = GameManager.get().getManager(TextureManager.class);
		container = new WidgetGroup();
		stage.addActor(container);
	}

	@Override
	public void render() {
		updateScreenPos();
		createTreeMenu(shopX, shopY, 130);
	}

	private Vector3 worldToGuiScreenCoordinates(float x, float y, float z) {
		Vector3 screen = GameManager.get().getManager(CameraManager.class).getCamera()
				.project(new Vector3(x, y - stage.getHeight() + 1, z));
		screen.y = -screen.y;
		return screen;
	}

	/**
	 * Updates screen position to match tile position.
	 */
	private void updateScreenPos() {
		Vector3 screenPos = tileToScreen(shopTileX, shopTileY);
		shopX = (int) screenPos.x;
		shopY = (int) screenPos.y;
	}

	/**
	 * Updates tile position to match mouse position.
	 */
	private void updateTilePos(int x, int y) {
		Vector2 tilePos = screenToTile(x, y);

		shopTileX = (int) tilePos.x;
		shopTileY = (int) tilePos.y;
	}

	/**
	 * Creates menu based on input parameters.
	 * 
	 * @param items
	 *            A HashMap with each AbstractEntity as the key and the
	 *            corresponding color as value
	 * @param x
	 *            Center x point
	 * @param y
	 *            Center y point
	 * @param radius
	 *            Radius of circle
	 */
	private void createTreeMenu(int x, int y, int radius) {
		Circle circle = new Circle(x, y, radius);
		shopShape = circle;

		container.clear();
		if (initiated)
			renderGui(x, y, radius);

	}

	private void renderGui(int x, int y, int radius) {
		Gdx.gl.glEnable(GL20.GL_BLEND);

		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);

		int numSegments = items.entrySet().size();
		shapeRenderer.setColor(new Color(0, 0, 0, SELECTED_ALPHA));
		if (mouseIn)
			shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));

		float guiY = stage.getHeight() - y;
		shapeRenderer.circle(x, guiY, radius);

		int segment = 0;
		int degrees = 360 / numSegments;
		int imgSize = 60;
		int seedSize = 20;
		String texture;
		// Draws each subsection of radial menu individually
		for (Map.Entry<? extends AbstractEntity, Color> entry : items.entrySet()) {
			Color c = entry.getValue();
			// Show which segment is highlighted by adjusting opacity
			float alpha = (segment == selectedSegment) ? SELECTED_ALPHA : UNSELECTED_ALPHA;
			// Set color and draw arc
			shapeRenderer.setColor(new Color(c.r, c.g, c.b, alpha));
			int startAngle = 360 * (segment) / (numSegments);
			shapeRenderer.arc(x, guiY, (int) (radius * 0.85), startAngle, degrees);

			// Add entity texture image
			texture = entry.getKey().getTexture();
			Image treeImg = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(texture))));

			float itemAngle = startAngle + degrees / 2;

			Vector2 offset = calculateDisplacement(radius/2,itemAngle);
			
			float itemX = x - imgSize / 2 + offset.x;
			float itemY = guiY - imgSize / 2 + offset.y;

			treeImg.setPosition(itemX, itemY);
			treeImg.setWidth(imgSize);
			treeImg.setHeight(imgSize);
			container.addActor(treeImg);

			// Add cost
			Table costContainer = new Table();
			costContainer.setFillParent(true);

			costContainer.defaults().width(20);
			costContainer.padTop(10);
			costContainer.padRight(10);
			
			Image seedImg = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("seed"))));
			//seedImg.setHeight(seedSize);
			//seedImg.setWidth(seedSize);
			//seedImg.setScaling(Scaling.fit);
			
			//offset = calculateDisplacement(radius*0.9f, itemAngle-2);
			//seedImg.setPosition(x - seedSize/2 + offset.x, guiY - seedSize/2 + offset.y);
			//seedImg.setRotation(itemAngle+90);
			
			Label costLbl = new Label("1", skin);
			//costLbl.setAlignment(Align.right);
			
			offset = calculateDisplacement(radius*0.86f, itemAngle+2);
			costContainer.setPosition(x + offset.x, guiY + offset.y);
			costContainer.setTransform(true);

			costContainer.add(seedImg).size(seedSize, seedSize);
			costContainer.add(costLbl).bottom().left();

			costContainer.addAction(Actions.rotateBy(itemAngle+90));
			container.addActor(costContainer);
			
			segment++;

		}

		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	/**
	 * Calculates the x and y offset required to place an object at distance r from
	 * center with angle degrees from right horizontal.
	 * 
	 * @param d
	 *            distance from center
	 * @param degrees
	 *            degrees from right horizontal line
	 * @return
	 */
	private Vector2 calculateDisplacement( float d, float degrees) {
		float offsetX = (float) (d * Math.cos(degrees * Math.PI / 180));
		float offsetY = (float) (d * Math.sin(degrees * Math.PI / 180));
		
		return new Vector2(offsetX, offsetY);
	}

	/**
	 * Determines which segment of a circle the mouse is in. This starts counting
	 * from right hand side counter clockwise.
	 * 
	 * @param mx
	 *            mouse point x
	 * @param my
	 *            mouse point y
	 * @return integer corresponding to the segment number the mouse is within or -1
	 *         if mouse was not within bounds.
	 */
	private void calculateSegment(float mx, float my) {

		int n = 3;
		float x = shopShape.x;
		float y = shopShape.y;
		double mouseAngle = Math.atan((my - y) / (mx - x));
		mouseAngle = mouseAngle * 180 / Math.PI;

		// Calculate actual angle with each quadrant
		if (my - y < 0)
			mouseAngle += (mx - x < 0) ? 180 : 360;
		else if (mx - x < 0)
			mouseAngle += 180;
		mouseAngle = 360 - mouseAngle; // make it anti clockwise

		double segmentAngle = 360 / n;
		int segment = (int) (mouseAngle / segmentAngle);
		this.selectedSegment = segment;

	}

	public void checkMouseOver(int x, int y) {
		mouseIn = shopShape.contains(x, y) ? true : false;
		if (initiated)
			calculateSegment(x, y);

	}

	public void initShop(int x, int y) {
		if (initiated) {
			buyTree();
			initiated = false;
		} else {
			updateTilePos(x, y);
			initiated = true;
			setTreeCoords(x, y);
		}
	}

	public void setTreeCoords(int x, int y) {
		Vector2 tile = screenToTile(x, y);
		treeX = (int) Math.floor(tile.x);
		treeY = (int) Math.floor(tile.y);
	}

	private Vector2 screenToTile(float x, float y) {
		Vector3 world = Render3D.screenToWorldCoordiates(x, y, 1);
		return Render3D.worldPosToTile(world.x, world.y);
	}

	private Vector3 tileToScreen(float x, float y) {
		Vector2 tile = Render3D.tileToWorldPos(x, y);
		Vector3 screent = worldToGuiScreenCoordinates(tile.x, tile.y, 1);
		return new Vector3(screent.x, screent.y, screent.z);
	}

	private void buyTree() {

		if (!WorldUtil.getEntityAtPosition(treeX, treeY).isPresent()) {
			MultiplayerManager multiplayerManager = GameManager.get().getManager(MultiplayerManager.class);
			AbstractTree newTree;

			// Select random tree, and either make it in singleplayer or broadcast it in mp
			/*
			 * switch (selectedSegment + 1) {
			 * 
			 * case 1: newTree = new ResourceTree(treeX, treeY, 0, new FoodResource(), 8);
			 * break; case 2: newTree = new ResourceTree(treeX, treeY, 0); break; default:
			 * newTree = new Tower(treeX, treeY, 0); break; }
			 */

			newTree = ((AbstractTree) items.keySet().toArray()[selectedSegment]).clone();
			newTree.setPosX(treeX);
			newTree.setPosY(treeY);
			newTree.setPosZ(0);
			System.out.println(newTree.toString());

			if (!multiplayerManager.isMultiplayer() || multiplayerManager.isMaster()) {
				AbstractTree.constructTree(newTree);
			} else {
				multiplayerManager.broadcastBuildOrder(newTree);
			}
		}
	}

	@Override
	public Vector2 getTileCoords() {
		return screenToTile(shopX, shopY);
	}

}
