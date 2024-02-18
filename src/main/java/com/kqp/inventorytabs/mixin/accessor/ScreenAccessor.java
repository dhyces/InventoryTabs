package com.kqp.inventorytabs.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
public interface ScreenAccessor {
    //@Accessor
    //ItemRenderer getItemRenderer();

    @Accessor
    Font getFont();
}
