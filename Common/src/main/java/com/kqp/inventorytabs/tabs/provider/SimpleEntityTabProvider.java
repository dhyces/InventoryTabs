package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.SimpleEntityTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleEntityTabProvider extends EntityTabProvider {
    private final Set<ResourceLocation> entityIds = new HashSet<>();

    public SimpleEntityTabProvider() {
    }

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);
        Set<SimpleEntityTab> tabsToRemove = new HashSet<>();
        List<SimpleEntityTab> entityTabs = tabs.stream().filter(tab -> tab instanceof SimpleEntityTab).map(tab -> (SimpleEntityTab) tab)
                .filter(tab -> entityIds.contains(tab.entityId)).toList();
        Level world = player.level;

        entityTabs.stream().filter(SimpleEntityTab::shouldBeRemoved).forEach(tabsToRemove::add);
        tabs.removeAll(tabsToRemove);
    }

    @Override
    public boolean matches(Entity entity) {
        return entityIds.contains(Registry.ENTITY_TYPE.getKey(entity.getType()));
    }

    public void addEntity(ResourceLocation entityId) {
        entityIds.add(entityId);
    }

    @Override
    public Tab createTab(Entity entity) {
        return new SimpleEntityTab(entity);
    }
}