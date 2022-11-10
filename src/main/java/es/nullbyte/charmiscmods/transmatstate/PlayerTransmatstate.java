package es.nullbyte.charmiscmods.transmatstate;


//This is an example of Player capability. It is used to store the player's transmat state.
import net.minecraft.nbt.CompoundTag;
public class PlayerTransmatstate {
    private int transmatstate;
    private final int TRANSMATESTATEMAX = 100;

    public int getTransmatstate() {
        return transmatstate;
    }

    public void addState(int add) {
        this.transmatstate = Math.min(transmatstate + add, TRANSMATESTATEMAX);
    }

    public void subState(int sub) {
        this.transmatstate = Math.max(transmatstate -sub, TRANSMATESTATEMAX);
    }

    public void setTransmatstate(int transmatstate) {
        transmatstate = Math.min(transmatstate, TRANSMATESTATEMAX);
        transmatstate = Math.max(transmatstate, 0);
        this.transmatstate = transmatstate;
    }

    public void copyFrom(PlayerTransmatstate source) {
        this.transmatstate = source.transmatstate;
    }

    public void saveNBTData (CompoundTag nbt){
        nbt.putInt("transmatstate", transmatstate);
    }

    public void loadNBTData (CompoundTag nbt){
        transmatstate = nbt.getInt("transmatstate");
    }
}
