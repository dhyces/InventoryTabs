package com.kqp.inventorytabs.init;

import com.kqp.inventorytabs.interf.TabManagerContainer;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class InventoryTabsClient {
    public static final KeyMapping NEXT_TAB_KEY_BIND = new KeyMapping(
            "inventorytabs.key.next_tab", InputConstants.Type.KEYSYM, InputConstants.KEY_TAB, "key.categories.inventory");

    public static final KeyMapping DISABLE_TABS_KEY_BIND = new KeyMapping(
            "inventorytabs.key.disable_tabs", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "key.categories.inventory");

    public static boolean serverDoSightCheckFlag = true;

    static void init() {
        // Handle state of tab managerInventoryTabsClient
        MinecraftForge.EVENT_BUS.addListener(InventoryTabsClient::onWorldLoad);
        MinecraftForge.EVENT_BUS.addListener(InventoryTabsClient::onKeyPressed);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(InventoryTabsClient::onRegisterKeyMappings);
    }

    private static void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld().isClientSide()) {
            Minecraft client = Minecraft.getInstance();

            if (client.screen != null) {
                TabManagerContainer tabManagerContainer = (TabManagerContainer) client;

                tabManagerContainer.getTabManager().update();
            }
        }
    }

    private static void onRegisterKeyMappings(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(NEXT_TAB_KEY_BIND);
            ClientRegistry.registerKeyBinding(DISABLE_TABS_KEY_BIND);
        });
    }

    private static void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (DISABLE_TABS_KEY_BIND.matches(event.getKey(), event.getScanCode())) {
            InventoryTabsConfig.renderTabs.set(DISABLE_TABS_KEY_BIND.consumeClick() != InventoryTabsConfig.renderTabs.get());
        }
    }
    
    public static boolean screenSupported(Screen screen) {
        return (screen instanceof AbstractContainerScreen<?>) && !(screen instanceof CreativeModeInventoryScreen);
    }

    public static <T extends Screen> boolean shouldRenderTabs(T screen) {
        return screenSupported(screen) && InventoryTabsConfig.renderTabs.get();
    }
}
