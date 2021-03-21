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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tipplerow.jam.lang.JamException;
import com.tipplerow.jam.math.Point;

import lombok.Getter;

/**
 * Describes a Bravais lattice with periodic boundaries.
 *
 * @author Scott Shaffer
 */
public final class Lattice {
    /**
     * The periodic dimensions of this lattice.
     */
    @Getter
    private final Period period;

    /**
     * The unit cell for this lattice.
     */
    @Getter
    private final UnitCell unitCell;

    private Lattice(UnitCell unitCell, Period period) {
        if (period.dimensionality() != unitCell.dimensionality())
            throw new IllegalArgumentException("Inconsistent unit cell and period dimensionality.");

        this.period = period;
        this.unitCell = unitCell;
    }

    /**
     * Creates a new lattice with a fixed unit cell and period.
     *
     * @param unitCell the unit cell for the lattice.
     *
     * @param period the periodic dimensions of the lattice.
     *
     * @return a new empty lattice with the specified unit cell and period.
     *
     * @throws IllegalArgumentException unless the unit cell and lattice
     * period have the same dimensionality.
     */
    public static Lattice create(UnitCell unitCell, Period period) {
        return new Lattice(unitCell, period);
    }

    /**
     * Identifies points whose absolute coordinates (not periodic
     * images) lie within the primary box for this lattice.
     *
     * @param point the point to examine.
     *
     * @return {@code true} iff the point lies within the primary box
     * (not a periodic image) of this lattice.
     */
    public boolean contains(Point point) {
        return period.contains(unitCell.indexOf(point));
    }

    /**
     * Returns the number of unique (non-periodic) sites in this
     * lattice.
     *
     * @return the number of unique (non-periodic) sites in this
     * lattice.
     */
    public long countSites() {
        return period.countSites();
    }

    /**
     * Returns the dimensionality of this lattice.
     *
     * @return the dimensionality of this lattice.
     */
    public int dimensionality() {
        return unitCell.dimensionality();
    }

    /**
     * Returns the periodic image of a continuous-space point
     * coordinate.
     *
     * @param point an absolute continuous-space coordinate.
     *
     * @return the periodic image of the specified point coordinate.
     */
    public Point imageOf(Point point) {
        return unitCell.pointAt(imageOf(unitCell.indexOf(point)));
    }

    /**
     * Returns the periodic image of an absolute unit cell index.
     *
     * @param index an absolute unit cell index.
     *
     * @return the periodic image of the specified unit cell index.
     */
    public UnitIndex imageOf(UnitIndex index) {
        return period.imageOf(index);
    }

    /**
     * Returns a list of all primary (non-periodic) points on this
     * lattice.
     *
     * @return a list of all primary (non-periodic) points on this
     * lattice.
     */
    public List<Point> listPoints() {
        var indexes = period.enumerate();
        var points  = new ArrayList<Point>(indexes.size());

        for (UnitIndex index : indexes)
            points.add(unitCell.pointAt(index));

        return points;
    }

    /**
     * Returns a list of all neighbors to a point on this lattice.
     *
     * @param point a point on this lattice (or near a point on this
     * lattice).
     *
     * @return a list of all neighbors to the point on this lattice
     * that is closest to the input point.
     */
    public List<Point> listNeighbors(Point point) {
        var pointIndex      = unitCell.indexOf(point);
        var neighborIndexes = unitCell.getNeighbors(pointIndex);
        var neighborPoints  = new ArrayList<Point>(neighborIndexes.size());

        for (UnitIndex neighborIndex : neighborIndexes)
            neighborPoints.add(unitCell.pointAt(neighborIndex));

        return neighborPoints;
    }

    /**
     * Returns a mapping from each primary unit index on this lattice
     * to a list of its nearest neighbors.
     *
     * @param coordType the desired type for the neighbor coordinates
     * (absolute or periodic images).
     *
     * @return a mapping from each primary unit index on this lattice
     * to a list of its nearest neighbors.
     */
    public Map<UnitIndex, List<UnitIndex>> mapIndexNeighbors(CoordType coordType) {
        List<UnitIndex> indexes = period.enumerate();
        Map<UnitIndex, List<UnitIndex>> neighborMap = new LinkedHashMap<>(indexes.size());

        switch (coordType) {
        case ABSOLUTE:
            mapAbsoluteIndexNeighbors(indexes, neighborMap);
            break;

        case IMAGE:
            mapIndexNeighborImages(indexes, neighborMap);
            break;

        default:
            throw JamException.runtime("Unknown coordinate type: [%s].", coordType);
        }

        return neighborMap;
    }

    private void mapAbsoluteIndexNeighbors(List<UnitIndex> indexes, Map<UnitIndex, List<UnitIndex>> neighborMap) {
        for (UnitIndex index : indexes)
            neighborMap.put(index, unitCell.getNeighbors(index));
    }

    private void mapIndexNeighborImages(List<UnitIndex> indexes, Map<UnitIndex, List<UnitIndex>> neighborMap) {
        for (UnitIndex index : indexes)
            neighborMap.put(index, listNeighborImages(index));
    }

    private List<UnitIndex> listNeighborImages(UnitIndex index) {
        List<UnitIndex> neighborCoords = unitCell.getNeighbors(index);
        List<UnitIndex> neighborImages = new ArrayList<>(neighborCoords.size());

        for (UnitIndex neighborCoord : neighborCoords)
            neighborImages.add(period.imageOf(neighborCoord));

        return neighborImages;
    }
}
