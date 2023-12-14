package com.kqp.inventorytabs.tabs.tab;

import com.kqp.inventorytabs.mixin.accessor.ScreenAccessor;
import com.kqp.inventorytabs.tabs.render.TabRenderInfo;
import com.kqp.inventorytabs.util.ChestUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Tab for chests
 */
public class ChestTab extends SimpleBlockTab {
    ItemStack itemStack;
    public ChestTab(ResourceLocation blockId, BlockPos blockPos) {
        super(blockId, blockPos);
        this.itemStack = new ItemStack(Registry.BLOCK.get(blockId));
    }

    @Override
    public boolean shouldBeRemoved() {
        ClientLevel level = Minecraft.getInstance().level;

        if (ChestBlock.isChestBlockedAt(level, blockPos)) {
            return true;
        }

        return super.shouldBeRemoved();
    }

    @Override
    public Component getHoverText() {
        if (itemStack.hasCustomHoverName()) {
            return itemStack.getHoverName();
        }
        return super.getHoverText();
    }

    @Override
    public void renderTabIcon(PoseStack poseStack, TabRenderInfo tabRenderInfo, AbstractContainerScreen<?> currentScreen) {
        itemStack = getItemFrame();
        ItemRenderer itemRenderer = ((ScreenAccessor) currentScreen).getItemRenderer();
        Font textRenderer = ((ScreenAccessor) currentScreen).getFont();
        itemRenderer.blitOffset = 100.0F;
        itemRenderer.renderAndDecorateItem(itemStack, tabRenderInfo.itemX, tabRenderInfo.itemY);
        itemRenderer.renderGuiItemDecorations(textRenderer, itemStack, tabRenderInfo.itemX, tabRenderInfo.itemY);
        itemRenderer.blitOffset = 0.0F;
    }

    public ItemStack getItemFrame() {
        Level world = Minecraft.getInstance().level;
        BlockPos doubleChestPos = ChestUtil.isDouble(world, blockPos) ? ChestUtil.getOtherChestBlockPos(world, blockPos) : blockPos;
        AABB box = new AABB(blockPos, doubleChestPos);
        double x = box.minX;    double y = box.minY;    double z = box.minZ;
        double x1 = box.maxX;   double y1 = box.maxY;   double z1 = box.maxZ;
        List<ItemFrame> list1 = world.getEntitiesOfClass(ItemFrame.class, new AABB(x-0.8, y, z, x1+1.8, y1+0.8, z1+0.8));
        List<ItemFrame> list2 = world.getEntitiesOfClass(ItemFrame.class, new AABB(x, y, z-0.8, x1+0.8, y1+0.8, z1+1.8));
        List<ItemFrame> list3 = world.getEntitiesOfClass(ItemFrame.class, new AABB(x, y-0.8, z, x1+0.8, y1+1.8, z1+0.8));
        Optional<ItemFrame> optional = Stream.of(list1, list2, list3).flatMap(List::stream).findFirst();
        return optional.map(ItemFrame::getItem).orElse(new ItemStack(world.getBlockState(blockPos).getBlock()));
    }
}
