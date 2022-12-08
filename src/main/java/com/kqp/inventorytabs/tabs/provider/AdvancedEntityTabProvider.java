package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.SimpleEntityTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class AdvancedEntityTabProvider extends EntityTabProvider {
    private final Map<ResourceLocation, TabFactory> entityMap = new HashMap<>();

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);
        Set<SimpleEntityTab> tabsToRemove = new HashSet<>();
        List<SimpleEntityTab> entityTabs = tabs.stream().filter(tab -> tab instanceof SimpleEntityTab).map(tab -> (SimpleEntityTab) tab)
                .filter(tab -> entityMap.containsKey(tab.entityId)).toList();

        entityTabs.stream().filter(SimpleEntityTab::shouldBeRemoved).forEach(tabsToRemove::add);
        tabs.removeAll(tabsToRemove);
    }

    @Override
    public boolean matches(Entity entity) {
        return entityMap.containsKey(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
    }

    @Override
    public Tab createTab(Entity entity) {
        return entityMap.get(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())).createTab(entity);
    }

    public boolean addEntity(ResourceLocation key, TabFactory factory) {
        return entityMap.putIfAbsent(key, factory) == null;
    }

    public interface TabFactory {
        Tab createTab(Entity entity);
    }
}
