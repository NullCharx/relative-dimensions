package es.nullbyte.charmiscmods.PlayerTimeLimit;

public class PvpManager {
    private static int PVPstate; //-1 PVP off, 0 PVP on, 1 ULTRA

    public PvpManager() {
        PVPstate = -1;
    }
    public PvpManager(int initialState) {
        if (initialState >= -1 && initialState <= 1){
            PVPstate = initialState;
        } else {
            PVPstate = -1;
        }
    }
    public static void setPVPstate(int state) {
        if(state >= -1 && state <= 1 && PVPstate==state) { //If the state is valid and it's not the same as the current state:
            if (isPVPultra()){ //If PVP is ultra, it means it can only decrease so:
                enableNaturalRegen(); //enable natural regen (normal and non-PVP)
                if(state == -1){ //Check if the target state is PVP off and if it is:
                    disableGlobalDamage(); //disable global damage too
                }
            } else if (isPVPon()){ //If the PVP is on, it means it can both increase and decrease so:
                if (state == 1){ //If it increases:
                    disableNaturalRegen(); //Disable natural regen (ULTA PVP). Global damage is already disabled
                } else if (state == -1){//if it decreases:
                    disableGlobalDamage(); //disable global damage
                }
            } else { //If the PVP is off, it means it can only increase so:
                enableGlobalDamage(); //enable global damage
                if (state == 1){ //Check if the target state is ULTRA PVP and if it is:
                    disableNaturalRegen(); //disable natural regen too
                }
            }
        }

    }

    public static void increasePVPstate() {
        if (PVPstate != 1){ //If the PVP state is not ULTRA PVP, it can increase
            if(isPVPoff()) { //if its currently off, it means it goes to PVP on:
                enableGlobalDamage(); //enable global damage
            } else if (isPVPon()){ //if its currently on, it means it goes to ULTRA PVP:
                disableNaturalRegen(); //Disable natural regen (ULTA PVP)
            }
            PVPstate++; //Increase the PVP state
        }
    }

    public static void decreasePVPstate() {
        if (PVPstate == -1){
            if(isPVPultra()) { //if its currently ultra, it means it goes to PVP on:
                enableNaturalRegen(); //enable natural regen
            } else if (isPVPon()){ //if its currently on, it means it goes to  PVP off:
                disableNaturalRegen(); //Disable global damage
            }
            PVPstate--; //Decrease the PVP state
        }
    }

    public static int getPVPstate() {
        return PVPstate;
    }

    public static boolean isPVPon() {
        return PVPstate == 0;
    }

    public static boolean isPVPultra() {
        return PVPstate == 1;
    }

    public static boolean isPVPoff() {
        return PVPstate == -1;
    }

    private static void disableGlobalDamage() {
        //TODO add all players to a team that has friendly fire disabled
    }
    private static void enableGlobalDamage() {
        //TODO remove all players from a team that has friendly fire disabled
    }

    private static void disableNaturalRegen() {
        //TODO disable natural regen (ULTRA PVP)
    }

    private static void enableNaturalRegen() {
        //TODO enable natural regen (normal and non-PVP)
    }
}
