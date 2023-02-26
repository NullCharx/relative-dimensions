package es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds;

public class PvpDamageGameRule {
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