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

import com.tipplerow.jam.math.DoubleUtil;
import com.tipplerow.jam.vector.VectorView;

import lombok.Getter;

/**
 * Represents a three-dimensional body-centered cubic (BCC) unit cell.
 *
 * @author Scott Shaffer
 */
public final class BCCUnitCell extends UnitCell3D {
    /**
     * The length of each side of the unit cell.
     */
    @Getter
    private final double side;

    private static final List<UnitIndex> TRANSLATION_VECTORS =
        List.of(UnitIndex3D.at(-1, -1, -1),
                UnitIndex3D.at( 0,  0, -1),
                UnitIndex3D.at( 0, -1,  0),
                UnitIndex3D.at(-1,  0,  0),
                UnitIndex3D.at( 1,  0,  0),
                UnitIndex3D.at( 0,  1,  0),
                UnitIndex3D.at( 0,  0,  1),
                UnitIndex3D.at( 1,  1,  1));

    /**
     * The fundamental BCC unit cell with unit side length.
     */
    public static final BCCUnitCell FUNDAMENTAL = new BCCUnitCell(1.0);

    /**
     * Creates a new BCC unit cell with a given side length.
     *
     * @param side the side length for the unit cell.
     *
     * @throws IllegalArgumentException unless the side length is
     * positive.
     */
    public BCCUnitCell(double side) {
        super(constructBasis(side));
        this.side = side;
    }

    private static List<VectorView> constructBasis(double side) {
        validateSide(side);

        return List.of(VectorView.of( 0.5 * side,  0.5 * side, -0.5 * side),
                       VectorView.of(-0.5 * side,  0.5 * side,  0.5 * side),
                       VectorView.of( 0.5 * side, -0.5 * side,  0.5 * side));
    }

    @Override public double getNeighborDistance() {
        return side * DoubleUtil.HALF_SQRT3;
    }

    @Override public List<UnitIndex> viewNeighborTranslationVectors() {
        return TRANSLATION_VECTORS;
    }
}
