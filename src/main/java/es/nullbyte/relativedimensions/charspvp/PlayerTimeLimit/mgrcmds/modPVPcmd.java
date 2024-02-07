package es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.mgrcmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.PvpManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class modPVPcmd {
    private static final SimpleCommandExceptionType ERROR_LEVEL_NOT_VALID = new SimpleCommandExceptionType(Component.translatable("cmds.modpvp.error.levelnotvalid"));
    private static final SimpleCommandExceptionType ERROR_HIGHEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("cmds.modpvp.error.highestlevel"));
    private static final SimpleCommandExceptionType ERROR_LOWEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("cmds.modpvp.error.lowestlevel"));

    private static final int permissionLevel = 3;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands belows

        dispatcher.register(Commands.literal("chpvp").requires((permission) -> { //Check OP or server agent permission
                    return permission.hasPermission(permissionLevel);
                }).then(Commands.literal("set").then(Commands.argument("level", StringArgumentType.string()).executes((pvpset) -> {//chpvp set <pvplevel>
                    String teamName = StringArgumentType.getString(pvpset, "level");
                    return setPVP(pvpset.getSource(),teamName);
                }))).then(Commands.literal("increase").executes((pvpadd) -> {//chpvp increase
                    return addPVP(pvpadd.getSource());
                })).then(Commands.literal("decrease").executes((pvpminus) -> { //chpvp decrease
                    return minusPVP(pvpminus.getSource());
                })).then(Commands.literal("refresh").executes((refreshcoll) -> { //chpvp refresh
                    return collisionSatateRefresh();
                })));
    }

    private static int setPVP(CommandSourceStack source, String levelStr) throws CommandSyntaxException {

        int level = Integer.parseInt(levelStr);
        if (level< -1 || level>1) {
            throw ERROR_LEVEL_NOT_VALID.create();
        }
        PvpManager.setPVPstate(level);
        prettyPrint(source);
        return 0;
    }

    private static int addPVP(CommandSourceStack source) throws CommandSyntaxException {
        if(PvpManager.isPVPultra()){
            throw ERROR_HIGHEST_LEVEL.create();
        }

        PvpManager.increasePVPstate();
        prettyPrint(source);
        return 0;
    }

    private static int minusPVP(CommandSourceStack source) throws CommandSyntaxException {
        if(PvpManager.isPVPoff()){
            throw ERROR_LOWEST_LEVEL.create();
        }
        PvpManager.decreasePVPstate();
        prettyPrint(source);
        return 0;
    }

    private static int collisionSatateRefresh() {
        if (PvpManager.isPVPoff()) {
            PvpManager.disableGlobalDamage();
        } else {
            PvpManager.enableGlobalDamage();
        }
        PvpManager.setPVPstate(PvpManager.getPVPstate());
        return 0;
    }
    private static void prettyPrint(CommandSourceStack source) {
        BlockPos deathBP =  new BlockPos(0,100,0);
        source.getLevel().playSound(null, deathBP, SoundEvents.AMBIENT_CAVE.get(), SoundSource.PLAYERS, 100.0f, 1.0f);
        MutableComponent message;
        if(PvpManager.isPVPoff()){
            message = Component.translatable("cmds.modpvp.level.off");
            message.withStyle(ChatFormatting.YELLOW);
        } else if (PvpManager.isPVPon()) {
            message = Component.translatable("cmds.modpvp.level.on");
            message.withStyle(ChatFormatting.DARK_RED);
        } else {
            message = Component.translatable("cmds.modpvp.level.ultra");
            message.withStyle(ChatFormatting.DARK_PURPLE);
        }


        for (ServerPlayer p : source.getLevel().getServer().getPlayerList().getPlayers()) {
            p.sendSystemMessage(message, false);
        }
    }
}
