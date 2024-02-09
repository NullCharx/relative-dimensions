package es.nullbyte.relativedimensions.datagen.loot.LootModifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.ItemInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.RANDOM;

/**
 * The mining dimensional shift makes ores drop a random item from different tiers, being common items from the
 * game. Modded items wont be included in this loot table for the time being unless they are added to a vanilla tag.
 */
public class MiningDimensionalShiftModifier extends LootModifier {
    public static final Supplier<Codec<MiningDimensionalShiftModifier>> CODEC = Suppliers.memoize(()
    -> RecordCodecBuilder.create(instance -> codecStart(instance).and(ForgeRegistries.ITEMS.getCodec()
            .fieldOf("item").forGetter(m -> m.item)).apply(instance, MiningDimensionalShiftModifier::new)));
    private final Item item;
    public MiningDimensionalShiftModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        populateLists();
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Check if the tool used is the aberrant pickaxe
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        //Acess the mined block
        BlockState block = context.getParamOrNull(LootContextParams.BLOCK_STATE);

        //Check if the tool is the aberrant pickaxe
        if (tool != null && tool.getItem() == ItemInit.ABERRANT_PICK.get() ) {
            //Aberrantism for ores
            if (block != null && block.is(Tags.Blocks.ORES) && block.getBlock() != BlockInit.ABERRANT_ORE.get()){
                generatedLoot.clear(); // Clear existing loot
                generatedLoot.add(new ItemStack(getRandomItemToDrop()));
            } else if (block != null && block.is(BlockTags.MINEABLE_WITH_PICKAXE) && block.getBlock() != BlockInit.ABERRANT_BLOCK.get()) {
                if (RANDOM.nextInt(100) < 20) {
                    generatedLoot.clear(); // Clear existing loot
                    generatedLoot.add(new ItemStack(getRandomItemToDrop()));
                }
                generatedLoot.clear(); // Clear existing loot
                generatedLoot.add(new ItemStack(getRandomItemToDrop()));
            }
            //Later: Dimensional shift for stone blocks, etc (Very low chances, do not use upper three tiers
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    List<Item> commonItems = new ArrayList<>();
    List<Item> uncommonItems = new ArrayList<>();
    List<Item> rareItems = new ArrayList<>();
    List<Item> epicItems = new ArrayList<>();
    List<Item> legendaryItems = new ArrayList<>();

    List<Item> oneInABazzilionItems = new ArrayList<>();

    public void populateLists() {
        getItemsFromBlockTag(Tags.Blocks.COBBLESTONE, 1);
        getItemsFromBlockTag(Tags.Blocks.COBBLESTONE_INFESTED, 1);
        getItemsFromBlockTag(Tags.Blocks.COBBLESTONE_MOSSY, 1);
        getItemsFromBlockTag(Tags.Blocks.COBBLESTONE_NORMAL, 1);
        getItemsFromBlockTag(Tags.Blocks.END_STONES, 1);
        getItemsFromBlockTag(Tags.Blocks.NETHERRACK, 1);
        getItemsFromBlockTag(Tags.Blocks.GRAVEL, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS, 1);
        getItemsFromBlockTag(Tags.Blocks.GRAVEL, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_BLACK, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_BLUE, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_BROWN, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_COLORLESS, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_CYAN, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_GRAY, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_GREEN, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_LIGHT_BLUE, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_LIGHT_GRAY, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_LIME, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_MAGENTA, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_ORANGE, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_PINK, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_PURPLE, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_RED, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_SILICA, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_TINTED, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_WHITE, 1);
        getItemsFromBlockTag(Tags.Blocks.GLASS_YELLOW, 1);
        getItemsFromBlockTag(Tags.Blocks.ORES_COPPER, 1);
        getItemsFromBlockTag(Tags.Blocks.ORES_COAL, 1);
        getItemsFromBlockTag(Tags.Blocks.SAND, 1);
        getItemsFromBlockTag(Tags.Blocks.SAND_COLORLESS, 1);
        getItemsFromBlockTag(Tags.Blocks.SAND_RED, 1);
        getItemsFromBlockTag(Tags.Blocks.SANDSTONE, 1);
        getItemsFromBlockTag(Tags.Blocks.ORES_IRON, 2);
        commonItems.add(Blocks.CRIMSON_PLANKS.asItem());
        commonItems.add(Blocks.OAK_PLANKS.asItem());
        commonItems.add(Blocks.SPRUCE_PLANKS.asItem());
        commonItems.add(Blocks.BIRCH_PLANKS.asItem());
        commonItems.add(Blocks.ACACIA_PLANKS.asItem());
        commonItems.add(Blocks.CHERRY_PLANKS.asItem());
        commonItems.add(Blocks.DARK_OAK_PLANKS.asItem());
        commonItems.add(Blocks.MANGROVE_PLANKS.asItem());
        commonItems.add(Blocks.WARPED_PLANKS.asItem());
        commonItems.add(Blocks.BLACKSTONE.asItem());
        commonItems.add(Blocks.TUFF.asItem());
        commonItems.add(Blocks.DIRT.asItem());
        commonItems.add(Blocks.SOUL_SAND.asItem());
        commonItems.add(Blocks.SOUL_SOIL.asItem());

        getItemsFromBlockTag(Tags.Blocks.BOOKSHELVES, 2);
        getItemsFromBlockTag(Tags.Blocks.ORES_LAPIS, 2);
        getItemsFromBlockTag(Tags.Blocks.ORES_REDSTONE, 2);
        getItemsFromBlockTag(Tags.Blocks.BOOKSHELVES, 2);
        uncommonItems.add(Blocks.OAK_LOG.asItem());
        uncommonItems.add(Blocks.SPRUCE_LOG.asItem());
        uncommonItems.add(Blocks.BIRCH_LOG.asItem());
        uncommonItems.add(Blocks.ACACIA_LOG.asItem());
        uncommonItems.add(Blocks.CHERRY_LOG.asItem());
        uncommonItems.add(Blocks.DARK_OAK_LOG.asItem());
        uncommonItems.add(Blocks.MANGROVE_LOG.asItem());
        uncommonItems.add(Blocks.WHITE_WOOL.asItem());
        uncommonItems.add(Blocks.MANGROVE_LOG.asItem());
        uncommonItems.add(Blocks.ORANGE_WOOL.asItem());
        uncommonItems.add(Blocks.MAGENTA_WOOL.asItem());
        uncommonItems.add(Blocks.LIGHT_BLUE_WOOL.asItem());
        uncommonItems.add(Blocks.YELLOW_WOOL.asItem());
        uncommonItems.add(Blocks.LIME_WOOL.asItem());
        uncommonItems.add(Blocks.PINK_WOOL.asItem());
        uncommonItems.add(Blocks.GRAY_WOOL.asItem());
        uncommonItems.add(Blocks.LIGHT_GRAY_WOOL.asItem());
        uncommonItems.add(Blocks.CYAN_WOOL.asItem());
        uncommonItems.add(Blocks.PURPLE_WOOL.asItem());
        uncommonItems.add(Blocks.BLUE_WOOL.asItem());
        uncommonItems.add(Blocks.BROWN_WOOL.asItem());
        uncommonItems.add(Blocks.GREEN_WOOL.asItem());
        uncommonItems.add(Blocks.RED_WOOL.asItem());
        uncommonItems.add(Blocks.BLACK_WOOL.asItem());
        uncommonItems.add(Blocks.COAL_BLOCK.asItem());
        uncommonItems.add(Blocks.COPPER_BLOCK.asItem());
        rareItems.add(Blocks.IRON_BLOCK.asItem());
        rareItems.add(Blocks.OAK_LOG.asItem());
        rareItems.add(Blocks.SPRUCE_LOG.asItem());
        rareItems.add(Blocks.BIRCH_LOG.asItem());
        rareItems.add(Blocks.ACACIA_LOG.asItem());
        rareItems.add(Blocks.CHERRY_LOG.asItem());
        rareItems.add(Blocks.DARK_OAK_LOG.asItem());
        rareItems.add(Blocks.MANGROVE_LOG.asItem());
        rareItems.add(Blocks.WHITE_WOOL.asItem());
        rareItems.add(Blocks.MANGROVE_LOG.asItem());
        rareItems.add(Blocks.ORANGE_WOOL.asItem());



        getItemsFromBlockTag(Tags.Blocks.ORES_EMERALD, 3);
        getItemsFromBlockTag(Tags.Blocks.ORES_GOLD, 3);
        getItemsFromBlockTag(Tags.Blocks.ORES_QUARTZ , 3);
        rareItems.add(Blocks.LODESTONE.asItem());
        rareItems.add(Blocks.LAPIS_BLOCK.asItem());
        rareItems.add(Blocks.REDSTONE_BLOCK.asItem());
        rareItems.add(Blocks.BOOKSHELF.asItem());
        rareItems.add(Blocks.NETHER_BRICKS.asItem());
        rareItems.add(Blocks.NETHER_WART_BLOCK.asItem());



        getItemsFromBlockTag(Tags.Blocks.ORES_DIAMOND, 4);
        getItemsFromBlockTag(Tags.Blocks.OBSIDIAN, 4);
        epicItems.add(Blocks.EMERALD_BLOCK.asItem());
        epicItems.add(Blocks.GOLD_BLOCK.asItem());
        epicItems.add(Blocks.QUARTZ_BLOCK.asItem());
        epicItems.add(Blocks.SHULKER_BOX.asItem());



        getItemsFromBlockTag(Tags.Blocks.ORES_NETHERITE_SCRAP, 5);
        legendaryItems.add(Blocks.BEDROCK.asItem());
        legendaryItems.add(Blocks.END_PORTAL_FRAME.asItem());
        legendaryItems.add(Blocks.ANCIENT_DEBRIS.asItem());

        oneInABazzilionItems.add(Blocks.NETHERITE_BLOCK.asItem());
        oneInABazzilionItems.add(Blocks.DIAMOND_BLOCK.asItem());




    }
    public  void getItemsFromBlockTag(TagKey<Block> blockTag, int tier) {
        // Create a list to hold the ItemStacks
        List<ItemStack> itemStacks = new ArrayList<>();

        // Access the registry and filter blocks by the tag
        List<Block> blocksInTag = ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> block.builtInRegistryHolder().is(blockTag))
                .collect(Collectors.toList());

        // Convert each block to its item form
        for (Block block : blocksInTag) {
            Item item = block.asItem();
            if (item != Items.AIR) { // Ensure the block has an associated item
                if (tier == 1) {
                    commonItems.add(item);
                } else if (tier == 2) {
                    uncommonItems.add(item);
                } else if (tier == 3) {
                    rareItems.add(item);
                } else if (tier == 4) {
                    epicItems.add(item);
                } else if (tier == 5) {
                    legendaryItems.add(item);
                }
            }
        }
    }

    public  void getItemsFromItemTag(TagKey<Item> blockTag, int tier) {
        // Create a list to hold the ItemStacks
        List<ItemStack> itemStacks = new ArrayList<>();

        // Access the registry and filter blocks by the tag
        List<Item> itemsInTag = ForgeRegistries.ITEMS.getValues().stream()
                .filter(block -> block.builtInRegistryHolder().is(blockTag))
                .toList();

        // Convert each block to its item form
        for (Item item : itemsInTag) {
            if (item != Items.AIR) { // Ensure the block has an associated item
                if (tier == 1) {
                    commonItems.add(item);
                } else if (tier == 2) {
                    uncommonItems.add(item);
                } else if (tier == 3) {
                    rareItems.add(item);
                } else if (tier == 4) {
                    epicItems.add(item);
                } else if (tier == 5) {
                    legendaryItems.add(item);
                }
            }
        }
    }


    public Item getRandomItemToDrop() {
        // Choose one item randomly from each list
        List<Item> selectedItems = new ArrayList<>();
        // List to hold one randomly chosen item from each category, with associated weights
        List<WeightedItem<Item>> weightedSelectedItems = new ArrayList<>();
        weightedSelectedItems.add(new WeightedItem<>(selectRandomItem(commonItems), 55));
        weightedSelectedItems.add(new WeightedItem<>(selectRandomItem(uncommonItems), 35));
        weightedSelectedItems.add(new WeightedItem<>(selectRandomItem(rareItems), 5.8));
        weightedSelectedItems.add(new WeightedItem<>(selectRandomItem(epicItems), 3.2));
        weightedSelectedItems.add(new WeightedItem<>(selectRandomItem(legendaryItems), 0.9101));
        weightedSelectedItems.add(new WeightedItem<>(selectRandomItem(oneInABazzilionItems), 0.0899));


        // Now, choose one item from the selected items with equal weights
        Item finalSelectedItem = selectWeightedRandomItem(weightedSelectedItems);

        System.out.println("Final Selected Item: " + finalSelectedItem);


        return finalSelectedItem;

    }

    public Item selectRandomItem(List<Item> items) {
        if (items.isEmpty()) return null; // Or handle this case as you see fit
        return items.get(RANDOM.nextInt(items.size()));
    }

    static class WeightedItem<T> {
        T item;
        double weight;

        public WeightedItem(T item, double weight) {
            this.item = item;
            this.weight = weight;
        }
    }

    // Utility method for weighted random selection from a list of weighted items
    public static <T> T selectWeightedRandomItem(List<WeightedItem<T>> weightedItems) {
        double totalWeight = weightedItems.stream().mapToDouble(wi -> wi.weight).sum();
        int index = 0;
        double randomNum = RANDOM.nextDouble(totalWeight);

        for (WeightedItem<T> wi : weightedItems) {
            randomNum -= wi.weight;
            if (randomNum < 0) {
                return wi.item;
            }
            index++;
        }

        return null; // Or handle this case as you see fit
    }
}
