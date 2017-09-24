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


    @Test
    public void collisionPointToCircle(){
        Point2D point1 = new Point2D(5f, 5f);
        Point2D point2 = new Point2D(5f, 9f);
        float diagOffset = (float) Math.sqrt(8) - 0.1f;
        Point2D point3 = new Point2D(5f + diagOffset, 5f + diagOffset);
        Circle2D circ1 = new Circle2D(5, 5, 4);
        Circle2D circ2 = new Circle2D(5, 5, 3.8f);

        assertTrue(point1.overlaps(circ1)); //overlaps
        assertTrue(circ1.overlaps(point1)); //overlaps reciprocity
        assertTrue(point2.overlaps(circ1)); //on edge
        assertTrue(point3.overlaps(circ1)); //close to edge diagonal
        assertFalse(point2.overlaps(circ2)); //non collision
        assertFalse(point3.overlaps(circ2)); //non collision diagonal
    }

    @Test
    public void collisionCircleToCircle() {
        Circle2D circ1 = new Circle2D(5, 5, 1);
        Circle2D circ2 = new Circle2D(10, 5, 3);
        Circle2D circ3 = new Circle2D(7, 7, 1);
        Circle2D circ4 = new Circle2D(6.5f, 5, 1);
        Circle2D circ5 = new Circle2D(7, 7, 2);

        assertFalse(circ1.overlaps(circ2)); // non collision linear
        assertFalse(circ1.overlaps(circ3)); // non collision diagonal
        assertTrue(circ1.overlaps(circ4)); // overlaps linear
        assertTrue(circ1.overlaps(circ5)); // overlaps diagonal
    }
/*
    @Test
    public void collisionPointToBox() {
        assertTrue(true); //TODO
        //overlaps
        //on edge
        //non collision
    }

    @Test
    public void collisionCircleToBox() {
        assertTrue(true); //TODO
        //overlaps
        //on edge
        //non collision
    }

    @Test
    public void collisionBoxToBox() {
        assertTrue(true); //TODO
        //overlaps
        //on edge
        //non collision
    }

    @Test
    public void collisionLineToBox() {
        assertTrue(true); //TODO
        //overlaps
        //on edge
        //non collision
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
        // self colisions
        assertTrue(point1.overlaps(point1));
        assertTrue(point3.overlaps(point3));

        //linear
        assertTrue(point1.distance(point2) == 3.5);
        assertTrue(point3.distance(point1) == 7);

        //angled
        assertTrue(compareFloat(point1.distance(point4), (float) Math.sqrt(50)));
        assertTrue(compareFloat(point3.distance(point4), (float) Math.sqrt(29)));

    }

    @Test
    public void distancePointToCircle() {
        Point2D point1 = new Point2D(5f, 5f);
        Point2D point2 = new Point2D(5f, 9f);
        float diagOffset = (float) Math.sqrt(8);
        Point2D point3 = new Point2D(5f + diagOffset, 5f + diagOffset);
        Circle2D circ1 = new Circle2D(5, 5, 4);
        Circle2D circ2 = new Circle2D(5, 5, 3.8f);

        assertTrue(point1.distance(circ1) < 0); //overlaps
        assertTrue(circ1.distance(point1) < 0); //overlaps reciprocity
        assertTrue(point2.distance(circ1) == 0); //on edge
        assertTrue(compareFloat(point3.distance(circ1), 0)); //on edge diagonal
        assertTrue(compareFloat(point2.distance(circ2), 0.2f)); //non collision
        assertTrue(compareFloat(point3.distance(circ2), 0.2f)); //non collision diagonal

    }

    @Test
    public void distanceCircleToCircle() {
        Circle2D circ1 = new Circle2D(5, 5, 1);
        Circle2D circ2 = new Circle2D(10, 5, 3);
        Circle2D circ3 = new Circle2D(7, 7, 1);
        Circle2D circ4 = new Circle2D(6.5f, 5, 1);
        Circle2D circ5 = new Circle2D(7, 7, 2);

        assertTrue(compareFloat(circ1.distance(circ2),1)); //non collision linear
        assertTrue(compareFloat(circ1.distance(circ3), (float) Math.sqrt(8) - 2)); //non collision diagonal
        assertTrue(circ1.distance(circ4) < 0);//overlaps linear
        assertTrue(circ1.distance(circ5) < 0);//overlaps diagonal
    }

    @Test
    public void distancePointToBox() {
        Point2D point1 = new Point2D(5f, 5f);
        Point2D point2 = new Point2D(5f, 9f);
        Box2D box1 = new Box2D(5, 8, 2, 2);
        Box2D box2 = new Box2D(5, 9, 2, 2);
        Box2D box3 = new Box2D(3, 3, 1, 1);
        Box2D box4 = new Box2D(3, 7, 1, 1);
        Box2D box5 = new Box2D(7, 3, 1, 1);
        Box2D box6 = new Box2D(7, 7, 1, 1);

        //point1
        assertTrue(compareFloat(point1.distance(box1), 2f)); //in line on one dimension
        assertTrue(compareFloat(box1.distance(point1), 2f));
        assertTrue(compareFloat(point1.distance(box2), 3f));
        assertTrue(compareFloat(point1.distance(box3), (float) Math.sqrt(4.5))); //diagonals
        assertTrue(compareFloat(point1.distance(box4), (float) Math.sqrt(4.5)));
        assertTrue(compareFloat(point1.distance(box5), (float) Math.sqrt(4.5)));
        assertTrue(compareFloat(point1.distance(box6), (float) Math.sqrt(4.5)));

        //point2
        assertTrue(point2.distance(box1) == 0); // on edge
        assertTrue(point2.distance(box2) < 0); // overlaps
        assertTrue(compareFloat(point2.distance(box3), (float) Math.sqrt(32.5)));
        assertTrue(compareFloat(point2.distance(box4), (float) Math.sqrt(4.5)));
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
