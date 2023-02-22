package es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PvpManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class modTimercmd {

    private static final SimpleCommandExceptionType ERROR_LEVEL_NOT_VALID = new SimpleCommandExceptionType(Component.translatable("Nivel PVP no valido. EL rango es de -1 (no pvp) a 1 (ULTRA)"));
    private static final SimpleCommandExceptionType ERROR_HIGHEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("El nivel PVP no puede aumentar más"));
    private static final SimpleCommandExceptionType ERROR_LOWEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("El nivel PVP no puede disminuir más"));

    private static final int permissionLevel = 2;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands belows

        dispatcher.register(Commands.literal("chtime").requires((permission) -> { //Check OP or server agent permission
                    return permission.hasPermission(permissionLevel);
                }).then(Commands.literal("add").then(Commands.argument("time", StringArgumentType.string())
                    .then(Commands.literal("player").executes((timeadd) -> {//chpvp increase
                        String secstoadd = StringArgumentType.getString(timeadd, "time");
                        String playername = StringArgumentType.getString(timeadd, "player");
                        return addtime(timeadd.getSource(),secstoadd,playername);
                    }
                )))).then(Commands.literal("decrease").executes((pvpminus) -> { //chpvp decrease
                })).then(Commands.literal("refresh").executes((refreshcoll) -> { //chpvp refresh
                    return collisionSatateRefresh(refreshcoll.getSource());
                })));
    };
    private static int addtime(CommandSourceStack source, String seconds,String playername) throws CommandSyntaxException {
        int level = Integer.parseInt(seconds);

        if (level< -1 || level>1) {
            throw ERROR_LEVEL_NOT_VALID.create();
        }  else {
            PvpManager.setPVPstate(level, source.getLevel() );
        }
        return 0;
    }

    private static int addPVP(CommandSourceStack source) throws CommandSyntaxException {
        if(PvpManager.isPVPultra()){
            throw ERROR_HIGHEST_LEVEL.create();
        }
        PvpManager.increasePVPstate(source.getLevel());
        return 0;
    }

    private static int minusPVP(CommandSourceStack source) throws CommandSyntaxException {
        if(PvpManager.isPVPoff()){
            throw ERROR_LOWEST_LEVEL.create();
        }
        PvpManager.decreasePVPstate(source.getLevel());
        return 0;
    }

    private static int collisionSatateRefresh(CommandSourceStack source) throws CommandSyntaxException {
        if (PvpManager.isPVPoff()) {
            PvpManager.disableGlobalDamage(source.getLevel());
        } else {
            PvpManager.enableGlobalDamage(source.getLevel());
        }
        return 0;
    }

}
