package es.nullbyte.relativedimensions.effects.init;

import es.nullbyte.relativedimensions.effects.VoidBleed;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModEffects {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final RegistryObject<MobEffect> VOID_BLEED = EFFECTS.register("void_bleed",
            () -> new VoidBleed(MobEffectCategory.HARMFUL, 0x3D1585));

    public static void registerEffects(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}
