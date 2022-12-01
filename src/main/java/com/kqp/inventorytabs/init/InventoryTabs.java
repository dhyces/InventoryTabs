package com.kqp.inventorytabs.init;

import com.kqp.inventorytabs.api.TabProviderRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(InventoryTabs.ID)
public class InventoryTabs {
    public static final String ID = "inventorytabs";

    public static boolean isBigInvLoaded;
    public static boolean isPlayerExLoaded;
    public static boolean isLevelzLoaded;

    public static ResourceLocation id(String path) {
        return new ResourceLocation(ID, path);
    }

    public InventoryTabs() {
        var spec = new ForgeConfigSpec.Builder();
        InventoryTabsConfig.setupConfig(spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, spec.build());

        isBigInvLoaded = ModList.get().isLoaded("biginv");
        isPlayerExLoaded = ModList.get().isLoaded("playerex");
        isLevelzLoaded = ModList.get().isLoaded("levelz");

        MinecraftForge.EVENT_BUS.addListener(this::playerJoin);
        MinecraftForge.EVENT_BUS.addListener(this::datapackReload);

        if (FMLLoader.getDist().isClient()) {
            InventoryTabsClient.init();
        }
    }

    public void onInitialize() {
//        ClientLoginConnectionEvents.INIT.register((handler, client) -> TabProviderRegistry.init("load"));
//        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> TabProviderRegistry.init("reload"));
    }

    private void playerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        TabProviderRegistry.init("load");
    }

    private void datapackReload(AddReloadListenerEvent event) {
//        TabProviderRegistry.init("reload"); //TODO: after datapacks are loaded
    }
}
