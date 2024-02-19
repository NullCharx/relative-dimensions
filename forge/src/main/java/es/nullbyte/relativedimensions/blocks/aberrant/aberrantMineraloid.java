package es.nullbyte.relativedimensions.blocks.aberrant;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

public class aberrantMineraloid extends Block {

    public aberrantMineraloid(Properties properties) {
        super(properties.emissiveRendering((state, world, pos) -> true)
                .lightLevel((light) -> 3));
    }


    
            
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter blocKGetter, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("block.relativedimensions.aberrant_mineraloid.tooltip").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE).withStyle(net.minecraft.ChatFormatting.ITALIC));
        super.appendHoverText(pStack, blocKGetter, pTooltipComponents, pIsAdvanced);
    }
}