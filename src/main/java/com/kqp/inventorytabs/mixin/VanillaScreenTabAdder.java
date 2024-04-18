package com.kqp.inventorytabs.mixin;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.kqp.inventorytabs.init.InventoryTabs;
import com.kqp.inventorytabs.init.InventoryTabsClient;
import com.kqp.inventorytabs.init.InventoryTabsConfig;
import com.kqp.inventorytabs.interf.TabManagerContainer;
import com.kqp.inventorytabs.tabs.TabManager;
import com.kqp.inventorytabs.tabs.render.TabRenderingHints;
import com.kqp.inventorytabs.tabs.tab.SimpleBlockTab;
import com.kqp.inventorytabs.tabs.tab.SimpleEntityTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import com.kqp.inventorytabs.util.ChestUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.fml.ModList;

@Mixin(AbstractContainerScreen.class)
public abstract class VanillaScreenTabAdder extends Screen implements TabRenderingHints {
    private static final boolean IS_BRB_LOADED = ModList.get().isLoaded("brb"); // Better Recipe Book compat
    
    protected VanillaScreenTabAdder(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void initRestoreStack(CallbackInfo callbackInfo) {
        Minecraft client = InventoryTabs.mc;
        TabManager tabManager = ((TabManagerContainer) client).getTabManager();
        if (tabManager.screenOpenedViaTab()) {
            tabManager.restoreCursorStack(client.gameMode, client.player, ((AbstractContainerScreen<?>) (Object) this).getMenu());
            tabManager.tabOpenedRecently = true; // Preserve value for later
        }
    }
    
    @Inject(method = "init", at = @At("RETURN"))
    private void initTabRenderer(CallbackInfo callbackInfo) {
        if (InventoryTabsClient.screenSupported(this)) {
            Minecraft client = InventoryTabs.mc;
            TabManager tabManager = ((TabManagerContainer) client).getTabManager();

            tabManager.onScreenOpen((AbstractContainerScreen<?>) (Object) this);

            if (tabManager.isResized) {
                tabManager.isResized = false;
                return;
            }

            Tab tabOpened = null;

            var thiz = (AbstractContainerScreen<?>) (Object) this;

            // We need to check if it's an inventory screen
            if (thiz instanceof InventoryScreen || tabManager.inventoryTabModified) {
                tabOpened = tabManager.tabs.get(0);
            } else if (!tabManager.screenOpenedViaTab()) { // Consumes flag
                // If the screen was NOT opened via tab,
                // check what block player is looking at for context

                if (client.hitResult instanceof BlockHitResult blockHitResult) {
                    var blockPos = blockHitResult.getBlockPos();

                    Set<BlockPos> matchingBlockPositions = new HashSet<>();
                    matchingBlockPositions.add(blockPos);

                    // For double chests
                    var world = client.level;
                    if (world.getBlockState(blockPos).getBlock() instanceof ChestBlock) {
                        if (ChestUtil.isDouble(world, blockPos)) {
                            matchingBlockPositions.add(ChestUtil.getOtherChestBlockPos(world, blockPos));
                        }
                    }

                    for (int i = 0; i < tabManager.tabs.size(); i++) {
                        Tab tab = tabManager.tabs.get(i);

                        if (tab instanceof SimpleBlockTab) {
                            if (matchingBlockPositions.contains(((SimpleBlockTab) tab).blockPos)) {
                                tabOpened = tab;
                                break;
                            }
                        }
                    }
                } else if (client.hitResult instanceof EntityHitResult entityHitResult) {
                    var entity = entityHitResult.getEntity();
                    for (int i = 0; i < tabManager.tabs.size(); i++) {
                        var tab = tabManager.tabs.get(i);
                        if (tab instanceof SimpleEntityTab entityTab) {
                            if (entity.getUUID().equals(entityTab.entity.getUUID())) {
                                tabOpened = tab;
                                break;
                            }
                        }
                    }
                }
            }

            if (tabOpened != null) {
                tabManager.onOpenTab(tabOpened);
            }
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    protected void drawBackgroundTabs(GuiGraphics gui, int mouseX, int mouseY, float delta,
            CallbackInfo callbackInfo) {
        if (InventoryTabsClient.shouldRenderTabs(this)) {
            if (!screenDoesDumbBlock()) {
                TabManager tabManager = TabManager.getInstance();

                tabManager.tabRenderer.renderBackground(gui);
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    protected void drawForegroundTabs(GuiGraphics gui, int mouseX, int mouseY, float delta,
                                      CallbackInfo callbackInfo) {
        if (InventoryTabsClient.shouldRenderTabs(this)) {
            TabManager tabManager = TabManager.getInstance();

            tabManager.tabRenderer.renderForeground(gui, mouseX, mouseY);
            tabManager.tabRenderer.renderHoverTooltips(gui, mouseX, mouseY);
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (InventoryTabsClient.shouldRenderTabs(this)) {
            TabManager tabManager = TabManager.getInstance();

            if (tabManager.mouseClicked(mouseX, mouseY, button)) {
                callbackInfo.setReturnValue(true);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> callbackInfo) {
        var disableKeyMatches = InventoryTabsClient.DISABLE_TABS_KEY_BIND.matches(keyCode, scanCode);
        if (disableKeyMatches) {
            InventoryTabsConfig.renderTabs.set(!InventoryTabsConfig.renderTabs.get());
        }
        if (InventoryTabsClient.shouldRenderTabs(this)) {
            TabManager tabManager = TabManager.getInstance();

            if (tabManager.keyPressed(keyCode, scanCode, modifiers)) {
                callbackInfo.setReturnValue(true);
            }
        }
    }


    @Override
    public int getTopRowXOffset() {
        if (!IS_BRB_LOADED) {
            AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;
            if (screen instanceof InventoryScreen) {
                if (((InventoryScreen) screen).getRecipeBookComponent().isVisible()) {
                    return 77;
                }
            } else if (screen instanceof AbstractFurnaceScreen) {
                if (((AbstractFurnaceScreen<?>) screen).recipeBookComponent.isVisible()) {
                    return 77;
                }
            } else if (screen instanceof CraftingScreen) {
                if (((CraftingScreen) screen).getRecipeBookComponent().isVisible()) {
                    return 77;
                }
            }
        }
        return 0;
    }

    @Override
    public int getBottomRowXOffset() {
        return getTopRowXOffset();
    }

    @Override
    public int getBottomRowYOffset() {
        return screenNeedsOffset() ? -1 : 0;
    }
    
    private boolean screenDoesDumbBlock() {
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;

        return screen instanceof CartographyTableScreen || screen instanceof LoomScreen
                || screen instanceof StonecutterScreen;
    }

    private boolean screenNeedsOffset() {
        AbstractContainerScreen<?> screen = (AbstractContainerScreen<?>) (Object) this;

        return screen instanceof ShulkerBoxScreen || screen instanceof ContainerScreen;
    }
}
