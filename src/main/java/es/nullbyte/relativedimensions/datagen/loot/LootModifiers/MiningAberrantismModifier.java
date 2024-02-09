package es.nullbyte.relativedimensions.datagen.loot.LootModifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.init.ItemInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The mining aberrantism modifier is a GLM that changes the dropped item for mineable blocks
 */
public class MiningAberrantismModifier extends LootModifier {
    public static final Supplier<Codec<MiningAberrantismModifier>> CODEC = Suppliers.memoize(()
    -> RecordCodecBuilder.create(instance -> codecStart(instance).and(ForgeRegistries.ITEMS.getCodec()
            .fieldOf("item").forGetter(m -> m.item)).apply(instance, MiningAberrantismModifier::new)));
    private final Item item;
    public MiningAberrantismModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
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
                generatedLoot.add(new ItemStack(BlockInit.ABERRANT_ORE.get())); // Add ABERRANT_ORE
            } else if (block != null && block.is(BlockTags.MINEABLE_WITH_PICKAXE) && block.getBlock() != BlockInit.ABERRANT_BLOCK.get()) {
                generatedLoot.clear(); // Clear existing loot
                generatedLoot.add(new ItemStack(BlockInit.ABERRANT_MINERALOID.get())); // Add ABERRANT_BLOCK
            }
            //Later: Aberrantism for stone blocks, etc
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
