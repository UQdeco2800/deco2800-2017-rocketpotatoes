package com.deco2800.potatoes.inventory;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
	
	private Map<AbstractInventoryItem, Integer> inventory;
	
	public Inventory(){
		inventory = new HashMap<AbstractInventoryItem, Integer>();
	}

}
