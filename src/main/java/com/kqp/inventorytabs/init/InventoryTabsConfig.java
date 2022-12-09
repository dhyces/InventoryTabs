package com.kqp.inventorytabs.init;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class InventoryTabsConfig {

    public static ForgeConfigSpec.BooleanValue doSightChecksFlag;
    public static ForgeConfigSpec.BooleanValue rotatePlayer;
    public static ForgeConfigSpec.ConfigValue<List<String>> excludeTab;
    public static ForgeConfigSpec.ConfigValue<List<String>> includeTab;
    public static ForgeConfigSpec.BooleanValue renderTabs;
    public static ForgeConfigSpec.BooleanValue debugEnabled;

    public static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Client");

        doSightChecksFlag = builder.define("doSightChecksFlag", () -> true);
        rotatePlayer = builder.define("rotatePlayer", () -> false);
        excludeTab = builder.define("excludeTab", List.of("tiered:reforging_station", "#techreborn:block_entities_without_inventories", "#inventorytabs:mod_compat_blacklist"));
        includeTab = builder.define("includeTab", List.of());
        renderTabs = builder.define("renderTabs", () -> true);
        debugEnabled = builder.define("debugEnabled", () -> false);
    }
}