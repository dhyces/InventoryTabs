package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.mixin.ShulkerBoxBlockInvoker;
import com.kqp.inventorytabs.tabs.tab.SimpleBlockTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides tabs for shulker boxes. Takes into account if it's blocked.
 */
public class ShulkerBoxTabProvider extends BlockTabProvider {
    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);

        Set<SimpleBlockTab> tabsToRemove = new HashSet<>();

        List<SimpleBlockTab> shulkerTabs = tabs.stream().filter(tab -> tab instanceof SimpleBlockTab)
                .map(tab -> (SimpleBlockTab) tab)
                .filter(tab -> ForgeRegistries.BLOCKS.getValue(tab.blockId) instanceof ShulkerBoxBlock).collect(Collectors.toList());

        // Add any chests that are blocked
        shulkerTabs.stream().filter(tab -> {
            BlockEntity blockEntity = player.level.getBlockEntity(tab.blockPos);

            if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                BlockState blockState = player.level.getBlockState(tab.blockPos);

                return !ShulkerBoxBlockInvoker.invokeCanOpen(blockState, player.level, tab.blockPos, shulkerBoxBlockEntity);
            }

            return false;
        }).forEach(tabsToRemove::add);

        tabs.removeAll(tabsToRemove);
    }

    @Override
    public boolean matches(Level world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof ShulkerBoxBlock;
    }

    @Override
    public Tab createTab(Level world, BlockPos pos) {
        return new SimpleBlockTab(ForgeRegistries.BLOCKS.getKey(world.getBlockState(pos).getBlock()), pos);
    }
}
