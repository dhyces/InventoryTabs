package com.kqp.inventorytabs.util;

import java.nio.DoubleBuffer;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;

/**
 * Utility class for manipulating the client's mouse position.
 */
@OnlyIn(Dist.CLIENT)
public class MouseUtil {
    private static double mouseX = -1D, mouseY = -1D;

    public static void push() {
        mouseX = getMouseX();
        mouseY = getMouseY();
    }

    public static void tryPop() {
        if (mouseX != -1D && mouseY != -1D) {
            InputConstants.grabOrReleaseMouse(Minecraft.getInstance().getWindow().getWindow(), 212993, mouseX,
                    mouseY);

            mouseX = -1D;
            mouseY = -1D;
        }
    }

    public static double getMouseX() {
        DoubleBuffer mouseBuf = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), mouseBuf, null);

        return mouseBuf.get(0);
    }

    public static double getMouseY() {
        DoubleBuffer mouseBuf = BufferUtils.createDoubleBuffer(1);
        GLFW.glfwGetCursorPos(Minecraft.getInstance().getWindow().getWindow(), null, mouseBuf);

        return mouseBuf.get(0);
    }
}
