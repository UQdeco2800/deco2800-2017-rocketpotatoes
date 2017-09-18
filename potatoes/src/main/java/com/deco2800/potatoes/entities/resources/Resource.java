package com.deco2800.potatoes.entities.resources;

/**
 * Resource is the type of item that users can have. Different resources with
 * different functions inherit from this class. Resources can be stored in a
 * player inventory or on the map as a ResourceEntity where its type is a
 * Resource. The primary two resources are FoodResource and SeedResource.
 *
 *@author Dion, Jordan
 */
public class Resource implements Comparable<Resource> {

	/*
	 * A string representation of the resource
	 */
	protected String resourceType;
	/*
	 * The resource image texture
	 */
	protected String texture;

	/**
	 * The default instantiation of a resource. This should be overrode by children
	 * classes but can be used for testing purposes.
	 */
	public Resource() {
		resourceType = "default";
		texture = "default";
	}

	/**
	 * Returns the name of the resource.
	 * 
	 * @return resourceType
	 * 				The string representation of the resource.
	 */
	public String getTypeName() {
		return resourceType;
	}

	/**
	 * Returns the texture of the resource.
	 * 
	 * @return texture
	 * 				The texture location.
	 */
	public String getTexture() {
		return texture;
	}


	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Resource)) {
			return false;
		}
		Resource other = (Resource) object; // the corridor to compare
		return other.getTypeName().equals(resourceType);
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
		return resourceType.compareTo(other.getTypeName());
	}
	
	/**
	 * Returns the string representation of the resource.
	 * 
	 * @return string The string representation of the resource.
	 */
	@Override
	public String toString() {
		return resourceType;
	}

}
