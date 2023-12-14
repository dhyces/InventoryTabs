package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.ChestTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import com.kqp.inventorytabs.util.ChestUtil;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides tabs for chests. Limits double chests to having only one tab and
 * takes into account if it's blocked.
 */
public class ChestTabProvider extends BlockTabProvider {
    private final Set<ResourceLocation> chestBlocks = new HashSet<>();

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);

        Set<ChestTab> tabsToRemove = new HashSet<>();

        List<ChestTab> chestTabs = tabs.stream().filter(tab -> tab instanceof ChestTab).map(tab -> (ChestTab) tab)
                .filter(tab -> chestBlocks.contains(tab.blockId)).collect(Collectors.toList());

        Level world = player.level;

        // Add any chests that are blocked
        chestTabs.stream().filter(tab -> ChestBlock.isChestBlockedAt(world, tab.blockPos)).forEach(tabsToRemove::add);

        for (ChestTab tab : chestTabs) {
            if (!tabsToRemove.contains(tab)) {
                if (ChestUtil.isDouble(world, tab.blockPos)) {
                    tabsToRemove.add(new ChestTab(tab.blockId, ChestUtil.getOtherChestBlockPos(world, tab.blockPos)));
                }
            }
        }

        tabs.removeAll(tabsToRemove);
    }

    public void addChestBlock(Block block) {
        chestBlocks.add(Registry.BLOCK.getKey(block));
    }

    public void addChestBlock(ResourceLocation blockId) {
        chestBlocks.add(blockId);
    }

    public void removeChestBlockId(ResourceLocation blockId) {
        chestBlocks.remove(blockId);
    }

    public Set<ResourceLocation> getChestBlockIds() {
        return this.chestBlocks;
    }

    @Override
    public boolean matches(Level world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();

        return chestBlocks.contains(Registry.BLOCK.getKey(block));
    }

    @Override
    public Tab createTab(Level world, BlockPos pos) {
        return new ChestTab(Registry.BLOCK.getKey(world.getBlockState(pos).getBlock()), pos);
    }
}
