package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.SimpleEntityTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class SimpleEntityTabProvider extends EntityTabProvider {
    private final Set<ResourceLocation> entityIds = new HashSet<>();

    public SimpleEntityTabProvider() {
    }

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);
//        Set<SimpleEntityTab> tabsToRemove = new HashSet<>();
//        List<SimpleEntityTab> entityTabs = tabs.stream().filter(tab -> tab instanceof SimpleEntityTab).map(tab -> (SimpleEntityTab) tab)
//                .filter(tab -> entityIds.contains(tab.entityId)).toList();
//        Level world = player.level;
    }

    @Override
    public boolean matches(Entity entity) {
        return entityIds.contains(ForgeRegistries.ENTITIES.getKey(entity.getType()));
    }

    public void addEntity(ResourceLocation entityId) {
        entityIds.add(entityId);
    }

    @Override
    public Tab createTab(Entity entity) {
        return new SimpleEntityTab(entity);
    }
}