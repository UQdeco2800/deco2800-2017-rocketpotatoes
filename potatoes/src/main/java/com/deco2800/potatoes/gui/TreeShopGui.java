package com.deco2800.potatoes.gui;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.entities.resources.FoodResource;
import com.deco2800.potatoes.entities.resources.SeedResource;
import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.managers.GameManager;
import com.deco2800.potatoes.managers.MultiplayerManager;
import com.deco2800.potatoes.managers.PlayerManager;
import com.deco2800.potatoes.managers.TextureManager;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TreeShopGui is generated when the user clicks on a tile on the map. It can
 * only be positioned on tiles where trees can be planted.
 * 
 * @author Dion Lao
 *
 */
public class TreeShopGui extends Gui implements SceneGui {
	private static final transient Logger LOGGER = LoggerFactory.getLogger(TreeShopGui.class);
	private Circle shopShape; // Circle around whole shop
	private Circle cancelShape; // Circle around cross in menu center
	private boolean mouseIn; // Mouse inside shopMenu
	private boolean mouseInCancel; // Mouse inside cancel circle
	private boolean initiated; // Menu should be visible and available
	private boolean plantable; // Set to true if mouseover terrain can plant tree
	private int selectedSegment; // Segment of menu currently being rendered
	private float shopX; // Screen x value of shop
	private float shopY; // Screen y value of shop
	private int shopTileX; // Tile x value of shop
	private int shopTileY; // Tile y value of shop
	private int treeX; // Tile x value where tree will be spawned
	private int treeY; // Tile y value where tree will be spawned

	// The trees that user can purchased. These will all be displayed in its own
	// segment
	private LinkedHashMap<AbstractTree, Color> items;

	private Stage stage;
	private TextureManager textureManager;
	private PlayerManager playerManager;
	private WidgetGroup container;
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

	// Opacity value for treeShop subsection when mouse is not hovering over it
	final private float UNSELECTED_ALPHA = 0.2f;
	// Opacity value for treeShop subsection when mouse hovers over
	final private float SELECTED_ALPHA = 0.5f;
	// Maximum number of tile lengths from player where you can plant trees
	final private int MAX_RANGE = 6;

	/**
	 * Instantiates shop with but doesn't display it yet.
	 */
	public TreeShopGui(Stage stage) {
		// Render menu
		this.stage = stage;
		playerManager = GameManager.get().getManager(PlayerManager.class);
		shopX = 0;
		shopY = 0;
		initiated = false;
		items = new LinkedHashMap<AbstractTree, Color>();
		items.put(new ResourceTree(treeX, treeY, new SeedResource(),0 ), Color.RED);
		items.put(new ResourceTree(treeX, treeY, new FoodResource(),0), Color.BLUE);
		items.put(new ProjectileTree(treeX, treeY), Color.YELLOW);
		items.put(new DamageTree(treeX, treeY, new LightningTreeType()),Color.GREEN);
		items.put(new DamageTree(treeX, treeY, new IceTreeType()),Color.ORANGE);
		items.put(new DamageTree(treeX, treeY, new FireTreeType()),Color.PURPLE);
		items.put(new DamageTree(treeX, treeY, new AcornTreeType()),Color.GREEN);


		for (AbstractTree tree : items.keySet()) {
			tree.setConstructionLeft(0);
		}
		textureManager = GameManager.get().getManager(TextureManager.class);
		container = new WidgetGroup();
		stage.addActor(container);
	}

	@Override
	public void render() {
		float distance = playerManager.distanceFromPlayer(shopTileX, shopTileY);
		if (distance > MAX_RANGE)
			closeShop();
		updateScreenPos();
		createTreeMenu(shopX, shopY, 110);
	}

	/**
	 * Returns maximum range of plantation area from player
	 */
	public int getMaxRange() {
		return MAX_RANGE;
	}

	/**
	 * Returns whether current treeShop is plantable
	 */
	public boolean getPlantable() {
		return plantable;
	}

	/**
	 * Sets plantable value
	 */
	public void setPlantable(boolean plantable) {
		this.plantable = plantable;
	}

	/**
	 * Updates screen position to match tile position.
	 */
	private void updateScreenPos() {
		Vector2 screenPos = Render3D.tileToScreen(stage, shopTileX, shopTileY);
		shopX = screenPos.x;
		shopY = screenPos.y;
	}

	/**
	 * Updates tile position to match mouse position.
	 */
	private void updateTilePos(int x, int y) {
		Vector2 tilePos = Render3D.screenToTile(x, y);

		shopTileX = (int) tilePos.x;
		shopTileY = (int) tilePos.y;

	}

	/**
	 * Creates menu based on input parameters.
	 *
	 * @param x
	 *            Center x point
	 * @param y
	 *            Center y point
	 * @param radius
	 *            Radius of circle
	 */
	private void createTreeMenu(float x, float y, int radius) {
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

	private void renderGui(float x, float y, int radius) {
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
		addCrossLbl(x, guiY);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	/**
	 * Renders the cross label for cancelling the treeShop.
	 */
	private void addCrossLbl(float guiX, float guiY) {
		Label cross = new Label("x", skin);
		cross.setFontScale(1.5f);
		cross.setPosition(guiX - cross.getWidth() / 2, guiY - cross.getHeight() / 2);
		cross.setColor(new Color(255, 255, 255, 0.8f));
		container.addActor(cross);
	}

	/**
	 * Renders the gui sections that are specific to the different items in the
	 * menu.
	 * 
	 */
	private void renderSubMenus(ShapeRenderer shapeRenderer, float guiX, float guiY, int radius) {

		int numSegments = items.entrySet().size();
		int segment = 0;
		int degrees = 360 / numSegments;
		int imgSize = 60;
		int seedSize = 20;
		String texture = "error_box";
		// Draws each subsection of radial menu individually
		for (Map.Entry<? extends AbstractEntity, Color> entry : items.entrySet()) {
			Color c = entry.getValue();
			// Show which segment is highlighted by adjusting opacity
			int startAngle = 360 * segment / numSegments;
			float alpha = segment == selectedSegment && mouseIn && !mouseInCancel ? SELECTED_ALPHA : UNSELECTED_ALPHA;
			float itemAngle = (float) (startAngle + degrees / 2);

			// Set color and draw arc
			shapeRenderer.setColor(new Color(c.r, c.g, c.b, alpha));
			renderQuadrantArea(shapeRenderer, startAngle, guiX, guiY, radius, degrees);

			Vector2 offset = calculateDisplacement((float) (radius / 2), itemAngle);

			// Render Items
			float itemX = guiX - imgSize / 2 + offset.x;
			float itemY = guiY - imgSize / 2 + offset.y;
			renderTreeImage(itemX, itemY, imgSize, texture, entry);

			// Add cost
			renderCostGui(offset, radius, itemAngle, guiX, guiY, seedSize);

			segment++;

		}

	}

	/**
	 * Renders the semi circle areas and colours them accordingly.
	 */
	private void renderQuadrantArea(ShapeRenderer shapeRenderer, int startAngle, float guiX, float guiY, int radius,
			int degrees) {
		// TODO make update with tree cost
		// Checks to see if user can afford it
		if (playerManager.getPlayer().getInventory().getQuantity(new SeedResource()) < 1)
			shapeRenderer.setColor(new Color(200, 200, 200, 0.6f));
		shapeRenderer.arc(guiX, guiY, (int) (radius * 0.85), startAngle, degrees);

	}

	/**
	 * Renders the tree displayed that are available.
	 */
	private void renderTreeImage(float itemX, float itemY, int imgSize, String texture,
			Map.Entry<? extends AbstractEntity, Color> entry) {
		// Add entity texture image
		texture = entry.getKey().getTexture();
		Image treeImg = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(texture))));

		treeImg.setPosition(itemX, itemY);
		treeImg.setWidth(imgSize);
		treeImg.setHeight(imgSize);
		container.addActor(treeImg);
	}

	/**
	 * Renders the resources and amount required to buy tree.
	 */
	private void renderCostGui(Vector2 offset, float radius, float itemAngle, float guiX, float guiY, int seedSize) {
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

		float n = items.size();
		float x = shopShape.x;
		float y = shopShape.y;

		double mouseAngle = calculateAngle(mx - x, my - y);

		double segmentAngle = 360f / n;
		selectedSegment = (int) (mouseAngle / segmentAngle);

	}

	/**
	 * Calculates the angle in degrees from the right horizontal anti-clockwise
	 * based on the change in x and y.
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
			mouseAngle += x < 0 ? 180 : 360;
		else if (x < 0)
			mouseAngle += 180;
		mouseAngle = 360 - mouseAngle; // make it anti clockwise
		return mouseAngle;
	}

	/**
	 * Determines there the mouse is in relation to the treeShop sets the global
	 * variables depending on what it is hovering over. Also calculates which
	 * quadrant it is in.
	 * 
	 * @param x
	 *            screen x value of mouse click
	 * @param y
	 *            screen y value of mouse click
	 */
	public void checkMouseOver(int x, int y) {
		mouseIn = shopShape.contains(x, y) ? true : false;
		mouseInCancel = cancelShape.contains(x, y) ? true : false;
		if (initiated)
			calculateSegment(x, y);

	}

	/**
	 * Starts up or closes treeShop depending on where the user has clicked.
	 * 
	 * @param x
	 *            screen x value of mouse click
	 * @param y
	 *            screen y value of mouse click
	 */
	public void initShop(int x, int y) {
		if (initiated && mouseIn) {
			if (mouseInCancel)
				closeShop();
			else {
				buyTree();
				initiated = false;
			}
		} else if (plantable) {
			updateTilePos(x, y);
			initiated = true;
			setTreeCoords();
		}
	}

	/**
	 * Update coordinates for tree planting in tile coordinates. If the distance to
	 * greater than max, sets to maximum range.
	 * 
	 */
	private void setTreeCoords() {
		treeX = shopTileX;
		treeY = shopTileY;
	}

	/**
	 * Places a tree where the treeShop is positioned
	 */
	private void buyTree() {

		if (!WorldUtil.getEntityAtPosition(treeX, treeY).isPresent()) {
			MultiplayerManager multiplayerManager = GameManager.get().getManager(MultiplayerManager.class);
			AbstractTree newTree;

			newTree = ((AbstractTree) items.keySet().toArray()[selectedSegment]).clone();
			newTree.setPosX(treeX + 0.5f);
			newTree.setPosY(treeY + 0.5f);

			if (!multiplayerManager.isMultiplayer() || multiplayerManager.isMaster()) {
				AbstractTree.constructTree(newTree);
			} else {
				multiplayerManager.broadcastBuildOrder(newTree);
			}
		}
	}

	@Override
	public Vector2 getTileCoords() {
		return Render3D.screenToTile(shopX, shopY);
	}

}
