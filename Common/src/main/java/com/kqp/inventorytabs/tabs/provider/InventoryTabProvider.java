package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.InventoryTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InventoryTabProvider implements TabProvider {
    private static final Set<ResourceLocation> inventoryItems = new HashSet<>();

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        Set<Item> itemSet = getItems();
        for (Item item : itemSet) {
            if (player.getInventory().contains(new ItemStack(item))) {
                Tab tab = new InventoryTab(item);
                if (tabs.stream().filter(c -> c instanceof InventoryTab).noneMatch(c -> ((InventoryTab) c).item == item)) {
                    tabs.add(tab);
                }
            }
        }
    }

    public void addItem(ResourceLocation blockId) {
        inventoryItems.add(blockId);
    }

    public Set<ResourceLocation> getItemIds() {
        return inventoryItems;
    }

    public static Set<Item> getItems() {
        return inventoryItems.stream().map(Registry.ITEM::get).collect(Collectors.toSet());
    }

}
