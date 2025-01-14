package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.SimpleBlockTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides tabs for simple blocks.
 */
public class SimpleBlockTabProvider extends BlockTabProvider {
    private final Set<ResourceLocation> blockIds = new HashSet<>();

    public SimpleBlockTabProvider() {
    }

    public void addBlock(Block block) {
        blockIds.add(ForgeRegistries.BLOCKS.getKey(block));
    }

    public void addBlock(ResourceLocation ResourceLocation) {
        blockIds.add(ResourceLocation);
    }

    public void removeBlock(Block block) {
        blockIds.remove(ForgeRegistries.BLOCKS.getKey(block));
    }

    public void removeBlock(ResourceLocation ResourceLocation) {
        blockIds.remove(ResourceLocation);
    }

    public Set<ResourceLocation> getBlockIds() {
        return this.blockIds;
    }

    public Set<Block> getBlocks() {
        return this.blockIds.stream().map(ForgeRegistries.BLOCKS::getValue).collect(Collectors.toSet());
    }

    @Override
    public boolean matches(Level world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);

        if (blockIds.contains(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()))) {
            return true;
        }

        return false;
    }

    @Override
    public Tab createTab(Level world, BlockPos pos) {
        return new SimpleBlockTab(ForgeRegistries.BLOCKS.getKey(world.getBlockState(pos).getBlock()), pos);
    }
}
