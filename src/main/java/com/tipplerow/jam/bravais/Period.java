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

/**
 * Defines the dimensions of a periodic lattice and translates
 * absolute lattice coordinates into their periodic images.
 *
 * @author Scott Shaffer
 */
public interface Period {
    /**
     * Creates a linear lattice period.
     *
     * @param N the periodic length.
     *
     * @return the new linear lattice period.
     *
     * @throws IllegalArgumentException unless the periodic dimension
     * is positive.
     */
    static Period linear(int N) {
        return new Period1D(N);
    }

    /**
     * Creates a linear lattice period.
     *
     * @param N the periodic length along both directions.
     *
     * @return the new square lattice period.
     *
     * @throws IllegalArgumentException unless the periodic dimension
     * is positive.
     */
    static Period square(int N) {
        return new Period2D(N, N);
    }

    /**
     * Creates a cubic lattice period.
     *
     * @param N the periodic length along all directions.
     *
     * @return the new cubic lattice period.
     *
     * @throws IllegalArgumentException unless the periodic dimension
     * is positive.
     */
    static Period cubic(int N) {
        return new Period3D(N, N, N);
    }

    /**
     * Creates an arbitrary lattice period.
     *
     * @param period the periodic length along each direction.
     *
     * @return a lattice period with the specified dimensions.
     *
     * @throws IllegalArgumentException unless the dimensionality lies
     * between one and three (inclusive).
     */
    static Period box(int... period) {
        switch (period.length) {
        case 1:
            return new Period1D(period[0]);
        
        case 2:
            return new Period2D(period[0], period[1]);
        
        case 3:
            return new Period3D(period[0], period[1], period[2]);

        default:
            throw new IllegalArgumentException("Invalid period dimensionality.");
        }
    }

    /**
     * Creates an N-dimensional box with sides of equal length.
     *
     * @param period the periodic length along each direction.
     * @param dim    the dimensionality of the box.
     *
     * @return a lattice period with the specified dimensions.
     *
     * @throws IllegalArgumentException unless the dimensionality lies
     * between one and three (inclusive).
     */
    static Period boxND(int period, int dim) {
        switch (dim) {
        case 1:
            return linear(period);

        case 2:
            return square(period);

        case 3:
            return cubic(period);

        default:
            throw new IllegalArgumentException("Invalid period dimensionality.");
        }
    }

    /**
     * Identifies unit indexes whose absolute coordinates (not
     * periodic images) lie within a periodic box.
     *
     * @param index the coordinate of an absolute unit index.
     *
     * @param period the lattice period.
     *
     * @return {@code true} iff the absolute index (not its periodic
     * image) lies within a box with the specified period, expressed
     * mathematically as {@code 0 <= index && index < period}.
     */
    static boolean contains(int index, int period) {
        return 0 <= index && index < period;
    }

    /**
     * Translates an absolute unit index into its periodic image.
     *
     * @param index the coordinate of an absolute unit index.
     *
     * @param period the lattice period.
     *
     * @return the periodic image.
     */
    static int imageOf(int index, int period) {
	int image = index % period;

	if (image < 0)
	    image += period;

	return image;
    }

    /**
     * Identifies unit indexes whose absolute coordinates (not
     * periodic images) lie within this periodic box.
     *
     * @param index an absolute unit index to examine.
     *
     * @return {@code true} iff the absolute index (not its periodic
     * image) lies within this periodic box.
     */
    boolean contains(UnitIndex index);

    /**
     * Returns the dimensionality of this lattice period.
     *
     * @return the dimensionality of this lattice period.
     */
    int dimensionality();

    /**
     * Returns the length of the period along a given dimension.
     *
     * @param dim the dimension of interest.
     *
     * @return the length of the period along the specified dimension.
     */
    int period(int dim);

    /**
     * Translates an absolute unit index into its periodic image.
     *
     * @param index an absolute coordinate.
     *
     * @return the periodic image.
     */
    UnitIndex imageOf(UnitIndex index);

    /**
     * Returns the number of distinct sites on a lattice with this
     * period.
     *
     * @return the number of distinct sites on a lattice with this
     * period.
     */
    long countSites();

    /**
     * Enumerates all images in the box defined by this period.
     *
     * @return a list containing all images in the box defined
     * by this period.
     */
    List<UnitIndex> enumerate();
}
