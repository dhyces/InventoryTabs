package com.kqp.inventorytabs;

import com.kqp.inventorytabs.api.TabProviderRegistry;
import com.kqp.inventorytabs.init.InventoryTabs;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.InteractionResult;

public class FabricInventoryTabs implements ModInitializer {

    static ConfigHolder<FabricInventoryTabsConfig> inventoryTabsConfig;

    @Override
    public void onInitialize() {
        InventoryTabs.init();
        inventoryTabsConfig = AutoConfig.register(FabricInventoryTabsConfig.class, GsonConfigSerializer::new);
        inventoryTabsConfig.registerSaveListener((configHolder, config) -> {
            TabProviderRegistry.init("save");
            return InteractionResult.SUCCESS;
        });
        ClientLoginConnectionEvents.INIT.register((handler, client) -> TabProviderRegistry.init("load"));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> TabProviderRegistry.init("reload"));
    }

    public static FabricInventoryTabsConfig getConfig() {
        return AutoConfig.getConfigHolder(FabricInventoryTabsConfig.class).getConfig();
    }
}
