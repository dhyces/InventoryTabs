package com.kqp.inventorytabs;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class ForgeInventoryTabsConfig {

    public static ForgeConfigSpec.BooleanValue doSightChecksFlag;
    public static ForgeConfigSpec.BooleanValue rotatePlayer;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> excludeTab;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> includeTab;
    public static ForgeConfigSpec.BooleanValue renderTabs;
    public static ForgeConfigSpec.BooleanValue debugEnabled;

    public static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Client");

        doSightChecksFlag = builder.define("doSightChecksFlag", () -> true);
        rotatePlayer = builder.define("rotatePlayer", () -> false);
        excludeTab = builder.defineList("excludeTab", Arrays.asList("tiered:reforging_station", "#inventorytabs:mod_compat_blacklist"), o -> true);
        includeTab = builder.defineList("includeTab", List.of(""), o -> true);
        renderTabs = builder.define("renderTabs", () -> true);
        debugEnabled = builder.define("debugEnabled", () -> false);
    }
}