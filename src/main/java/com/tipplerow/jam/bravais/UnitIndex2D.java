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

final class UnitIndex2D implements UnitIndex {
    final int i;
    final int j;

    private UnitIndex2D(int i, int j) {
        this.i = i;
        this.j = j;
    }

    static final UnitIndex2D ORIGIN = at(0, 0);

    static UnitIndex2D at(int i, int j) {
        return new UnitIndex2D(i, j);
    }

    @Override public int coord(int dim) {
        switch (dim) {
        case 0:
            return i;

        case 1:
            return j;

        default:
            throw new IndexOutOfBoundsException("Invalid index dimension.");
        }
    }

    @Override public int dimensionality() {
        return 2;
    }

    @Override public UnitIndex2D plus(UnitIndex that) {
        return plus2D((UnitIndex2D) that);
    }

    private UnitIndex2D plus2D(UnitIndex2D that) {
        return at(this.i + that.i,
                  this.j + that.j);
    }

    @Override public UnitIndex2D times(int scalar) {
        return at(scalar * i, scalar * j);
    }

    @Override public int[] toArray() {
        return new int[] { i, j };
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof UnitIndex2D) && equalsUnitIndex2D((UnitIndex2D) obj);
    }

    private boolean equalsUnitIndex2D(UnitIndex2D that) {
        return this.i == that.i
            && this.j == that.j;
    }

    @Override public int hashCode() {
        return i + (j << 15);
    }

    @Override public String toString() {
        return String.format("UnitIndex(%d, %d)", i, j);
    }
}
