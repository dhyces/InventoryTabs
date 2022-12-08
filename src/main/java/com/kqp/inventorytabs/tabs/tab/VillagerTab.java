package com.kqp.inventorytabs.tabs.tab;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

public class VillagerTab extends SimpleEntityTab {

    public VillagerTab(Entity entity) {
        super(entity);
    }

    @Override
    public void open() {
        super.open();
    }

    @Override
    public boolean shouldBeRemoved() {
        if (entity instanceof Villager villager) {
            if (villager.getVillagerData().getProfession().equals(VillagerProfession.NITWIT) || villager.getVillagerData().getProfession().equals(VillagerProfession.NONE)) {
                return true;
            }
        }

        return super.shouldBeRemoved();
    }
}
