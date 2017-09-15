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

    //------------------ end of nested class ---------------------

    private float [][] graph;
    private int size;
    private List<Vertex> vertexList;
    private Map<Integer, Vertex> cloud;
    private PriorityQueue<Vertex> outside;
    private Map<Box3D, Box3D> tree;
    private VertexPriority priority;

    public MinimumSpanningTree(int size) {

        this.size = size;
        this.graph = new float[size][size];
        this.vertexList = new ArrayList<>();
        this.priority = new VertexPriority();
        this.outside = new PriorityQueue<>(size, priority);
        this.cloud = new HashMap<>();

    }


    public void addVertex(Box3D entry, int address) {
        vertexList.add(new Vertex(entry, address));
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public List<Vertex> getVertexList() {
        return vertexList;
    }


    private void updateLeastEdges() {

        float temp;
        for (int i = 0; i < this.getSize(); i++) {
            temp = this.getGraphEntry(i, i);
            for (int j = 0; j < this.getSize(); j++) {
                if (cloud.containsKey(j)) {
                   if (temp > getGraphEntry(i, j)) temp = getGraphEntry(i, j);
                }
            }
            this.vertexList.get(i).setLeastEdge(temp);
        }
    }

    private void initOutside() {

        for (int i = 0; i < this.getSize(); i++) {
            outside.add(vertexList.get(i));
        }
    }

    public Vertex findClosest(Box3D target) {
        // Set up initial variables to check distance
        // to vertexList[0].
        Line line = new Line(
                target.getX(),
                target.getY(),
                vertexList.get(0).getEntry().getX(),
                vertexList.get(0).getEntry().getY()
        );
        float distance = line.getDistance();
        Vertex closest = vertexList.get(0);
        // Iterate through vertexList from position 1 and replace
        // closest Vertex if applicable.
        for (int i = 1; i < vertexList.size(); i++) {
           line = new Line(
                   target.getX(),
                   target.getY(),
                   vertexList.get(i).getEntry().getX(),
                   vertexList.get(i).getEntry().getY()
           );
           // Set new closest if line->distance is < distance.
           if (line.getDistance() < distance) {
               distance = line.getDistance();
               closest = vertexList.get(i);
           }
        }
        return closest;
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

    public HashMap<Box3D, Box3D> createTree(Vertex start) {

        HashMap<Box3D, Box3D> tree = new HashMap<>();
        Vertex temp;
        // Add the starting vertex to the cloud to begin MST.
        cloud.put(start.getAddress(), start);
        updateLeastEdges();
        // Remove starting vertex from outside as it's the first
        // entry added to the cloud.
        initOutside();
        outside.remove(start);

        while (!outside.isEmpty()) {
            temp = outside.poll();
            cloud.put(temp.getAddress(), temp);
            tree.put(temp.getEntry(), this.getVertexList().get(temp.getLeastEdgeAddress()).getEntry());
            updateLeastEdges();
        }
        return tree;
    }


}
