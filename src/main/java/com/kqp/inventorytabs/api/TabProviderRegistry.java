package com.kqp.inventorytabs.api;

import com.kqp.inventorytabs.init.FakeLevel;
import com.kqp.inventorytabs.init.InventoryTabs;
import com.kqp.inventorytabs.init.InventoryTabsConfig;
import com.kqp.inventorytabs.interf.TabManagerContainer;
import com.kqp.inventorytabs.tabs.provider.*;
import com.kqp.inventorytabs.tabs.tab.RidableInventoryTab;
import com.kqp.inventorytabs.tabs.tab.VillagerTab;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Registry for tab providers.
 */
public class TabProviderRegistry {
    private static final Logger LOGGER = LogManager.getLogger("InventoryTabs");
    private static final Map<ResourceLocation, TabProvider> TAB_PROVIDERS = new HashMap<>();

    public static final PlayerInventoryTabProvider PLAYER_INVENTORY_TAB_PROVIDER = register(
            InventoryTabs.id("player_inventory_tab_provider"), new PlayerInventoryTabProvider());
    public static final SimpleEntityTabProvider ENTITY_TAB_PROVIDER = register(
            InventoryTabs.id("entity_tab_provider"), new SimpleEntityTabProvider());
    public static final AdvancedEntityTabProvider ADVANCED_ENTITY_TAB_PROVIDER = register(
            InventoryTabs.id("advanced_entity_tab_provider"), new AdvancedEntityTabProvider());
    public static final SimpleBlockTabProvider SIMPLE_BLOCK_TAB_PROVIDER = register(
            InventoryTabs.id("simple_block_tab_provider"), new SimpleBlockTabProvider());
    public static final ChestTabProvider CHEST_TAB_PROVIDER = register(
            InventoryTabs.id("chest_tab_provider"), new ChestTabProvider());
    public static final EnderChestTabProvider ENDER_CHEST_TAB_PROVIDER = register(
            InventoryTabs.id("ender_chest_tab_provider"), new EnderChestTabProvider());
    public static final ShulkerBoxTabProvider SHULKER_BOX_TAB_PROVIDER = register(
            InventoryTabs.id("shulker_box_tab_provider"), new ShulkerBoxTabProvider());
    public static final UniqueTabProvider UNIQUE_TAB_PROVIDER = register(
            InventoryTabs.id("crafting_table_tab_provider"), new UniqueTabProvider());
    public static final LecternTabProvider LECTERN_TAB_PROVIDER = register(
            InventoryTabs.id("lectern_tab_provider"), new LecternTabProvider());
    public static final InventoryTabProvider INVENTORY_TAB_PROVIDER = register(
            InventoryTabs.id("inventory_tab_provider"), new InventoryTabProvider());

    public static void init(String configMsg) {
        LOGGER.info("InventoryTabs: Attempting to %s config...".formatted(configMsg));
        if (InventoryTabsConfig.debugEnabled.get()) {
            LOGGER.warn("InventoryTabs: DEBUG ENABLED");
        }
        Set<String> invalidSet = new HashSet<>();
        Set<String> tagSet = new HashSet<>();
        Set<String> blockSet = new HashSet<>();
        for (String overrideEntry : InventoryTabsConfig.excludeTab.get()) {
            if (overrideEntry.startsWith("#")) {
                tagSet.add(overrideEntry.trim().substring(1));
            } else {
                blockSet.add(overrideEntry);
            }
        }
        ForgeRegistries.BLOCKS.forEach(block -> {
            if (block instanceof EntityBlock) {
                if (block instanceof AbstractChestBlock) {
                    registerChest(block);
                } else if (!(block instanceof AbstractBannerBlock) && !(block instanceof SignBlock) && !(block instanceof AbstractSkullBlock) && !(block instanceof BeehiveBlock) && !(block instanceof BedBlock) && !(block instanceof BellBlock) && !(block instanceof CampfireBlock) && !(block instanceof CommandBlock) && !(block instanceof ComparatorBlock) && !(block instanceof ConduitBlock) && !(block instanceof DaylightDetectorBlock) && !(block instanceof EndGatewayBlock) && !(block instanceof EndPortalBlock) && !(block instanceof JigsawBlock) && !(block instanceof JukeboxBlock) && !(block instanceof MovingPistonBlock) && !(block instanceof SculkSensorBlock) && !(block instanceof SpawnerBlock) && !(block instanceof StructureBlock)) {
                    registerSimpleBlock(block);
                }
            } else if (block instanceof CraftingTableBlock && !(block instanceof FletchingTableBlock) || block instanceof AnvilBlock || block instanceof CartographyTableBlock || block instanceof GrindstoneBlock || block instanceof LoomBlock || block instanceof StonecutterBlock) {
                registerUniqueBlock(block);
            }
            configRemove(block, tagSet, invalidSet);
        });
        configRemove(blockSet);
        configAdd();
        var fakeLevel = new FakeLevel();
        if (InventoryTabsConfig.entityTabsBeta.get()) { // TODO: Remove this when it goes out of beta
            ForgeRegistries.ENTITY_TYPES.forEach(entityType -> {
                var entity = entityType.create(fakeLevel);
                if (entity instanceof Container || entity instanceof InventoryCarrier || entity instanceof ContainerListener) {
                    if (entity instanceof Villager) {
                        registerEntity(ForgeRegistries.ENTITY_TYPES.getKey(entityType), VillagerTab::new);
                    } else if (entity instanceof AbstractHorse) {
                        registerEntity(ForgeRegistries.ENTITIES.getKey(entityType), RidableInventoryTab::new);
                    } else if (!(entity instanceof Piglin) && !(entity instanceof Allay)) {
                        registerEntity(ForgeRegistries.ENTITY_TYPES.getKey(entityType));
                    }
                }
            });
        }

        Minecraft client = Minecraft.getInstance();
        TabManagerContainer tabManagerContainer = (TabManagerContainer) client;
        tabManagerContainer.getTabManager().removeTabs();
        LOGGER.info(configMsg.equals("save") ? "InventoryTabs: Config saved!": "InventoryTabs: Config %sed!".formatted(configMsg));
    }

    private static void modCompatAdd() {
        registerInventoryTab(new ResourceLocation("onastick", "crafting_table_on_a_stick"));
        registerInventoryTab(new ResourceLocation("onastick", "smithing_table_on_a_stick"));
        registerInventoryTab(new ResourceLocation("onastick", "cartography_table_on_a_stick"));
        registerInventoryTab(new ResourceLocation("onastick", "anvil_on_a_stick"));
        registerInventoryTab(new ResourceLocation("onastick", "loom_on_a_stick"));
        registerInventoryTab(new ResourceLocation("onastick", "grindstone_on_a_stick"));
        registerInventoryTab(new ResourceLocation("onastick", "stonecutter_on_a_stick"));
        registerInventoryTab(new ResourceLocation("craftingpad", "craftingpad"));
    }
    public static boolean isValid(String overrideEntry, String[] splitEntry, Set<String> invalidSet) {
        if (splitEntry.length != 2) {
            invalidSet.add(overrideEntry);
            return false;
        }
        return true;
    }
    private static void configRemove(Set<String> blockSet) {
        for (String overrideEntry : blockSet) {
            if (InventoryTabsConfig.debugEnabled.get()) {
                LOGGER.info("Excluding: %s".formatted(overrideEntry));
            }
            removeSimpleBlock(new ResourceLocation(overrideEntry));
        }
    }
    private static void configRemove(Block block, Set<String> tagSet, Set<String> invalidSet) {
        for (String overrideEntry : tagSet) {
            String[] splitEntry = overrideEntry.split(":"); // split into two parts: namespace, id
            if (isValid(overrideEntry, splitEntry, invalidSet)) {
                if (block.defaultBlockState().is(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(splitEntry[0], splitEntry[1])))) {
                    removeSimpleBlock(block);
                    if (InventoryTabsConfig.debugEnabled.get()) {
                        LOGGER.info("Excluding: %s".formatted(block));
                    }
                }
            }
        }
    }

    private static void configAdd() {
        for (String included_tab : InventoryTabsConfig.includeTab.get()) {
            if (InventoryTabsConfig.debugEnabled.get()) {
                LOGGER.info("Including: %s".formatted(included_tab));
            }
            registerSimpleBlock(new ResourceLocation(included_tab));
        }
    }
    public static void registerInventoryTab(ResourceLocation itemId) {
        INVENTORY_TAB_PROVIDER.addItem(itemId);
    }

    /**
     * Used to register a block with the simple block tab provider.
     *
     * @param block
     */
    public static void registerSimpleBlock(Block block) {
        if (InventoryTabsConfig.debugEnabled.get()) {
            LOGGER.info("Registering: %s".formatted(block));
        }
        SIMPLE_BLOCK_TAB_PROVIDER.addBlock(block);
    }

    /**
     * Used to register a block ResourceLocation with the simple block tab provider.
     *
     * @param blockId
     */
    public static void registerSimpleBlock(ResourceLocation blockId) {
        if (InventoryTabsConfig.debugEnabled.get()) {
            LOGGER.info("Registering: %s".formatted(blockId));
        }
        SIMPLE_BLOCK_TAB_PROVIDER.addBlock(blockId);
    }

    public static void removeSimpleBlock(Block block) {
        SIMPLE_BLOCK_TAB_PROVIDER.removeBlock(block);
    }
    public static void removeSimpleBlock(ResourceLocation blockId) {
        SIMPLE_BLOCK_TAB_PROVIDER.removeBlock(blockId);
    }

    /**
     * Used to register a chest with the chest tab provider.
     *
     * @param block
     */
    public static void registerChest(Block block) {
        if (InventoryTabsConfig.debugEnabled.get()) {
            LOGGER.info("Registering: " + block);
        }
        CHEST_TAB_PROVIDER.addChestBlock(block);
    }

    public static void registerUniqueBlock(Block block) {
        if (InventoryTabsConfig.debugEnabled.get()) {
            LOGGER.info("Registering: " + block);
        }
        UNIQUE_TAB_PROVIDER.addUniqueBlock(block);
    }

    public static void registerEntity(ResourceLocation entityId) {
        if (InventoryTabsConfig.debugEnabled.get()) {
            LOGGER.info("Registering: " + entityId);
        }
        ENTITY_TAB_PROVIDER.addEntity(entityId);
    }

    public static void registerEntity(ResourceLocation entityId, AdvancedEntityTabProvider.TabFactory factory) {
        if (InventoryTabsConfig.debugEnabled.get()) {
            LOGGER.info("Registering: " + entityId);
        }
        ADVANCED_ENTITY_TAB_PROVIDER.addEntity(entityId, factory);
    }

    /**
     * Used to register a chest with the chest tab provider.
     *
     * @param blockId
     */
    public static void registerChest(ResourceLocation blockId) {
        CHEST_TAB_PROVIDER.addChestBlock(blockId);
    }

    public static <T extends TabProvider> T register(ResourceLocation id, T tabProvider) {
        TAB_PROVIDERS.put(id, tabProvider);

        return tabProvider;
    }

    public static List<TabProvider> getTabProviders() {
        return List.copyOf(TAB_PROVIDERS.values());
    }
}