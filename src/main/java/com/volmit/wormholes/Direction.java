package com.volmit.wormholes;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Directions
 *
 * @author cyberpwn
 */
public enum Direction {
    U(0, 1, 0, Cuboid.CuboidDirection.Up),
    D(0, -1, 0, Cuboid.CuboidDirection.Down),
    N(0, 0, -1, Cuboid.CuboidDirection.North),
    S(0, 0, 1, Cuboid.CuboidDirection.South),
    E(1, 0, 0, Cuboid.CuboidDirection.East),
    W(-1, 0, 0, Cuboid.CuboidDirection.West);

    private final int x;
    private final int y;
    private final int z;
    private final Cuboid.CuboidDirection f;

    Direction(int x, int y, int z, Cuboid.CuboidDirection f) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.f = f;
    }

    public static List<Direction> news() {
        List<Direction> d = new ArrayList<>();
        d.add(N);
        d.add(E);
        d.add(W);
        d.add(S);
        return d;
    }

    public static List<Direction> udnews() {
        List<Direction> d = new ArrayList<>();
        d.add(U);
        d.add(D);
        d.add(N);
        d.add(E);
        d.add(W);
        d.add(S);
        return d;
    }

    /**
     * Get the directional value from the given byte from common directional blocks
     * (MUST BE BETWEEN 0 and 5 INCLUSIVE)
     *
     * @param b the byte
     * @return the direction or null if the byte is outside of the inclusive range
     * 0-5
     */
    public static Direction fromByte(byte b) {
        if (b > 5 || b < 0) {
            return null;
        }

        if (b == 0) {
            return D;
        } else if (b == 1) {
            return U;
        } else if (b == 2) {
            return N;
        } else if (b == 3) {
            return S;
        } else if (b == 4) {
            return W;
        } else {
            return E;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case D:
                return "Down";
            case E:
                return "East";
            case N:
                return "North";
            case S:
                return "South";
            case U:
                return "Up";
            case W:
                return "West";
        }

        return "?";
    }

    public boolean isVertical() {
        return equals(D) || equals(U);
    }

    public boolean isCrooked(Direction to) {
        if (equals(to.reverse())) {
            return false;
        }

        return !equals(to);
    }

    public Direction reverse() {
        switch (this) {
            case D:
                return U;
            case E:
                return W;
            case N:
                return S;
            case S:
                return N;
            case U:
                return D;
            case W:
                return E;
            default:
                break;
        }

        return null;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public Cuboid.CuboidDirection f() {
        return f;
    }

    /**
     * Get the byte value represented in some directional blocks
     *
     * @return the byte value
     */
    public byte byteValue() {
        switch (this) {
            case D:
                return 0;
            case E:
                return 5;
            case N:
                return 2;
            case S:
                return 3;
            case U:
                return 1;
            case W:
                return 4;
            default:
                break;
        }

        return -1;
    }

    public BlockPos toVector() {
        return new BlockPos(x, y, z);
    }

    public net.minecraft.core.Direction.Axis getAxis() {
        switch (this) {
            case D:
                return net.minecraft.core.Direction.Axis.Y;
            case E:
                return net.minecraft.core.Direction.Axis.X;
            case N:
                return net.minecraft.core.Direction.Axis.Z;
            case S:
                return net.minecraft.core.Direction.Axis.Z;
            case U:
                return net.minecraft.core.Direction.Axis.Y;
            case W:
                return net.minecraft.core.Direction.Axis.X;
        }

        return null;
    }
}