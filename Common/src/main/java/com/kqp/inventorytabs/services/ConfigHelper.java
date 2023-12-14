package com.kqp.inventorytabs.services;

import java.util.List;

public interface ConfigHelper {
    boolean doSightChecks();
    boolean shouldRotatePlayer();
    boolean shouldRenderTabs();
    boolean isDebug();
    List<String> excludedTabs();
    List<String> includedTabs();

    void setDoSightChecks(boolean val);
    void setShouldRotatePlayer(boolean val);
    void setShouldRenderTabs(boolean val);
    void setIsDebug(boolean val);
    void setExcludedTabs(List<String> tabs);
    void setIncludedTabs(List<String> tabs);
}
