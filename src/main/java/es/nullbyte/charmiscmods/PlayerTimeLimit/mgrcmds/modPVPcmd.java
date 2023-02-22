package es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class modPVPcmd {
    private static final int TEAM_NON_EXISTENT = 1;
    private static final int TEAM_ALREADY_EXISTENT = 2;
    private static final int MEMBER_ALREADY_ADDED = 3;
    private static final int MEMBER_NON_EXISTENT_ONTEAM = 4;

    private static final SimpleCommandExceptionType ERROR_LEVEL_NOT_VALID = new SimpleCommandExceptionType(Component.translatable("Nivel PVP no valido. EL rango es de -1 (no pvp) a 1 (ULTRA)"));
    private static final SimpleCommandExceptionType ERROR_HIGHEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("El nivel PVP no puede aumentar más"));
    private static final SimpleCommandExceptionType ERROR_LOWEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("El nivel PVP no puede disminuir más"));

    private static final int permissionLevel = 2;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands belows

        dispatcher.register(Commands.literal("chpvp").requires((permission) -> { //Check OP or server agent permission
                    return permission.hasPermission(permissionLevel);
                }).then(Commands.literal("set").then(Commands.argument("level", StringArgumentType.string()).executes((pvpset) -> {//modteam list players <TeamName>
                    String teamName = StringArgumentType.getString(pvpset, "level");
                    return setPVP(pvpset.getSource(),teamName);
                }))));
    };
    private static int setPVP(CommandSourceStack source, String levelStr) throws CommandSyntaxException {
        int level = Integer.parseInt(levelStr);
        if (level<-1 || level>1) {
            throw ERROR_LEVEL_NOT_VALID.create();
        }  else {
            source.sendSystemMessage(Component.literal(String.format("Nivel PVP establecido a: " + level)));
        }
        return 0;
    }


}
