package com.kqp.inventorytabs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.kqp.inventorytabs.init.InventoryTabs;
import com.kqp.inventorytabs.interf.TabManagerContainer;

import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "sendOpenInventory", at = @At("HEAD"))
    private void inventorytabs_sendOpenInventory(CallbackInfo ci) {
        ((TabManagerContainer)InventoryTabs.mc).getTabManager().inventoryTabModified = true;
    }
}
