package com.kqp.inventorytabs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.kqp.inventorytabs.init.InventoryTabs;
import com.kqp.inventorytabs.init.InventoryTabsClient;
import com.kqp.inventorytabs.interf.TabManagerContainer;
import com.kqp.inventorytabs.tabs.TabManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@Mixin(CartographyTableScreen.class)
public class CartographyTableScreenTabAdder {
    @Inject(method = "renderBg", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/screens/inventory/CartographyTableScreen;renderBackground(Lnet/minecraft/client/gui/GuiGraphics;)V"))
    protected void drawBackgroundTabs(GuiGraphics gui, float delta, int mouseX, int mouseY,
                                      CallbackInfo callbackInfo) {
        if (InventoryTabsClient.shouldRenderTabs((CartographyTableScreen)(Object)this)) {
            Minecraft client = InventoryTabs.mc;
            TabManager tabManager = ((TabManagerContainer) client).getTabManager();

            tabManager.tabRenderer.renderBackground(gui);
        }
    }
}
