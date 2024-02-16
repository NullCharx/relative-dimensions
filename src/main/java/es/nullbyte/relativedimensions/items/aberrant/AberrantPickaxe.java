package es.nullbyte.relativedimensions.items.aberrant;

import es.nullbyte.relativedimensions.items.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;


public class AberrantPickaxe extends PickaxeItem {
    public AberrantPickaxe(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    //https://moddingtutorials.org/advanced-items


    // makes it repairable
    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack tool, ItemStack material) {
        return material.getItem() == ModItems.AVID_SDPT.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level plevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        //MAke a component which has the translation key of the tooltip and then the modifiers formatted as bold purple text
        pTooltipComponents.add(Component.translatable("item.relativedimensions.aberrant_pickaxe.tooltip"));
        pTooltipComponents.add(Component.translatable("item.relativedimensions.aberrant_pickaxe.tooltip_modifiers_list").withStyle(ChatFormatting.BOLD, ChatFormatting.LIGHT_PURPLE));

        super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
    }
}
