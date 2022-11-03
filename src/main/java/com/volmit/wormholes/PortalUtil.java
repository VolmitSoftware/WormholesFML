package com.volmit.wormholes;

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
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.nether_portal.BreakablePortalEntity;
import qouteall.q_misc_util.api.DimensionAPI;

public class PortalUtil {
    public static boolean linkPortals(Player player, ServerLevel level, Direction dir1, String dim1, Cuboid c1, Direction dir2, String dim2, Cuboid c2)
    {
        player.sendMessage(new TextComponent("Creating"), player.getUUID());
        Portal portal = new Portal(IPRegistry.PORTAL.get(),level);
        PortalAPI.setPortalOrthodoxShape(portal, dir1, c1.insetAABB());
        McHelper.spawnServerEntity(portal);
        Portal portal2 = new Portal(IPRegistry.PORTAL.get(),level);
        PortalAPI.setPortalOrthodoxShape(portal2, dir2, c2.insetAABB());
        McHelper.spawnServerEntity(portal2);
        portal.setDestinationDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim2)));
        portal2.setDestinationDimension(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim1)));
        player.sendMessage(new TextComponent("Created"), player.getUUID());
        return true;
    }

    public static void createPortal(ServerLevel level) {
            Portal portal = new Portal((EntityType) IPRegistry.PORTAL.get(),level);
            McHelper.spawnServerEntity(portal);
    }
}
