package com.volmit.wormholes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

public class FrameBlock extends Block {
    public static final int MAX_PORTAL_RADIUS = 6;
    public static final BooleanProperty FRAME_USE = BooleanProperty.create("worm");
    public static final IntegerProperty FRAME_REL_X = IntegerProperty.create("wormx", 0, (MAX_PORTAL_RADIUS * 2) + 1);
    public static final IntegerProperty FRAME_REL_Y = IntegerProperty.create("wormy", 0, (MAX_PORTAL_RADIUS * 2) + 1);
    public static final IntegerProperty FRAME_REL_Z = IntegerProperty.create("wormz", 0, (MAX_PORTAL_RADIUS * 2) + 1);

    public FrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FRAME_USE, false)
            .setValue(FRAME_REL_X, MAX_PORTAL_RADIUS)
                .setValue(FRAME_REL_Y, MAX_PORTAL_RADIUS)
                .setValue(FRAME_REL_Z, MAX_PORTAL_RADIUS));
    }

    public static int sign(int i) {
        return i - MAX_PORTAL_RADIUS;
    }

    public static int unsign(int i) {
        return i + MAX_PORTAL_RADIUS;
    }

    public static boolean isLinked(BlockState state) {
        return state.getValue(FRAME_USE);
    }

    public static BlockPos getLinkedPortal(BlockPos p, BlockState b) {
        return new BlockPos(
                p.getX() + sign(b.getValue(FRAME_REL_X)),
                p.getY() + sign(b.getValue(FRAME_REL_Y)),
                p.getZ() + sign(b.getValue(FRAME_REL_Z)));
    }

    public static BlockState linkPortal(BlockPos portalPos, BlockPos blockPos, BlockState state) {
        return state.setValue(FRAME_REL_X, unsign(portalPos.getX() - blockPos.getX()))
                .setValue(FRAME_REL_Y, unsign(portalPos.getY() - blockPos.getY()))
                .setValue(FRAME_REL_Z, unsign(portalPos.getZ() - blockPos.getZ()))
            .setValue(FRAME_USE, true);
    }

    public static void breakCheckLogic(ServerLevel level, BlockPos pos, BlockState state) {
        if(!isLinked(state)) {
            return;
        }

        BlockPos id = getLinkedPortal(pos, state);
        level.getEntitiesOfClass(Portal.class, new AABB(id).inflate(0.5), (f) -> true).forEach((e) -> {
            PortalManipulation.removeConnectedPortals(e, (px) -> {
            });
            e.remove(Entity.RemovalReason.KILLED);
        });
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME_REL_X, FRAME_REL_Y, FRAME_REL_Z, FRAME_USE);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pLevel.isClientSide()) {
            return;
        }

        breakCheckLogic((ServerLevel) pLevel, pPos, pState);
        super.destroy(pLevel, pPos, pState);
    }
}
