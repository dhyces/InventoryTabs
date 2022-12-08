package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.init.InventoryTabsConfig;
import com.kqp.inventorytabs.tabs.tab.Tab;
import com.kqp.inventorytabs.util.BlockUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Tab provider that exposes tabs based on nearby blocks.
 */
public abstract class BlockTabProvider implements TabProvider {
    public static final int SEARCH_DISTANCE = 5;

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        Level level = player.level;

        int reach = SEARCH_DISTANCE;

        // TODO: make this better and check line of sight
        for (int x = -reach; x <= reach; x++) {
            for (int y = -reach; y <= reach; y++) {
                for (int z = -reach; z <= reach; z++) {
                    BlockPos blockPos = player.blockPosition().offset(x, y, z);

                    if (matches(level, blockPos)) {
                        boolean add = false;

                        if (InventoryTabsConfig.doSightChecksFlag.get()) {
                            BlockHitResult hitResult = BlockUtil.getLineOfSight(blockPos, player, reach);

                            if (hitResult != null) {
                                add = true;
                            }
                        } else {
                            Vec3 playerHead = player.position().add(0D, player.getEyeHeight(player.getPose()), 0D);
                            Vec3 blockVec = new Vec3(blockPos.getX() + 0.5D, blockPos.getY() + 0.5D,
                                    blockPos.getZ() + 0.5D);

                            if (blockVec.subtract(playerHead).lengthSqr() <= reach * reach) {
                                add = true;
                            }
                        }

                        if (add) {
                            Tab tab = createTab(level, blockPos);

                            if (!tabs.contains(tab)) {
                                tabs.add(tab);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks to see if block at passed block position matches criteria.
     *
     * @param world
     * @param pos
     * @return
     */
    public abstract boolean matches(Level world, BlockPos pos);

    /**
     * Method to create tabs.
     *
     * @param world
     * @param pos
     * @return
     */
    public abstract Tab createTab(Level world, BlockPos pos);
}
