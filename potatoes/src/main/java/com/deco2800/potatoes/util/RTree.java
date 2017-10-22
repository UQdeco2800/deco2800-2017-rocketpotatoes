package com.deco2800.potatoes.util;

import com.deco2800.potatoes.collisions.Box2D;
import com.deco2800.potatoes.collisions.Shape2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

/**
 * Class for storing sets of 2D points, and doing spatial queries on them.
 *
 * Acts as a mapping from instances of generic type Key to Shape2D instances. All instances of Key
 * are considered to be globally unique within the RTree. As well as performing standard lookup
 * queries (backed by a hashmap) from Key instances to Shape2Ds, this structure allows for reverse
 * lookups from Shape2D instances to Key instances (or sets of Key instances, depending on the
 * lookup type).
 *
 * WARNINGS: this class assumes that the shapes inserted into it have a degree of sanity.
 * It is assumed that any subset of the positions in the RTree with size greater than or equal to
 * Block.BLOCK_SIZE * Block.MIN_RATIO will be capable of having a Box2D drawn surrounding them with
 * non-zero side lengths.
 */
public class RTree<Key> {
    // Lookup table from keys to shapes.
    private Map<Key, Shape2D> forwardLookup;
    // Root node of the RTree itself.
    private Block<Key> root;

    /**
     * Creates an empty RTree.
     */
    public RTree() {
        forwardLookup = new HashMap<>();
        root = Block.root();
    }

    /**
     * Inserts a new key/position pair into the RTree.
     *
     * @param k
     *          The key of the key/position pair being inserted.
     * @param position
     *          The position associated with they key.
     */
    public void insert(Key k, Shape2D position) {
        forwardLookup.put(k, position);
        if (root.insert(new Bucket(k, position))) {
            // split the root
            List<Block> newRoot = new ArrayList<>();
            newRoot.add(root.split());
            newRoot.add(root);
            root = Block.branch(newRoot);
        }
    }

    /**
     * Moves a key within the RTree to a new position.
     *
     * @param k
     *          The key being moved.
     * @param newPosition
     *          The new position to be associated with the key.
     */
    public void move(Key k, Shape2D newPosition) {
        remove(k);
        insert(k, newPosition);
    }

    /**
     * Removes a key/position pair from the RTree.
     *
     * @param k
     *          The key of the key/position pair being removed.
     */
    public void remove(Key k) {
        Shape2D position = forwardLookup.remove(k);
        if (position != null) {
            root.remove(new Bucket<>(k, position));
            if (root.isBranch() && root.getChildren().size() == 1) {
                root = (Block<Key>) root.getChildren().get(0);
            }
        }
    }

    /**
     * Finds the position associated with a key in the RTree.
     *
     * @param k
     *          The key being queried.
     * @return
     *          The position associated with that key within the RTree.
     */
    public Shape2D find(Key k) {
        return forwardLookup.get(k);
    }

    /**
     * Finds the nearest key to a given position (by the Shape2D distance method) where the additionalCheck returns true. Ties are broken
     * arbitrarily.
     *
     * @param position
     *          The position keys are searched near.
     * @param additionalCheck
     *          The additional requirement for the result
     * @return
     *          The key that is closest to the position.
     */
    public Key findClosest(Shape2D position, Predicate<Key> additionalCheck) {
        return root.findClosest(position, additionalCheck);
    }

    /**
     * Finds the nearest key to a given position (by the Shape2D distance method). Ties are broken
     * arbitrarily.
     *
     * @param position
     *          The position keys are searched near.
     * @return
     *          The key that is closest to the position.
     */
    public Key findClosest(Shape2D position) {
        return root.findClosest(position, (x) -> true);
    }

    /**
     * Finds the keys within the RTree that overlap the given position (by the Shape2D overlaps
     * method).
     *
     * @param position
     *          The position that overlaps are checked with.
     * @return
     *          The keys that are within this shape.
     */
    public Collection<Key> findOverlapping(Shape2D position) {
        Collection<Key> output = new ArrayList<>();
        root.addAllOverlapping(position, output);
        return output;
    }

    /**
     * Internal block of storage within the RTree.
     * This represents a node within the tree, which either stores a list of child blocks, or a
     * list of child buckets. 
     *
     */
    private static class Block<Key> {
        // Number of nodes within a block.
        private static final int BLOCK_SIZE = 20;
        private static final float MIN_RATIO = 0.4f;

        // Child nodes of the block. If isLeaf is true, then leafChildren must be an actual
        // array and the value of children is undefined. If is false, then children must be an
        // actual array, and the value of leafChildren is undefined.
        private final boolean isLeaf;
        private List<Block<Key>> children;
        private List<Bucket<Key>> leafChildren;

        // A Box2D guaranteed to contain all children of this block.
        private Box2D minimumBoundingRectangle;

        /**
         * Partial constructor, for internal use only.
         *
         * @param isLeaf
         *          Is this block a leaf node or not?
         */
        private Block(boolean isLeaf) {
            this.isLeaf = isLeaf;
        }

        /**
         * Creates a new root block.
         */
        public static Block root() {
            Block output = new Block(true);
            output.leafChildren = new ArrayList<>();
            output.minimumBoundingRectangle = null;
            return output;
        }

        /**
         * Create a new leaf block.
         *
         * @param children
         *          The children of the block.
         */
        public static Block leaf(List<Bucket> children) {
            Block output = new Block(true);
            output.leafChildren = children;
            output.minimumBoundingRectangle = Box2D.surrounding(children.stream().map(child -> child.position)).get();
            return output;
        }

        /**
         * Create a new branch (internal, non-leaf) block.
         *
         * @param children
         *          The children of the block.
         */
        public static Block branch(List<Block> children) {
            Block output = new Block(false);
            output.children = children;
            output.minimumBoundingRectangle = Box2D.surrounding(
                    children.stream().map(child -> child.minimumBoundingRectangle)).get();
            return output;
        }

        /**
         * Getter, returns whether this node is not a leaf.
         *
         * @return
         *          True for branches, false for leaves.
         */
        public boolean isBranch() {
            return !isLeaf;
        }

        /**
         * Getter for children. Does not return a copy, and should only be called on the root node.
         */
        public List getChildren() {
            return isLeaf ? leafChildren : children;
        }

        /**
         * Inserts a bucket into one of the child buckets.
         *
         * @param b
         *          The bucket being inserted.
         * @require
         *          This must not be a leaf node.
         */
        private void insertIntoChildBucket(Bucket b) {
            int selection = -1;
            float cheapest = Float.MAX_VALUE;
            for (int i = 0; i < children.size(); ++i) {
                Box2D newBounds = Box2D.surrounding(Stream.of(
                            children.get(i).minimumBoundingRectangle, b.getPosition())).get();
                float cost = newBounds.getXLength() + newBounds.getYLength();
                if (cost < cheapest) {
                    selection = i;
                    cheapest = cost;
                }
            }
            if (children.get(selection).insert(b)) {
                children.add(children.get(selection).split());
            }

        }

        /**
         * If this block has overflown during a resize operation, this method will remove ~half of
         * the children from this block, and return a new block containing those children.
         *
         * @require
         *          This block is not a leaf node, and it has overflown.
         * @return
         *          The new block containing ~half of this block's children.
         */
        private Block splitBranch() { 
            boolean isResultHorizontal = true;
            float cheapest = Float.MAX_VALUE;
            int splitIndex = -1;

            children.sort(Comparator.comparingDouble(
                        child -> child.minimumBoundingRectangle.getX()));
            for (int i = (int) ceil(MIN_RATIO * BLOCK_SIZE);
                    i < floor((1 - MIN_RATIO) * BLOCK_SIZE); ++i) {
                Optional<Box2D> maybeFirst = Box2D.surrounding(children.stream()
                        .limit(i)
                        .map(j -> j.minimumBoundingRectangle));
                Optional<Box2D> maybeSecond = Box2D.surrounding(children.stream()
                        .skip(i)
                        .map(j -> j.minimumBoundingRectangle));

                if (!maybeFirst.isPresent() || !maybeSecond.isPresent()) {
                    continue;
                }

                Box2D first = maybeFirst.get(), second = maybeSecond.get();

                float cost = first.getXLength() + first.getYLength() + second.getXLength()
                    + second.getYLength();

                if (cost < cheapest) {
                    splitIndex = i;
                }
            }

            children.sort(Comparator.comparingDouble(
                        child -> child.minimumBoundingRectangle.getY()));
            for (int i = (int) ceil(MIN_RATIO * BLOCK_SIZE);
                    i < floor((1 - MIN_RATIO) * BLOCK_SIZE); ++i) {
                Optional<Box2D> maybeFirst = Box2D.surrounding(children.stream()
                        .limit(i)
                        .map(j -> j.minimumBoundingRectangle));
                Optional<Box2D> maybeSecond = Box2D.surrounding(children.stream()
                        .skip(i)
                        .map(j -> j.minimumBoundingRectangle));

                if (!maybeFirst.isPresent() || !maybeSecond.isPresent()) {
                    continue;
                }

                Box2D first = maybeFirst.get(), second = maybeSecond.get();

                float cost = first.getXLength() + first.getYLength() + second.getXLength()
                    + second.getYLength();

                if (cost < cheapest) {
                    isResultHorizontal = false;
                    splitIndex = i;
                }
            }

            if (isResultHorizontal) {
                children.sort(Comparator.comparingDouble(
                            child -> child.minimumBoundingRectangle.getX()));
            }

            List<Block> newChildren = children.stream().skip(splitIndex).collect(Collectors.toList());
            children = children.stream().limit(splitIndex).collect(Collectors.toList());

            minimumBoundingRectangle = 
                Box2D.surrounding(children.stream().map(j -> j.minimumBoundingRectangle)).get();

            return Block.branch(newChildren);
        }

        /**
         * If this block has overflown during a resize operation, this method will remove ~half of
         * the children from this block, and return a new block containing those children.
         *
         * @require
         *          This block is a leaf node, and it has overflown.
         * @return
         *          The new block containing ~half of this block's children.
         */
        private Block splitLeaf() { 
            boolean isResultHorizontal = true;
            float cheapest = Float.MAX_VALUE;
            int splitIndex = -1;

            leafChildren.sort(Comparator.comparingDouble(
                        child -> child.getPosition().getX()));
            for (int i = (int) ceil(MIN_RATIO * BLOCK_SIZE); 
                    i < floor((1 - MIN_RATIO) * BLOCK_SIZE); ++i) {
                Optional<Box2D> maybeFirst = Box2D.surrounding(leafChildren.stream()
                        .limit(i)
                        .map(j -> j.getPosition()));
                Optional<Box2D> maybeSecond = Box2D.surrounding(leafChildren.stream()
                        .skip(i)
                        .map(j -> j.getPosition()));

                if (!maybeFirst.isPresent() || !maybeSecond.isPresent()) {
                    continue;
                }

                Box2D first = maybeFirst.get(), second = maybeSecond.get();

                float cost = first.getXLength() + first.getYLength() + second.getXLength()
                    + second.getYLength();

                if (cost < cheapest) {
                    splitIndex = i;
                }
            }

            leafChildren.sort(Comparator.comparingDouble(
                        child -> child.getPosition().getY()));
            for (int i = (int) ceil(MIN_RATIO * BLOCK_SIZE);
                    i < floor((1 - MIN_RATIO) * BLOCK_SIZE); ++i) {
                Optional<Box2D> maybeFirst = Box2D.surrounding(leafChildren.stream()
                        .limit(i)
                        .map(j -> j.getPosition()));
                Optional<Box2D> maybeSecond = Box2D.surrounding(leafChildren.stream()
                        .skip(i)
                        .map(j -> j.getPosition()));

                if (!maybeFirst.isPresent() || !maybeSecond.isPresent()) {
                    continue;
                }

                Box2D first = maybeFirst.get(), second = maybeSecond.get();

                float cost = first.getXLength() + first.getYLength() + second.getXLength()
                    + second.getYLength();

                if (cost < cheapest) {
                    isResultHorizontal = false;
                    splitIndex = i;
                }
            }

            if (isResultHorizontal) {
                leafChildren.sort(Comparator.comparingDouble(
                            child -> child.getPosition().getX()));
            }

            List<Bucket> newChildren =
                leafChildren.stream().skip(splitIndex).collect(Collectors.toList());
            leafChildren = leafChildren.stream().limit(splitIndex).collect(Collectors.toList());

            minimumBoundingRectangle = 
                Box2D.surrounding(leafChildren.stream().map(j -> j.getPosition())).get();

            return Block.leaf(newChildren);
        }

        /**
         * If this block has overflown during a resize operation, this method will remove ~half of
         * the children from this block, and return a new block containing these children. If this
         * node is a leaf node, the node returned will also be a leaf node, and vice versa.
         */
        public Block split() {
            if (isLeaf) {
                return splitLeaf();
            } else {
                return splitBranch();
            }
        }

        /**
         * Inserts a new bucket into this block.
         *
         * @param b
         *          The bucket being inserted.
         * @return
         *          True if the node has overflown and needs to be resized.
         */
        public boolean insert(Bucket b) {
            if (isLeaf) {
                leafChildren.add(b);
                if (leafChildren.size() <= BLOCK_SIZE) { // do not nead to overflow
                    minimumBoundingRectangle = Box2D.surrounding(
                            Stream.of(minimumBoundingRectangle, b.getPosition())).orElse(null);
                    return false;
                } else { // need to overflow, so why bother dealing with the other stuff?
                    return true;
                }
            } else {
                insertIntoChildBucket(b);
                minimumBoundingRectangle = Box2D.surrounding(children.stream()
                        .map(child -> child.minimumBoundingRectangle)).get();
                return children.size() > BLOCK_SIZE;
            }
        }

        /**
         * Joins two buckets together.
         * @param other
         *              The bucket being joined.
         * @require
         *              Either this and other are both leafs, or they are both branches.
         */
        private void join(Block other) {
            if (isLeaf) {
                leafChildren.addAll(other.leafChildren);
                minimumBoundingRectangle = Box2D.surrounding(
                        leafChildren.stream().map(child -> child.getPosition())).get();
            } else {
                children.addAll(other.children);
                minimumBoundingRectangle = Box2D.surrounding(
                        children.stream().map(child -> child.minimumBoundingRectangle)).get();
            }
        }
        
        /**
         * Removes a bucket from this block.
         *
         * @param b
         *          The bucket being removed.
         * @return
         *          True if the node has underflowed and needs to be resized.
         * @require
         *          The bucket b is inside the current minimumBoundingRectangle.
         */
        public boolean remove(Bucket b) {
            if (isLeaf) {
                if (leafChildren.contains(b)) {
                    leafChildren.remove(b);
                }
                minimumBoundingRectangle = Box2D.surrounding(
                        leafChildren.stream().map(child -> child.getPosition())).get();
                return leafChildren.size() < MIN_RATIO * BLOCK_SIZE;

            } else {
                Block toBeMaybeJoined = null;
                for (Block child: children) {
                    if (child.minimumBoundingRectangle.overlaps(b.getPosition())
                            && child.remove(b)) {
                        // child has overflown
                        toBeMaybeJoined = child;
                        break;
                    }
                }

                final Block toBeJoined = toBeMaybeJoined;


                // handle joining
                if (toBeJoined != null) {
                    Block joinWith = children.stream().filter(child -> !child.equals(toBeJoined))
                        .min(Comparator.comparingDouble(child -> {
                            Box2D newBounds = Box2D.surrounding(Stream.of(child, toBeJoined)
                                    .map(block -> block.minimumBoundingRectangle)).get();
                            return newBounds.getXLength() + newBounds.getYLength();
                        })).get();

                    children.remove(joinWith);
                    toBeJoined.join(joinWith);
                }

                if (children.size() < MIN_RATIO * BLOCK_SIZE) {
                    return true;
                } else {
                    minimumBoundingRectangle = Box2D.surrounding(
                            children.stream().map(child -> child.minimumBoundingRectangle)).get();
                    return false;
                }
            }
        }

        /**
         * Finds all the keys within this subset of the RTree that overlap the given position, and
         * add them to an existing collection.
         *
         * @param position
         *              The position that overlaps are checked with.
         * @param output
         *              The collection that outputs are appended to.
         */
        public void addAllOverlapping(Shape2D position, Collection<Key> output) {
            if (isLeaf) {
                leafChildren.stream()
                    .filter(child -> child.getPosition().overlaps(position))
                    .map(child -> child.getKey())
                    .forEach(key -> output.add(key));
            } else {
                children.stream()
                    .filter(child -> child.minimumBoundingRectangle.overlaps(position))
                    .forEach(child -> child.addAllOverlapping(position, output));
            }
        }

        /**
         * Finds the key that is closest to a given position. It is assumed that this method is
         * only called on the root node.
         *
         * @param position
         *              The position whos nearest neighbour is being found.
         * @return
         *              The key of the nearest neighbour. Null if the RTree is empty.
         */
        public Key findClosest(Shape2D position, Predicate<Key> additionalCheck) {
            if (isLeaf && leafChildren.size() == 0) {
                return null;
            }

            Key output = null;
            float currentDistance = Float.MAX_VALUE;
            PriorityQueue<Block> queue = new PriorityQueue<>(Comparator.comparingDouble(
                        child -> child.minimumBoundingRectangle.distance(position)));
            if (isLeaf) {
                queue.add(this);
            } else {
                queue.addAll(children);
            }
            while (!queue.isEmpty() && queue.peek().minimumBoundingRectangle.distance(position) <
                    currentDistance) {
                Block<Key> head = queue.poll();

                if (head.isLeaf) {
                    for (Bucket<Key> bucket: head.leafChildren) {
                        if (bucket.getPosition().distance(position) < currentDistance && additionalCheck.test(bucket.getKey())) {
                            currentDistance = bucket.getPosition().distance(position);
                            output = bucket.getKey();
                        }
                    }
                } else {
                    queue.addAll(head.children);
                }
            }

            return output;
        }
    }

    /**
     * Class to store a single key/value pair.
     */
    private static class Bucket<Key> {
        private Key k;
        private Shape2D position;

        public Bucket(Key k, Shape2D position) {
            this.k = k;
            this.position = position;
        }

        public Key getKey() {
            return k;
        }

        public Shape2D getPosition() {
            return position;
        }

        public void setPosition(Shape2D position) {
            this.position = position;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof Bucket)) {
                return false;
            }

            Bucket<Key> otherBucket = (Bucket<Key>) other;
            // all equality cares about is the key, different position doesn't matter
            return otherBucket.getKey().equals(k);
        }

        @Override
        public int hashCode() {
            return k.hashCode();
        }
    }
}
