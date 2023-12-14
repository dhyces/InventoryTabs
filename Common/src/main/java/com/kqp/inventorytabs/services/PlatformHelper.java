package com.kqp.inventorytabs.services;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;

public interface PlatformHelper {
    boolean isModLoaded(String modid);

    double getReachDistance(Player player);
}
