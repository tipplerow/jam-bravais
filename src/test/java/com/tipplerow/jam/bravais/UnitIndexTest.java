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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class UnitIndexTest {
    @Test public void testAtCoord() {
        assertCoord(UnitIndex.at(3), 3);
        assertCoord(UnitIndex.at(3, 5), 3, 5);
        assertCoord(UnitIndex.at(3, 5, 2), 3, 5, 2);

        assertCoord(UnitIndex.at(new int[] { 3 }), 3);
        assertCoord(UnitIndex.at(new int[] { 3, 5 }), 3, 5);
        assertCoord(UnitIndex.at(new int[] { 3, 5, 2 }), 3, 5, 2);
    }

    private void assertCoord(UnitIndex index, int... coords) {
        assertEquals(coords.length, index.dimensionality());

        for (int dim = 0; dim < coords.length; ++dim)
            assertEquals(coords[dim], index.coord(dim));
    }

    @Test public void testCompareTo() {
        UnitIndex i10 = UnitIndex.at(10);
        UnitIndex i20 = UnitIndex.at(20);
        UnitIndex i30 = UnitIndex.at(30);
        UnitIndex i40 = UnitIndex.at(40);
        UnitIndex i50 = UnitIndex.at(50);

        UnitIndex ij52 = UnitIndex.at(5, 2);
        UnitIndex ij43 = UnitIndex.at(4, 3);
        UnitIndex ij53 = UnitIndex.at(5, 3);
        UnitIndex ij63 = UnitIndex.at(6, 3);
        UnitIndex ij54 = UnitIndex.at(5, 4);

        UnitIndex ijk110 = UnitIndex.at(1, 1, 0);
        UnitIndex ijk101 = UnitIndex.at(1, 0, 1);
        UnitIndex ijk001 = UnitIndex.at(0, 1, 1);
        UnitIndex ijk111 = UnitIndex.at(1, 1, 1);
        UnitIndex ijk211 = UnitIndex.at(2, 1, 1);
        UnitIndex ijk121 = UnitIndex.at(1, 2, 1);
        UnitIndex ijk112 = UnitIndex.at(1, 1, 2);

        runCompareTest(i10, i20, i30, i40, i50,
                       ij52, ij43, ij53, ij63, ij54,
                       ijk110, ijk101, ijk001, ijk111, ijk211, ijk121, ijk112);
    }

    private void runCompareTest(UnitIndex... indexes) {
        List<UnitIndex> ordered = List.of(indexes);

        for (int k = 1; k < ordered.size(); ++k)
            assertTrue(ordered.get(k).compareTo(ordered.get(k - 1)) > 0);

        List<UnitIndex> shuffled = new ArrayList<>(ordered);
        Collections.shuffle(shuffled);

        List<UnitIndex> sorted = new ArrayList<>(shuffled);
        Collections.sort(sorted);

        assertEquals(ordered, sorted);
    }

    @Test public void testDimensionality() {
        assertEquals(1, UnitIndex.at(3).dimensionality());
        assertEquals(2, UnitIndex.at(3, 5).dimensionality());
        assertEquals(3, UnitIndex.at(3, 5, 2).dimensionality());
    }

    @Test public void testOrigin() {
        assertEquals(UnitIndex.at(0), UnitIndex.origin(1));
        assertEquals(UnitIndex.at(0, 0), UnitIndex.origin(2));
        assertEquals(UnitIndex.at(0, 0, 0), UnitIndex.origin(3));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("jam.bravais.BravaisUnitIndexTest");
    }
}
