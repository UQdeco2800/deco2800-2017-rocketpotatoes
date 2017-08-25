package com.deco2800.potatoes.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.exceptions.InvalidResourceException;
import com.deco2800.potatoes.gui.InventoryGui;

public class Inventory {
	
	/*
	 * Logger for all info/warning/error logs
	 */
	private static final transient Logger LOGGER = LoggerFactory.getLogger(Inventory.class);
	
	/*
	 * A mapping of possible resource items to the number of items the player
	 * holds
	 */
	private GuiManager guiManager;
	
	/*
	 * A mapping of possible resource items to the number of items the player
	 * holds
	 */
	private TreeMap<Resource, Integer> inventoryMap = new TreeMap<Resource,Integer>();
	
	/*
	 * invariant:
	 * 
	 * Inventory!=null && !inventory.containsValue(null) &&
	 * 
	 * for each resource in inventory.keySet(), inventory.get(resource) >= 0
	 * 
	 */

	public Inventory() {
		// Empty constructor for serialization
	}

	/**
	 * <p>
	 * Creates a new instance of the class in which every resource quantity is set to 0
	 * </p>
	 * 
	 * <p>
	 * For any resource in resources, this.getQuantity(resource) == 0
	 * </p>
	 */
	public Inventory(HashSet<Resource> resources) throws InvalidResourceException {
		inventoryMap = new TreeMap<Resource,Integer>();
		if (resources == null) {
			throw new InvalidResourceException("Resources cannot be null, please instantiate the class with valid resources");
		}
		for (Resource resource : resources) {
			if (resource == null) throw new InvalidResourceException("Resource cannot be null, please instantiate the class with valid resources");
			inventoryMap.put(resource, 0);
		}
	}

	public TreeMap<Resource, Integer> getMap(){
		// Testing purposes
		return inventoryMap;
	}
	
	/**
	 * <p>
	 * Returns the resources stored in this object
	 * </p>
	 * 
	 * @return the a set of resources
	 */
	public Set<Resource> getInventoryResources() {
		return new HashSet<Resource>(inventoryMap.keySet());
	}
	
	/**
	 * <p>
	 * Adds a resource to the inventory with 0 quantity
	 * </p>
	 * 
	 */
	public void addInventoryResource(Resource resource) {
		if (resource == null){
			LOGGER.error("Please supply a valid resource");
		}
		if (!getInventoryResources().contains(resource)){
			inventoryMap.put(resource, 0);
		}
	}
	
	/**
	 * <p>
	 * Removes a resource from the inventory (and its associated quantity)
	 * </p>
	 */
	public void removeInventoryResource(Resource resource) {
		if (resource == null || !getInventoryResources().contains(resource)){
			LOGGER.error("Please supply a valid resource");
		}
		System.out.println(resource);
		System.out.println(inventoryMap);
		inventoryMap.remove(resource);
	}
	
	
	/**
	 * <p>
	 * Returns the quantity for any given resource.
	 * </p>
	 * 
	 * <p>
	 * The quantity is always non-negative -> this method will always return a
	 * positive integer or zero.
	 * </p>
	 * 
	 * @param resource
	 *            the resource whose associated quantity will be returned
	 * @return the number of items of the given resource
	 */
	public int getQuantity(Resource resource) {
		if (!this.getInventoryResources().contains(resource)) {
			LOGGER.error("Please supply a valid resource");
		}
		return inventoryMap.get(resource);
	}
	
	/**
	 * <p>
	 * Returns the quantity for any given resource and zero if non existing.
	 * </p>
	 * 
	 * <p>
	 * The quantity is always non-negative -> this method will always return a
	 * positive integer or zero.
	 * </p>
	 * 
	 * @param resource
	 *            the resource whose associated quantity will be returned
	 * @return the number of items of the given resource
	 */
	public int getAbsoluteQuantity (Resource resource) {
		try {
			return getQuantity(resource);
		} catch (InvalidResourceException e) {
			return 0;
		}
	}

	/**
	 * <p>
	 * Updates the quantity of resources by adding parameter amount to the
	 * current quantity.
	 * </p>
	 * 
	 * <p>
	 * Parameter amount may be either a negative or positive integer (or zero),
	 * but an Exception will be thrown if the result of adding amount to the
	 * current resource on the corridor will result in a negative quantity for
	 * that resource
	 * </p>
	 * 
	 * @param resource
	 *            the resource whose amount will be updated
	 * @param amount
	 *            the number of resources that will be added
	 * @return result
	 * 			  returns 1 if successful and 0 otherwise
	 */
	public int updateQuantity(Resource resource, int amount) {
		int result = 1;
		
		if (!this.getInventoryResources().contains(resource)) {
			LOGGER.error("Please supply a valid resource");
			result = 0;
		} else {
			int currentAmount = getQuantity(resource);
			
			// check that the resource amount would not become negative.
			if (currentAmount + amount < 0) {
				LOGGER.warn("Sorry, not enough " + resource.toString());
				result = 0;
			} else {
				inventoryMap.put(resource, currentAmount + amount);
				guiManager = (GuiManager)GameManager.get().getManager(GuiManager.class);
				((InventoryGui)guiManager.getGui(InventoryGui.class)).increaseInventory(resource.getTypeName(), currentAmount + amount);
			}
			
		}
		
		return result;
			
	}

	/**
	 * <p>
	 * This method adds all of the items in parameter extraIventory to this
	 * object.
	 * </p>
	 * 
	 * <p>
	 * That is, for each resource, this method updates the quantity of that
	 * resource in this object
	 * </p>
	 * 
	 * <p>
	 * This method will not modify extraInventory (unless this == extraInventory)
	 * </p>
	 * 
	 * @param extraInventory
	 *            the extra items to be added to this object
	 */
	public void updateInventory(Inventory extraItems) {
		if (extraItems == null){
			LOGGER.warn("Cannot add null to Inventory");
		}
		for (Resource resource : extraItems.inventoryMap.keySet()) {
			if (inventoryMap.containsKey(resource)) {
				inventoryMap.put(resource, getQuantity(resource) + extraItems.getQuantity(resource));
			} else {
				inventoryMap.put(resource, extraItems.getQuantity(resource));
			}
			
		}
	}
	
	

}