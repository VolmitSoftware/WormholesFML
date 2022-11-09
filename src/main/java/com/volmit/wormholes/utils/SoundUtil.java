package com.volmit.wormholes.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class SoundUtil {
    public static void play(ServerLevel level, Vec3 v3, SoundEvent sound, float volume, float pitch) {
        level.playSound(null, v3.x, v3.y, v3.z, sound, SoundSource.BLOCKS, volume, pitch);
    }
}
