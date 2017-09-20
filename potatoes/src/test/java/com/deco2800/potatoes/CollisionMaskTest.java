package com.deco2800.potatoes;


import com.deco2800.potatoes.collisions.Point2D;
import com.deco2800.potatoes.collisions.Circle2D;
import com.deco2800.potatoes.collisions.Box2D;

import org.junit.Test;

import static org.junit.Assert.*;


public class CollisionMaskTest {

    private boolean compareFloat(float a, float b) {
        float delta = 0.00001f;
        return Math.abs(a-b) < delta;

    }

    @Test
    public void duplicatesEqual() {
        Point2D point1 = new Point2D(5.7f, 8.3f);
        Point2D point2 = new Point2D(5.7f, 8.3f);
        assertEquals(point1, point2);

        Box2D box1 = new Box2D(3.7f, 9.6f, 2.8f, 8.5f);
        Box2D box2 = new Box2D(3.7f, 9.6f, 2.8f, 8.5f);
        assertEquals(box1, box2);

        Circle2D circ1 = new Circle2D(3.7f, 9.6f, 2.8f);
        Circle2D circ2 = new Circle2D(3.7f, 9.6f, 2.8f);
        assertEquals(circ1, circ2);
    }

    @Test
    public void nearValuesUnequal() {
        Point2D point1 = new Point2D(5.7f, 8.3f);
        Point2D point2 = new Point2D(8.3f, 5.7f);
        assertNotEquals ("Point2D is falsely recognising near values as equal", point1, point2);

        point1 = new Point2D(5.7f, 8.3f);
        point2 = new Point2D(5.70001f, 8.3f);
        assertNotEquals ("Point2D is falsely recognising near values as equal", point1, point2);

        Box2D box1 = new Box2D(3.7f, 3.80001f, 2.8f, 8.5f);
        Box2D box2 = new Box2D(3.7f, 3.8f, 2.8f, 8.5f);
        assertNotEquals("Box2D is falsely recognising near values as equal", box1, box2);

        Circle2D circ1 = new Circle2D(3.7f, 9.6f, 2.8f);
        Circle2D circ2 = new Circle2D(3.7f, 9.6f, 2.80001f);
        assertNotEquals("Circle2D is falsely recognising near values as equal", circ1, circ2);
    }

    @Test
    public void collisionPointToPoint() {
        Point2D point1 = new Point2D(5.7f, 8.3f);
        Point2D point2 = new Point2D(5.7f, 8.3f);
        assertTrue("point1 does not think it overlaps itself", point1.overlaps(point1));
        assertTrue("point1 does not think it overlaps point2", point1.overlaps(point2));
        assertTrue("point2 does not think it overlaps point1", point2.overlaps(point1));

        //non collision
        point1 = new Point2D(5.7f, 8.3001f);
        point2 = new Point2D(5.7f, 8.3f);
        assertFalse("point1 thinks it overlaps point2", point1.overlaps(point2));
        assertFalse("point2 thinks it overlaps point1", point2.overlaps(point1));
    }

/*
    @Test
    public void collisionPointToCircle(){
        assertTrue(true); //TODO
    }

    @Test
    public void collisionCircleToCircle() {
        assertTrue(true); //TODO
    }

    @Test
    public void collisionPointToBox() {
        assertTrue(true); //TODO
    }

    @Test
    public void collisionCircleToBox() {
        assertTrue(true); //TODO
    }

    @Test
    public void collisionBoxToBox() {
        assertTrue(true); //TODO
    }

    @Test
    public void collisionLineToBox() {
        assertTrue(true); //TODO
    }
*/
    @Test
    public void distancePointToPoint() {
        Point2D point1 = new Point2D(0f, 0f);
        Point2D point2 = new Point2D(3.5f, 0f);
        Point2D point3 = new Point2D(0f, 7f);
        Point2D point4 = new Point2D(5f, 5f);

        //reflexive
        assertTrue(point1.distance(point2) == point2.distance(point1));
        assertTrue(point3.distance(point4) == point4.distance(point3));

        //self distance = 0
        assertTrue(point3.distance(point3) == 0);
        assertTrue(point1.distance(point1) == 0);

        //linear
        assertTrue(point1.distance(point2) == 3.5);
        assertTrue(point3.distance(point1) == 7);

        //angled
        assertTrue(compareFloat(point1.distance(point4), (float) Math.sqrt(50)));
        assertTrue(compareFloat(point3.distance(point4), (float) Math.sqrt(29)));

    }
/*
    @Test
    public void distancePointToCircle() {
        assertTrue(true); //TODO
    }

    @Test
    public void distanceCircleToCircle() {
        assertTrue(true); //TODO
    }
*/
    @Test
    public void distancePointToBox() {
        Point2D point1 = new Point2D(5f, 5f);
        Point2D point2 = new Point2D(5f, 10f);
        Box2D box1 = new Box2D(5, 8, 2, 2);
        Box2D box2 = new Box2D(5, 10, 2, 2);
        Box2D box3 = new Box2D(3, 3, 1, 1);
        Box2D box4 = new Box2D(3, 7, 1, 1);
        Box2D box5 = new Box2D(7, 3, 1, 1);
        Box2D box6 = new Box2D(7, 7, 1, 1);

        assertTrue(compareFloat(point1.distance(box1), 1f)); //TODO returning -1 instead of 1
        //TODO: this test is still in progress
    }
/*
    @Test
    public void distanceCircleToBox() {
        assertTrue(true); //TODO
    }

    @Test
    public void distanceBoxToBox() {
        assertTrue(true); //TODO
    }

    @Test
    public void distanceLineToPoint() {
        assertTrue(true); //TODO
    }

    @Test
    public void distanceLineToCircle() {
        assertTrue(true); //TODO
    }

    @Test
    public void distanceLineToBox() {
        assertTrue(true); //TODO
    }
*/
}
