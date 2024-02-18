package com.kqp.inventorytabs.tabs.tab;

import com.kqp.inventorytabs.init.InventoryTabs;

import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class RidableInventoryTab extends SimpleEntityTab {

    public RidableInventoryTab(Entity entity) {
        super(entity);
    }

    @Override
    public void open() {
        if (!entity.hasPassenger(InventoryTabs.mc.player)) {
        	InventoryTabs.mc.player.input.shiftKeyDown = true;
            super.open();
            InventoryTabs.mc.player.input.shiftKeyDown = false;
            InventoryTabs.mc.getConnection().send(new ServerboundPlayerCommandPacket(InventoryTabs.mc.player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
        } else {
            super.open();
        }
    }

    @Override
    public boolean shouldBeRemoved() {
        if (entity instanceof AbstractHorse horse) {
            if (!horse.isTamed()) {
                return true;
            }
        }
        return super.shouldBeRemoved();
    }
}
