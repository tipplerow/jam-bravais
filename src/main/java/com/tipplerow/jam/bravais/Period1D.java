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
import java.util.List;

final class Period1D extends AbstractPeriod {
    private final int nx;

    Period1D(int nx) {
        validateDimension(nx);

        this.nx = nx;
    }

    @Override public int dimensionality() {
        return 1;
    }

    @Override public UnitIndex imageOf(UnitIndex index) {
        validateDimensionality(index);

        return UnitIndex.at(Period.imageOf(index.coord(0), nx));
    }

    @Override public int period(int dim) {
        if (dim == 0)
            return nx;
        else
            throw new IllegalArgumentException("Invalid period dimension.");
    }

    @Override public List<UnitIndex> enumerate() {
        List<UnitIndex> images = new ArrayList<>(nx);

        for (int i = 0; i < nx; ++i)
            images.add(UnitIndex.at(i));

        assert images.size() == countSites();
        return images;
    }

    @Override public String toString() {
        return String.format("Period(%d)", nx);
    }
}
