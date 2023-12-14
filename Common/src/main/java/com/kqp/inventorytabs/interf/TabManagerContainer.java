package com.kqp.inventorytabs.interf;

import com.kqp.inventorytabs.tabs.TabManager;

/**
 * Interface for holding the tab manager. Gets injected into
 * {@link net.minecraft.client.Minecraft}.
 */
public interface TabManagerContainer {
    TabManager getTabManager();
}
