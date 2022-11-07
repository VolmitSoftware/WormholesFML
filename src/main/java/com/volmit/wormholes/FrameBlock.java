package com.volmit.wormholes;

import com.volmit.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

import java.util.concurrent.atomic.AtomicBoolean;

public class FrameBlock extends Block {
    public static final int MAX_PORTAL_RADIUS = 6;
    public static final DirectionProperty FRAME_INNER = DirectionProperty.create("wormdir");
    public static final BooleanProperty FRAME_ACTIVE = BooleanProperty.create("wormset");

    public FrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FRAME_INNER, Direction.UP)
                .setValue(FRAME_ACTIVE, false));
    }

    public static int sign(int i) {
        return i - MAX_PORTAL_RADIUS;
    }

    public static int unsign(int i) {
        return i + MAX_PORTAL_RADIUS;
    }

    public static boolean isLinked(BlockState state) {
        return state.getValue(FRAME_ACTIVE);
    }

    public static BlockPos getLinkedPortal(BlockPos p, BlockState b) {
        return new BlockPos(
                p.getX() + b.getValue(FRAME_INNER).getStepX(),
                p.getY() + b.getValue(FRAME_INNER).getStepY(),
                p.getZ() + b.getValue(FRAME_INNER).getStepZ());
    }

    public static BlockState linkPortal(Direction dir, BlockState state) {
        return state.setValue(FRAME_ACTIVE, true).setValue(FRAME_INNER, dir);
    }

    public static void breakCheckLogic(ServerLevel level, BlockPos pos, BlockState state) {
        if (!isLinked(state)) {
            return;
        }

        BlockPos id = getLinkedPortal(pos, state);
        AtomicBoolean removed = new AtomicBoolean(false);
        level.getEntitiesOfClass(Portal.class, new AABB(id).inflate(0.5), (f) -> true).forEach((e) -> {
            PortalManipulation.removeConnectedPortals(e, (px) -> {
            });
            removed.set(true);
            e.remove(Entity.RemovalReason.KILLED);
        });

        if (removed.get()) {
            SoundUtil.play(level, new AABB(id).getCenter(), SoundEvents.IRON_GOLEM_DEATH, 0.25f, 0.25f);
            SoundUtil.play(level, new AABB(id).getCenter(), SoundEvents.CONDUIT_DEACTIVATE, 1f, 0.25f);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME_ACTIVE, FRAME_INNER);
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
