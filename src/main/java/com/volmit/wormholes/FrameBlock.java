package com.volmit.wormholes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

import java.util.UUID;

public class FrameBlock extends Block {
    public static final LongProperty FRAME_MSB = LongProperty.create("worm_msb", Long.MIN_VALUE, Long.MAX_VALUE);
    public static final LongProperty FRAME_LSB = LongProperty.create("worm_lsb", Long.MIN_VALUE, Long.MAX_VALUE);

    public FrameBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(FRAME_MSB, 0L)
            .setValue(FRAME_LSB, 0L));
    }

    public static UUID getLinkedPortal(BlockState b) {
        long lsb = b.getValue(FRAME_LSB);
        long msb = b.getValue(FRAME_MSB);


        if(lsb == 0 && msb == 0) {
            return null;
        }

        return new UUID(msb, lsb);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FRAME_MSB, FRAME_LSB);
    }

    public static void linkPortal(UUID id, BlockState state) {
        state.setValue(FRAME_MSB, id.getMostSignificantBits());
        state.setValue(FRAME_LSB, id.getLeastSignificantBits());
    }

    public static void unlinkPortal(BlockState state) {
        state.setValue(FRAME_MSB, 0L);
        state.setValue(FRAME_LSB, 0L);
    }

    public static void breakCheckLogic(ServerLevel level, BlockPos pos, BlockState state) {
        UUID id = getLinkedPortal(state);

        if(id != null) {
            Entity p = level.getEntity(id);

            if(p instanceof Portal r) {
                PortalManipulation.removeConnectedPortals(r, (px) -> {});
                r.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if(pLevel.isClientSide()) {
            return;
        }

        breakCheckLogic((ServerLevel) pLevel, pPos, pState);
        super.destroy(pLevel, pPos, pState);
    }
}
