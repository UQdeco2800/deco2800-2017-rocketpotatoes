package com.deco2800.potatoes.util;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Shape2D;

import java.util.Optional;

/**
 * Class for storing sets of 2D points, and doing spatial queries on them.
 *
 * Acts as a mapping from instances of generic type Key to Shape2D instances. All instances of Key
 * are considered to be globally unique within the RTree. As well as performing standard lookup
 * queries (backed by a hashmap) from Key instances to Shape2Ds, this structure allows for reverse
 * lookups from Shape2D instances to Key instances (or sets of Key instances, depending on the 
 * lookup type).
 */
public class RTree<Key> {

    /**
     * Inserts a new key/position pair into the RTree.
     * Worst case run-time: O(log n) where n is the number of pairs in the tree.
     *
     * @param k
     *          The key of the key/position pair being inserted.
     * @param position
     *          The position associated with they key.
     */
    public void insert(Key k, Shape2D position) {
    }

    /**
     * Moves a key within the RTree to a new position.
     * Worst case run-time: O(log n) where n is the number of pairs in the tree.
     *
     * @param k
     *          The key being moved.
     * @param newPosition
     *          The new position to be associated with the key.
     */
    public void move(Key k, Shape2D newPosition) {
    }

    /**
     * Removes a key/position pair from the RTree.
     * Worst case run-time: O(log n) where n is the number of pairs in the tree.
     *
     * @param k
     *          The key of the key/position pair being removed.
     */
    public void remove(Key k) {
    }

    /**
     * Finds the position associated with a key in the RTree.
     * Worst case run-time: O(1).
     *
     * @param k
     *          The key being queried.
     * @return
     *          The position associated with that key within the RTree.
     */
    public Shape2D find(Key k) {
    }

    /**
     * Finds the nearest key to a given position (by the Shape2D distance method). Ties are broken 
     * arbitrarily.
     * Worst case run-time: O(log n) where n is the number of pairs in the tree.
     *
     * @param position
     *          The position keys are searched near.
     * @return
     *          The key that is closest to the position.
     */
    public Key findClosest(Shape2D position) {
    }
}
