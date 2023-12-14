package com.kqp.inventorytabs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;

public class ChestUtil {
    public static boolean isDouble(Level world, BlockPos blockPos) {
        var blockState = world.getBlockState(blockPos);
        if (blockState.hasProperty(BlockStateProperties.CHEST_TYPE)) {
            var type = blockState.getValue(ChestBlock.TYPE);
            return type == ChestType.LEFT || type == ChestType.RIGHT;
        }
        return false;
    }

    public static BlockPos getOtherChestBlockPos(Level world, BlockPos blockPos) {
        var blockState = world.getBlockState(blockPos);

        Direction facing = blockState.getValue(ChestBlock.FACING);
        Direction otherBlockDir;

        ChestType type = blockState.getValue(ChestBlock.TYPE);

        if (type == ChestType.LEFT) {
            otherBlockDir = facing.getClockWise();
        } else {
            otherBlockDir = facing.getCounterClockWise();
        }

        return blockPos.relative(otherBlockDir);
    }
}
