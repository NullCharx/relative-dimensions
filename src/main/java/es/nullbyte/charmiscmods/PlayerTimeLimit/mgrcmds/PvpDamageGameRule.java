package es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds;

public class PvpDamageGameRule {
    //TODO Se if ther is a way to dsiable hurt animation and also syncronhize the damage state!!!
    private static Boolean pvpDamage = true;


    public static void set(Boolean value) {
        pvpDamage = value;
    }


    public static Boolean get() {
        return pvpDamage;
    }


    public boolean getName() {
        return pvpDamage;
    }
}