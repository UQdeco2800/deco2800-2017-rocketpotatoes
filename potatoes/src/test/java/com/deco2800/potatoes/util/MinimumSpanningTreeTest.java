package com.deco2800.potatoes.util;

import static org.hamcrest.CoreMatchers.is;

import com.deco2800.potatoes.collisions.Line;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertThat;

import com.deco2800.potatoes.collisions.Point2D;

public class MinimumSpanningTreeTest {

    float gameHeight = 100;
    float gameWidth = 100;
    int numberOfNodes = 20;
    float largeWeight = 100;
    ArrayList<Point2D> nodes;
    MinimumSpanningTree tree;

    @Before
    public void setUp() {

        nodes = new ArrayList<>();
        // Create nodes
        for (int i = 0; i < numberOfNodes; i++) {
            nodes.add(new Point2D(
                    (float) (Math.random() * gameWidth),      // x coordinate
                    (float) (Math.random() * gameHeight)     // y coordinate
            ));
        }
        // Create minimum spanning tree
        tree = new MinimumSpanningTree(numberOfNodes);
        // Add vertices to graph
        for (int i = 0; i < numberOfNodes; i++) {
            tree.addVertex(nodes.get(i), i);
        }
        // Add weights to graph
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (i == j) {
                    tree.putGraphEntry(largeWeight, i, j);
                    continue;
                }
                Line line = new Line(
                        nodes.get(i).getX(),
                        nodes.get(i).getY(),
                        nodes.get(j).getX(),
                        nodes.get(j).getY()
                );
                tree.putGraphEntry(line.getDistance(), i, j);

            }
        }

    }

    @Test
    public void leakTest() {

        int rounds = 100;

        for (int i = 0; i < rounds; i++) {

        }
    }

}
