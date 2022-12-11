package com.kqp.inventorytabs.tabs.tab;

import com.kqp.inventorytabs.init.InventoryTabsConfig;
import com.kqp.inventorytabs.tabs.provider.BlockTabProvider;
import com.kqp.inventorytabs.util.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

/**
 * Generic tab for blocks.
 */
public class SimpleBlockTab extends Tab {
    public final ResourceLocation blockId;
    public final BlockPos blockPos;

    public SimpleBlockTab(ResourceLocation blockId, BlockPos blockPos) {
        super(new ItemStack(Minecraft.getInstance().level.getBlockState(blockPos).getBlock()));
        this.blockId = blockId;
        this.blockPos = blockPos;
    }

    @Override
    public void open() {
        Minecraft client = Minecraft.getInstance();
        BlockHitResult hitResult;

        if (InventoryTabsConfig.doSightChecksFlag.get()) {
            hitResult = BlockUtil.getLineOfSight(blockPos, client.player, 5D);
        } else {
            hitResult = new BlockHitResult(Vec3.atCenterOf(blockPos), Direction.EAST, blockPos, false);
        }

        if (hitResult != null) {
            if (InventoryTabsConfig.rotatePlayer.get()) {
                Minecraft.getInstance().player.lookAt(EntityAnchorArgument.Anchor.EYES,
                        Vec3.atCenterOf(blockPos));
            }

            Minecraft.getInstance().gameMode.useItemOn(client.player, InteractionHand.MAIN_HAND, hitResult);
        }
    }

    @Override
    public boolean shouldBeRemoved() {
        AbstractClientPlayer player = Minecraft.getInstance().player;

        if (!Objects.equals(ForgeRegistries.BLOCKS.getKey(player.level.getBlockState(blockPos).getBlock()), blockId)) {
            return true;
        }

        if (InventoryTabsConfig.doSightChecksFlag.get()) {
            if (BlockUtil.getLineOfSight(blockPos, player, 5D) == null) {
                return true;
            } else {
                return !BlockUtil.inRange(blockPos, player, 5D);
            }
        }
        Vec3 playerHead = player.position().add(0D, player.getEyeHeight(player.getPose()), 0D);

        return Vec3.atCenterOf(blockPos).subtract(playerHead).lengthSqr() > BlockTabProvider.SEARCH_DISTANCE
                * BlockTabProvider.SEARCH_DISTANCE;

    }

    @Override
    public Component getHoverText() {
        Level world = Minecraft.getInstance().level;

        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity != null) {
            CompoundTag tag = blockEntity.saveWithoutMetadata();

            if (tag.contains("CustomName", Tag.TAG_STRING)) {
                return Component.Serializer.fromJson(tag.getString("CustomName"));
            }
        }

        return Component.translatable(world.getBlockState(blockPos).getBlock().getDescriptionId());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleBlockTab tab = (SimpleBlockTab) o;
        return Objects.equals(blockPos, tab.blockPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockPos);
    }
}
