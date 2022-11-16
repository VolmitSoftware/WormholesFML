package com.volmit.wormholes.content;

import com.mojang.datafixers.util.Either;
import com.volmit.Wormholes;
import com.volmit.wormholes.utils.Cuboid;
import com.volmit.wormholes.utils.Framer;
import com.volmit.wormholes.utils.PortalUtil;
import com.volmit.wormholes.utils.SoundUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Wormholes.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemWand extends Item {
    public ItemWand(Properties properties) {
        super(properties.stacksTo(1).fireResistant()
                .durability(86).rarity(Rarity.EPIC));
    }

    @SubscribeEvent
    public static void onTooltipGather(RenderTooltipEvent.GatherComponents e) {
        if (e.getItemStack().getItem() instanceof ItemWand) {
            e.getTooltipElements().add(Either.left(new TextComponent("§5Right-Click§7 to select/bind a frame")));
            e.getTooltipElements().add(Either.left(new TextComponent("§5Sneak-Right-Click§7 to rotate the portal")));
            e.getTooltipElements().add(Either.left(new TextComponent("§7§oRotates the portal §5180°§7,§o Based on Looking Direction")));
        }
    }

    public static Direction computeDirection(BlockPos point, BlockPos toward, Cuboid cc) {
        Set<Direction> allowed = new HashSet<>();

        if (cc == null) {
            allowed.addAll(Arrays.stream(Direction.values()).toList());
        } else if (cc.getSizeY() == 1) {
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

    @NotNull
    public static EntityHitResult getNearestEntity(Player player) {
        float playerRotX = player.getXRot();
        float playerRotY = player.getYRot();
        Vec3 startPos = player.getEyePosition();
        float f2 = Mth.cos(-playerRotY * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-playerRotY * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-playerRotX * ((float) Math.PI / 180F));
        float additionY = Mth.sin(-playerRotX * ((float) Math.PI / 180F));
        float additionX = f3 * f4;
        float additionZ = f2 * f4;
        double d0 = 15;
        Vec3 endVec = startPos.add((double) additionX * d0, (double) additionY * d0, (double) additionZ * d0);
        AABB startEndBox = new AABB(startPos, endVec);
        Entity entity = player;
        for (Entity entity1 : player.level.getEntities(player, startEndBox, (val) -> true)) {
            AABB aabb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
            Optional<Vec3> optional = aabb.clip(startPos, endVec);
            if (aabb.contains(startPos)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    startPos = optional.orElse(startPos);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startPos.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == player.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            startPos = vec31;
                        }
                    } else {
                        entity = entity1;
                        startPos = vec31;
                        d0 = d1;
                    }
                }
            }
        }
        if (entity == player) {
            return new EntityHitResult(player);
        } else {
            return new EntityHitResult(entity);
        }
    }

    public void clear(ItemStack item) {
        item.removeTagKey("wormholesdim");
        item.removeTagKey("wormholesframe");
        item.removeTagKey("wormholesdir");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) {
            return super.use(pLevel, pPlayer, pUsedHand);
        }
        if (pPlayer.isCrouching() && getNearestEntity(pPlayer).getEntity() instanceof Portal p) {
            if (pLevel.getServer() != null && pPlayer.getLevel().getServer() != null) {

                if (p.getDestDim().location().getNamespace().contains("wormholes") || p.getOriginDim().location().getNamespace().contains("wormholes")) {
                    SoundUtil.play((ServerLevel) pPlayer.getLevel(), pPlayer.position(), SoundEvents.ENDER_CHEST_OPEN, 1f, 3.25f);
                    return super.use(pLevel, pPlayer, pUsedHand);
                }

                System.out.println("Rotating portal: " + pPlayer.getLookAngle().get(Direction.Axis.Y));
                double yaw = pPlayer.getLookAngle().get(Direction.Axis.Y) * 100;

                if (yaw > 75 || yaw < -75) {
                    System.out.println("Rotating portal on the X axis");
                    pLevel.getServer().getCommands().performCommand(new CommandSourceStack(pPlayer, pPlayer.getEyePosition(), pPlayer.getRotationVector(), (ServerLevel) pPlayer.getLevel(), 4, "Wormhole", pPlayer.getDisplayName(), pPlayer.getLevel().getServer(), pPlayer), "/portal rotate_portal_rotation_along x 180");
                    SoundUtil.play((ServerLevel) pPlayer.getLevel(), pPlayer.position(), SoundEvents.CHEST_CLOSE, 1f, 3.25f);
                } else {
                    System.out.println("Rotating portal on the Z axis");
                    pLevel.getServer().getCommands().performCommand(new CommandSourceStack(pPlayer, pPlayer.getEyePosition(), pPlayer.getRotationVector(), (ServerLevel) pPlayer.getLevel(), 4, "Wormhole", pPlayer.getDisplayName(), pPlayer.getLevel().getServer(), pPlayer), "/portal rotate_portal_rotation_along y 180");
                    SoundUtil.play((ServerLevel) pPlayer.getLevel(), pPlayer.position(), SoundEvents.CHEST_CLOSE, 1f, 3.25f);
                }
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide()) {
            return super.useOn(pContext);
        }

        if (pContext.getPlayer() == null) {
            return super.useOn(pContext);
        }

        if (getNearestEntity(pContext.getPlayer()).getEntity() instanceof Portal) {
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
                        SoundUtil.play((ServerLevel) pContext.getLevel(), pContext.getPlayer().position(), SoundEvents.BELL_RESONATE, 1f, 0.9f);
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
