package com.volmit.util;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class SoundUtil {
    public static void play(ServerLevel level, Player obs, Vec3 v3, SoundEvent sound, float volume, float pitch) {
        level.playSound(obs, v3.x, v3.y, v3.z, sound, obs.getSoundSource(), volume, pitch);
    }
}
