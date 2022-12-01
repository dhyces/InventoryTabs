package com.kqp.inventorytabs.mixin;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import com.kqp.inventorytabs.init.InventoryTabsClient;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin_SoftConflict {
	@Shadow @Final private static Map<String, KeyMapping> ALL;
	
	@Shadow private InputConstants.Key key;
	
	@Shadow private int clickCount;
	
	@Shadow public abstract void setDown(boolean pressed);
	
	@Shadow private boolean isDown;
	
	@Inject(method = "click", at = @At(value = "FIELD", target = "Lnet/minecraft/client/KeyMapping;clickCount:I"),
			locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private static void onKeyPressed(InputConstants.Key key, CallbackInfo ci, KeyMapping binding) {
		KeyMappingMixin_SoftConflict alternative = (KeyMappingMixin_SoftConflict) (Object) findAlternative(key, binding, InventoryTabsClient.NEXT_TAB_KEY_BIND);
		if(alternative != null) {
			alternative.clickCount++;
			ci.cancel();
		}
	}
	
	@Inject(method = "set", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;setDown(Z)V"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	private static void keyPressed(InputConstants.Key key, boolean pressed, CallbackInfo ci, Iterator<KeyMapping> var2, KeyMapping keymapping) {
		KeyMapping alternative = findAlternative(key, keymapping, InventoryTabsClient.NEXT_TAB_KEY_BIND);
		if(alternative != null) {
			alternative.setDown(pressed);
			ci.cancel();
		}
	}
	
	private static KeyMapping findAlternative(InputConstants.Key key, KeyMapping binding, KeyMapping alternativeTo) {
		Screen screen = Minecraft.getInstance().screen;
		if(binding == alternativeTo && !InventoryTabsClient.screenSupported(screen)) {
			for(KeyMapping value : ALL.values()) {
				KeyMappingMixin_SoftConflict self = (KeyMappingMixin_SoftConflict) (Object) value;
				InputConstants.Key bound = self.key;
				if(Objects.equals(bound, key) && value != alternativeTo) {
					return value;
				}
			}
		}
		return null;
	}
}
