package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.ChestTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides tabs for ender chests. Limits amount of ender chest tabs to only one
 * and takes into account if it's blocked.
 */
public class EnderChestTabProvider extends BlockTabProvider {
    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);

        Set<ChestTab> tabsToRemove = new HashSet<>();

        List<ChestTab> chestTabs = tabs.stream().filter(tab -> tab instanceof ChestTab).map(tab -> (ChestTab) tab)
                .filter(tab -> tab.blockId == Registry.BLOCK.getKey(Blocks.ENDER_CHEST)).collect(Collectors.toList());

        Level world = player.level;

        // Add any chests that are blocked
        chestTabs.stream().filter(tab -> ChestBlock.isChestBlockedAt(world, tab.blockPos)).forEach(tabsToRemove::add);

        boolean found = false;

        for (ChestTab tab : chestTabs) {
            if (!tabsToRemove.contains(tab)) {
                if (!found) {
                    found = true;
                } else {
                    tabsToRemove.add(tab);
                }
            }
        }

        tabs.removeAll(tabsToRemove);
    }

    @Override
    public boolean matches(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST;
    }

    @Override
    public Tab createTab(Level world, BlockPos pos) {
        return new ChestTab(Registry.BLOCK.getKey(Blocks.ENDER_CHEST), pos);
    }
}
