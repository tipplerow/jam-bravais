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

import com.tipplerow.jam.math.DoubleComparator;
import com.tipplerow.jam.math.Point;
import com.tipplerow.jam.vector.VectorView;

/**
 * Provides a partial implementation of the {@code UnitCell} interface.
 *
 * @author Scott Shaffer
 */
public abstract class AbstractUnitCell implements UnitCell {
    private final List<VectorView> basis;

    protected AbstractUnitCell(List<VectorView> basis) {
        this.basis = Collections.unmodifiableList(basis);
    }

    /**
     * Ensures that the number of primitive (basis) vectors and their
     * dimensionality match the dimensionality of a unit cell.
     *
     * @param basis the basis vectors to validate.
     *
     * @param dimensionality the dimensionality of the unit cell.
     *
     * @throws IllegalArgumentException unless the number of primitive
     * (basis) vectors and their dimensionality match the specified
     * dimensionality.
     */
    public static void validateBasis(List<VectorView> basis, int dimensionality) {
        if (basis.size() != dimensionality)
            throw new IllegalArgumentException("Inconsistent number of basis vectors.");

        for (VectorView vector : basis)
            if (vector.length() != dimensionality)
                throw new IllegalArgumentException("Inconsistent basis vector dimensionality.");
    }

    /**
     * Ensures that the dimensionality of a continuous-space location
     * matches the dimensionality of this unit cell.
     *
     * @param point the point to validate.
     *
     * @throws IllegalArgumentException unless the dimensionality of
     * the input point matches the dimensionality of this unit cell.
     */
    public void validateDimensionality(Point point) {
        if (point.dimensionality() != this.dimensionality())
            throw new IllegalArgumentException("Inconsistent point dimensionality.");
    }

    /**
     * Ensures that the dimensionality of a discrete unit index
     * matches the dimensionality of this unit cell.
     *
     * @param index the index to validate.
     *
     * @throws IllegalArgumentException unless the dimensionality of
     * the input index matches the dimensionality of this unit cell.
     */
    public void validateDimensionality(UnitIndex index) {
        if (index.dimensionality() != this.dimensionality())
            throw new IllegalArgumentException("Inconsistent index dimensionality.");
    }

    /**
     * Ensures that the side length in a unit cell is positive.
     *
     * @param side the side length to validate.
     *
     * @throws IllegalArgumentException unless the side length is
     * positive.
     */
    public static void validateSide(double side) {
        if (!DoubleComparator.DEFAULT.isPositive(side))
            throw new IllegalArgumentException("Non-positive side length.");
    }

    @Override public List<UnitIndex> getNeighbors(UnitIndex index) {
        List<UnitIndex> transVecs = viewNeighborTranslationVectors();
        List<UnitIndex> neighbors = new ArrayList<>(transVecs.size());

        for (UnitIndex transVec : transVecs)
            neighbors.add(index.plus(transVec));

        return neighbors;
    }

    @Override public List<VectorView> viewBasis() {
        return basis;
    }
}
