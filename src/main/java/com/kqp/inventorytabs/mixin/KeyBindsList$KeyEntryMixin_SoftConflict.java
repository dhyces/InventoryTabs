package com.kqp.inventorytabs.mixin;

import com.kqp.inventorytabs.init.InventoryTabsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

/**
 * The 'Tab' KeyMapping conflicts with the multiplayer player list keybind, but since you can only see the player list when outside the inventory
 * anyways, the conflict can be soft and not hard.
 */
@Mixin(KeyBindsList.KeyEntry.class)
public class KeyBindsList$KeyEntryMixin_SoftConflict {
	@Shadow @Final private KeyMapping key;
	
	@ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;setMessage(Lnet/minecraft/network/chat/Component;)V"))
	public Component setMessage(Component text) {
		TextColor c = text.getStyle().getColor();
		if(c != null && c.getValue() == Objects.requireNonNull(ChatFormatting.RED.getColor())) {
			if(this.key == InventoryTabsClient.NEXT_TAB_KEY_BIND) {
				text = text.copy().withStyle(ChatFormatting.GOLD);
			}
		}
		return text;
	}
}
