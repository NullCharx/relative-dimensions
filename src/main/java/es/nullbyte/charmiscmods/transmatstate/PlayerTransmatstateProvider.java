package es.nullbyte.charmiscmods.transmatstate;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

// a transmatstate capability should be made available to the player
public class PlayerTransmatstateProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerTransmatstate> TRANSMATSTATE_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerTransmatstate>() {});
    private PlayerTransmatstate transmatstate = null;
    private final LazyOptional<PlayerTransmatstate> optional = LazyOptional.of(this::createPlayerTransmatstate);

    private PlayerTransmatstate createPlayerTransmatstate() {
        if(this.transmatstate == null) {
            this.transmatstate = new PlayerTransmatstate();
        }
        return this.transmatstate;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == TRANSMATSTATE_CAPABILITY) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerTransmatstate().saveNBTData(nbt); //Returns the already created instance of PlayerTransmatstate if there is one, otherwise creates a new one
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerTransmatstate().loadNBTData(nbt);
    }
}
