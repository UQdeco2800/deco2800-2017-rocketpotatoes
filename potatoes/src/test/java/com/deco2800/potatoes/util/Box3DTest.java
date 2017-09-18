package com.deco2800.potatoes.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Box3DTest {


    @Test
    public void testEquals() throws Exception {

        Box3D box1 = new Box3D(1, 2, 3, 0, 0, 0);
        Box3D box2 = new Box3D(1, 2, 3, 0, 0, 0);

        assertThat("Hashcodes don't equal",box1.hashCode() == box2.hashCode(), is(equalTo(true)));
        assertThat("box1 and box2 don't equal",box1.equals(box2), is(equalTo(true)));

    }

}