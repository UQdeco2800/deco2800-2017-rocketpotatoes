package com.deco2800.potatoes;


import com.deco2800.potatoes.collisions.*;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.deco2800.potatoes.util.MathUtil.compareFloat;


public class Shape2DTest {



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
    public void negativeLengthsSwapped() {
        Box2D box1 = new Box2D(5, 5, -3, -3);
        assertTrue(box1.getXLength() == 3 && box1.getYLength() == 3 );
        box1.setYLength(-3);
        box1.setXLength(-3);
        assertTrue(box1.getXLength() == 3 && box1.getYLength() == 3 );

        Circle2D circ1 = new Circle2D(5, 5, -3);
        assertTrue(circ1.getRadius() == 3);
        circ1.setRadius(-3);
        assertTrue(circ1.getRadius() == 3);
    }

    //TODO area

    //TODO line equals


    //colision
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
        assertFalse(point2.overlaps(circ1)); //on edge
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

    @Test
    public void collisionPointToBox() {
        Point2D point1 = new Point2D(5f, 5f);
        Point2D point2 = new Point2D(5f, 9f);
        Box2D box1 = new Box2D(5, 8, 2, 2);
        Box2D box2 = new Box2D(5, 9, 2, 2);
        Box2D box3 = new Box2D(3, 3, 1, 1);
        Box2D box4 = new Box2D(3, 7, 1, 1);
        Box2D box5 = new Box2D(7, 3, 1, 1);
        Box2D box6 = new Box2D(7, 7, 1, 1);

        //point1
        assertFalse(point1.overlaps(box1)); //in line on one dimension
        assertFalse(box1.overlaps(point1));
        assertFalse(point1.overlaps(box2));
        assertFalse(point1.overlaps(box3)); //diagonals
        assertFalse(point1.overlaps(box4));
        assertFalse(point1.overlaps(box5));
        assertFalse(point1.overlaps(box6));

        //point2
        assertFalse(point2.overlaps(box1)); // on edge
        assertTrue(point2.overlaps(box2)); // overlaps
        assertFalse(point2.overlaps(box3));
        assertFalse(point2.overlaps(box4));
    }

    @Test
    public void collisionCircleToBox() {
        Circle2D circ1 = new Circle2D(5, 5, 1.5f);
        Circle2D circ2 = new Circle2D(5, 9, 1.5f);
        Circle2D circ3 = new Circle2D(5, 10.5f, 1.5f);
        Box2D box1 = new Box2D(5, 8, 2, 2);
        Box2D box2 = new Box2D(5, 9, 2, 2);
        Box2D box3 = new Box2D(3, 3, 1, 1);
        Box2D box4 = new Box2D(3, 7, 1, 1);
        Box2D box5 = new Box2D(7, 3, 1, 1);
        Box2D box6 = new Box2D(7, 7, 1, 1);

        //circ1
        assertFalse(circ1.overlaps(box1)); //in line on one dimension
        assertFalse(box1.overlaps(circ1));
        assertFalse(circ1.overlaps(box2));
        assertFalse(circ1.overlaps(box3)); //diagonals
        assertFalse(circ1.overlaps(box4));
        assertFalse(circ1.overlaps(box5));
        assertFalse(circ1.overlaps(box6));

        //circ2
        assertFalse(circ2.overlaps(box3));
        assertFalse(circ2.overlaps(box4));

        //circ3
        assertFalse(circ3.overlaps(box1)); // on edge
        assertTrue(circ3.overlaps(box2)); // overlaps
    }

    @Test
    public void collisionBoxToBox() {
    Box2D box1 = new Box2D(5, 5, 2, 2);
    assertTrue(box1.overlaps(box1)); //self overlap

    Box2D box2 = new Box2D(5, 6, 2, 2);
    assertTrue(box1.overlaps(box2)); //in-line overlap

    Box2D box3 = new Box2D(6, 6, 2, 2);
    assertTrue(box1.overlaps(box3)); //overlap diagonal

    Box2D box4 = new Box2D(5, 7, 2, 2);
    assertFalse(box1.overlaps(box4)); //edge-to-edge

    Box2D box5 = new Box2D(7, 7, 2, 2);
    assertFalse(box1.overlaps(box5)); //edge-to-edge diagonal

    Box2D box6 = new Box2D(8, 10, 4, 2);
    assertFalse(box1.overlaps(box6)); //distant diagonal
    }

    //distance
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

    @Test
    public void distanceCircleToBox() {
        Circle2D circ1 = new Circle2D(5, 5, 1.5f);
        Circle2D circ2 = new Circle2D(5, 9, 1.5f);
        Circle2D circ3 = new Circle2D(5, 10.5f, 1.5f);
        Box2D box1 = new Box2D(5, 8, 2, 2);
        Box2D box2 = new Box2D(5, 9, 2, 2);
        Box2D box3 = new Box2D(3, 3, 1, 1);
        Box2D box4 = new Box2D(3, 7, 1, 1);
        Box2D box5 = new Box2D(7, 3, 1, 1);
        Box2D box6 = new Box2D(7, 7, 1, 1);

        //circ1
        assertTrue(compareFloat(circ1.distance(box1), 2f - 1.5f)); //in line on one dimension
        assertTrue(compareFloat(box1.distance(circ1), 2f - 1.5f));
        assertTrue(compareFloat(circ1.distance(box2), 3f - 1.5f));
        assertTrue(compareFloat(circ1.distance(box3), (float) Math.sqrt(4.5) - 1.5f)); //diagonals
        assertTrue(compareFloat(circ1.distance(box4), (float) Math.sqrt(4.5) - 1.5f));
        assertTrue(compareFloat(circ1.distance(box5), (float) Math.sqrt(4.5) - 1.5f));
        assertTrue(compareFloat(circ1.distance(box6), (float) Math.sqrt(4.5) - 1.5f));

        //circ2
        assertTrue(compareFloat(circ2.distance(box3), (float) Math.sqrt(32.5) - 1.5f));
        assertTrue(compareFloat(circ2.distance(box4), (float) Math.sqrt(4.5) - 1.5f));

        //circ3
        assertTrue(circ3.distance(box1) == 0); // on edge
        assertTrue(circ3.distance(box2) < 0); // overlaps
    }

    @Test
    public void distanceBoxToBox() {
        Box2D box1 = new Box2D(5, 5, 2, 2);
        assertTrue(box1.distance(box1) < 0); //self overlap

        Box2D box2 = new Box2D(5, 6, 2, 2);
        assertTrue(box1.distance(box2) < 0); //in-line overlap

        Box2D box3 = new Box2D(6, 6, 2, 2);
        assertTrue(box1.distance(box3) < 0); //overlap diagonal

        Box2D box4 = new Box2D(5, 7, 2, 2);
        assertTrue(box1.distance(box4) == 0); //edge-to-edge

        Box2D box5 = new Box2D(7, 7, 2, 2);
        assertTrue(compareFloat(box1.distance(box5), 0)); //edge-to-edge diagonal

        Box2D box6 = new Box2D(9, 10, 4, 2);
        assertTrue(compareFloat(box1.distance(box6), (float) Math.sqrt(10))); //distant diagonal
    }

    //Line checking
    @Test
    public void collisionLineToBox() {

        Box2D box1 = new Box2D(5, 5, 2, 2);
        assertFalse(new Line2D(2, 7, 8, 7).overlaps(box1));         //Horizontal above
        assertTrue(new Line2D(2, 4.5f, 8, 4.5f).overlaps(box1));    //Horizontal through
        assertFalse(new Line2D(7, 2, 7, 8).overlaps(box1));         //Vertical above
        assertTrue(new Line2D(4.5f, 2, 4.5f, 8).overlaps(box1));    //Vertical through

        //quadrants labelled 1 through 9, starting top left, natural reading order
        assertFalse(new Line2D(2, 7, 6.5f, 6.5f).overlaps(box1));   // 1 to 2 desc
        assertFalse(new Line2D(6.5f, 6.5f, 8, 7).overlaps(box1));   // 2 to 3 rise
        assertFalse(new Line2D(6.5f, 9, 9, 6.5f).overlaps(box1));   // 2 to 6
        assertFalse(new Line2D(6.5f, 20, 20, 3.5f).overlaps(box1)); // 2 to 9
        assertFalse(new Line2D(2, 5, 3, 5).overlaps(box1));         // 4 to 4 flat
        assertFalse(new Line2D(3, 3, 6, 3).overlaps(box1));         // 8 to 8 flat
        assertFalse(new Line2D(0, 0, 1, 1).overlaps(box1));      // 7 to 7 rise
        assertFalse(new Line2D(5, 2, 5.5f, 1.5f).overlaps(box1));   // 7 to 7 desc
        assertFalse(new Line2D(4.5f, 2, 5.5f, 2).overlaps(box1));   // 6 to 7 flat
        assertTrue(new Line2D(5, 5, 5.5f, 6.5f).overlaps(box1));    // 5 to 6 rise
        assertTrue(new Line2D(5, 5, 8, 8).overlaps(box1));          // 5 to 3 through corner

        //touching
        assertTrue(new Line2D(3, 4, 6, 4).overlaps(box1));         // bottom flat
        assertTrue(new Line2D(4, 4, 6, 3.5f).overlaps(box1));      // bottom left corner first node
    }

    @Test
    public void distanceLineToPoint() {
        Point2D point1 = new Point2D(5,5);

        assertTrue(compareFloat(new Line2D(2, 7, 8, 7).distance(point1), 2));    //Horizontal above
        assertTrue(compareFloat(new Line2D(2, 5, 8, 5).distance(point1), 0));    //Horizontal through
        assertTrue(compareFloat(new Line2D(7, 2, 7, 8).distance(point1), 2));    //Vertical above
        assertTrue(compareFloat(new Line2D(5, 2, 5, 8).distance(point1), 0));    //Vertical through
        System.out.println("br: " + new Line2D(0, 10, 10, 0).distance(point1));
        assertTrue(compareFloat(new Line2D(0, 10, 10, 0).distance(point1), 0));    //Diagonal through
        assertTrue(compareFloat(new Line2D(1, 10, 11, 0).distance(point1), (float) Math.sqrt(2) / 2));    //diagonal above

    }

    @Test
    public void distanceLineToCircle() {
        Circle2D circ1 = new Circle2D(5,5,0.5f);


        //System.out.println("Breaky dist: " + new Line2D(2, 7, 8, 7).distance(circ1));
        assertTrue(compareFloat(new Line2D(2, 7, 8, 7).distance(circ1), 1.5f));     //Horizontal above
        assertTrue(new Line2D(2, 5, 8, 5) .distance(circ1)< 0);                     //Horizontal through
        assertTrue(compareFloat(new Line2D(7, 2, 7, 8).distance(circ1), 1.5f));     //Vertical above
        assertTrue(new Line2D(5, 2, 5, 8) .distance(circ1)< 0);                     //Vertical through
        assertTrue(new Line2D(0, 10, 10, 0) .distance(circ1)< 0);                   //Diagonal through
        assertTrue(compareFloat(new Line2D(5, 10, 15, 0).distance(circ1),           //diagonal above
                (float) Math.sqrt(2) * 2.5f - 0.5f));
    }

    @Test
    public void distanceLineToBox() {
        Box2D box1 = new Box2D(5, 5, 2, 2);
        assertTrue(compareFloat(new Line2D(2, 7, 8, 7).distance(box1), 1));         //Horizontal above
        assertTrue(new Line2D(2, 4.5f, 8, 4.5f).distance(box1) < 0);    //Horizontal through
        assertTrue(compareFloat(new Line2D(7, 2, 7, 8).distance(box1), 1));         //Vertical above
        assertTrue(new Line2D(4.5f, 2, 4.5f, 8).distance(box1) < 0);    //Vertical through

        //quadrants labelled 1 through 9, starting top left, natural reading order
        assertTrue(compareFloat(new Line2D(2, 7, 5f, 6.5f).distance(box1), 0.5f));                      // 1 to 2 desc
        assertTrue(compareFloat(new Line2D(5f, 6.5f, 8, 7).distance(box1), 0.5f));                      // 2 to 3 rise
        assertTrue(compareFloat(new Line2D(5f, 9, 9, 6.5f).distance(box1),
                   new Line2D(5f, 9, 9, 6.5f).distance(new Point2D(6, 6))));                            // 2 to 6
        assertTrue(compareFloat(new Line2D(6.5f, 20, 20, 3.5f).distance(box1),
                   new Line2D(6.5f, 20, 20, 3.5f).distance(new Point2D(6, 6))));                        // 2 to 9
        assertTrue(compareFloat(new Line2D(2, 5, 3, 5).distance(box1), 1));                             // 4 to 4 flat
        assertTrue(compareFloat(new Line2D(3, 3, 6, 3).distance(box1), 1));                             // 8 to 8 flat
        assertTrue(compareFloat(new Line2D(0, 0, 1, 1).distance(box1), (float) Math.sqrt(2) * 3));      // 7 to 7 rise
        assertTrue(compareFloat(new Line2D(0, 1, 1, 0).distance(box1), (float) Math.sqrt(2) * 3.5f));   // 7 to 7 desc
        assertTrue(compareFloat(new Line2D(1, 2, 5.5f, 2).distance(box1), 2));                          // 7 to 8 flat
        assertTrue(new Line2D(5, 5, 5.5f, 6.5f).distance(box1) < 0);                                    // 5 to 6 rise
        assertTrue(new Line2D(5, 5, 8, 8).distance(box1) < 0);                                          // 5 to 3 through corner

        //touching covered by collisionLineToBox

    }



}
