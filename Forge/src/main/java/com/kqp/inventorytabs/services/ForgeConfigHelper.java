package com.kqp.inventorytabs.services;

import com.kqp.inventorytabs.ForgeInventoryTabsConfig;

import java.util.List;

public class ForgeConfigHelper implements ConfigHelper {
    @Override
    public boolean doSightChecks() {
        return ForgeInventoryTabsConfig.doSightChecksFlag.get();
    }

    @Override
    public boolean shouldRotatePlayer() {
        return ForgeInventoryTabsConfig.rotatePlayer.get();
    }

    @Override
    public boolean shouldRenderTabs() {
        return ForgeInventoryTabsConfig.renderTabs.get();
    }

    @Override
    public boolean isDebug() {
        return ForgeInventoryTabsConfig.debugEnabled.get();
    }

    @Override
    public List<String> excludedTabs() {
        return (List<String>) ForgeInventoryTabsConfig.excludeTab.get();
    }

    @Override
    public List<String> includedTabs() {
        return (List<String>) ForgeInventoryTabsConfig.includeTab.get();
    }

    @Override
    public void setDoSightChecks(boolean val) {
        ForgeInventoryTabsConfig.doSightChecksFlag.set(val);
    }

    @Override
    public void setShouldRotatePlayer(boolean val) {
        ForgeInventoryTabsConfig.rotatePlayer.set(val);
    }

    @Override
    public void setShouldRenderTabs(boolean val) {
        ForgeInventoryTabsConfig.renderTabs.set(val);
    }

    @Override
    public void setIsDebug(boolean val) {
        ForgeInventoryTabsConfig.debugEnabled.set(val);
    }

    @Override
    public void setExcludedTabs(List<String> tabs) {
        ForgeInventoryTabsConfig.excludeTab.set(tabs);
    }

    @Override
    public void setIncludedTabs(List<String> tabs) {
        ForgeInventoryTabsConfig.includeTab.set(tabs);
    }
}
