package es.nullbyte.relativedimensions.effects;

import es.nullbyte.relativedimensions.effects.aberrant.sword.DimensionalShift;
import es.nullbyte.relativedimensions.effects.aberrant.sword.VoidBleed;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOD_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MOD_ID);
    public static final RegistryObject<MobEffect> VOID_BLEED = MOD_EFFECTS.register("void_bleed",
            () -> new VoidBleed(MobEffectCategory.HARMFUL, 0x3D1585));

    public static final RegistryObject<MobEffect> DIMENSIONAL_NAUSEA = MOD_EFFECTS.register("dimensional_nausea",
            () -> new DimensionalShift(MobEffectCategory.HARMFUL, 0x3D1585));

    public static void registerEffects(IEventBus eventBus) {
        MOD_EFFECTS.register(eventBus);
    }
}