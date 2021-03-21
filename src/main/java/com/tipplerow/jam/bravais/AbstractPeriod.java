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

/**
 * Provides are partial implementation of the {@code Period} interface.
 *
 * @author Scott Shaffer
 */
public abstract class AbstractPeriod implements Period {
    /**
     * Ensures that a period length is valid.
     *
     * @param period the period length to validate.
     *
     * @throws IllegalArgumentException unless the period length is
     * positive.
     */
    public static void validateDimension(int period) {
	if (period < 1)
	    throw new IllegalArgumentException("Non-positive periodic dimension.");
    }

    /**
     * Ensures that the dimensionality of a discrete unit index
     * matches the dimensionality of this lattice period.
     *
     * @param index the index to validate.
     *
     * @throws IllegalArgumentException unless the dimensionality of
     * the input index matches the dimensionality of this period.
     */
    public void validateDimensionality(UnitIndex index) {
        if (index.dimensionality() != this.dimensionality())
            throw new IllegalArgumentException("Inconsistent index dimensionality.");
    }

    @Override public boolean contains(UnitIndex index) {
        validateDimensionality(index);

        for (int dim = 0; dim < dimensionality(); ++dim)
            if (!Period.contains(index.coord(dim), period(dim)))
                return false;

        return true;
    }

    @Override public long countSites() {
        long count = 1;

        for (int dim = 0; dim < dimensionality(); ++dim)
            count *= period(dim);

        return count;
    }
}
