/*
 * Copyright (C) 2021 Scott Shaffer - All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tipplerow.jam.bravais;

import java.util.List;

import com.tipplerow.jam.collect.JamLists;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class PeriodTest {
    @Test public void testContains() {
        assertFalse(Period.contains(-1, 5));
        assertFalse(Period.contains( 5, 5));

        assertTrue(Period.contains(0, 5));
        assertTrue(Period.contains(1, 5));
        assertTrue(Period.contains(2, 5));
        assertTrue(Period.contains(3, 5));
        assertTrue(Period.contains(4, 5));

        Period p23 = Period.box(2, 3);

        assertFalse(p23.contains(UnitIndex.at(-1,  0)));
        assertFalse(p23.contains(UnitIndex.at(-1,  1)));
        assertFalse(p23.contains(UnitIndex.at( 0, -1)));
        assertFalse(p23.contains(UnitIndex.at( 1, -1)));
        assertFalse(p23.contains(UnitIndex.at( 0,  3)));
        assertFalse(p23.contains(UnitIndex.at( 1,  3)));
        assertFalse(p23.contains(UnitIndex.at( 2,  0)));
        assertFalse(p23.contains(UnitIndex.at( 2,  1)));

        assertTrue(p23.contains(UnitIndex.at(0, 0)));
        assertTrue(p23.contains(UnitIndex.at(0, 1)));
        assertTrue(p23.contains(UnitIndex.at(0, 2)));
        assertTrue(p23.contains(UnitIndex.at(1, 0)));
        assertTrue(p23.contains(UnitIndex.at(1, 1)));
        assertTrue(p23.contains(UnitIndex.at(1, 2)));
    }

    @Test public void testCountSites() {
        assertEquals(  10, Period.box(10).countSites());
        assertEquals( 200, Period.box(10, 20).countSites());
        assertEquals(6000, Period.box(10, 20, 30).countSites());
    }

    @Test public void testEnumerate1() {
        Period box = Period.box(3);
        List<UnitIndex> images = box.enumerate();

        assertEquals(3, images.size());
        assertTrue(JamLists.isSorted(images));

        assertUnitIndex(images.get(0), 0);
        assertUnitIndex(images.get(1), 1);
        assertUnitIndex(images.get(2), 2);

        for (UnitIndex image : images)
            assertTrue(box.contains(image));
    }

    private void assertUnitIndex(UnitIndex actual, int... expected) {
        assertEquals(expected.length, actual.dimensionality());

        for (int dim = 0; dim < expected.length; ++dim)
            assertEquals(expected[dim], actual.coord(dim));
    }

    @Test public void testEnumerate2() {
        Period box = Period.box(2, 3);
        List<UnitIndex> images = box.enumerate();

        assertEquals(6, images.size());
        assertTrue(JamLists.isSorted(images));

        assertUnitIndex(images.get(0), 0, 0);
        assertUnitIndex(images.get(1), 1, 0);
        assertUnitIndex(images.get(2), 0, 1);
        assertUnitIndex(images.get(3), 1, 1);
        assertUnitIndex(images.get(4), 0, 2);
        assertUnitIndex(images.get(5), 1, 2);

        for (UnitIndex image : images)
            assertTrue(box.contains(image));
    }

    @Test public void testEnumerate3() {
        Period box = Period.box(2, 3, 4);
        List<UnitIndex> images = box.enumerate();

        assertEquals(24, images.size());
        assertTrue(JamLists.isSorted(images));

        assertUnitIndex(images.get(0), 0, 0, 0);
        assertUnitIndex(images.get(1), 1, 0, 0);
        assertUnitIndex(images.get(2), 0, 1, 0);
        assertUnitIndex(images.get(3), 1, 1, 0);
        assertUnitIndex(images.get(4), 0, 2, 0);
        assertUnitIndex(images.get(5), 1, 2, 0);

        assertUnitIndex(images.get(6),  0, 0, 1);
        assertUnitIndex(images.get(7),  1, 0, 1);
        assertUnitIndex(images.get(8),  0, 1, 1);
        assertUnitIndex(images.get(9),  1, 1, 1);
        assertUnitIndex(images.get(10), 0, 2, 1);
        assertUnitIndex(images.get(11), 1, 2, 1);

        assertUnitIndex(images.get(12), 0, 0, 2);
        assertUnitIndex(images.get(13), 1, 0, 2);
        assertUnitIndex(images.get(14), 0, 1, 2);
        assertUnitIndex(images.get(15), 1, 1, 2);
        assertUnitIndex(images.get(16), 0, 2, 2);
        assertUnitIndex(images.get(17), 1, 2, 2);

        assertUnitIndex(images.get(18), 0, 0, 3);
        assertUnitIndex(images.get(19), 1, 0, 3);
        assertUnitIndex(images.get(20), 0, 1, 3);
        assertUnitIndex(images.get(21), 1, 1, 3);
        assertUnitIndex(images.get(22), 0, 2, 3);
        assertUnitIndex(images.get(23), 1, 2, 3);

        for (UnitIndex image : images)
            assertTrue(box.contains(image));
    }

    @Test public void testImageOf() {
        assertEquals(9, Period.imageOf(-21, 10));
        assertEquals(0, Period.imageOf(-20, 10));
        assertEquals(1, Period.imageOf(-19, 10));

        assertEquals(9, Period.imageOf(-11, 10));
        assertEquals(0, Period.imageOf(-10, 10));
        assertEquals(1, Period.imageOf( -9, 10));

        assertEquals(8, Period.imageOf(-2, 10));
        assertEquals(9, Period.imageOf(-1, 10));
        assertEquals(0, Period.imageOf( 0, 10));
        assertEquals(1, Period.imageOf( 1, 10));
        assertEquals(2, Period.imageOf( 2, 10));

        assertEquals(9, Period.imageOf( 9, 10));
        assertEquals(0, Period.imageOf(10, 10));
        assertEquals(1, Period.imageOf(11, 10));

        assertEquals(9, Period.imageOf(19, 10));
        assertEquals(0, Period.imageOf(20, 10));
        assertEquals(1, Period.imageOf(21, 10));
    }
}
