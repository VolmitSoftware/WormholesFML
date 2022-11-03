/*------------------------------------------------------------------------------
 -   Adapt is a Skill/Integration plugin  for Minecraft Bukkit Servers
 -   Copyright (c) 2022 Arcane Arts (Volmit Software)
 -
 -   This program is free software: you can redistribute it and/or modify
 -   it under the terms of the GNU General Public License as published by
 -   the Free Software Foundation, either version 3 of the License, or
 -   (at your option) any later version.
 -
 -   This program is distributed in the hope that it will be useful,
 -   but WITHOUT ANY WARRANTY; without even the implied warranty of
 -   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 -   GNU General Public License for more details.
 -
 -   You should have received a copy of the GNU General Public License
 -   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 -----------------------------------------------------------------------------*/

package com.volmit.wormholes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;

import java.awt.Dimension;
import java.util.*;

/**
 * Cuboids
 *
 * @author cyberpwn
 */
public class Cuboid {
    protected int x1, y1, z1;
    protected int x2, y2, z2;

    /**
     * Construct a Cuboid given two Location objects which represent any two corners
     * of the Cuboid.
     *
     * @param l1 one of the corners
     * @param l2 the other corner
     */
    public Cuboid(BlockPos l1, BlockPos l2) {
        x1 = Math.min(l1.getX(), l2.getX());
        y1 = Math.min(l1.getY(), l2.getY());
        z1 = Math.min(l1.getZ(), l2.getZ());
        x2 = Math.max(l1.getX(), l2.getX());
        y2 = Math.max(l1.getY(), l2.getY());
        z2 = Math.max(l1.getZ(), l2.getZ());
    }

    public List<BlockPos> getBlockPositions()
    {
        List<BlockPos> positions = new ArrayList<>();
        for(int x = x1; x <= x2; x++)
        {
            for(int y = y1; y <= y2; y++)
            {
                for(int z = z1; z <= z2; z++)
                {
                    positions.add(new BlockPos(x, y, z));
                }
            }
        }
        return positions;
    }

    /**
     * Construct a one-block Cuboid at the given Location of the Cuboid.
     *
     * @param l1 location of the Cuboid
     */
    public Cuboid(BlockPos l1) {
        this(l1, l1);
    }

    /**
     * Copy constructor.
     *
     * @param other the Cuboid to copy
     */
    public Cuboid(Cuboid other) {
        this(other.x1, other.y1, other.z1, other.x2, other.y2, other.z2);
    }

    /**
     * Construct a Cuboid in the given World and xyz co-ordinates
     *
     * @param x1    X co-ordinate of corner 1
     * @param y1    Y co-ordinate of corner 1
     * @param z1    Z co-ordinate of corner 1
     * @param x2    X co-ordinate of corner 2
     * @param y2    Y co-ordinate of corner 2
     * @param z2    Z co-ordinate of corner 2
     */
    public Cuboid(int x1, int y1, int z1, int x2, int y2, int z2) {
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    /**
     * Set the locations
     *
     * @param l1 a
     * @param l2 b
     */
    public void set(BlockPos l1, BlockPos l2) {
        y1 = Math.min(l1.getY(), l2.getY());
        x1 = Math.min(l1.getX(), l2.getX());
        z1 = Math.min(l1.getZ(), l2.getZ());
        x2 = Math.max(l1.getX(), l2.getX());
        y2 = Math.max(l1.getY(), l2.getY());
        z2 = Math.max(l1.getZ(), l2.getZ());
    }

    public Cuboid flatten(int level) {
        return new Cuboid(x1, level, z1, x2, level, z2);
    }

    /**
     * Get the Location of the lower northeast corner of the Cuboid (minimum XYZ
     * co-ordinates).
     *
     * @return Location of the lower northeast corner
     */
    public BlockPos getLowerNE() {
        return new BlockPos(x1, y1, z1);
    }

    /**
     * Get the Location of the upper southwest corner of the Cuboid (maximum XYZ
     * co-ordinates).
     *
     * @return Location of the upper southwest corner
     */
    public BlockPos getUpperSW() {
        return new BlockPos(x2, y2, z2);
    }

    /**
     * Get the the centre of the Cuboid
     *
     * @return Location at the centre of the Cuboid
     */
    public BlockPos getCenter() {
        int x1 = getUpperX() + 1;
        int y1 = getUpperY() + 1;
        int z1 = getUpperZ() + 1;
        return new BlockPos(getLowerX() + (x1 - getLowerX()) / 2.0, getLowerY() + (y1 - getLowerY()) / 2.0, getLowerZ() + (z1 - getLowerZ()) / 2.0);
    }

    /**
     * Get the size of this Cuboid along the X axis
     *
     * @return Size of Cuboid along the X axis
     */
    public int getSizeX() {
        return (x2 - x1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Y axis
     *
     * @return Size of Cuboid along the Y axis
     */
    public int getSizeY() {
        return (y2 - y1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Z axis
     *
     * @return Size of Cuboid along the Z axis
     */
    public int getSizeZ() {
        return (z2 - z1) + 1;
    }

    /**
     * Get the minimum X co-ordinate of this Cuboid
     *
     * @return the minimum X co-ordinate
     */
    public int getLowerX() {
        return x1;
    }

    /**
     * Get the minimum Y co-ordinate of this Cuboid
     *
     * @return the minimum Y co-ordinate
     */
    public int getLowerY() {
        return y1;
    }

    /**
     * Get the minimum Z co-ordinate of this Cuboid
     *
     * @return the minimum Z co-ordinate
     */
    public int getLowerZ() {
        return z1;
    }

    /**
     * Get the maximum X co-ordinate of this Cuboid
     *
     * @return the maximum X co-ordinate
     */
    public int getUpperX() {
        return x2;
    }

    /**
     * Get the maximum Y co-ordinate of this Cuboid
     *
     * @return the maximum Y co-ordinate
     */
    public int getUpperY() {
        return y2;
    }

    /**
     * Get the maximum Z co-ordinate of this Cuboid
     *
     * @return the maximum Z co-ordinate
     */
    public int getUpperZ() {
        return z2;
    }

    /**
     * Get the Blocks at the eight corners of the Cuboid.
     *
     * @return array of Block objects representing the Cuboid corners
     */
    public BlockPos[] corners() {
        BlockPos[] res = new BlockPos[8];
        res[0] = new BlockPos(x1, y1, z1);
        res[1] = new BlockPos(x1, y1, z2);
        res[2] = new BlockPos(x1, y2, z1);
        res[3] = new BlockPos(x1, y2, z2);
        res[4] = new BlockPos(x2, y1, z1);
        res[5] = new BlockPos(x2, y1, z2);
        res[6] = new BlockPos(x2, y2, z1);
        res[7] = new BlockPos(x2, y2, z2);
        return res;
    }

    /**
     * Expand the Cuboid in the given direction by the given amount. Negative
     * amounts will shrink the Cuboid in the given direction. Shrinking a cuboid's
     * face past the opposite face is not an error and will return a valid Cuboid.
     *
     * @param dir    the direction in which to expand
     * @param amount the number of blocks by which to expand
     * @return a new Cuboid expanded by the given direction and amount
     */
    public Cuboid expand(CuboidDirection dir, int amount) {
        switch (dir) {
            case West:
                return new Cuboid(x1 - amount, y1, z1, x2, y2, z2);
            case East:
                return new Cuboid(x1, y1, z1, x2 + amount, y2, z2);
            case North:
                return new Cuboid(x1, y1, z1 - amount, x2, y2, z2);
            case South:
                return new Cuboid(x1, y1, z1, x2, y2, z2 + amount);
            case Down:
                return new Cuboid(x1, y1 - amount, z1, x2, y2, z2);
            case Up:
                return new Cuboid(x1, y1, z1, x2, y2 + amount, z2);
            default:
                throw new IllegalArgumentException("invalid direction " + dir);
        }
    }

    public Cuboid expand(Direction dir, int amount) {
        int ax = dir.toVector().getX() == 1 ? amount : 0;
        int sx = dir.toVector().getX() == -1 ? -amount : 0;
        int ay = dir.toVector().getY() == 1 ? amount : 0;
        int sy = dir.toVector().getY() == -1 ? -amount : 0;
        int az = dir.toVector().getZ() == 1 ? amount : 0;
        int sz = dir.toVector().getZ() == -1 ? -amount : 0;
        return new Cuboid(x1 + sx, y1 + sy, z1 + sz, x2 + ax, y2 + ay, z2 + az);
    }

    /**
     * Shift the Cuboid in the given direction by the given amount.
     *
     * @param dir    the direction in which to shift
     * @param amount the number of blocks by which to shift
     * @return a new Cuboid shifted by the given direction and amount
     */
    public Cuboid shift(CuboidDirection dir, int amount) {
        return expand(dir, amount).expand(dir.opposite(), -amount);
    }

    /**
     * Outset (grow) the Cuboid in the given direction by the given amount.
     *
     * @param dir    the direction in which to outset (must be Horizontal, Vertical, or
     *               Both)
     * @param amount the number of blocks by which to outset
     * @return a new Cuboid outset by the given direction and amount
     */
    public Cuboid outset(CuboidDirection dir, int amount) {
        Cuboid c;
        switch (dir) {
            case Horizontal:
                c = expand(CuboidDirection.North, amount).expand(CuboidDirection.South, amount).expand(CuboidDirection.East, amount).expand(CuboidDirection.West, amount);
                break;
            case Vertical:
                c = expand(CuboidDirection.Down, amount).expand(CuboidDirection.Up, amount);
                break;
            case Both:
                c = outset(CuboidDirection.Horizontal, amount).outset(CuboidDirection.Vertical, amount);
                break;
            default:
                throw new IllegalArgumentException("invalid direction " + dir);
        }
        return c;
    }

    /**
     * Inset (shrink) the Cuboid in the given direction by the given amount.
     * Equivalent to calling outset() with a negative amount.
     *
     * @param dir    the direction in which to inset (must be Horizontal, Vertical, or
     *               Both)
     * @param amount the number of blocks by which to inset
     * @return a new Cuboid inset by the given direction and amount
     */
    public Cuboid inset(CuboidDirection dir, int amount) {
        return outset(dir, -amount);
    }

    /**
     * Return true if the point at (x,y,z) is contained within this Cuboid.
     *
     * @param x the X co-ordinate
     * @param y the Y co-ordinate
     * @param z the Z co-ordinate
     * @return true if the given point is within this Cuboid, false otherwise
     */
    public boolean contains(int x, int y, int z) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    /**
     * Check if the given Location is contained within this Cuboid.
     *
     * @param l the Location to check for
     * @return true if the Location is within this Cuboid, false otherwise
     */
    public boolean contains(BlockPos l) {
        return contains(l.getX(), l.getY(), l.getZ());
    }

    /**
     * Get the volume of this Cuboid.
     *
     * @return the Cuboid volume, in blocks
     */
    public int volume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }

    /**
     * Get the Cuboid representing the face of this Cuboid. The resulting Cuboid
     * will be one block thick in the axis perpendicular to the requested face.
     *
     * @param dir which face of the Cuboid to get
     * @return the Cuboid representing this Cuboid's requested face
     */
    public Cuboid getFace(CuboidDirection dir) {
        switch (dir) {
            case Down:
                return new Cuboid(x1, y1, z1, x2, y1, z2);
            case Up:
                return new Cuboid(x1, y2, z1, x2, y2, z2);
            case West:
                return new Cuboid(x1, y1, z1, x1, y2, z2);
            case East:
                return new Cuboid(x2, y1, z1, x2, y2, z2);
            case North:
                return new Cuboid(x1, y1, z1, x2, y2, z1);
            case South:
                return new Cuboid(x1, y1, z2, x2, y2, z2);
            default:
                throw new IllegalArgumentException("Invalid direction " + dir);
        }
    }

    /**
     * Get the Cuboid big enough to hold both this Cuboid and the given one.
     *
     * @param other the other Cuboid to include
     * @return a new Cuboid large enough to hold this Cuboid and the given Cuboid
     */
    public Cuboid getBoundingCuboid(Cuboid other) {
        if (other == null) {
            return this;
        }

        int xMin = Math.min(getLowerX(), other.getLowerX());
        int yMin = Math.min(getLowerY(), other.getLowerY());
        int zMin = Math.min(getLowerZ(), other.getLowerZ());
        int xMax = Math.max(getUpperX(), other.getUpperX());
        int yMax = Math.max(getUpperY(), other.getUpperY());
        int zMax = Math.max(getUpperZ(), other.getUpperZ());

        return new Cuboid(xMin, yMin, zMin, xMax, yMax, zMax);
    }


    /**
     * Set all the blocks within the Cuboid to the given MaterialData, using a
     * MassBlockUpdate object for fast updates.
     *
     * @param mat
     *            the MaterialData to set
     * @param mbu
     *            the MassBlockUpdate object
     */

    /**
     * Reset the light level of all blocks within this Cuboid.
     */

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#clone()
     */
    public Cuboid clone() {
        return new Cuboid(this);
    }

    public AABB insetAABB() {
        return new AABB(x1, y1, z1, x2, y2, z2);
    }

    public enum CuboidDirection {

        North,
        East,
        South,
        West,
        Up,
        Down,
        Horizontal,
        Vertical,
        Both,
        Unknown;

        public CuboidDirection opposite() {
            switch (this) {
                case North:
                    return South;
                case East:
                    return West;
                case South:
                    return North;
                case West:
                    return East;
                case Horizontal:
                    return Vertical;
                case Vertical:
                    return Horizontal;
                case Up:
                    return Down;
                case Down:
                    return Up;
                case Both:
                    return Both;
                default:
                    return Unknown;
            }
        }
    }
}