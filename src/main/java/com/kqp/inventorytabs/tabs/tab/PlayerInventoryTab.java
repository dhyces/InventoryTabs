package com.kqp.inventorytabs.tabs.tab;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

/**
 * Tab for the player's inventory.
 */
public class PlayerInventoryTab extends Tab {
    public PlayerInventoryTab() {
        super(getRenderItemStack());
    }

    @Override
    public void open() {
        Minecraft client = Minecraft.getInstance();
        client.setScreen(new InventoryScreen(client.player));
    }

    @Override
    public boolean shouldBeRemoved() {
        return false;
    }

    @Override
    public Component getHoverText() {
        return new TextComponent("Inventory");
    }

    @Override
    public String toString() {
        return "PLAYER INVENTORY TAB";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    private static ItemStack getRenderItemStack() {
        ItemStack itemStack = new ItemStack(Blocks.PLAYER_HEAD);
        itemStack.getOrCreateTag().putString("SkullOwner",
                Minecraft.getInstance().player.getGameProfile().getName());

        return itemStack;
    }
}
