package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.datagen.loot.LootTables.aberrantblocks.AberrantBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider {

    //Creates all the loot table providers. The second argument is a set of modifier flags, the thirs is a list with
    //al the subproviders. In this case, we only have one subprovider, ModBlockLootTables, in which we specify in the
    //second argument, is a provider for block loot tables
    public static LootTableProvider create (PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(AberrantBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
