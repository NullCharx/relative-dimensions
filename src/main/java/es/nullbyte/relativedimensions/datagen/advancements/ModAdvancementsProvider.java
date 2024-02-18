package es.nullbyte.relativedimensions.datagen.advancements;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.ModItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModAdvancementsProvider implements ForgeAdvancementProvider.AdvancementGenerator {



    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        AdvancementHolder aberrant_root = Advancement.Builder.advancement()
            .display(new DisplayInfo(new ItemStack(BlockInit.ABERRANT_ORE.get()), //icon
                Component.translatable("advancements." + MOD_ID + ".aberrant_root.title"), //Title
                Component.translatable("advancements." + MOD_ID + ".aberrant_root.toast"), //Description
                Optional.of(new ResourceLocation(MOD_ID, "textures/block/aberrant_ore.png")), //Texture full path for the advancement window background
                AdvancementType.TASK, //Advancement type
                true, true, false)) //Show the advancement notification, Show chat notif, hidden advancement
            //Get the criterion for the advancement.
            .addCriterion("has_aberrant_oreshard", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ABERRANT_SHARD.get()))
            .addCriterion("has_aberrant_log", InventoryChangeTrigger.TriggerInstance.hasItems(BlockInit.ABERRANT_LOG.get()))
            //If not specified, the advancement will be unlocked with all the criteria are met. (Equivalent of AdvancementRequirements.allOf)
            .requirements(AdvancementRequirements.anyOf(List.of("has_aberrant_oreshard", "has_aberrant_log")))
            .save(saver, new ResourceLocation(MOD_ID, "aberrant_root_advancement"));


        AdvancementHolder aberrant_tool_made = Advancement.Builder.advancement()
            .display(new DisplayInfo(new ItemStack(ModItems.ABERRANT_PICK.get()),
                Component.translatable("advancements." + MOD_ID + ".aberrant_tool.title"),
                Component.translatable("advancements." + MOD_ID + ".aberrant_tool.toast"), //Description
                Optional.empty(), //Texture full path (optional)
                AdvancementType.TASK, //Advancement type
                true, true, false)) //Show the advancement notification, Show chat notif, hidden advancement
            .parent(aberrant_root)
            .addCriterion("has_aberrant_axe", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ABERRANT_AXE.get().asItem()))
            .addCriterion("has_aberrant_sword", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ABERRANT_SWORD.get().asItem()))
            .addCriterion("has_aberrant_pick", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ABERRANT_PICK.get().asItem()))
            .requirements(AdvancementRequirements.anyOf(List.of("has_aberrant_axe", "has_aberrant_sword", "has_aberrant_pick")))
            .save(saver, new ResourceLocation(MOD_ID, "aberrant_tool_advancement"));

        AdvancementHolder teleportation_root = Advancement.Builder.advancement()
            .display(new DisplayInfo(new ItemStack(BlockInit.ABERRANT_BLOCK.get()),
                Component.translatable("advancements." + MOD_ID + ".teleportation_root.title"),
                Component.translatable("advanczements." + MOD_ID + ".teleportation_root.toast"), //Description
                Optional.of(new ResourceLocation(MOD_ID, "textures/item/avidsdpt.png")), //Texture full path
                AdvancementType.TASK , //Advancement type
                true, true, false)) //Show the advancement notification, Show chat notif, hidden advancement
            //The criterion is the player using the item
                .addCriterion("using_avidsdpt",UsingItemTrigger.TriggerInstance.lookingAt(
                    EntityPredicate.Builder.entity().of(EntityType.PLAYER),
                    ItemPredicate.Builder.item().of(ModItems.AVID_SDPT.get())
                ))
                .addCriterion("using_avidsdpt_onloc", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                    LocationPredicate.Builder.atYLocation(MinMaxBounds.Doubles.ANY),
                    ItemPredicate.Builder.item().of(ModItems.AVID_SDPT.get())))
                .requirements(AdvancementRequirements.anyOf(List.of("using_avidsdpt", "using_avidsdpt_onloc")))
                .save(saver, new ResourceLocation(MOD_ID, "teleportation_root_advancement"));


    }
}
