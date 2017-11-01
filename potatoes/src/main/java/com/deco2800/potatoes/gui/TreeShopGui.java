package com.deco2800.potatoes.gui;

import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.deco2800.potatoes.entities.resources.*;
import com.deco2800.potatoes.entities.trees.*;
import com.deco2800.potatoes.managers.*;
import com.deco2800.potatoes.renderering.Render3D;
import com.deco2800.potatoes.util.WorldUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TreeShopGui is generated when the user clicks on a tile on the map. It can
 * only be positioned on tiles where trees can be planted.
 *
 * @author Dion Lao
 */
public class TreeShopGui extends Gui implements SceneGui {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(TreeShopGui.class);
    private static final float ANGLE_PADDING = 2.0f;
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
    private ArrayList<TreeState> treeStates;
    private ArrayList<TreeState> unlockedTreeStates;
    private HashMap<String, Color> treeColorCode;


    // Opacity value for treeShop subsection when mouse is not hovering over it
    private static final float UNSELECTED_ALPHA = 0.2f;
    // Opacity value for treeShop subsection when mouse hovers over
    private static final float SELECTED_ALPHA = 0.5f;
    // Maximum number of tile lengths from player where you can plant trees
    private static final int MAX_RANGE = 6;

     private int shopRadius = 150;

    /**
     * Instantiates shop with but doesn't display it yet.
     */
    public TreeShopGui(Stage stage) {
        InputManager input = GameManager.get().getManager(InputManager.class);
        input.addKeyDownListener(this::handleKeyDown);
        // Render menu
        this.stage = stage;
        playerManager = GameManager.get().getManager(PlayerManager.class);
        shopX = 0;
        shopY = 0;
        initiated = false;
        treeColorCode = new HashMap<String, Color>();
        treeColorCode.put("resource", Color.GREEN);
        treeColorCode.put("damage", Color.RED);
        treeColorCode.put("defense", Color.BLUE);
        items = new LinkedHashMap<AbstractTree, Color>();


        initTreeState();
        refreshTreeStates();
        for (TreeState treeState : treeStates) {
            items.put(treeState.getTree(), treeColorCode.get(treeState.getTreeType()));
        }

        for (AbstractTree tree : items.keySet()) {
            tree.setConstructionLeft(0);
        }
        textureManager = GameManager.get().getManager(TextureManager.class);
        container = new WidgetGroup();
        stage.addActor(container);
    }

    /**
     * Updates the list of unlocked tree states
     */
    public void refreshTreeStates() {
        unlockedTreeStates = new ArrayList<>();
        if (treeStates != null){
            for (TreeState treeState : treeStates) {
                if (treeState.isUnlocked())
                    unlockedTreeStates.add(treeState);
            }
        }

    }

    private AbstractTree getTreeBinding(int hotkey){
        if (unlockedTreeStates.size() >= hotkey){
            return unlockedTreeStates.get(unlockedTreeStates.size() - hotkey).getTree();
        } else {
            return null;
        }
    }

    private void handleKeyDown(int keycode) {
        if (!initiated)
            return;

        // Tree planting hotkey
        if (keycode > Input.Keys.NUM_0 && keycode < Input.Keys.NUM_9){
            AbstractTree tree = getTreeBinding(keycode - Input.Keys.NUM_0);
            if (tree != null) {
                buyTree(tree.createCopy());
                closeShop();
            }
        }
        
    }

    private void initTreeState() {
        treeStates = new ArrayList<>();

        // Seed resource tree
        Inventory seedTreeCost = new Inventory();
        seedTreeCost.updateQuantity(new SeedResource(), 2);
        TreeState seedTreeState = new TreeState(new SeedTree(treeX, treeY), 
        		seedTreeCost, true, "resource");
        treeStates.add(seedTreeState);

        // Food resource tree
        Inventory foodTreeCost = new Inventory();
        foodTreeCost.updateQuantity(new SeedResource(), 1);
        foodTreeCost.updateQuantity(new FoodResource(), 1);
        TreeState foodTreeState = new TreeState(new FoodTree(treeX, treeY), 
        		foodTreeCost, false, "resource");
        treeStates.add(foodTreeState);
        
        // Pine resource tree
        Inventory pineTreeCost = new Inventory();
        pineTreeCost.updateQuantity(new SeedResource(), 1);
        pineTreeCost.updateQuantity(new PineconeResource(), 1);
        TreeState pineTreeState = new TreeState(new PineTree(treeX, treeY), 
        		pineTreeCost, false, "resource");
        treeStates.add(pineTreeState);

        // Lightning tree
        Inventory lightningTreeCost = new Inventory();
        lightningTreeCost.updateQuantity(new FishMeatResource(), 4);
        lightningTreeCost.updateQuantity(new PearlResource(), 3);
        lightningTreeCost.updateQuantity(new TreasureResource(), 4);
        DamageTree lightningTree = new DamageTree(treeX, treeY, new LightningTreeType());
        TreeState lightningTreeState = new TreeState(lightningTree, lightningTreeCost,
                false, "damage");
        treeStates.add(lightningTreeState);

        // Ice tree
        Inventory iceTreeCost = new Inventory();
        iceTreeCost.updateQuantity(new IceCrystalResource(), 2);
        iceTreeCost.updateQuantity(new SealSkinResource(), 4);
        iceTreeCost.updateQuantity(new IceCrystalResource(), 4);
        DamageTree iceTree = new DamageTree(treeX, treeY, new IceTreeType());
        TreeState iceTreeState = new TreeState(iceTree, iceTreeCost, false, "defense");
        treeStates.add(iceTreeState);

        // Fire tree
        Inventory fireTreeCost = new Inventory();
        fireTreeCost.updateQuantity(new ObsidianResource(), 2);
        fireTreeCost.updateQuantity(new CoalResource(), 4);
        fireTreeCost.updateQuantity(new BonesResource(), 3);
        DamageTree fireTree = new DamageTree(treeX, treeY, new FireTreeType());
        TreeState fireTreeState = new TreeState(fireTree, fireTreeCost, false,
                "damage");
        treeStates.add(fireTreeState);

        // Acorn tree
        Inventory acornTreeCost = new Inventory();
        acornTreeCost.updateQuantity(new PineconeResource(), 2);
        acornTreeCost.updateQuantity(new FoodResource(), 2);
        acornTreeCost.updateQuantity(new SeedResource(), 3);
        DamageTree acornTree = new DamageTree(treeX, treeY, new AcornTreeType());
        TreeState acornTreeState = new TreeState(acornTree, acornTreeCost, true,
                "damage");
        treeStates.add(acornTreeState);
        
        // Cactus tree
        Inventory cactusTreeCost = new Inventory();
        cactusTreeCost.updateQuantity(new CactusThornResource(), 2);
        cactusTreeCost.updateQuantity(new PricklyPearResource(), 3);
        cactusTreeCost.updateQuantity(new TumbleweedResource(), 4);
        DamageTree cactusTree = new DamageTree(treeX, treeY, new CactusTreeType());
        TreeState cactusTreeState = new TreeState(cactusTree, cactusTreeCost, false,
        		"damage");
        treeStates.add(cactusTreeState);
        
        // Defense tree
        Inventory defenseTreeCost = new Inventory();
        defenseTreeCost.updateQuantity(new SeedResource(), 1);
        DefenseTree defenseTree = new DefenseTree(treeX, treeY);
        TreeState defenseTreeState = new TreeState(defenseTree, defenseTreeCost, false,
        		"defense");
        treeStates.add(defenseTreeState);
    }

    /**
     * Adds treeStates into list of treeStates. If the tree type already exists,
     * replace it.
     */
    public void addTreeState(TreeState treeState) {
        removeTreeState(treeState.getTree());
        treeStates.add(treeState);
    }

    /**
     * Removes treeState of tree type.
     */
    public void removeTreeState(AbstractTree tree) {
        if (treeStates.contains(tree)) {
            treeStates.remove(tree);
        }
    }

    /**
     * Returns a createCopy of treeStates.
     */
    public ArrayList<TreeState> getTreeStates() {
        ArrayList<TreeState> clone = new ArrayList<TreeState>();
        clone.addAll(treeStates);
        return clone;
    }

    /**
     * Returns TreeState of type tree
     *
     * @param tree
     * @return
     */
    public TreeState getTreeStateByTree(AbstractTree tree) {
        for (TreeState treeState : treeStates) {
            if (treeState.getTree().toString().equals(tree.toString()))
                return treeState;
        }
        return null;
    }

    /**
     * Returns TreeState with name treeName
     * @param treeName name of tree
     * @return treeState with name treeName
     */
    public TreeState getTreeStateByName(String treeName) {
        for (TreeState treeState : treeStates) {
            if (treeState.getTree().getName().equals(treeName))
                return treeState;
        }
        return null;
    }

    @Override
    public void render() {
        float distance = playerManager.distanceFromPlayer(shopTileX, shopTileY);
        if (distance > MAX_RANGE)
            closeShop();
        updateScreenPos();
        createTreeMenu(shopX, shopY, shopRadius);
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
     * @param x      Center x point
     * @param y      Center y point
     * @param radius Radius of circle
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
     * @param x      x value of center of shop
     * @param y      y value of center of shop
     * @param radius radius of shop
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
     */
    private void renderSubMenus(ShapeRenderer shapeRenderer, float guiX, float guiY, int radius) {
        int numSegments = unlockedTreeStates.size();
        if (numSegments == 0)
            return;
        int segment = 0;
        int degrees = 360 / numSegments;
        int imgSize = 60;
        int seedSize = 20;
        String texture = "error_box";
        // Draws each subsection of radial menu individually
        for (Map.Entry<? extends AbstractTree, Color> entry : items.entrySet()) {
            if (getTreeStateByTree(entry.getKey()).isUnlocked()) {
                Color c = entry.getValue();
                // Show which segment is highlighted by adjusting opacity
                int startAngle = 360 * segment / numSegments;
                float alpha = segment == selectedSegment && mouseIn && !mouseInCancel ?
                        SELECTED_ALPHA : UNSELECTED_ALPHA;
                float itemAngle = startAngle + (float) degrees / 2;

                // Set color and draw arc
                shapeRenderer.setColor(new Color(c.r, c.g, c.b, alpha));
                renderQuadrantArea(shapeRenderer, startAngle, guiX, guiY, radius, degrees,
                        entry.getKey());

                Vector2 offset = calculateDisplacement((float)radius / 2, itemAngle);

                // Render Items
                float itemX = guiX - imgSize / 2 + offset.x;
                float itemY = guiY - imgSize / 2 + offset.y;
                renderTreeImage(itemX, itemY, imgSize, entry, numSegments - segment);

                // Add cost
                TreeState treeState = getTreeStateByTree(entry.getKey());
                Inventory cost = treeState.getCost();
                int n = cost.getInventoryResources().size();
                int i = 1;
                for (Resource resource : cost.getInventoryResources()) {
                    float costAngle = startAngle + (float) degrees * i / (n + 1);
                    renderCostGui(offset, radius, costAngle, guiX, guiY, seedSize,
                            resource.getTexture(), cost.getQuantity(resource));
                    i++;
                }
                segment++;
            }
        }
    }

    /**
     * Renders the semi circle areas and colours them accordingly.
     */
    private void renderQuadrantArea(ShapeRenderer shapeRenderer, int startAngle, float guiX, float guiY, int radius,
                                    int degrees, AbstractTree tree) {
        // Checks to see if user can afford it
        if (!playerManager.getPlayer().canAfford(tree))
            shapeRenderer.setColor(new Color(200, 200, 200, 0.6f));
        shapeRenderer.arc(guiX, guiY, (int) (radius * 0.85), startAngle, degrees);

    }

    /**
     * Renders the tree displayed that are available.
     */
    private void renderTreeImage(float itemX, float itemY, int imgSize,
                                 Map.Entry<? extends AbstractTree, Color> entry, int
                                         number) {
        // Add entity texture image
        String texture = entry.getKey().getTexture();
        Image treeImg = new Image(new TextureRegionDrawable(new TextureRegion(textureManager.getTexture(texture))));

        treeImg.setPosition(itemX, itemY);
        treeImg.setWidth(imgSize);
        treeImg.setHeight(imgSize);
        container.addActor(treeImg);

        // Add label and name
        Label treeLbl = new Label(number +": "+entry.getKey().getName(), skin);
        treeLbl.setFontScale(0.7f);
        treeLbl.setPosition(itemX, itemY);
        container.addActor(treeLbl);
    }

    /**
     * Renders the resources and amount required to buy tree.
     */
    private void renderCostGui(Vector2 offset, float radius, float itemAngle, float
            guiX, float guiY, int seedSize, String sprite, int cost) {

        Table costContainer = new Table();
        costContainer.setFillParent(true);

        costContainer.defaults().width(20);
        costContainer.pad(20f);

        Image seedImg = new Image(new TextureRegionDrawable(new TextureRegion
                (textureManager.getTexture(sprite))));
        Label costLbl = new Label(Integer.toString(cost), skin);

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
     * @param d       distance from center
     * @param degrees degrees from right horizontal line
     * @return a Vector2 of the offset
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
     * @param mx mouse point x
     * @param my mouse point y
     */
    private void calculateSegment(float mx, float my) {

        float n = unlockedTreeStates.size();
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
     * @param x Change in x
     * @param y Change in y
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
     * @param x screen x value of mouse click
     * @param y screen y value of mouse click
     */
    public void checkMouseOver(int x, int y) {
        mouseIn = shopShape.contains(x, y);
        mouseInCancel = cancelShape.contains(x, y);
        if (initiated)
            calculateSegment(x, y);

    }

    /**
     * Starts up or closes treeShop depending on where the user has clicked.
     *
     * @param x screen x value of mouse click
     * @param y screen y value of mouse click
     */
    public void initShop(int x, int y) {
        if (!GameManager.get().isPaused()) {
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
    }

    /**
     * Update coordinates for tree planting in tile coordinates. If the distance to
     * greater than max, sets to maximum range.
     */
    private void setTreeCoords() {
        treeX = shopTileX;
        treeY = shopTileY;
    }

    /**
     * Places a tree where the treeShop is positioned
     */
    private void buyTree() {

        AbstractTree newTree;
        newTree = unlockedTreeStates.get(selectedSegment).getTree().createCopy();
        buyTree(newTree);
    }

    /**
     * Places a tree where the treeShop is positioned
     */
    private void buyTree(AbstractTree newTree) {

        if (!WorldUtil.getEntityAtPosition(treeX, treeY).isPresent()) {

            newTree.setPosX(treeX + 0.5f);
            newTree.setPosY(treeY + 0.5f);
            AbstractTree.constructTree(newTree);
        }
    }


    @Override
    public Vector2 getTileCoords() {
        return Render3D.screenToTile(shopX, shopY);
    }



}
