package es.nullbyte.relativedimensions.blocks.aberrant;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class aberrantBlock extends Block {

    public aberrantBlock(Properties properties) {
        super(properties.emissiveRendering((state, world, pos) -> true)
                .lightLevel((light) -> 15));
    }


    
            
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter blocKGetter, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("block.relativedimensions.aberrant_block.tooltip").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE).withStyle(net.minecraft.ChatFormatting.ITALIC));
        super.appendHoverText(pStack, blocKGetter, pTooltipComponents, pIsAdvanced);
    }
}