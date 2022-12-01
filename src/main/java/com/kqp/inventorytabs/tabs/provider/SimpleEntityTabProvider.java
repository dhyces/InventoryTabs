package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.SimpleEntityTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleEntityTabProvider extends EntityTabProvider {
    private final Set<ResourceLocation> entities = new HashSet<>();

    public SimpleEntityTabProvider() {
    }

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);
        Set<SimpleEntityTab> tabsToRemove = new HashSet<>();
        List<SimpleEntityTab> entityTabs = tabs.stream().filter(tab -> tab instanceof SimpleEntityTab).map(tab -> (SimpleEntityTab) tab)
                .filter(tab -> entities.contains(tab.entityId)).toList();
        Level world = player.level;
    }

    @Override
    public boolean matches(Entity entity) {
        return entities.contains(new ResourceLocation("minecraft:entity.minecraft.chest_minecart"));
    }

    public void addEntity(ResourceLocation entityId) {
        entities.add(entityId);
    }

    @Override
    public Tab createTab(Entity entity) {
        return new SimpleEntityTab(entity);
    }
}