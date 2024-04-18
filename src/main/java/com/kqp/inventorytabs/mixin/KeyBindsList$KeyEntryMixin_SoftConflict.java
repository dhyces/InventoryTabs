package com.kqp.inventorytabs.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.kqp.inventorytabs.init.InventoryTabsClient;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.controls.KeyBindsList;

/**
 * The 'Tab' KeyMapping conflicts with the multiplayer player list keybind, but since you can only see the player list when outside the inventory
 * anyways, the conflict can be soft and not hard.
 */
@Mixin(KeyBindsList.KeyEntry.class)
public class KeyBindsList$KeyEntryMixin_SoftConflict {
	@Shadow @Final private KeyMapping key;
	
	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"), index = 4)
	public int setColor(int color) {
		if(color == ChatFormatting.RED.getColor() && key == InventoryTabsClient.NEXT_TAB_KEY_BIND) {
			color = ChatFormatting.GOLD.getColor();
		}
		return color;
	}
}
