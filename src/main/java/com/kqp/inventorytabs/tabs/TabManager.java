package com.kqp.inventorytabs.tabs;

import com.kqp.inventorytabs.api.TabProviderRegistry;
import com.kqp.inventorytabs.init.InventoryTabsClient;
import com.kqp.inventorytabs.interf.TabManagerContainer;
import com.kqp.inventorytabs.mixin.accessor.AbstractContainerScreenAccessor;
import com.kqp.inventorytabs.tabs.render.TabRenderInfo;
import com.kqp.inventorytabs.tabs.render.TabRenderer;
import com.kqp.inventorytabs.tabs.render.TabRenderingHints;
import com.kqp.inventorytabs.tabs.tab.PlayerInventoryTab;
import com.kqp.inventorytabs.tabs.tab.SimpleEntityTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import com.kqp.inventorytabs.util.MouseUtil;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.kqp.inventorytabs.init.InventoryTabs.*;

/**
 * Manages everything related to tabs.
 */
public class TabManager {
    public final List<Tab> tabs;
    public Tab currentTab;
    //private Comparator<Tab> currentSorter;

    private AbstractContainerScreen<?> currentScreen;
    public int currentPage = 0;
    public boolean tabOpenedRecently;
    public boolean inventoryTabModified; // used for when the player's inventory is controlled by the server, ie horse, chest boat
    public boolean isResized; // used when the mc window is resized so the current tab is not lost
    public int prevCursorStackSlot = -1;

    public final TabRenderer tabRenderer;

    public TabManager() {
        this.tabs = new ArrayList<>();
        this.tabRenderer = new TabRenderer(this);
    }

    public void update() {
        refreshAvailableTabs();

        tabRenderer.update();
    }

    public void setCurrentTab(Tab tab) {
        this.currentTab = tab;
    }
    public void removeTabs() {
        for (int i = 0; i < tabs.size(); i++) {
            tabs.remove(i);
            i--;
        }
    }

    private void refreshAvailableTabs() {
        // Remove old ones
        tabs.removeIf(Tab::shouldBeRemoved);

        AbstractClientPlayer player = Minecraft.getInstance().player;

        if (player != null && player.isAlive()) {
            // Add new tabs
            TabProviderRegistry.getTabProviders().forEach(tabProvider -> {
                tabProvider.addAvailableTabs(player, tabs);
            });
        }

        if (currentTab != null) {
            for (int i = 0; i < tabs.size(); i++) {
                Tab tab = tabs.get(i);
                if (currentTab != tab && currentTab.equals(tab)) {
                    // We've come across a tab we already have open
                    tabs.set(i, currentTab);
                    break;
                }
            }
        }

        // Sort
        tabs.sort(Comparator.<Tab, Integer>comparing(o -> o instanceof SimpleEntityTab ? -1 : 1).thenComparing(Tab::getPriority).reversed().thenComparing(tab -> tab.getHoverText().getString()));
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int guiWidth = ((AbstractContainerScreenAccessor) currentScreen).getImageWidth();
            int guiHeight = ((AbstractContainerScreenAccessor) currentScreen).getImageHeight();
            int x = (currentScreen.width - guiWidth) / 2;
            int y = (currentScreen.height - guiHeight) / 2;

            if (mouseX > x && mouseX < x + guiWidth && mouseY > y && mouseY < y + guiHeight) {
                return false;
            }

            // Check back button
            if (new Rectangle(x - TabRenderer.BUTTON_WIDTH - 4 + ((TabRenderingHints) currentScreen).getTopRowXOffset(), y - 16, TabRenderer.BUTTON_WIDTH,
                    TabRenderer.BUTTON_HEIGHT).contains(mouseX, mouseY)) {
                if (canGoBackAPage()) {
                    setCurrentPage(currentPage - 1);
                    playClick();

                    return true;
                }
            }

            // Check forward button
            if (new Rectangle(x + guiWidth + 4 + ((TabRenderingHints) currentScreen).getTopRowXOffset(), y - 16, TabRenderer.BUTTON_WIDTH, TabRenderer.BUTTON_HEIGHT)
                    .contains(mouseX, mouseY)) {
                if (canGoForwardAPage()) {
                    setCurrentPage(currentPage + 1);
                    playClick();

                    return true;
                }
            }

            TabRenderInfo[] tabRenderInfos = tabRenderer.getTabRenderInfos();

            for (int i = 0; i < tabRenderInfos.length; i++) {
                TabRenderInfo tabRenderInfo = tabRenderInfos[i];

                if (tabRenderInfo != null) {
                    if (tabRenderInfo.tabReference != currentTab) {
                        Rectangle rect = new Rectangle(tabRenderInfo.x, tabRenderInfo.y, tabRenderInfo.texW,
                                tabRenderInfo.texH);

                        if (rect.contains(mouseX, mouseY)) {
                            onTabClick(tabRenderInfo.tabReference);

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (InventoryTabsClient.NEXT_TAB_KEY_BIND.matches(keyCode, scanCode)) {
            int currentTabIndex = tabs.indexOf(currentTab);
            if (Screen.hasShiftDown()) {
                if (currentTabIndex > 0) {
                    onTabClick(tabs.get(currentTabIndex - 1));
                } else {
                    onTabClick(tabs.get(tabs.size() - 1));
                }
                return true;
            } else {
                if (currentTabIndex < tabs.size() - 1) {
                    onTabClick(tabs.get(currentTabIndex + 1));
                } else {
                    onTabClick(tabs.get(0));
                }

                return true;
            }
        }

        return false;
    }

    public void onScreenOpen(AbstractContainerScreen<?> screen) {
        refreshAvailableTabs();

        setCurrentScreen(screen);
        MouseUtil.tryPop();
    }

    public void restoreCursorStack(MultiPlayerGameMode manager, AbstractClientPlayer player, AbstractContainerMenu currentHandler) {
        // Try restore the cursor stack if it exists and wasn't dropped.
        if (manager!= null && this.prevCursorStackSlot != -1) {
            currentHandler.findSlot(player.getInventory(), this.prevCursorStackSlot).ifPresent((screenSlot) ->{
                manager.handleInventoryMouseClick(
                        currentHandler.containerId,
                        screenSlot,
                        0, // Mouse Left Click
                        ClickType.PICKUP,
                        player
                );
            });
            this.prevCursorStackSlot = -1;
        }
    }

    public void onTabClick(Tab tab) {
        // Push current mouse position
        // This is to persist mouse position across screens
        if (!Minecraft.getInstance().player.isCreative() || !(tab instanceof PlayerInventoryTab)) {
            MouseUtil.push();
        }

        // Set tab open flag
        tabOpenedRecently = true;

        Minecraft client = Minecraft.getInstance();
        AbstractContainerMenu handler = client.player.containerMenu;
        this.prevCursorStackSlot = -1;

        if (handler != null) {

            // Preserve the cursor stack
            ItemStack prevCursorStack = client.player.containerMenu.getCarried();
            if (prevCursorStack != null && !prevCursorStack.isEmpty()) {
                this.prevCursorStackSlot = client.player.getInventory().getFreeSlot();

                if (this.prevCursorStackSlot != -1 && client.gameMode != null) {
                    // Put the cursor stack there
                    handler.findSlot(client.player.getInventory(), this.prevCursorStackSlot).ifPresent((screenSlot) -> {
                        client.gameMode.handleInventoryMouseClick(
                                handler.containerId,
                                screenSlot,
                                InputConstants.MOUSE_BUTTON_LEFT,
                                ClickType.PICKUP,
                                client.player
                        );
                    });
                }
            }

            // Close any handled screens
            // This fixes the inventory desync issue
            client.getConnection().send(new ServerboundContainerClosePacket(handler.containerId));
        }

        // Open new tab
        onOpenTab(tab);
        tab.open();
    }

    public void onOpenTab(Tab tab) {
        if (currentTab != null && currentTab != tab) {
            inventoryTabModified = false;
            currentTab.onClose();
        }

        setCurrentTab(tab);
        setCurrentPage(pageOf(tab));
    }

    public int pageOf(Tab tab) {
        int index = tabs.indexOf(tab);
        if(isBigInvLoaded) {
            return index / (getMaxRowLength() * 2 + 5);
        } else if(isPlayerExLoaded || isLevelzLoaded) {
            return index / (getMaxRowLength() * 2 - 2);
        } else {
            return index / (getMaxRowLength() * 2);
        }
    }

    public int getMaxRowLength() {
        int guiWidth = ((AbstractContainerScreenAccessor) currentScreen).getImageWidth();
        int maxRowLength = guiWidth / (TabRenderer.TAB_WIDTH + 1);

        return maxRowLength;
    }

    public void setCurrentScreen(AbstractContainerScreen<?> screen) {
        this.currentScreen = screen;
    }

    public AbstractContainerScreen<?> getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentPage(int page) {
        int maxRowLength = getMaxRowLength() * 2;
        if (isPlayerExLoaded) {
            maxRowLength =- 3;
        } else if (isLevelzLoaded) {
            maxRowLength =- 2;
        }
        if (page > 0 && tabs.size() < maxRowLength) {
            System.err.println("Not enough tabs to paginate, ignoring");

            return;
        }

        if (this.currentPage != page) {
            tabRenderer.resetPageTextRefreshTime();
        }

        this.currentPage = page;
    }

    public boolean screenOpenedViaTab() {
        if (tabOpenedRecently) {
            tabOpenedRecently = false;

            return true;
        }

        return false;
    }

    public int getMaxPages() {
        if(isBigInvLoaded) {
            return tabs.size() / (getMaxRowLength() * 2 + 6);
        } else if(isPlayerExLoaded) {
            return tabs.size() / (getMaxRowLength() * 2 - 2);
        } else if(isLevelzLoaded) {
            return tabs.size() / (getMaxRowLength() * 2 - 1);
        } else {
            return tabs.size() / (getMaxRowLength() * 2 + 1);
        }
    }

    public boolean canGoBackAPage() {
        return currentPage != 0;
    }

    public boolean canGoForwardAPage() {
        return currentPage < getMaxPages();
    }

    public static TabManager getInstance() {
        return ((TabManagerContainer) Minecraft.getInstance()).getTabManager();
    }

    public static void playClick() {
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}

