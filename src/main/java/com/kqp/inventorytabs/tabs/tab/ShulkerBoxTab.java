package com.kqp.inventorytabs.tabs.tab;

import com.kqp.inventorytabs.init.InventoryTabs;
import com.kqp.inventorytabs.mixin.ShulkerBoxBlockInvoker;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Tab for shulker boxes.
 */
public class ShulkerBoxTab extends SimpleBlockTab {
    public ShulkerBoxTab(ResourceLocation blockId, BlockPos blockPos) {
        super(blockId, blockPos);
    }

    @Override
    public boolean shouldBeRemoved() {
        AbstractClientPlayer player = InventoryTabs.mc.player;

        BlockEntity blockEntity = player.level.getBlockEntity(blockPos);

        if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            BlockState blockState = player.level.getBlockState(blockPos);

            return !ShulkerBoxBlockInvoker.invokeCanOpen(blockState, player.level, blockPos, shulkerBoxBlockEntity);
        }

        return super.shouldBeRemoved();
    }
}
