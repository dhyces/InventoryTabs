package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.SimpleBlockTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * Provides tabs for blocks that should only have one tab at a time (e.g. Crafting Tables).
 **/
public class UniqueTabProvider extends BlockTabProvider {
    private final Set<ResourceLocation> uniqueBlocks = new HashSet<>();

    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);
        Set<ResourceLocation> tabsToRemove = new HashSet<>();
        List<SimpleBlockTab> craftingTableTabs = tabs.stream().filter(tab -> tab instanceof SimpleBlockTab).map(tab -> (SimpleBlockTab) tab)
                .filter(tab -> uniqueBlocks.contains(tab.blockId)).toList();

        for (SimpleBlockTab tab : craftingTableTabs) {
            if (!tabsToRemove.add(tab.blockId)) {
                tabs.remove(tab);
            }
        }
    }

    public void addUniqueBlock(Block block) {
        uniqueBlocks.add(ForgeRegistries.BLOCKS.getKey(block));
    }

    public void addUniqueBlock(ResourceLocation blockId) {
        uniqueBlocks.add(blockId);
    }

    public void removeUniqueBlockId(ResourceLocation blockId) {
        uniqueBlocks.remove(blockId);
    }
    @Override
    public boolean matches(Level world, BlockPos pos) {
        return uniqueBlocks.contains(ForgeRegistries.BLOCKS.getKey(world.getBlockState(pos).getBlock()));
    }

    @Override
    public Tab createTab(Level world, BlockPos pos) {
        return new SimpleBlockTab(ForgeRegistries.BLOCKS.getKey(world.getBlockState(pos).getBlock()), pos);
    }
}
