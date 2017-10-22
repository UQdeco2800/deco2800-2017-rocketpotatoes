package com.deco2800.potatoes.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


import org.junit.Test;

public class LineTest {

    /**
     * Test the orientation of a line.
     */
    @Test
    public void orientation() {

        Line lineOne = new Line(1, 2, 10, 7);
        Line lineTwo = new Line(6, 4, 0, 6);
        assertThat("Orientation showing clockwise not counter-clockwise",
                Line.orientation(lineOne.getEndPointOne(), lineOne.getEndPointTwo(), lineTwo.getEndPointTwo()),
                is(equalTo(2)));
        assertThat("Orientation showing counter-clockwise not clockwise",
                Line.orientation(lineTwo.getEndPointOne(), lineTwo.getEndPointTwo(), lineOne.getEndPointTwo()),
                is(equalTo(1)));
    }


    @Test
    public void doIntersect() {

        Line lineOne = new Line(1, 2, 10, 7);
        Line lineTwo = new Line(6, 4, 0, 6);
        Line lineThree = new Line(0, 0, 2, 0);
        Line lineFour = new Line(0, 0, 5, 0);
        Line lineFive = new Line(0, 2, 5, 2);
        Line lineSix = new Line(3, 0, 6, 0);
        assertThat("Line 2 does intersect line 1", lineOne.doIntersect(lineTwo), is(equalTo(true)));
        assertThat("Line 1 does intersect line 2", lineTwo.doIntersect(lineOne), is(equalTo(true)));
        assertThat("Line 3 does not intersect line 2", lineThree.doIntersect(lineTwo), is(equalTo(false)));
        assertThat("Line 4 does intersect line 3", lineThree.doIntersect(lineFour), is(equalTo(true)));
        assertThat("Line 3 does intersect line 4", lineFour.doIntersect(lineThree), is(equalTo(true)));
        assertThat("Line 4 does not intersect line 5", lineFour.doIntersect(lineFive), is(equalTo(false)));
        assertThat("Line 3 does not intersect line 2", lineThree.doIntersect(lineSix), is(equalTo(false)));
    }

    @Test
    public void equalTest() {
        Line lineOne = new Line(1, 1, 2, 2);
        Line lineTwo = new Line(1, 1, 2, 2);
        assertThat("Hashcode's don't equal", lineOne.hashCode() == lineTwo.hashCode(), is(equalTo(true)));
        assertThat("Line 1 doesn't equal line 2", lineTwo.equals(lineOne), is(equalTo(true)));
        lineTwo.equals(lineTwo);
        lineTwo.getEndPointOne().setX(3);
        lineTwo.getEndPointOne().setY(2);
    }

}