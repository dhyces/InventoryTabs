package com.kqp.inventorytabs.tabs.provider;

import com.kqp.inventorytabs.tabs.tab.SimpleBlockTab;
import com.kqp.inventorytabs.tabs.tab.Tab;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class LecternTabProvider extends BlockTabProvider {
    @Override
    public void addAvailableTabs(AbstractClientPlayer player, List<Tab> tabs) {
        super.addAvailableTabs(player, tabs);
        Set<SimpleBlockTab> tabsToRemove = new HashSet<>();
        List<SimpleBlockTab> lecternTabs = tabs.stream().filter(tab -> tab instanceof SimpleBlockTab).map(tab -> (SimpleBlockTab) tab)
                .filter(tab -> tab.blockId == ForgeRegistries.BLOCKS.getKey(Blocks.LECTERN)).collect(Collectors.toList());
        lecternTabs.stream().filter(tab -> {
            BlockEntity blockEntity = player.level.getBlockEntity(tab.blockPos);

            if (blockEntity instanceof LecternBlockEntity) {
                BlockState blockState = player.level.getBlockState(tab.blockPos);

                return !blockState.getValue(BlockStateProperties.HAS_BOOK);
            }

            return false;
        }).forEach(tabsToRemove::add);

        tabs.removeAll(tabsToRemove);
    }
    @Override
    public boolean matches(Level world, BlockPos pos) {
        return false;
    }

    @Override
    public Tab createTab(Level world, BlockPos pos) {
        return null;
    }
}
