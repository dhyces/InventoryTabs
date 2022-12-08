package com.kqp.inventorytabs.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EntityUtil {
    public static Optional<EntityHitResult> getLineOfSight(Entity entity, Player player, double distance) {
        var playerEyePos = getPlayerEyePos(player);
        var entityPos = entity.position();

        for (Vec3 offset : getOffsets(entity)) {
//            if (entityPos.add(offset).subtract(playerEyePos).lengthSqr() >= distance * distance) {
//                continue;
//            }
            var entityHitResult = entityClip(player, playerEyePos, entityPos.add(offset), entity, distance);
            var blockHitResult = BlockUtil.blockClip(player, playerEyePos, entityPos.add(offset));
            if (entityHitResult != null && !entityHitResult.getType().equals(HitResult.Type.MISS)) {
                if (blockHitResult.getType().equals(HitResult.Type.MISS)) {
                    return Optional.of(entityHitResult);
                } else {
                    return canInteract(playerEyePos, Vec3.atLowerCornerOf(blockHitResult.getBlockPos()), entityHitResult.getLocation()) ? Optional.of(entityHitResult) : Optional.empty();
                }
            }
        }
        return Optional.empty();
    }

    public static Vec3 getPlayerEyePos(Player player) {
        return player.position().add(0, player.getEyeHeight(player.getPose()), 0);
    }

    public static AABB aabbFromPlayer(Player player, double distance) {
        return player.getBoundingBox().expandTowards(player.getViewVector(1).scale(distance)).inflate(1);
    }

    @Nullable
    public static EntityHitResult entityClip(Player player, Vec3 startVec, Vec3 endVec, @Nullable Entity entity, double distance) {
        return ProjectileUtil.getEntityHitResult(player, startVec, endVec, aabbFromPlayer(player, distance), entity1 -> entity == null || entity1.is(entity), distance * distance);
    }

    public static boolean canInteract(Vec3 playerEyePos, Vec3 blockPos, Vec3 entityPos) {
        var distToBlock = playerEyePos.distanceToSqr(blockPos);
        var distToEntity = playerEyePos.distanceToSqr(entityPos);
        if (distToEntity <= distToBlock) {
            return true;
        }
        return false;
    }

    public static boolean canInteract(Player player, Entity entity, double distance) {
        var playerEyePos = player.position().add(0, player.getEyeHeight(player.getPose()), 0);
        var entityPos = entity.position().add(0, 0.5, 0);
        var entityHitResult = entityClip(player, playerEyePos, entityPos, entity, distance);
        if (entityHitResult != null && !entityHitResult.getType().equals(HitResult.Type.MISS)) {
            var distToEntity = playerEyePos.distanceToSqr(entityHitResult.getLocation());
            var blockHitResult = BlockUtil.blockClip(player, playerEyePos, entityPos);
            if (!blockHitResult.getType().equals(HitResult.Type.MISS)) {
                var distToBlock = playerEyePos.distanceToSqr(blockHitResult.getLocation());
                if (distToEntity <= distToBlock) {
                    return true;
                }
            } else {
                return distToEntity <= distance * distance;
            }
        }

        return false;
    }

    private static List<Vec3> getOffsets(Entity entity) {
        var height = entity.getBbHeight();
        var halfHeight = height / 2f;
        var halfWidth = entity.getBbWidth() / 2f;
        List<Vec3> list = new ArrayList<>();
        // No change
        list.add(Vec3.ZERO);
        // Center
        list.add(new Vec3(0, halfHeight, 0));
        // Corners
        list.add(new Vec3(halfWidth, 0, halfWidth));
        list.add(new Vec3(halfWidth, 0, -halfWidth));
        list.add(new Vec3(-halfWidth, 0, halfWidth));
        list.add(new Vec3(-halfWidth, 0, -halfWidth));
        list.add(new Vec3(halfWidth, height, halfWidth));
        list.add(new Vec3(halfWidth, height, -halfWidth));
        list.add(new Vec3(-halfWidth, height, halfWidth));
        list.add(new Vec3(-halfWidth, height, -halfWidth));
        // Sides
        list.add(new Vec3(0, halfHeight, halfWidth));
        list.add(new Vec3(halfWidth, halfHeight, 0));
        list.add(new Vec3(0, halfHeight, -halfWidth));
        list.add(new Vec3(-halfWidth, halfHeight, 0));

        // Corners slightly in
        list.add(new Vec3(halfWidth-0.2, 0.2, halfWidth-0.2));
        list.add(new Vec3(halfWidth-0.2, 0.2, -halfWidth+0.2));
        list.add(new Vec3(-halfWidth+0.2, 0.2, halfWidth-0.2));
        list.add(new Vec3(-halfWidth+0.2, 0.2, -halfWidth+0.2));
        list.add(new Vec3(halfWidth-0.2, height-0.2, halfWidth-0.2));
        list.add(new Vec3(halfWidth-0.2, height-0.2, -halfWidth+0.2));
        list.add(new Vec3(-halfWidth+0.2, height-0.2, halfWidth-0.2));
        list.add(new Vec3(-halfWidth+0.2, height-0.2, -halfWidth+0.2));
        // Sides slightly in
        list.add(new Vec3(0, halfHeight, halfWidth-0.2));
        list.add(new Vec3(halfWidth-0.2, halfHeight, 0));
        list.add(new Vec3(0, halfHeight, -halfWidth+0.2));
        list.add(new Vec3(-halfWidth+0.2, halfHeight, 0));

        return list;
    }
}
