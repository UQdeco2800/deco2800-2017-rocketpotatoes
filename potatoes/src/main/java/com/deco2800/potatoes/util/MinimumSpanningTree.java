package com.deco2800.potatoes.util;

import java.util.*;
import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.entities.AbstractEntity;
import com.deco2800.potatoes.managers.GameManager;
import static com.deco2800.potatoes.util.MathUtil.compareFloat;


/**
 * Object to manage methods for creating a minimum spanning tree.
 */



public class MinimumSpanningTree {
    /**
     * MinimumSpanningTree takes {@code Point2D} as vertices to a weighted graph in the form of an adjacency matrix.
     * It uses the Prim–Dijkstra algorithm to return a MST in the form of an {@code HashMap} of {@code Point2D} leading
     * back to goal {@code Point2D}.
     */

    //------------------ Nested Vertex Class --------------------

    /**
     * Vertex is a container class for {@code Point2D}.
     */
    public static class Vertex {

        private Point2D entry;
        private int address;
        private int leastEdgeAddress;
        private float leastEdge;

        public Vertex(Point2D entry, int address) {
            this.entry = entry;
            this.address = address;
        }

        /**
         * Indicates whether some other object is "equal to" this one.
         * The {@code equals} method implements an equivalence relation
         * on non-null object references:
         *
         * @param obj the reference object with which to compare.
         * @return {@code true} if this object is the same as the obj
         * argument; {@code false} otherwise.
         */
        @Override
        public boolean equals(Object obj) {

            if(!(obj instanceof Vertex)) {
                return false;
            }
            // Cast to Vertex
            Vertex other = (Vertex) obj;
            // Check address
            return this.getAddress() == other.getAddress();
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + (entry != null ? entry.hashCode() : 0);
            result = 31 * result + address;
            result = 31 * result + leastEdgeAddress;
            result = 31 * result + (leastEdge <= -0.001f||leastEdge >= +0.001f ? Float.floatToIntBits(leastEdge) : 0);
            return result;
        }

        public Point2D getEntry() {
            return entry;
        }

        public void setEntry(Point2D entry) {
            this.entry = entry;
        }

        public int getAddress() {
            return address;
        }

        public float getLeastEdge() {
            return leastEdge;
        }

        public void setLeastEdge(float leastEdge) {
            this.leastEdge = leastEdge;
        }

        public int getLeastEdgeAddress() {
            return leastEdgeAddress;
        }

        public void setLeastEdgeAddress(int leastEdgeAddress) {
            this.leastEdgeAddress = leastEdgeAddress;
        }
    }

    public class VertexPriority implements Comparator<Vertex> {

        /**
         * Compares {@code Vertex} for order.  Returns a negative integer,
         * zero, or a positive integer as the first argument is less than, equal
         * to, or greater than the second.
         *
         * @param o1 the first object to be compared.
         * @param o2 the second object to be compared.
         * @return a negative integer, zero, or a positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         */
        @Override
        public int compare(Vertex o1, Vertex o2) {

            if (o1.equals(o2)) {
                return 0;
            }
            if (compareFloat(o1.getLeastEdge(), o2.getLeastEdge())) {
                return o1.getAddress() < o2.getAddress() ? -1 : 1;
            }
            return o1.getLeastEdge() < o2.getLeastEdge() ? -1 : 1;
        }
    }

    //------------------ end of nested classes ---------------------

    // Value to inflate edge weights.
    private static final float LARGE_WEIGHT = 1f / 0f;
    // 2D matrix of edge weights
    private float [][] graph;
    // Number of vertices
    private int size;
    private List<Vertex> vertexList;
    private Map<Integer, Vertex> cloud;
    private PriorityQueue<Vertex> outside;

    public MinimumSpanningTree(int size) {

        this.size = size;
        this.graph = new float[size][size];
        this.vertexList = new ArrayList<>();
        this.outside = new PriorityQueue<>(size, new VertexPriority());
        this.cloud = new HashMap<>();

    }


    public void addVertex(Point2D entry, int address) {

        // Check address is valid.
        if (address < 0 || address > this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        this.getVertexList().add(new Vertex(entry, address));
    }

    public void insertVertex(Point2D entry, int address) {

        // Check address is valid.
        if (address < 0 || address > this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        this.getVertexList().get(address).setEntry(entry);
    }

    public int getSize() {
        return size;
    }


    public List<Vertex> getVertexList() {
        return vertexList;
    }


    private void updateLeastEdges() {

        float temp;
        int address;
        for (int i = 0; i < this.getSize(); i++) {
            temp = this.getGraphEntry(i, i);
            address = i;
            for (int j = 0; j < this.getSize(); j++) {
                if (cloud.containsKey(j) && temp > getGraphEntry(i, j)) {
                   temp = getGraphEntry(i, j);
                   address = j;
                }
            }
            this.vertexList.get(i).setLeastEdge(temp);
            this.vertexList.get(i).setLeastEdgeAddress(address);
        }
    }

    private void initOutside() {

        for (int i = 0; i < this.getSize(); i++) {
            outside.add(vertexList.get(i));
        }
    }

    /**
     * Add the start and goal position to the weighted graph matrix so a complete minimum spanning tree can be made.
     *
     * @param goal The target position of the MST.
     * @param start The position of entity calling the MST.
     */
    public void addStartGoal(Point2D goal, Point2D start) {

        // Line representing edge between vertices.
        Line edge;
        // Add start and goal vertices
        insertVertex(goal, 0);
        insertVertex(start, 1);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < this.getSize(); j++) {
                // Inflate weight for edges between vertices and themselves.
                if (i == j) {
                    this.putGraphEntry(LARGE_WEIGHT, i, j);
                    continue;
                }
                // Create edge line between vertices.
                edge = new Line(vertexList.get(i).getEntry(), vertexList.get(j).getEntry());
                // Check for obstructed edges
                if (checkLineClash(edge)) {
                    // Obstruction found
                    this.putGraphEntry(LARGE_WEIGHT, i, j);
                    // Reflect value
                    this.putGraphEntry(LARGE_WEIGHT, j, i);
                    continue;
                }
                // No obstructions
                this.putGraphEntry(edge.getDistance(), i, j);
                // Reflect value
                this.putGraphEntry(edge.getDistance(), j, i);
            }
        }
    }

    /**
     * Populate the weighted adjacency matrix with the distance between vertices. If a line between each vertex pair
     * is intersected by a line contained in list obstacles, then the edge weight is inflated to ensure that that edge
     * will not be included in the minimum spanning tree.
     *
     */
    public void initGraphWeightMatrix() {

        // Line representing edge between vertices.
        Line edge;
        // Iterate through edge weight matrix graph and check for clashes.
        // If no clashes occur, add edge weight as the distance between vertices.
        // Start at position 2 as position 0 and 1 are placeholders for the start
        // and goal locations that will be added later.
        for (int i = 2; i < this.getSize(); i++) {
            for (int j = 2; j < this.getSize(); j++) {
                // Matrix is symmetric so skip over bottom half and reflect
                // values latter.
                if (j < i) {
                    continue;
                }
                // Inflate weight for edges between vertices and themselves.
                if (i == j) {
                    this.putGraphEntry(LARGE_WEIGHT, i, j);
                    continue;
                }
                // Create edge line between vertices.
                edge = new Line(vertexList.get(i).getEntry(), vertexList.get(j).getEntry());
                // Check for obstructed edges
                if (checkLineClash(edge)) {
                    // Obstruction found
                    putGraphEntry(LARGE_WEIGHT, i, j);
                    continue;
                }
                // No obstructions
                putGraphEntry(edge.getDistance(), i, j);
            }
        }
    }

    /**
     * Takes a {@code Line} object and tests it against a list of Lines to check if any intersect.
     * @return true in edge intersects with any lines in obstacles; false otherwise.
     */
    public boolean checkLineClash(Line line) {
        boolean output = false;

        for (AbstractEntity e : GameManager.get().getWorld().getEntities().values()) {
            if (e.isStatic() && 0 > e.getMask().distance(line.getEndPointOne().getX(),
                        line.getEndPointOne().getY(), line.getEndPointTwo().getX(), line.getEndPointTwo().getY())) {
                output = true;
                break;
            }
        }

        return output;
    }

    /**
     * Place entry in adjacency matrix.
     * @param entry
     * @param row
     * @param col
     * @throws IndexOutOfBoundsException
     */
    public void putGraphEntry(float entry, int row, int col) {

        if (row < 0 || row > this.getSize() || col < 0 || col > this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        this.graph[row][col] = entry;
    }

    /**
     * 
     * @param row
     * @param col
     * @return
     * @throws IndexOutOfBoundsException
     */
    public float getGraphEntry(int row, int col) {

        if (row < 0 || row > this.getSize() || col < 0 || col > this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        return this.graph[row][col];
    }

    public HashMap<Point2D, Point2D> createTree(Point2D goal, Point2D start) {

        HashMap<Point2D, Point2D> tree = new HashMap<>();
        Vertex temp;
        this.addStartGoal(goal, start);
        cloud.clear();
        // Add the goal vertex (index 0 of vertexList) to the cloud to begin MST.
        cloud.put(0, this.getVertexList().get(0));
        updateLeastEdges();
        // Remove starting vertex from outside as it's the first
        // entry added to the cloud.
        initOutside();
        outside.remove(this.getVertexList().get(0));

        while (!outside.isEmpty()) {
            temp = outside.poll();
            cloud.put(temp.getAddress(), temp);
            tree.put(temp.getEntry(), this.getVertexList().get(temp.getLeastEdgeAddress()).getEntry());
            updateLeastEdges();
        }
        return tree;
    }


}
