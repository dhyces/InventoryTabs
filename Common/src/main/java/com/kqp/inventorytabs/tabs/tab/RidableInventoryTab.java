package com.kqp.inventorytabs.tabs.tab;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class RidableInventoryTab extends SimpleEntityTab {

    public RidableInventoryTab(Entity entity) {
        super(entity);
    }

    @Override
    public void open() {
        if (!entity.hasPassenger(Minecraft.getInstance().player)) {
            Minecraft.getInstance().player.input.shiftKeyDown = true;
            super.open();
            Minecraft.getInstance().player.input.shiftKeyDown = false;
            Minecraft.getInstance().getConnection().send(new ServerboundPlayerCommandPacket(Minecraft.getInstance().player, ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY));
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
