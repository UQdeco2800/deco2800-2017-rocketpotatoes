package com.deco2800.potatoes.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.deco2800.potatoes.entities.Resource;
import com.deco2800.potatoes.exceptions.InvalidResourceException;
import com.deco2800.potatoes.exceptions.InvalidInventoryException;

public class Inventory {

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
	 * @throws Exception 
	 * 			if the resource is null
	 */
	public void addInventoryResource(Resource resource) throws InvalidResourceException {
		if (resource == null){
			throw new InvalidResourceException("Please supply a valid resource");
		}
		if (!getInventoryResources().contains(resource)){
			inventoryMap.put(resource, 0);
		}
	}
	
	/**
	 * <p>
	 * Removes a resource from the inventory (and its associated quantity)
	 * </p>
	 * 
	 * @throws Exception 
	 * 			if the resource is null or is not in inventoryMap
	 */
	public void removeInventoryResource(Resource resource) throws InvalidResourceException {
		if (resource == null || !getInventoryResources().contains(resource)){
			throw new InvalidResourceException ("Please supply a valid resource");
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
	 * @throws Exception
	 *             if the resource is not in this.getInventoryResources()
	 */
	public int getQuantity(Resource resource) throws InvalidResourceException {
		if (!this.getInventoryResources().contains(resource)) {
			throw new InvalidResourceException("Please supply a valid resource");
		}
		return inventoryMap.get(resource);
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
	 * @throws Exception
	 *             if resource is not already in this.getInventoryResources()
	 * @throws Exception
	 *             if the addition of amount and the current quantity for that
	 *             resource is negative (i.e. less than zero).
	 */
	public void updateQuantity(Resource resource, int amount) throws Exception {
		if (!this.getInventoryResources().contains(resource)) {
			throw new InvalidResourceException("Please supply a valid resource");
		}

		int currentAmount = getQuantity(resource);
		// check that the resource amount would not become negative.
		if (currentAmount + amount < 0) {
			throw new InvalidResourceException("Sorry, not enough " + resource.toString());
		}

		inventoryMap.put(resource, currentAmount + amount);
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
	 * @throws Exception
	 *             if extraItems is null
	 */
	public void updateInventory(Inventory extraItems) throws Exception {
		if (extraItems == null){
			throw new InvalidInventoryException("Cannot add null to Inventory");
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
