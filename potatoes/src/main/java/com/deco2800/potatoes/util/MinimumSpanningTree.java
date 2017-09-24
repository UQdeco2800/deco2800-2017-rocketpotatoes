package com.deco2800.potatoes.util;

import java.util.*;
import com.deco2800.potatoes.util.MinimumSpanningTree.Vertex;

public class MinimumSpanningTree {

    //------------------ Nested Vertex Class --------------------

    public static class Vertex {

        private Box3D entry;
        private int address;
        private int leastEdgeAddress;
        private float leastEdge;

        public Vertex(Box3D entry, int address) {
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
            return (this.getAddress() == other.getAddress());
        }


        public Box3D getEntry() {
            return entry;
        }

        public void setEntry(Box3D entry) {
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
         * Compares its two arguments for order.  Returns a negative integer,
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
            if (o1.getLeastEdge() == o2.getLeastEdge()) {
                return (o1.getAddress() < o2.getAddress()) ? -1 : 1;
            }
            return (o1.getLeastEdge() < o2.getLeastEdge()) ? -1 : 1;
        }
    }

    //------------------ end of nested classes ---------------------

    // Value to inflate edge weights.
    private static final float LARGE_WEIGHT = 1000f;
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


    public void addVertex(Box3D entry, int address) throws IndexOutOfBoundsException {

        // Check address is valid.
        if (address < 0 || address > this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        this.getVertexList().add(new Vertex(entry, address));
    }

    public void insertVertex(Box3D entry, int address) throws IndexOutOfBoundsException {

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
                if (cloud.containsKey(j)) {
                   if (temp > getGraphEntry(i, j)) {
                       temp = getGraphEntry(i, j);
                       address = j;
                   }
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
     * @param obstacles List of Lines as boarder of static entities on the map.
     */
    public void addStartGoal(Box3D goal, Box3D start, ArrayList<Line> obstacles) {

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
                if (checkLineClash(edge, obstacles)) {
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
     * @param obstacles ArrayList of {@code Line} objects that represent the boarders of static collidable
     */
    public void initGraphWeightMatrix(ArrayList<Line> obstacles) {

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
                if (this.checkLineClash(edge, obstacles)) {
                    // Obstruction found
                    this.putGraphEntry(LARGE_WEIGHT, i, j);
                    continue;
                }
                // No obstructions
                this.putGraphEntry(edge.getDistance(), i, j);
            }
        }
    }

    /**
     * Takes a {@code Line} object and tests it against a list of Lines to check in any intersect.
     * @param edge Line object tested.
     * @param obstacles Line objects in list
     * @return true in edge intersects with any lines in obstacles; false otherwise.
     */
    public boolean checkLineClash(Line edge, ArrayList<Line> obstacles) {

        // Iterate through obstacles and check if
        // edge between vertices is obstructed.
        for (Line line: obstacles) {
            if(edge.doIntersect(line)) {
                // Edge is obstructed.
                return true;
            }
        }
        // No obstruction.
        return false;
    }


    public void putGraphEntry(float entry, int row, int col) throws IndexOutOfBoundsException {

        if (row < 0 || row > this.getSize() || col < 0 || col > this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        this.graph[row][col] = entry;
    }

    public float getGraphEntry(int row, int col) throws IndexOutOfBoundsException {

        if (row < 0 || row > this.getSize() || col < 0 || col > this.getSize()) {
            throw new IndexOutOfBoundsException();
        }
        return this.graph[row][col];
    }

    public HashMap<Box3D, Box3D> createTree(Box3D goal, Box3D start, ArrayList<Line> obstacles) {

        HashMap<Box3D, Box3D> tree = new HashMap<>();
        Vertex temp;
        cloud.clear();
        this.addStartGoal(goal, start, obstacles);
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
