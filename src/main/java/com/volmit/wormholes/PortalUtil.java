package com.volmit.wormholes;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import qouteall.imm_ptl.core.McHelper;
import qouteall.imm_ptl.core.platform_specific.IPRegistry;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.nether_portal.BreakablePortalEntity;
import qouteall.q_misc_util.api.DimensionAPI;

public class PortalUtil {
    public static void createPortal(ServerLevel level) {
            Portal portal = new Portal((EntityType) IPRegistry.PORTAL.get(),level);
            McHelper.spawnServerEntity(portal);
    }
}
