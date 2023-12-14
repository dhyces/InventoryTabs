package com.kqp.inventorytabs.mixin;

import com.kqp.inventorytabs.interf.TabManagerContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "sendOpenInventory", at = @At("HEAD"))
    private void inventorytabs_sendOpenInventory(CallbackInfo ci) {
        ((TabManagerContainer)Minecraft.getInstance()).getTabManager().inventoryTabModified = true;
    }
}
