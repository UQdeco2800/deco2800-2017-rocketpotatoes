package com.deco2800.potatoes.entities;

import java.util.Map;
import com.deco2800.potatoes.entities.AbstractEntity;

public class Resource implements Comparable<Resource> {
	
	protected String resourceType;
	protected String imageSource;
	
	public Resource() {
		resourceType = "ordinary";
		imageSource = "defaultImage.png";
	}
	
	/*public Resource(String name, float posX, float posY, float posZ, String texture) {
		super(posX, posY, posZ, 1f, 1f, 1f, 1f, 1f, texture);
		
		this.name = name;
		resourceType = "ordinary";
		imageSource = "defaultImage.png";
	}*/
	
	public String getType() {
		return resourceType;
	}
	
	public String getImageSource() {
		return imageSource;
	}
	
	/**
	 * <p>
	 * Returns the string representation of the resource.
	 * </p>
	 * 
	 * @return string
	 * 		The string representation of the resource.
	 */
	public String toString() {
		return resourceType;
	}
	
	@Override
    public boolean equals(Object object) {
        if (!(object instanceof Resource)) {
            return false;
        }
        Resource other = (Resource) object; // the corridor to compare
        return other.getType().equals(resourceType);
    }

    @Override
    public int hashCode() {
        // We create a polynomial hash-code based on the object string and type.
        final int prime = 31; // an odd base prime
        int result = 1; // the hash code under construction
        result = prime * result + resourceType.hashCode();
        return result;
    }
    
    
    @Override
    public int compareTo(Resource other) {
        return resourceType.compareTo(other.getType());
    }
    

}
