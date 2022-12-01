package com.kqp.inventorytabs.init;

import com.kqp.inventorytabs.interf.TabManagerContainer;

import com.kqp.inventorytabs.tabs.TabManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public class InventoryTabsClient {
    public static final KeyMapping NEXT_TAB_KEY_BIND = new KeyMapping(
            "inventorytabs.key.next_tab", InputConstants.Type.KEYSYM, InputConstants.KEY_TAB, "key.categories.inventory");

    public static boolean serverDoSightCheckFlag = true;

    static void init() {
        // Handle state of tab managerInventoryTabsClient
        MinecraftForge.EVENT_BUS.addListener(InventoryTabsClient::onWorldLoad);
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
    
    public static boolean screenSupported(Screen screen) {
        return (screen instanceof AbstractContainerScreen<?>) && !(screen instanceof CreativeModeInventoryScreen);
    }
}
