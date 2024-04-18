package com.kqp.inventorytabs.tabs.provider;

import java.util.List;

import com.kqp.inventorytabs.init.InventoryTabsConfig;
import com.kqp.inventorytabs.tabs.tab.Tab;
import com.kqp.inventorytabs.util.EntityUtil;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class EntityTabProvider implements TabProvider {
    public static final int SEARCH_DISTANCE = 5;

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        Level level = player.level;
        var reach = player.getBlockReach();
        List<Entity> entityList = level.getEntities(player, EntityUtil.aabbFromPlayer(player, reach));

        for (Entity entity : entityList) {
                if (matches(entity)) {
                    boolean add = false;

                    if (InventoryTabsConfig.doSightChecksFlag.get()) {
                        var lineOfSight = EntityUtil.getLineOfSight(entity, player, reach);
                        if (lineOfSight.isPresent()) {
                            add = true;
                        }
                    } else {
                        Vec3 playerHead = player.position().add(0D, player.getEyeHeight(player.getPose()), 0D);
                        Vec3 blockVec = new Vec3(entity.getX() + 0.5D, entity.getY() + 0.5D,
                                entity.getZ() + 0.5D);

                        if (blockVec.subtract(playerHead).lengthSqr() < reach * reach) {
                            add = true;
                        }
                    }

                    if (add) {
                        Tab tab = createTab(entity);

                        if (!tabs.contains(tab)) {
                            tabs.add(tab);
                        }
                    }
                }
        }
    }
    /**
     * Checks to see if entity passed in matches criteria.
     *
     * @param entity
     * @return
     */
    public abstract boolean matches(Entity entity);

    /**
     * Method to create tabs.
     *
     * @param entity
     * @return
     */
    public abstract Tab createTab(Entity entity);
}