package com.kqp.inventorytabs.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BlockUtil {
    public static boolean inRange(BlockPos pos, Player player, double distance) {
        double distanceSquared = distance * distance;

        Vec3 playerHead = player.position().add(0D, player.getEyeHeight(player.getPose()), 0D);
        Vec3 blockVec = new Vec3(pos.getX(), pos.getY(), pos.getZ());

        for (Vec3 sightOffset : SIGHT_OFFSETS) {
            if (blockVec.add(sightOffset).distanceToSqr(playerHead) <= distanceSquared) {
                return true;
            }
        }

        return false;
    }
    public static BlockHitResult getLineOfSight(BlockPos pos, Player player, double distance) {
        Level world = player.level;
        BlockState blockState = world.getBlockState(pos);
        double distanceSquared = distance * distance;

        Vec3 playerHead = player.position().add(0D, player.getEyeHeight(player.getPose()), 0D);
        Vec3 blockVec = new Vec3(pos.getX(), pos.getY(), pos.getZ());

        for (Vec3 sightOffset : SIGHT_OFFSETS) {
            Vec3 blockPosCheck = blockVec.add(sightOffset);

            BlockHitResult result = getBlockHitResult(playerHead, blockPosCheck, distanceSquared, world, pos,
                    blockState);

            if (result != null) {
                if (result.getBlockPos().equals(pos)) {
                    return result;
                }
            }
        }

        return null;
    }

    private static BlockHitResult getBlockHitResult(Vec3 playerHead, Vec3 blockVec, double distanceSquared,
            Level world, BlockPos pos, BlockState blockState) {
        if (blockVec.subtract(playerHead).lengthSqr() >= distanceSquared) {
            return null;
        }

        BlockHitResult result = world.clip(new ClipContext(playerHead, blockVec, ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE, Minecraft.getInstance().player));

        if (result.getType() == HitResult.Type.BLOCK && result.getBlockPos().equals(pos)) {
            return result;
        }

        return null;
    }

    private static final Vec3[] SIGHT_OFFSETS = {
            // Center
            new Vec3(0.5D, 0.5D, 0.5D),

            // Corners
            new Vec3(0.0D, 0.0D, 0.0D), new Vec3(1.0D, 0.0D, 0.0D), new Vec3(0.0D, 1.0D, 0.0D),
            new Vec3(0.0D, 0.0D, 1.0D), new Vec3(1.0D, 1.0D, 0.0D), new Vec3(0.0D, 1.0D, 1.0D),
            new Vec3(1.0D, 0.0D, 1.0D), new Vec3(1.0D, 1.0D, 1.0D),

            // Side centers
            new Vec3(0.5D, 0D, 0.5D), new Vec3(0.5D, 1D, 0.5D), new Vec3(0.0D, 0.5D, 0.5D),
            new Vec3(1.0D, 0.5D, 0.5D), new Vec3(0.5D, 0.5D, 0.0D), new Vec3(0.5D, 0.5D, 1.0D),

            // Corners, slightly in
            new Vec3(0.2D, 0.2D, 0.2D), new Vec3(0.8D, 0.2D, 0.2D), new Vec3(0.2D, 0.8D, 0.2D),
            new Vec3(0.2D, 0.2D, 0.8D), new Vec3(0.8D, 0.8D, 0.2D), new Vec3(0.2D, 0.8D, 0.8D),
            new Vec3(0.8D, 0.2D, 0.8D), new Vec3(0.8D, 0.8D, 0.8D),

            // Side centers, slightly in
            new Vec3(0.5D, 0.2D, 0.5D), new Vec3(0.5D, 0.8D, 0.5D), new Vec3(0.2D, 0.5D, 0.5D),
            new Vec3(0.8D, 0.5D, 0.5D), new Vec3(0.5D, 0.5D, 0.2D), new Vec3(0.5D, 0.5D, 0.8D), };
}
