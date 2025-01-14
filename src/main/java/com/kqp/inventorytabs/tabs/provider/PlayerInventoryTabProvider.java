package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.PlayerInventoryTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;

import java.util.List;

public class PlayerInventoryTabProvider implements TabProvider {
    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        for (Tab tab : tabs) {
            if (tab instanceof PlayerInventoryTab) {
                return;
            }
        }
        tabs.add(new PlayerInventoryTab());
    }
}
