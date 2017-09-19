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
import com.deco2800.potatoes.entities.Player;
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
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;

public class TreeShopGui extends Gui implements SceneGui {
	private Circle shopShape;
	private Circle cancelShape;
	private int selectedSegment;
	private LinkedHashMap<AbstractTree, Color> items;
	private boolean mouseIn; // Mouse inside shopMenu
	private boolean mouseInCancel; // Mouse inside cancel circle
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
	private Player player;

	final private float UNSELECTED_ALPHA = 0.2f;
	final private float SELECTED_ALPHA = 0.5f;
	final private int MAX_RANGE = 4;

	public TreeShopGui(Stage stage) {
		// Render menu
		this.stage = stage;
		player = new Player();
		shopX = 300;
		shopY = 300;
		initiated = false;
		items = new LinkedHashMap<AbstractTree, Color>();
		items.put(new ResourceTree(treeX, treeY, 0, new SeedResource(), 2), Color.RED);
		items.put(new ResourceTree(treeX, treeY, 0, new FoodResource(), 8), Color.BLUE);
		items.put(new Tower(treeX, treeY, 0), Color.YELLOW);
		for (AbstractTree tree : items.keySet()) {
			tree.setConstructionLeft(0);
		}
		textureManager = GameManager.get().getManager(TextureManager.class);
		container = new WidgetGroup();
		stage.addActor(container);
	}

	@Override
	public void render() {
		updateScreenPos();
		createTreeMenu(shopX, shopY, 110);
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

		Player player = GameManager.get().getManager(PlayerManager.class).getPlayer();
		Vector3 playerPos = tileToScreen(player.getPosX(), player.getPosY());
		double distance = Math.sqrt(Math.pow(shopX - playerPos.x, 2.0) + Math.pow(shopY - playerPos.y, 2));
		
		float tileWidth = (int) GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
		float range = MAX_RANGE * tileWidth;
		if (distance > range) {
			double angle = calculateAngle(shopX-playerPos.x, shopY-playerPos.y);
			angle = 360 - angle;
			angle = Math.toRadians(angle);
			shopX = (int) (range * Math.cos(angle) + playerPos.x);
			shopY = (int) (range * Math.sin(angle) + playerPos.y);
		}
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
		shopShape = new Circle(x, y, radius);
		cancelShape = new Circle(x, y, radius * 0.2f);

		container.clear();

		if (initiated)
			renderGui(x, y, radius);

	}

	/**
	 * Renders the shapes and gui elements of treeShop.
	 * 
	 * @param x
	 *            x value of center of shop
	 * @param y
	 *            y value of center of shop
	 * @param radius
	 *            radius of shop
	 */

	private void renderGui(int x, int y, int radius) {
		Gdx.gl.glEnable(GL20.GL_BLEND);

		ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);

		float guiY = stage.getHeight() - y;

		shapeRenderer.setColor(new Color(0, 0, 0, SELECTED_ALPHA));
		if (mouseIn)
			shapeRenderer.setColor(new Color(0, 0, 0, 0.7f));
		shapeRenderer.circle(x, guiY, radius);

		renderSubMenus(shapeRenderer, x, guiY, radius);

		shapeRenderer.setColor(new Color(0, 0, 0, 0.5f));
		if (mouseInCancel)
			shapeRenderer.setColor(new Color(10, 10, 10, 0.9f));
		shapeRenderer.circle(x, guiY, cancelShape.radius);
		Label cross = new Label("x", skin);
		cross.setFontScale(1.5f);
		cross.setPosition(x - cross.getWidth() / 2, guiY - cross.getHeight() / 2);
		cross.setColor(new Color(255, 255, 255, 0.8f));
		container.addActor(cross);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	/**
	 * Renders the gui sections that are specific to the different items in the
	 * menu.
	 * 
	 */
	private void renderSubMenus(ShapeRenderer shapeRenderer, int guiX, float guiY, int radius) {

		int numSegments = items.entrySet().size();
		int segment = 0;
		int degrees = 360 / numSegments;
		int imgSize = 60;
		int seedSize = 20;
		String texture;
		// Draws each subsection of radial menu individually
		for (Map.Entry<? extends AbstractEntity, Color> entry : items.entrySet()) {
			Color c = entry.getValue();
			// Show which segment is highlighted by adjusting opacity
			float alpha = (segment == selectedSegment && mouseIn && !mouseInCancel) ? SELECTED_ALPHA : UNSELECTED_ALPHA;
			// Set color and draw arc
			shapeRenderer.setColor(new Color(c.r, c.g, c.b, alpha));
			// TODO make update with tree cost
			if (player.getInventory().getQuantity(new SeedResource())<1)
				shapeRenderer.setColor(new Color(200,200,200,0.8f));
			int startAngle = 360 * (segment) / (numSegments);
			shapeRenderer.arc(guiX, guiY, (int) (radius * 0.85), startAngle, degrees);

			// Add entity texture image
			texture = entry.getKey().getTexture();
			Image treeImg = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(texture))));

			float itemAngle = startAngle + degrees / 2;

			Vector2 offset = calculateDisplacement(radius / 2, itemAngle);

			float itemX = guiX - imgSize / 2 + offset.x;
			float itemY = guiY - imgSize / 2 + offset.y;

			treeImg.setPosition(itemX, itemY);
			treeImg.setWidth(imgSize);
			treeImg.setHeight(imgSize);
			container.addActor(treeImg);

			// Add cost
			Table costContainer = new Table();
			costContainer.setFillParent(true);

			costContainer.defaults().width(20);
			costContainer.pad(20f);

			Image seedImg = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture("seed"))));

			Label costLbl = new Label("1", skin);

			offset = calculateDisplacement(radius * 0.86f, itemAngle + 2);
			costContainer.setPosition(guiX + offset.x, guiY + offset.y);
			costContainer.setTransform(true);

			costContainer.add(seedImg).size(seedSize, seedSize);
			costContainer.add(costLbl).bottom().left();

			costContainer.addAction(Actions.rotateBy(itemAngle + 90));
			container.addActor(costContainer);

			segment++;

		}

	}

	/**
	 * Removes treeShop from screen without planting a tree.
	 */
	public void closeShop() {
		initiated = false;
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
	private Vector2 calculateDisplacement(float d, float degrees) {
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

		float n = 3;
		float x = shopShape.x;
		float y = shopShape.y;

		double mouseAngle = calculateAngle(mx - x, my - y);

		double segmentAngle = 360f / n;
		int segment = (int) (mouseAngle / segmentAngle);
		this.selectedSegment = segment;

	}

	/**
	 * Calculates the angle in degrees from the right horizontal anti-clockwise based on the
	 * change in x and y.
	 * 
	 * @param x
	 *            Change in x
	 * @param y
	 *            Change in y
	 * @return Angle anti-clockwise from right horizontal
	 */
	private double calculateAngle(float x, float y) {
		double mouseAngle = Math.atan(y / x);
		mouseAngle = mouseAngle * 180 / Math.PI;

		// Calculate actual angle with each quadrant
		if (y < 0)
			mouseAngle += (x < 0) ? 180 : 360;
		else if (x < 0)
			mouseAngle += 180;
		mouseAngle = 360 - mouseAngle; // make it anti clockwise
		return mouseAngle;
	}

	public void checkMouseOver(int x, int y) {
		mouseIn = shopShape.contains(x, y) ? true : false;
		mouseInCancel = cancelShape.contains(x, y) ? true : false;
		if (initiated)
			calculateSegment(x, y);

	}

	public void initShop(int x, int y) {
		player = GameManager.get().getManager(PlayerManager.class).getPlayer();
		if (initiated && mouseIn) {
			if (mouseInCancel)
				closeShop();
			else {
				buyTree();
				initiated = false;
			}
		} else {
			updateTilePos(x, y);
			initiated = true;
			setTreeCoords(x, y);
		}
	}

	/**
	 * Update coordinates for tree planting in tile coordinates. If the distance to
	 * greater than max, sets to maximum range.
	 * 
	 */
	private void setTreeCoords(int x, int y) {

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
			
			newTree = ((AbstractTree) items.keySet().toArray()[selectedSegment]).clone();
			newTree.setPosX(treeX);
			newTree.setPosY(treeY);
			newTree.setPosZ(0);

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
