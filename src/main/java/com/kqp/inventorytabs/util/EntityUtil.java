package com.kqp.inventorytabs.util;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class EntityUtil {
    public static Optional<EntityHitResult> getLineOfSight(Entity entity, Player player, double distance) {
        var playerEyeHeight = player.getEyeHeight(player.getPose());
        var playerPos = player.position();
        var playerEyePos = playerPos.add(0, playerEyeHeight, 0);
        var entityPos = entity.position();
        var aabb = player.getBoundingBox().expandTowards(player.getViewVector(1).scale(distance)).inflate(1);

        var timer = new Timer();

        var offsets = getOffsets(entity);
        for (int i = 0; i < offsets.size(); i++) {
            var offset = offsets.get(i);
            if (entityPos.add(offset).subtract(playerEyePos).lengthSqr() >= distance * distance) {
                continue;
            }
            var entityHitResult = ProjectileUtil.getEntityHitResult(player, playerEyePos, entityPos.add(offset), aabb, entity1 -> entity1.is(entity), distance);
            //timer.schedule(task(player, entityPos, offset), 1000 + (1000 * i));
            if (entityHitResult != null && !entityHitResult.getType().equals(HitResult.Type.MISS)) {
                return Optional.of(entityHitResult);
            }
        }
        return Optional.empty();
    }

    private static TimerTask task(Player player, Vec3 entityPos, Vec3 offset) {
        return new TimerTask() {
            @Override
            public void run() {
                player.lookAt(EntityAnchorArgument.Anchor.EYES, entityPos.add(offset));
            }
        };
    }

    private static List<Vec3> getOffsets(Entity entity) {
        var pos = entity.position();
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
