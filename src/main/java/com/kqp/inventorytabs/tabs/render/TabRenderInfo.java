package com.kqp.inventorytabs.tabs.render;

import com.kqp.inventorytabs.tabs.tab.Tab;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Data class that describes how a tab should be rendered.
 */
@OnlyIn(Dist.CLIENT)
public class TabRenderInfo {
    public Tab tabReference;
    public int index;
    public int x, y;
    public int texW, texH, texU, texV;
    public int itemX, itemY;
}
