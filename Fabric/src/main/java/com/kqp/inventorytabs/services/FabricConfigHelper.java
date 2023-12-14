package com.kqp.inventorytabs.services;

import com.kqp.inventorytabs.FabricInventoryTabs;

import java.util.List;

public class FabricConfigHelper implements ConfigHelper {
    @Override
    public boolean doSightChecks() {
        return FabricInventoryTabs.getConfig().doSightChecksFlag;
    }

    @Override
    public boolean shouldRotatePlayer() {
        return FabricInventoryTabs.getConfig().rotatePlayer;
    }

    @Override
    public boolean shouldRenderTabs() {
        return FabricInventoryTabs.getConfig().shouldRenderTabs;
    }

    @Override
    public boolean isDebug() {
        return FabricInventoryTabs.getConfig().debugEnabled;
    }

    @Override
    public List<String> excludedTabs() {
        return FabricInventoryTabs.getConfig().excludeTab;
    }

    @Override
    public List<String> includedTabs() {
        return FabricInventoryTabs.getConfig().includeTab;
    }

    @Override
    public void setDoSightChecks(boolean val) {
        FabricInventoryTabs.getConfig().doSightChecksFlag = val;
    }

    @Override
    public void setShouldRotatePlayer(boolean val) {
        FabricInventoryTabs.getConfig().rotatePlayer = val;
    }

    @Override
    public void setShouldRenderTabs(boolean val) {
        FabricInventoryTabs.getConfig().shouldRenderTabs = val;
    }

    @Override
    public void setIsDebug(boolean val) {
        FabricInventoryTabs.getConfig().debugEnabled = val;
    }

    @Override
    public void setExcludedTabs(List<String> tabs) {
        FabricInventoryTabs.getConfig().excludeTab = tabs;
    }

    @Override
    public void setIncludedTabs(List<String> tabs) {
        FabricInventoryTabs.getConfig().includeTab = tabs;
    }
}
