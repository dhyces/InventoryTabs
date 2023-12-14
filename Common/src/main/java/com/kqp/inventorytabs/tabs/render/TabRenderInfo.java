package com.kqp.inventorytabs.tabs.render;

import com.kqp.inventorytabs.tabs.tab.Tab;

/**
 * Data class that describes how a tab should be rendered.
 */
public class TabRenderInfo {
    public Tab tabReference;
    public int index;
    public int x, y;
    public int texW, texH, texU, texV;
    public int itemX, itemY;
}
