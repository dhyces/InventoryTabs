package com.kqp.inventorytabs.mixin;

import com.kqp.inventorytabs.init.InventoryTabsClient;
import com.kqp.inventorytabs.interf.TabManagerContainer;
import com.kqp.inventorytabs.tabs.TabManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(StonecutterScreen.class)
public class StonecutterScreenTabAdder {
    @Inject(method = "renderBg", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/screens/inventory/StonecutterScreen;renderBackground(Lcom/mojang/blaze3d/vertex/PoseStack;)V"))
    protected void drawBackgroundTabs(PoseStack poseStack, float delta, int mouseX, int mouseY,
                                      CallbackInfo callbackInfo) {
        if (InventoryTabsClient.shouldRenderTabs((StonecutterScreen)(Object)this)) {
            Minecraft client = Minecraft.getInstance();
            TabManager tabManager = ((TabManagerContainer) client).getTabManager();

            tabManager.tabRenderer.renderBackground(poseStack);
        }
    }
}
