package com.deco2800.potatoes.util;

import com.deco2800.potatoes.collisions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RTreeTest {

    RTree<Integer> tree;

    @Before
    public void setUp() {
        tree = new RTree<>();
    }

    @After
    public void tearDown() {
        tree = null;
    }

    @Test
    public void createWithoutExploding() {
        tree = new RTree<>();
    }

    /**
     * Ensure that the add method works for simple cases.
     */
    @Test
    public void addToTree() {

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                tree.insert(10 * x + y, new Point2D(x, y));
            }
        }
    }

    /**
     * Ensure that the add method works, and the split methods also work. Also can be used as a
     * little bit of a sanity check for benchmarking purposes.
     */
    @Test
    public void addLotsToTree() {

        int count = 0;

        for (int x = -50; x < 50; ++x) {
            for (int y = -50; y < 50; ++y) {
                tree.insert(count++, new Circle2D(x, y, 0.5f));
            }
        }

        for (int x = 1000; x < 1050; ++x) {
            for (int y = -2000; y < -1950; ++y) {
                tree.insert(count++, new Box2D(x, y, 0.9f, 0.9f));
            }
        }

        for (int x = 25; x < 1025; x += 200) {
            for (int y = 0; y < 2000; y += 200) {
                tree.insert(count++, new Point2D(x, y));
            }
        }
    }

    @Test
    public void findOverlaps() {

        List<Shape2D> shapes = new ArrayList<>();
        Collection<Integer> results;

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                shapes.add(new Point2D(x, y));
            }
        }

        for (int i = 0; i < shapes.size(); ++i) {
            tree.insert(i, shapes.get(i));
        }

        // single point
        results = tree.findOverlapping(new Box2D(2, 2, 1, 1));
        assertTrue(results.size() == 1);
        assertTrue(results.contains(77));

        // no points
        results = tree.findOverlapping(new Box2D(6, 6, 1, 1));
        assertTrue(results.isEmpty());

        // all the points
        results = tree.findOverlapping(new Circle2D(0, 0, 8));
        assertTrue(results.size() == 100);

        // some of the points
        results = tree.findOverlapping(new Box2D(-4, -4, 2.1f, 2.1f));
        assertTrue(results.size() == 9);
        for (int x = -5; x <= -3; ++x) {
            for (int y = -5; y <= -3; ++y) {
                assertTrue(results.contains(10 * (x + 5) + (y + 5)));
            }
        }
    }

    @Test
    public void findOverlapsInBigTree() {

        List<Shape2D> shapes = new ArrayList<>();
        Collection<Integer> results;

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                shapes.add(new Point2D(x, y));
            }
        }

        int count = shapes.size();

        for (int x = -110; x < -10; ++x) {
            for (int y = -50; y < 50; ++y) {
                tree.insert(count++, new Circle2D(x, y, 0.5f));
            }
        }

        for (int x = 1000; x < 1050; ++x) {
            for (int y = -2000; y < -1950; ++y) {
                tree.insert(count++, new Box2D(x, y, 0.9f, 0.9f));
            }
        }

        for (int x = 25; x < 1025; x += 200) {
            for (int y = 0; y < 2000; y += 200) {
                tree.insert(count++, new Point2D(x, y));
            }
        }

        for (int i = 0; i < shapes.size(); ++i) {
            tree.insert(i, shapes.get(i));
        }

        // single point
        results = tree.findOverlapping(new Box2D(2, 2, 1, 1));
        assertTrue(results.size() == 1);
        assertTrue(results.contains(77));

        // no points
        results = tree.findOverlapping(new Box2D(6, 6, 1, 1));
        assertTrue(results.isEmpty());

        // all the points
        results = tree.findOverlapping(new Circle2D(0, 0, 8));
        assertTrue(results.size() == 100);

        // some of the points
        results = tree.findOverlapping(new Box2D(-4, -4, 2.1f, 2.1f));
        assertTrue(results.size() == 9);
        for (int x = -5; x <= -3; ++x) {
            for (int y = -5; y <= -3; ++y) {
                assertTrue(results.contains(10 * (x + 5) + (y + 5)));
            }
        }
    }

    @Test
    public void findNearest() {

        List<Shape2D> shapes = new ArrayList<>();

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                shapes.add(new Point2D(x, y));
            }
        }

        for (int i = 0; i < shapes.size(); ++i) {
            tree.insert(i, shapes.get(i));
        }

        for (int i = 0, x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                assertTrue(tree.findClosest(new Point2D(x + 0.1f, y + 0.1f)) == i++);
            }
        }
    }

    @Test
    public void findNearestInBigTree() {

        List<Shape2D> shapes = new ArrayList<>();
        Collection<Integer> results;

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                shapes.add(new Point2D(x, y));
            }
        }

        int count = shapes.size();

        for (int x = -110; x < -10; ++x) {
            for (int y = -50; y < 50; ++y) {
                tree.insert(count++, new Circle2D(x, y, 0.5f));
            }
        }

        for (int x = 1000; x < 1050; ++x) {
            for (int y = -2000; y < -1950; ++y) {
                tree.insert(count++, new Box2D(x, y, 0.9f, 0.9f));
            }
        }

        for (int x = 25; x < 1025; x += 200) {
            for (int y = 0; y < 2000; y += 200) {
                tree.insert(count++, new Point2D(x, y));
            }
        }

        for (int i = 0; i < shapes.size(); ++i) {
            tree.insert(i, shapes.get(i));
        }

        for (int i = 0, x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                assertTrue(tree.findClosest(new Point2D(x + 0.1f, y + 0.1f)) == i++);
            }
        }
    }

    // relies on findOverlapping and insert
    @Test
    public void removeFromTree() {


        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                tree.insert(10 * x + y, new Point2D(x, y));
            }
        }

        Shape2D filter = new Box2D(0, 0, 3, 3);
        for (Integer key: tree.findOverlapping(filter)) {
            tree.remove(key);
        }

        assertTrue(tree.findOverlapping(filter).size() == 0);
    }

    @Test
    public void removeFromBigTree() {

        List<Shape2D> shapes = new ArrayList<>();
        Collection<Integer> results;

        for (int x = -5; x < 5; ++x) {
            for (int y = -5; y < 5; ++y) {
                shapes.add(new Point2D(x, y));
            }
        }

        int count = shapes.size();

        for (int x = -110; x < -10; ++x) {
            for (int y = -50; y < 50; ++y) {
                tree.insert(count++, new Circle2D(x, y, 0.5f));
            }
        }

        for (int x = 1000; x < 1050; ++x) {
            for (int y = -2000; y < -1950; ++y) {
                tree.insert(count++, new Box2D(x, y, 0.9f, 0.9f));
            }
        }

        for (int x = 25; x < 1025; x += 200) {
            for (int y = 0; y < 2000; y += 200) {
                tree.insert(count++, new Point2D(x, y));
            }
        }

        for (int i = 0; i < shapes.size(); ++i) {
            tree.insert(i, shapes.get(i));
        }
        Shape2D filter = new Box2D(0, 0, 3, 3);
        for (Integer key: tree.findOverlapping(filter)) {
            tree.remove(key);
        }

        assertTrue(tree.findOverlapping(filter).size() == 0);
    }
}
