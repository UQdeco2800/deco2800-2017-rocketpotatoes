package com.deco2800.potatoes.entities.trees;

public class DamageTreeType implements Comparable<DamageTreeType> {


    /*
     * A string representation of the damage tree
     */

    protected String damageTreeType;
    /*
     * The resource image texture
     */
    protected String texture;

    /**
     * The default instantiation of a resource. This should be overrode by children
     *
     */
    public DamageTreeType() {
        damageTreeType = "default";
        texture = "default";
    }

    /**
     * Returns the name of the damage tree.
     *
     * @return damageTreeType
     * 				The string representation of the damage tree.
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
        return this.texture;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DamageTreeType)) {
            return false;
        }
        DamageTreeType other = (DamageTreeType) object; // the corridor to compare
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
    public int compareTo(DamageTreeType other) {
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
