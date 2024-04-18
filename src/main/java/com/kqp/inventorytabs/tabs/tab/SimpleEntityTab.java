package com.kqp.inventorytabs.tabs.tab;

import java.util.Objects;

import com.kqp.inventorytabs.init.InventoryTabs;
import com.kqp.inventorytabs.init.InventoryTabsConfig;
import com.kqp.inventorytabs.util.EntityUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public class SimpleEntityTab extends Tab {
    public final ResourceLocation entityId;
    public final Entity entity;

    public SimpleEntityTab(Entity entity) {
        super(entity.getPickResult() != null ? entity.getPickResult() : new ItemStack(Items.BARRIER));
        this.entity = entity;
        this.entityId = EntityType.getKey(entity.getType());
    }

    @Override
    public void open() {
        AbstractClientPlayer player = Minecraft.getInstance().player;
        Minecraft.getInstance().gameMode.interact(player, entity, player.getUsedItemHand());
    }

    @Override
    public boolean shouldBeRemoved() {
        if (entity.isRemoved()) {
            return true;
        }
        var player = Minecraft.getInstance().player;
        if (InventoryTabsConfig.doSightChecksFlag.get()) {
            if (!EntityUtil.canInteract(player, entity, player.getEntityReach())) {
                return true;
            }
        }
        // TODO: fix this, entities can interact with entities that are farther and this doesn't catch it
        return entity.position().distanceTo(Minecraft.getInstance().player.position()) > player.getEntityReach();
    }

    @Override
    public Component getHoverText() {
        return entity.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleEntityTab tab = (SimpleEntityTab) o;
        if (!Objects.equals(entityId, tab.entityId)) {
            return false;
        }
        return entity.getUUID().equals(tab.entity.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity.getUUID());
    }
}