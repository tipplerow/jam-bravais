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

final class UnitIndex3D implements UnitIndex {
    final int i;
    final int j;
    final int k;

    private UnitIndex3D(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    static final UnitIndex3D ORIGIN = at(0, 0, 0);

    static UnitIndex3D at(int i, int j, int k) {
        return new UnitIndex3D(i, j, k);
    }

    @Override public int coord(int dim) {
        switch (dim) {
        case 0:
            return i;

        case 1:
            return j;

        case 2:
            return k;

        default:
            throw new IndexOutOfBoundsException("Invalid index dimension.");
        }
    }

    @Override public int dimensionality() {
        return 3;
    }

    @Override public UnitIndex3D plus(UnitIndex that) {
        return plus3D((UnitIndex3D) that);
    }

    private UnitIndex3D plus3D(UnitIndex3D that) {
        return at(this.i + that.i,
                  this.j + that.j,
                  this.k + that.k);
    }

    @Override public UnitIndex3D times(int scalar) {
        return at(scalar * i, scalar * j, scalar * k);
    }

    @Override public int[] toArray() {
        return new int[] { i, j, k };
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof UnitIndex3D) && equalsUnitIndex3D((UnitIndex3D) obj);
    }

    private boolean equalsUnitIndex3D(UnitIndex3D that) {
        return this.i == that.i
            && this.j == that.j
            && this.k == that.k;
    }

    @Override public int hashCode() {
        return i + (j << 10) + (k << 20);
    }

    @Override public String toString() {
        return String.format("UnitIndex(%d, %d, %d)", i, j, k);
    }
}
