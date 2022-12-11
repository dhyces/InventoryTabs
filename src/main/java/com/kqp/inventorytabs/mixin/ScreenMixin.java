package com.kqp.inventorytabs.mixin;

import com.kqp.inventorytabs.tabs.TabManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Screen.class)
public class ScreenMixin {

    @Shadow @Nullable protected Minecraft minecraft;

    @Inject(method = "resize", at = @At("HEAD"))
    private void inventorytabs_resize(Minecraft pMinecraft, int pWidth, int pHeight, CallbackInfo ci) {
        if (minecraft != null && minecraft.level != null && ((Object)this) instanceof AbstractContainerScreen<?> screen && TabManager.getInstance().getCurrentScreen() == screen) {
            var tabManager = TabManager.getInstance();
            tabManager.isResized = true;
        }
    }
}
