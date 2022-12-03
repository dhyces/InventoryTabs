package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public abstract class EntityTabProvider implements TabProvider {
    public static final int SEARCH_DISTANCE = 5;
    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        Level world = player.level;
        List<Entity> entityList = world.getEntitiesOfClass(Entity.class, new AABB(player.blockPosition().getX()-SEARCH_DISTANCE, player.blockPosition().getY()-SEARCH_DISTANCE, player.blockPosition().getZ()-SEARCH_DISTANCE, player.blockPosition().getX()+SEARCH_DISTANCE, player.blockPosition().getY()+SEARCH_DISTANCE, player.blockPosition().getZ()+SEARCH_DISTANCE));

        for (Entity entity : entityList) {
            if (!(entity instanceof Player) && ((entity instanceof Container) || (entity instanceof InventoryCarrier) || (entity instanceof ContainerListener))) {
                if (matches(entity)) {
                    boolean add = false;

                    Vec3 playerHead = player.position().add(0D, player.getEyeHeight(player.getPose()), 0D);
                    Vec3 blockVec = new Vec3(entity.getX() + 0.5D, entity.getY() + 0.5D,
                            entity.getZ() + 0.5D);

                    if (blockVec.subtract(playerHead).lengthSqr() <= SEARCH_DISTANCE * SEARCH_DISTANCE) {
                        add = true;
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