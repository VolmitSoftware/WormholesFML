package com.volmit.wormholes;

import com.mojang.math.Quaternion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.Mirror;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.imm_ptl.core.portal.PortalState;
import qouteall.imm_ptl.core.portal.nether_portal.BreakablePortalEntity;
import qouteall.imm_ptl.core.render.PortalGroup;
import qouteall.q_misc_util.api.DimensionAPI;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PortalUtil {
    public static boolean linkPortals(Player player, ServerLevel level, Direction dir1, String dim1, Cuboid c1, Direction dir2, String dim2, Cuboid c2) {
        Set<BlockPos> positions1 = new HashSet<>();
        Set<BlockPos> positions2 = new HashSet<>();
        ServerLevel l1 = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim2)));
        ServerLevel l2 = level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim2)));
        for(BlockPos i : c1.getBlockPositions()) {
            if(l1.getBlockState(i).getBlock().equals(Content.Blocks.FRAME.get())) {
                positions1.add(i);
            }
        }

        for(BlockPos i : c2.getBlockPositions()) {
            if(l1.getBlockState(i).getBlock().equals(Content.Blocks.FRAME.get())) {
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
        Vec3 cross1 = angle1.cross(angle2);
        Vec3 cross2 = angle2.cross(angle1);
        DQuaternion q1 = new DQuaternion(cross1.x(), cross1.y(), cross1.z(), Math.sqrt(
            (angle1.lengthSqr() * angle2.lengthSqr()) + angle1.dot(angle2)
        )).getNormalized();
        DQuaternion q2 = new DQuaternion(cross2.x(), cross2.y(), cross2.z(), Math.sqrt(
            (angle1.lengthSqr() * angle2.lengthSqr()) + angle2.dot(angle1)
        )).getNormalized();
        Portal portal =  IPRegistry.PORTAL.get().create(l1);
        PortalAPI.setPortalOrthodoxShape(portal, dir1, frame1);
        portal.setDestinationDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim2)));
        portal.setDestination(pos2);
        portal.setPos(pos1);
        portal.setRotationTransformationD(q1);
        Portal portal2 =  IPRegistry.PORTAL.get().create(l2);
        PortalAPI.setPortalOrthodoxShape(portal2, dir2, frame2);
        portal2.setDestinationDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim1)));
        portal2.setDestination(pos1);
        portal2.setPos(pos2);
        portal2.setRotationTransformationD(q2);
        PortalManipulation.completeBiFacedPortal(portal2, (EntityType<Portal>) portal.getType());
        PortalManipulation.completeBiFacedPortal(portal, (EntityType<Portal>) portal.getType());
        McHelper.spawnServerEntity(portal2);
        McHelper.spawnServerEntity(portal);

        for(BlockPos i : positions1) {
            if(l1.getBlockState(i).getBlock().equals(Content.Blocks.FRAME.get())) {
                BlockState state = l1.getBlockState(i);
                l1.setBlockAndUpdate(i, FrameBlock.linkPortal(new BlockPos((int)pos1.x(),(int) pos1.y(), (int)pos1.z()), i, state));
            }
        }

        for(BlockPos i : positions2) {
            if(l2.getBlockState(i).getBlock().equals(Content.Blocks.FRAME.get())) {
                BlockState state = l2.getBlockState(i);
                l2.setBlockAndUpdate(i, FrameBlock.linkPortal(new BlockPos((int)pos2.x(),(int) pos2.y(), (int)pos2.z()), i, state));
            }
        }

        return true;
    }

    private static AABB fix(AABB aabb, Direction direction, Direction otherDirection)
    {
        if(isPositive(direction))
        {
            return aabb.move(direction.getStepX() * 0.5, direction.getStepY() * 0.5, direction.getStepZ() * 0.5);
        }

        else {
            return aabb.move(-direction.getStepX() * 0.5, -direction.getStepY() * 0.5, -direction.getStepZ() * 0.5);
        }
    }

    private static boolean isPositive(Direction direction)
    {
        return Math.max(Math.max(direction.getStepX(), direction.getStepY()), direction.getStepZ()) == 1;
    }

    private static Vec3 clean(Vec3 f)
    {
        return new Vec3((int)f.x(), (int)f.y(), (int)f.z());
    }
}
