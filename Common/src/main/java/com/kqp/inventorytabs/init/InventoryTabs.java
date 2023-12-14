package com.kqp.inventorytabs.init;

import com.kqp.inventorytabs.services.Services;
import net.minecraft.resources.ResourceLocation;

public class InventoryTabs {
    public static final String MODID = "inventorytabs";

    private static boolean bigInvLoaded;
    private static boolean playerExLoaded;
    private static boolean levelzLoaded;

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void init() {
        bigInvLoaded = Services.PLATFORM_HELPER.isModLoaded("biginv");
        playerExLoaded = Services.PLATFORM_HELPER.isModLoaded("playerex");
        levelzLoaded = Services.PLATFORM_HELPER.isModLoaded("levelz");
    }

    public static boolean isBigInvLoaded() {
        return bigInvLoaded;
    }

    public static boolean isPlayerExLoaded() {
        return playerExLoaded;
    }

    public static boolean isLevelzLoaded() {
        return levelzLoaded;
    }
}
