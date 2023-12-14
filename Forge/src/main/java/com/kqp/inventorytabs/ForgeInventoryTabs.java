package com.kqp.inventorytabs;

import com.kqp.inventorytabs.api.TabProviderRegistry;
import com.kqp.inventorytabs.init.InventoryTabs;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(InventoryTabs.MODID)
public class ForgeInventoryTabs {

    public ForgeInventoryTabs() {
        InventoryTabs.init();

        ForgeConfigSpec.Builder spec = new ForgeConfigSpec.Builder();
        ForgeInventoryTabsConfig.setupConfig(spec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, spec.build());

        MinecraftForge.EVENT_BUS.addListener(this::playerJoin);
        MinecraftForge.EVENT_BUS.addListener(this::datapackReload);

        if (FMLLoader.getDist().isClient()) {
            ForgeInventoryTabsClient.init();
        }
    }

    public void onInitialize() {
//        ClientLoginConnectionEvents.INIT.register((handler, client) -> TabProviderRegistry.init("load"));
//        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> TabProviderRegistry.init("reload"));
    }

    private void playerJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        TabProviderRegistry.init("load");
    }

    private void datapackReload(OnDatapackSyncEvent event) {
//        TabProviderRegistry.init("reload"); //TODO: after datapacks are loaded
    }
}
