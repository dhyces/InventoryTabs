package com.kqp.inventorytabs.tabs.tab;

import com.kqp.inventorytabs.init.InventoryTabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class InventoryTab extends Tab {
    public final Item item;
    public InventoryTab(Item itemId) {
        super(new ItemStack(itemId));
        this.item = itemId;
    }

    @Override
    public void open() {
        System.out.println("TESTING: Opening inventory tab");
        AbstractClientPlayer player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        System.out.println("Player: "+ player);
        System.out.println("Level: "+ level);
        System.out.println("Item: "+ item);
        System.out.println("ItemStack: "+ new ItemStack(item));
        System.out.println("Used hand: "+ player.getUsedItemHand());
        Item item = new ItemStack(this.item).getItem();
        item.use(level, player, player.getUsedItemHand());
        //itemId.use(world, player, player.getActiveHand());
    }

    @Override
    public boolean shouldBeRemoved() {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        return (player == null || !player.getInventory().contains(new ItemStack(item)));
    }

    @Override
    public Component getHoverText() {
        return Component.literal(item.getDescription().getString());
    }
}
