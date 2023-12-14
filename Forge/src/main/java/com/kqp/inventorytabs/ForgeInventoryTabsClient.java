package com.kqp.inventorytabs;

import com.kqp.inventorytabs.init.InventoryTabsClient;
import com.kqp.inventorytabs.interf.TabManagerContainer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.concurrent.CompletableFuture;

public class ForgeInventoryTabsClient {


    public static boolean serverDoSightCheckFlag = true;

    static void init() {
        // Handle state of tab managerInventoryTabsClient
        MinecraftForge.EVENT_BUS.addListener(ForgeInventoryTabsClient::onWorldLoad);
        MinecraftForge.EVENT_BUS.addListener(ForgeInventoryTabsClient::onKeyPressed);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeInventoryTabsClient::onRegisterKeyMappings);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeInventoryTabsClient::onReloadAssets);
    }

    private static void onReloadAssets(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((pPreparationBarrier, pResourceManager, pPreparationsProfiler, pReloadProfiler, pBackgroundExecutor, pGameExecutor) -> {
            return CompletableFuture.runAsync(ForgeInventoryTabsClient::reloadTabs, pGameExecutor).thenCompose(pPreparationBarrier::wait);
        });
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            reloadTabs();
        }
    }

    private static void reloadTabs() {
        Minecraft client = Minecraft.getInstance();
        if (client.level != null) {
            if (client.screen != null) {
                TabManagerContainer tabManagerContainer = (TabManagerContainer) client;

                tabManagerContainer.getTabManager().update();
            }
        }
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(InventoryTabsClient.NEXT_TAB_KEY_BIND);
        event.register(InventoryTabsClient.DISABLE_TABS_KEY_BIND);
    }

    private static void onKeyPressed(InputEvent.Key event) {
        if (InventoryTabsClient.DISABLE_TABS_KEY_BIND.matches(event.getKey(), event.getScanCode())) {
            ForgeInventoryTabsConfig.renderTabs.set(InventoryTabsClient.DISABLE_TABS_KEY_BIND.consumeClick() != ForgeInventoryTabsConfig.renderTabs.get());
        }
    }
}
