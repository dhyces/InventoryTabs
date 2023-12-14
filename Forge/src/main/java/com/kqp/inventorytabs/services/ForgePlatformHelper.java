package com.kqp.inventorytabs.services;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

public class ForgePlatformHelper implements PlatformHelper {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public double getReachDistance(Player player) {
        return player.getReachDistance();
    }
}
