package com.kqp.inventorytabs;

import com.kqp.inventorytabs.init.InventoryTabsClient;
import com.kqp.inventorytabs.interf.TabManagerContainer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;

public class FabricInventoryTabsClient implements ClientModInitializer {
    public static boolean serverDoSightCheckFlag = true;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_WORLD_TICK.register(world -> {
            Minecraft client = Minecraft.getInstance();

            if (client.screen != null) {
                TabManagerContainer tabManagerContainer = (TabManagerContainer) client;

                tabManagerContainer.getTabManager().update();
            }
        });

        KeyBindingHelper.registerKeyBinding(InventoryTabsClient.NEXT_TAB_KEY_BIND);
        KeyBindingHelper.registerKeyBinding(InventoryTabsClient.DISABLE_TABS_KEY_BIND);
    }
}
