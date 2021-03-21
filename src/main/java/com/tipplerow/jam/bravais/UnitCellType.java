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
 * Enumerates the supported unit cell types.
 *
 * @author Scott Shaffer
 */
public enum UnitCellType {
    /**
     * The body-centered cubic unit cell.
     */
    BCC {
        @Override public UnitCell create(double... length) {
            if (length.length == 1)
                return new BCCUnitCell(length[0]);
            else
                throw new IllegalArgumentException("Exactly one length is required.");
        }

        @Override public UnitCell fundamental() {
            return BCCUnitCell.FUNDAMENTAL;
        }
    },

    /**
     * The simple cubic unit cell.
     */
    CUBIC {
        @Override public UnitCell create(double... length) {
            if (length.length == 1)
                return new SimpleCubicUnitCell(length[0]);
            else
                throw new IllegalArgumentException("Exactly one length is required.");
        }

        @Override public UnitCell fundamental() {
            return SimpleCubicUnitCell.FUNDAMENTAL;
        }
    },

    /**
     * The face-centered cubic unit cell.
     */
    FCC {
        @Override public UnitCell create(double... length) {
            if (length.length == 1)
                return new FCCUnitCell(length[0]);
            else
                throw new IllegalArgumentException("Exactly one length is required.");
        }

        @Override public UnitCell fundamental() {
            return FCCUnitCell.FUNDAMENTAL;
        }
    },

    /**
     * The one-dimensional unit cell.
     */
    LINEAR {
        @Override public UnitCell create(double... length) {
            if (length.length == 1)
                return LinearUnitCell.create(length[0]);
            else
                throw new IllegalArgumentException("Exactly one length is required.");
        }

        @Override public UnitCell fundamental() {
            return LinearUnitCell.FUNDAMENTAL;
        }
    },

    /**
     * The two-dimensional hexagonal unit cell.
     */
    HEXAGONAL {
        @Override public UnitCell create(double... sides) {
            if (sides.length == 1)
                return new HexagonalUnitCell(sides[0]);
            else
                throw new IllegalArgumentException("Exactly one side length is required.");
        }

        @Override public UnitCell fundamental() {
            return HexagonalUnitCell.FUNDAMENTAL;
        }
    },

    /**
     * The two-dimensional square unit cell.
     */
    SQUARE {
        @Override public UnitCell create(double... sides) {
            if (sides.length == 1)
                return new SquareUnitCell(sides[0]);
            else
                throw new IllegalArgumentException("Exactly one side length is required.");
        }

        @Override public UnitCell fundamental() {
            return SquareUnitCell.FUNDAMENTAL;
        }
    };

    /**
     * Creates a new unit cell of this type.
     *
     * @param sides the lengths of the sides of the unit cell.
     *
     * @return a new unit cell of this type with the specified side
     * lengths.
     *
     * @throws IllegalArgumentException unless the side lengths are
     * valid for this lattice type.
     */
    public abstract UnitCell create(double... sides);

    /**
     * Returns the fundamental unit cell for this type (typically with
     * unit side length).
     *
     * @return the fundamental unit cell for this type (typically with
     * unit side length).
     */
    public abstract UnitCell fundamental();
}
