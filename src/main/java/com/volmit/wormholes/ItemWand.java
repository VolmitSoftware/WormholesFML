package com.volmit.wormholes;

import com.volmit.util.SoundUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ItemWand extends Item {
    public ItemWand(Properties properties) {
        super(properties.stacksTo(1).fireResistant()
                .durability(86).rarity(Rarity.RARE));
    }

    public void clear(ItemStack item) {
        item.removeTagKey("wormholesdim");
        item.removeTagKey("wormholesframe");
        item.removeTagKey("wormholesdir");
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide()) {
            return super.useOn(pContext);
        }

        pContext.getPlayer().getCooldowns().addCooldown(this, 5);
        if (pContext.getPlayer().isCrouching()) {
            pContext.getPlayer().displayClientMessage(new TextComponent("Hole Applicator Cleared."), true);
            clear(pContext.getItemInHand());
            SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.AMETHYST_BLOCK_BREAK, 1f, 0.5f);
            SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.DEEPSLATE_BREAK, 1f, 0.8f);
            return super.useOn(pContext);
        }

        BlockPos pos = pContext.getClickedPos();

        if (ContentRegistry.blockList().contains(pContext.getLevel().getBlockState(pos).getBlock())) {
            Framer f = new Framer(pContext.getLevel(), pos);
            Cuboid c = f.validate();
            BlockPos playerPos = pContext.getPlayer().blockPosition();

            if (c != null) {
                if (hasData(pContext.getItemInHand())) {
                    if (PortalUtil.linkPortals(pContext.getPlayer(), (ServerLevel) pContext.getLevel(), getDirection(pContext.getItemInHand()), getDimension(pContext.getItemInHand()),
                            getCuboid(pContext.getItemInHand()), computeDirection(playerPos, c.getCenter(), c), pContext.getLevel().dimension().location().toString(), c.clone())) {
                        pContext.getPlayer().displayClientMessage(new TextComponent("Gateway Created!"), true);

                        clear(pContext.getItemInHand());
                        if (!pContext.getPlayer().isCreative()) {
                            pContext.getItemInHand().setDamageValue(pContext.getItemInHand().getDamageValue() + 1);
                        }
                        SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.BELL_RESONATE, 1f, 1.25f);
                        SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.BELL_RESONATE, 1f, 1.25f);
                        SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.END_PORTAL_SPAWN, 0.25f, 0.5f);
                    } else {
                        clear(pContext.getItemInHand());
                        SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.AMETHYST_BLOCK_BREAK, 1f, 0.2f);
                    }
                    return super.useOn(pContext);
                }

                pContext.getPlayer().displayClientMessage(new TextComponent("First Portal Frame Bound!"), true);
                setCuboid(pContext.getItemInHand(), c);
                setDimension(pContext.getItemInHand(), pContext.getLevel().dimension().location().toString());
                setDirection(pContext.getItemInHand(), computeDirection(playerPos, c.getCenter(), c));
                SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.DEEPSLATE_TILES_BREAK, 1f, 1.85f);
                SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.AMETHYST_BLOCK_PLACE, 1f, 0.5f);
            }
        }

        return super.useOn(pContext);
    }

    public boolean hasData(ItemStack stack) {
        return stack.hasTag() && getCuboid(stack) != null && getDimension(stack) != null && getDirection(stack) != null;
    }

    public void setDimension(ItemStack stack, String dim) {
        stack.getOrCreateTag().putString("wormholesdim", dim);
    }

    public String getDimension(ItemStack stack) {
        return stack.getOrCreateTag().getString("wormholesdim");
    }

    public static Direction computeDirection(BlockPos point, BlockPos toward, Cuboid cc) {
        Set<Direction> allowed = new HashSet<>();

        if(cc == null) {
            allowed.addAll(Arrays.stream(Direction.values()).toList());
        }else if (cc.getSizeY() == 1) {
            allowed.add(Direction.UP);
            allowed.add(Direction.DOWN);
        } else if (cc.getSizeX() == 1) {
            allowed.add(Direction.EAST);
            allowed.add(Direction.WEST);
        } else if (cc.getSizeZ() == 1) {
            allowed.add(Direction.NORTH);
            allowed.add(Direction.SOUTH);
        }

        BlockPos vec = toward.subtract(point);
        double x = vec.getX();
        double y = vec.getY();
        double z = vec.getZ();
        double l = Math.sqrt(x * x + y * y + z * z);
        x /= l;
        y /= l;
        z /= l;

        Vec3 v = new Vec3(x, y, z);

        double m = Double.MAX_VALUE;
        Direction d = Direction.NORTH;

        for (Direction i : allowed) {
            double dx = v.distanceTo(new Vec3(i.getStepX(), i.getStepY(), i.getStepZ()));

            if (dx < m) {
                m = dx;
                d = i;
            }
        }

        return d;
    }

    public Direction getDirection(ItemStack stack) {
        return Direction.from3DDataValue(stack.getOrCreateTag().getInt("wormholesdir"));
    }

    public void setDirection(ItemStack stack, Direction dir) {
        stack.getOrCreateTag().putInt("wormholesdir", dir.get3DDataValue());
    }

    public void setCuboid(ItemStack stack, Cuboid c) {
        stack.getOrCreateTag().putIntArray("wormholesframe", new int[]{
                c.getLowerX(),
                c.getLowerY(),
                c.getLowerZ(),
                c.getUpperX(),
                c.getUpperY(),
                c.getUpperZ(),
        });
    }

    public Cuboid getCuboid(ItemStack item) {
        try {
            int[] f = item.getTag().getIntArray("wormholesframe");
            return new Cuboid(f[0], f[1], f[2], f[3], f[4], f[5]);
        } catch (Throwable e) {
            return null;
        }
    }
}
