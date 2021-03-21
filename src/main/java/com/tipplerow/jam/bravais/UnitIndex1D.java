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

final class UnitIndex1D implements UnitIndex {
    final int index;

    private UnitIndex1D(int index) {
        this.index = index;
    }

    static final UnitIndex1D ORIGIN = at(0);

    static UnitIndex1D at(int index) {
        return new UnitIndex1D(index);
    }

    @Override public int coord(int dim) {
        if (dim == 0)
            return index;
        else
            throw new IndexOutOfBoundsException("Invalid index dimension.");
    }

    @Override public int dimensionality() {
        return 1;
    }

    @Override public UnitIndex1D plus(UnitIndex that) {
        return plus1D((UnitIndex1D) that);
    }

    private UnitIndex1D plus1D(UnitIndex1D that) {
        return at(this.index + that.index);
    }

    @Override public UnitIndex1D times(int scalar) {
        return at(scalar * index);
    }

    @Override public int[] toArray() {
        return new int[] { index };
    }

    @Override public boolean equals(Object obj) {
        return (obj instanceof UnitIndex1D) && equalsUnitIndex1D((UnitIndex1D) obj);
    }

    private boolean equalsUnitIndex1D(UnitIndex1D that) {
        return this.index == that.index;
    }

    @Override public int hashCode() {
        return index;
    }

    @Override public String toString() {
        return String.format("UnitIndex(%d)", index);
    }
}
