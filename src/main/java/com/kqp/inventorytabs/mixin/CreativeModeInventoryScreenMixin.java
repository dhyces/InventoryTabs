package com.kqp.inventorytabs.mixin;

import com.kqp.inventorytabs.interf.TabManagerContainer;
import com.kqp.inventorytabs.tabs.TabManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin {

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void mouseReleased(double pMouseX, double pMouseY, int pButton, CallbackInfoReturnable<Boolean> cir) {
        TabManager tabManager = ((TabManagerContainer) Minecraft.getInstance()).getTabManager();
        if (tabManager.screenOpenedViaTab()) {
            cir.setReturnValue(true);
        }
    }
}