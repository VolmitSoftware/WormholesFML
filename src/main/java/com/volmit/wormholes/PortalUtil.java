package com.volmit.wormholes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalExtension;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.HashSet;
import java.util.Set;

public class PortalUtil {

    public static boolean linkPortals(Player player, ServerLevel level, Direction dir1, String dim1, Cuboid c1, Direction dir2, String dim2, Cuboid c2) {
        Set<BlockPos> positions1 = new HashSet<>();
        Set<BlockPos> positions2 = new HashSet<>();
        ServerLevel l1 = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim1)));
        ServerLevel l2 = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim2)));

        for (BlockPos i : c1.getBlockPositions()) {
            if (ContentRegistry.blockList().contains(l1.getBlockState(i).getBlock())) {
                positions1.add(i);
            }
        }

        for (BlockPos i : c2.getBlockPositions()) {
            if (ContentRegistry.blockList().contains(l1.getBlockState(i).getBlock())) {
                positions2.add(i);
            }
        }

        c1 = c1.insetPortal(l1);
        c2 = c2.insetPortal(l2);
        AABB frame1 = fix(c1.aabb(), dir1, dir2);
        AABB frame2 = fix(c2.aabb(), dir2, dir1);
        Vec3 pos1 = frame1.getCenter();
        Vec3 pos2 = frame2.getCenter();
        Vec3 angle1 = new Vec3(dir1.getStepX(), dir1.getStepY(), dir1.getStepZ());
        Vec3 angle2 = new Vec3(dir2.getStepX(), dir2.getStepY(), dir2.getStepZ());
        DQuaternion q1 = getQuaternion(angle1, angle2, dir1.equals(dir2.getOpposite()));
        DQuaternion q2 = getQuaternion(angle2, angle1, dir2.equals(dir1.getOpposite()));
        Portal portal = IPRegistry.PORTAL.get().create(l1);
        PortalAPI.setPortalOrthodoxShape(portal, dir1, frame1);
        portal.setDestinationDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim2)));
        portal.setDestination(pos2);
        portal.setPos(pos1);
        portal.setRotationTransformationD(q1);

        Portal portal2 = IPRegistry.PORTAL.get().create(l2);
        PortalAPI.setPortalOrthodoxShape(portal2, dir2, frame2);
        portal2.setDestinationDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim1)));
        portal2.setDestination(pos1);
        portal2.setPos(pos2);
        portal2.setRotationTransformationD(q2);

        PortalExtension.get(portal).bindCluster = true;
        PortalExtension.get(portal2).bindCluster = true;

        McHelper.spawnServerEntity(configure(portal));
        McHelper.spawnServerEntity(configure(portal2));
        McHelper.spawnServerEntity(configure(PortalManipulation.createFlippedPortal(portal, (EntityType<Portal>) portal.getType())));
        McHelper.spawnServerEntity(configure(PortalManipulation.createFlippedPortal(portal2, (EntityType<Portal>) portal2.getType())));

        for (BlockPos i : positions1) {
            if (ContentRegistry.blockList().contains(l1.getBlockState(i).getBlock())) {
                BlockState state = l1.getBlockState(i);
                l1.setBlockAndUpdate(i, FrameBlock.linkPortal(ItemWand.computeDirection(i, new BlockPos((int) pos1.x(), (int) pos1.y(), (int) pos1.z()), null), state));
            }
        }

        for (BlockPos i : positions2) {
            if (ContentRegistry.blockList().contains(l2.getBlockState(i).getBlock())) {
                BlockState state = l2.getBlockState(i);
                l2.setBlockAndUpdate(i, FrameBlock.linkPortal(ItemWand.computeDirection(i, new BlockPos((int) pos2.x(), (int) pos2.y(), (int) pos2.z()), null), state));
            }
        }

        return true;
    }

    private static Portal configure(Portal p) {
        return p;
    }

    private static DQuaternion getQuaternion(Vec3 angle1, Vec3 angle2, boolean flip) {
        if (flip) {
            return DQuaternion.identity; //  DQuaternion.rotationByDegrees(angle1, 180);
        }

        Vec3 cross = angle1.cross(angle2);
        return new DQuaternion(cross.x(), cross.y(), cross.z(), Math.sqrt(
                (angle1.lengthSqr() * angle2.lengthSqr()) + angle1.dot(angle2)
        )).getNormalized();
    }

    private static AABB fix(AABB aabb, Direction direction, Direction otherDirection) {
        if (isPositive(direction)) {
            return aabb.move(direction.getStepX() * 0.5, direction.getStepY() * 0.5, direction.getStepZ() * 0.5);
        } else {
            return aabb.move(-direction.getStepX() * 0.5, -direction.getStepY() * 0.5, -direction.getStepZ() * 0.5);
        }
    }

    private static boolean isPositive(Direction direction) {
        return Math.max(Math.max(direction.getStepX(), direction.getStepY()), direction.getStepZ()) == 1;
    }

    private static Vec3 clean(Vec3 f) {
        return new Vec3((int) f.x(), (int) f.y(), (int) f.z());
    }
}
