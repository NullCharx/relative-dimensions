package es.nullbyte.relativedimensions.datagen.loot.LootModifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import es.nullbyte.relativedimensions.blocks.init.BlockInit;
import es.nullbyte.relativedimensions.items.init.ItemInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
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
            if (block != null && block.is(Tags.Blocks.ORES)) {
                //Dimensional shift by tiers
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
