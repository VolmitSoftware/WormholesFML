package com.volmit.wormholes;

import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Framer {
    private Set<BlockPos> positionsFound = new HashSet<>();
    private Set<BlockPos> positionsChecked = new HashSet<>();
    private BlockPos cursor;
    private Level level;
    private boolean done = false;

    public Framer(Level level, BlockPos cursor) {
        this.level = level;
        this.cursor = cursor;
    }

    List<BlockPos> check = List.of(
        new BlockPos(1, 0, 0),
        new BlockPos(-1, 0, 0),
        new BlockPos(0, 1, 0),
        new BlockPos(0, -1, 0),
        new BlockPos(0, 0, 1),
        new BlockPos(0, 0, -1)
    );

    public Framer tick(int amt) {
        for(int i = 0; i < amt; i++) {
            if(done) {
                return this;
            }

            tick();
        }

        return this;
    }

    public Cuboid validate() {
        if(!done) {
            return null;
        }

        Comparator<BlockPos> comp = (a, b) -> {
            if(a.getX() < b.getX()) {
                return -1;
            }

            if(a.getX() > b.getX()) {
                return 1;
            }

            if(a.getY() < b.getY()) {
                return -1;
            }

            if(a.getY() > b.getY()) {
                return 1;
            }

            if(a.getZ() < b.getZ()) {
                return -1;
            }

            if(a.getZ() > b.getZ()) {
                return 1;
            }

            return 0;
        };

        BlockPos min = positionsFound.stream().min(comp).orElse(null);
        BlockPos max = positionsFound.stream().max(comp).orElse(null);
        Cuboid c = new Cuboid(min, max);
       return (isValid(c.getFace(Cuboid.CuboidDirection.North))
           && isValid(c.getFace(Cuboid.CuboidDirection.South))
           && isValid(c.getFace(Cuboid.CuboidDirection.East))
           && isValid(c.getFace(Cuboid.CuboidDirection.West))) ||
           (isValid(c.getFace(Cuboid.CuboidDirection.North))
               && isValid(c.getFace(Cuboid.CuboidDirection.South))
               && isValid(c.getFace(Cuboid.CuboidDirection.Up))
               && isValid(c.getFace(Cuboid.CuboidDirection.Down))) ||
           (isValid(c.getFace(Cuboid.CuboidDirection.Up))
               && isValid(c.getFace(Cuboid.CuboidDirection.Down))
               && isValid(c.getFace(Cuboid.CuboidDirection.East))
               && isValid(c.getFace(Cuboid.CuboidDirection.West))) ? c : null;
    }

    private boolean isValid(Cuboid frame) {
        for(int i = frame.getLowerNE().getX(); i <= frame.getUpperSW().getX(); i++) {
            for(int j = frame.getLowerNE().getY(); j <= frame.getUpperSW().getY(); j++) {
                for(int k = frame.getLowerNE().getZ(); k <= frame.getUpperSW().getZ(); k++) {
                    if(!positionsFound.contains(new BlockPos(i, j, k))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void tick() {
        if(done) {
            return;
        }

        int checked = positionsChecked.size();

        for(BlockPos i : check) {
            BlockPos o = cursor.offset(i);
            if(!positionsChecked.contains(o)) {
                positionsChecked.add(o);
                if(level.getBlockState(o).getBlock().equals(Content.Blocks.FRAME.get())) {
                    positionsFound.add(o);
                    cursor = o;
                    return;
                }
            }
        }

        if(checked == positionsChecked.size()) {
            done = true;
        }
    }
}
