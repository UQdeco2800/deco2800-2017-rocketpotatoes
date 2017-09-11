package com.deco2800.potatoes.entities;

public class Damage implements Comparable<Resource> {


    /*
     * A string representation of the damage tree
     */

    protected String damageTreeType;
    /*
     * The damage tree image texture
     */
    protected String texture;

    /**
     * The default instantiation of a damage tree. This should be overrode by children
     * classes but can be used for testing purposes.
     */
    public Damage() {
        damageTreeType = "default";
        texture = "default";
    }

    /**
     * Returns the name of the damage tree.
     *
     * @return resourceType
     * 				The string representation of the resource.
     */
    public String getTypeName() {
        return damageTreeType;
    }

    /**
     * Returns the texture of the damage tree.
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
        return other.getTypeName().equals(damageTreeType);
    }

    @Override
    public int hashCode() {
        // We create a polynomial hash-code based on the object string and type.
        final int prime = 31; // an odd base prime
        int result = 1; // the hash code under construction
        result = prime * result + damageTreeType.hashCode();
        return result;
    }

    @Override
    public int compareTo(Resource other) {
        return damageTreeType.compareTo(other.getTypeName());
    }

    /**
     * Returns the string representation of the damage tree.
     *
     * @return string The string representation of the damage tree.
     */
    @Override
    public String toString() {
        return damageTreeType;
    }

}
