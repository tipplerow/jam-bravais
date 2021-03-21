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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import com.tipplerow.jam.math.Point;
import lombok.Getter;

/**
 * Tracks a population of occupants on a Bravais lattice.
 * 
 * @author Scott Shaffer
 */
public final class Population<T> {
    /**
     * The underlying Bravais lattice.
     */
    @Getter
    private final Lattice lattice;

    // Mapping from occupants to their ABSOLUTE unit cell indexes...
    private final Map<T, UnitIndex> indexMap = new HashMap<>();

    // Bidirectional mapping between occupants and the PERIODIC IMAGES
    // of their unit cell indexes...
    private final BiMap<UnitIndex, T> imageBiMap = HashBiMap.create();

    private Population(Lattice lattice) {
        this.lattice = lattice;
    }

    /**
     * Creates a new empty lattice.
     *
     * @param <T> the run-time type of the lattice occupants.
     *
     * @param lattice the Bravais lattice to contain the occupants.
     *
     * @return a new empty lattice.
     */
    public static <T> Population<T> empty(Lattice lattice) {
        return new Population<>(lattice);
    }

    /**
     * Identifies occupants on this lattice.
     *
     * @param occupant an occupant to examine.
     *
     * @return {@code true} iff this lattice contains the specified
     * occupant.
     */
    public boolean contains(T occupant) {
        return indexMap.containsKey(occupant);
    }

    /**
     * Identifies occupants on this lattice.
     *
     * @param occupants a collection of occupants to examine.
     *
     * @return {@code true} iff this lattice contains every occupant
     * in the specified collection.
     */
    public boolean containsAll(Collection<? extends T> occupants) {
        for (T occupant : occupants)
            if (!contains(occupant))
                return false;

        return true;
    }

    /**
     * Returns the number of occupants on this lattice.
     *
     * @return the number of occupants on this lattice.
     */
    public int countOccupants() {
        return indexMap.size();
    }

    /**
     * Fills this lattice moving from left-to-right, bottom-to-top with
     * occupants created by a Supplier.  Any previous occupants will be
     * removed.
     *
     * @param supplier the source of new occupants.
     */
    public void fill(Supplier<? extends T> supplier) {
        List<UnitIndex> images = lattice.getPeriod().enumerate();

        for (UnitIndex image : images)
            place(supplier.get(), image);
    }

    /**
     * Fills this lattice with occupants moving from left-to-right,
     * bottom-to-top in the order returned by the collection iterator.
     * Any previous occupants will be removed.
     *
     * @param occupants the occupants to add to this lattice.
     *
     * @throws IllegalArgumentException unless the number of occupants
     * exactly matches the number of distinct sites on this lattice.
     */
    public void fill(Collection<? extends T> occupants) {
        if (occupants.size() != lattice.getPeriod().countSites())
            throw new IllegalArgumentException("Occupants do not exactly fill the lattice.");

        List<UnitIndex> images = lattice.getPeriod().enumerate();
        assert occupants.size() == images.size();

        int imageOrdinal = 0;

        for (T occupant : occupants) {
            place(occupant, images.get(imageOrdinal));
            ++imageOrdinal;
        }
    }

    /**
     * Returns the discrete-space index of the unit cell occupied by
     * an occupant on this lattice (in absolute coordinates, not as
     * a periodic image).
     *
     * @param occupant the occupant to locate.
     *
     * @return the discrete-space index of the specified occupant
     * ({@code null} if this lattice does not contain the occupant).
     */
    public UnitIndex indexOf(T occupant) {
        return indexMap.get(occupant);
    }

    /**
     * Identifies empty lattices.
     *
     * @return {@code true} iff this lattice contains no occupants.
     */
    public boolean isEmpty() {
        return indexMap.isEmpty();
    }

    /**
     * Identifies completely full lattices.
     *
     * @return {@code true} iff every site in this lattice is
     * occupied.
     */
    public boolean isFull() {
        return countOccupants() == lattice.countSites();
    }

    /**
     * Identifies occupied unit cells.
     *
     * @param point a continuous-space coordinate to examine (given as
     * an absolute coordinate, not necessarily a periodic image).
     *
     * @return {@code true} iff the unit cell containing the specified
     * point is occupied.
     */
    public boolean isOccupied(Point point) {
        return isOccupied(lattice.getUnitCell().indexOf(point));
    }

    /**
     * Identifies occupied unit cells.
     *
     * @param index the discrete index of the unit cell to examine
     * (given as an absolute index, not necessarily a periodic image).
     *
     * @return {@code true} iff the unit cell with the specified
     * discrete index is occupied.
     */
    public boolean isOccupied(UnitIndex index) {
        return imageBiMap.containsKey(lattice.imageOf(index));
    }

    /**
     * Returns a list containing the occupants of this lattice (in no
     * particular order).
     *
     * @return a list containing the occupants of this lattice (in no
     * particular order).
     */
    public List<T> listOccupants() {
        return new ArrayList<>(indexMap.keySet());
    }

    /**
     * Returns the continuous-space position of an occupant on this
     * lattice (in absolute coordinates, not as a periodic image).
     *
     * @param occupant the occupant to locate.
     *
     * @return the continuous-space position of the specified occupant
     * ({@code null} if this lattice does not contain the occupant).
     */
    public Point locate(T occupant) {
        UnitIndex index = indexOf(occupant);

        if (index != null)
            return lattice.getUnitCell().pointAt(index);
        else
            return null;
    }

    /**
     * Returns a map containing the absolute continuous-space
     * positions of all lattice occupants.
     *
     * @return a map containing the absolute continuous-space
     * positions of all lattice occupants.
     */
    public Map<T, Point> mapPoints() {
        Map<T, Point> pointMap = new HashMap<>(indexMap.size());

        for (var entry : indexMap.entrySet())
            pointMap.put(entry.getKey(), lattice.getUnitCell().pointAt(entry.getValue()));

        return pointMap;
    }

    /**
     * Returns the nearest neighbors of an occupant on this lattice.
     *
     * @param occupant the occupant to locate.
     *
     * @return the nearest neighbors of the specified occupant (empty
     * if this lattice does not contain the occupant).
     */
    public List<T> neighborsOf(T occupant) {
        UnitIndex occupantIndex = indexOf(occupant);

        if (occupantIndex == null)
            return List.of();

        var neighborIndexes = lattice.getUnitCell().getNeighbors(occupantIndex);
        var neighborOccupants = new ArrayList<T>(neighborIndexes.size());

        for (UnitIndex neighborIndex : neighborIndexes) {
            T neighborOccupant = occupantAt(neighborIndex);

            if (neighborOccupant != null)
                neighborOccupants.add(neighborOccupant);
        }

        return neighborOccupants;
    }

    /**
     * Returns the occupant of the unit cell containing a given point
     * (applying periodic boundary conditions if necessary).
     *
     * @param point a continuous-space coordinate to examine.
     *
     * @return the occupant of the unit cell containing the specified
     * point ({@code null} if the cell is unoccupied).
     */
    public T occupantAt(Point point) {
        return occupantAt(lattice.getUnitCell().indexOf(point));
    }

    /**
     * Returns the occupant of a unit cell (applying periodic boundary
     * conditions if necessary).
     *
     * @param index the discrete index of the unit cell to examine.
     *
     * @return the occupant of the specified unit cell ({@code null}
     * if the cell is unoccupied).
     */
    public T occupantAt(UnitIndex index) {
        return imageBiMap.get(lattice.imageOf(index));
    }

    /**
     * Places an occupant on this lattice in the unit cell containing
     * a given point.
     *
     * @param occupant the occupant to place on this lattice.
     *
     * @param point the continuous-space coordinate of the desired
     * location.
     *
     * @return the previous occupant of the unit cell ({@code null} if
     * the cell was unoccupied).
     */
    public T place(T occupant, Point point) {
        return place(occupant, lattice.getUnitCell().indexOf(point));
    }
    
    /**
     * Places an occupant on this lattice in the unit cell containing
     * a given point.
     *
     * @param occupant the occupant to place on this lattice.
     *
     * @param index the discrete index of the destination unit cell.
     *
     * @return the previous occupant of the unit cell ({@code null} if
     * the cell was unoccupied).
     */
    public T place(T occupant, UnitIndex index) {
        T prevOcc = imageBiMap.forcePut(lattice.imageOf(index), occupant);

        if (prevOcc != null && prevOcc != occupant)
            indexMap.remove(prevOcc);

        indexMap.put(occupant, index);
        assert indexMap.size() == imageBiMap.size();

        return prevOcc;
    }

    /**
     * Removes an occupant from this lattice (has no effect if the
     * occupant is not present).
     *
     * @param occupant the occupant to remove.
     */
    public void remove(T occupant) {
        indexMap.remove(occupant);
        imageBiMap.inverse().remove(occupant);
    }
    
    /**
     * Removes an existing lattice occupant and adds a new occupant at
     * the same location.
     *
     * @param oldOccupant the existing occupant to replace.
     *
     * @param newOccupant the new occupant to add.
     *
     * @throws IllegalArgumentException unless this lattice contains
     * the old occupant.
     */
    public void replace(T oldOccupant, T newOccupant) {
        UnitIndex index = indexOf(oldOccupant);

        if (index != null)
            place(newOccupant, index);
        else
            throw new IllegalArgumentException("Missing lattice occupant.");
    }
    
    /**
     * Swaps the locations of two occupants on this lattice.
     *
     * @param occ1 the first occupant to swap.
     *
     * @param occ2 the second occupant to swap.
     *
     * @throws IllegalArgumentException unless this lattice contains
     * both occupants.
     */
    public void swap(T occ1, T occ2) {
        UnitIndex index1 = indexOf(occ1);
        UnitIndex index2 = indexOf(occ2);

        if (index1 != null && index2 != null) {
            place(occ1, index2);
            place(occ2, index1);
        }
        else
            throw new IllegalArgumentException("Missing lattice occupant.");
    }

    /**
     * Identifies unoccupied neighbors to a given lattice site.
     *
     * @param index the (absolute) index of a lattice site.
     *
     * @return a list containing the (absolute) indexes of all
     * unoccupied nearest neighbors to the specified site.
     */
    public List<UnitIndex> unoccupiedNeighbors(UnitIndex index) {
        List<UnitIndex> neighbors = lattice.getUnitCell().getNeighbors(index);
        List<UnitIndex> unoccupied = new ArrayList<>(neighbors.size());

        for (UnitIndex neighbor : neighbors)
            if (!isOccupied(neighbor))
                unoccupied.add(neighbor);

        return unoccupied;
    }
}
