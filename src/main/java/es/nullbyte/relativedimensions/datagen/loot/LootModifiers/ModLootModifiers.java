package es.nullbyte.relativedimensions.datagen.loot.LootModifiers;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);

    //Regular loot modifier
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
        LOOT_MODIFIERS_SERIALIZERS.register("add_item", AddItemModifier.CODEC);

    //Mining aberrantism loot modifier: For aberrant pickaxe, ores and other mineable blocks will drop aberrant
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> MINING_ABERRANTISM =
            LOOT_MODIFIERS_SERIALIZERS.register(MOD_ID + ":mining_aberrantism", MiningAberrantismModifier.CODEC);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> MINING_DIMENSIONAL_SHIFT =
            LOOT_MODIFIERS_SERIALIZERS.register(MOD_ID + ":mining_dimensional_shift", MiningDimensionalShiftModifier.CODEC);
    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS_SERIALIZERS.register(eventBus);
    }
}
