package es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.mgrcmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.relativedimensions.charspvp.network.DailyTimeLimitHandler;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.relativedimensions.charspvp.network.packet.S2CDailyTimeLimit;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Supplier;

public class modTimercmd extends PlayerTimeManager {
    private static final SimpleCommandExceptionType ERROR_USER_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("cmds.timerpvp.error.usernotfound"));
    private static final SimpleCommandExceptionType RESET_TIME_OUT_OF_RANGE =  new SimpleCommandExceptionType(Component.translatable("cmds.timerpvp.error.timeargumentOOB"));
    private static final SimpleCommandExceptionType DAILY_AMOUNT_TO_LOW =  new SimpleCommandExceptionType(Component.translatable("cmds.timerpvp.error.timeargumenttoosmall"));
    private static final SimpleCommandExceptionType DAILY_AMOUNT_OUT_OF_RANGE =  new SimpleCommandExceptionType(Component.translatable("cmds.timerpvp.error.timeargumenttoobig"));

    private static final int permissionLevel = 3;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands belows

        dispatcher.register(Commands.literal("chtime").requires((permission) -> { //Check OP or server agent permission
                    return permission.hasPermission(permissionLevel);
                }).then(Commands.literal("add").then(Commands.argument("time", IntegerArgumentType.integer())
                    .then(Commands.argument("player", StringArgumentType.string()).executes((timeadd) -> {//chpvp increase
                        int secstoadd = IntegerArgumentType.getInteger(timeadd, "time");
                        String playername = StringArgumentType.getString(timeadd, "player");
                        return addTime(timeadd.getSource(),secstoadd,playername);
                    })
                ))).then(Commands.literal("remove").then(Commands.argument("time", IntegerArgumentType.integer())
                .then(Commands.argument("player", StringArgumentType.string()).executes((timeremove) -> {//chpvp increase
                    int secstoadd = IntegerArgumentType.getInteger(timeremove, "time");
                        String playername = StringArgumentType.getString(timeremove, "player");
                        return substractTime(timeremove.getSource(),secstoadd,playername);
                })))).then(Commands.literal("set").then(Commands.argument("time", IntegerArgumentType.integer())
                    .then(Commands.argument("player", StringArgumentType.string()).executes((timeset) -> {//chpvp increase
                    int secstoadd = IntegerArgumentType.getInteger(timeset, "time");
                        String playername = StringArgumentType.getString(timeset, "player");
                        return setTime(timeset.getSource(),secstoadd,playername);
                })))).then(Commands.literal("show").then(Commands.argument("player", StringArgumentType.string())
                    .executes((timeshow) -> {//chpvp increase
                        String playername = StringArgumentType.getString(timeshow, "player");
                        return showTime(timeshow.getSource(),playername);
                })).then(Commands.literal("limit") .executes((showstate) -> {
                    return showDailyLimit(showstate.getSource());
                })).then(Commands.literal("resetTime") .executes((showstatereset) -> {
                    return showResetTime(showstatereset.getSource());
                }))).then(Commands.literal("isenabled") .executes((showstate) -> {
                    return timerState(showstate.getSource());
                })).then(Commands.literal("toggleTimer") .executes((togglestate) -> {
                    return toggleTimer(togglestate.getSource());
                })).then(Commands.literal("change").then(Commands.literal("resetTime")
                .then(Commands.argument("hour", IntegerArgumentType.integer()).then(Commands.argument("minute", IntegerArgumentType.integer())
                .executes((resetChange) -> {//chpvp increase
                    int hour = IntegerArgumentType.getInteger(resetChange, "hour");
                    int min = IntegerArgumentType.getInteger(resetChange, "minute");
                    return changeResetTime(resetChange.getSource(),hour,min);
                })))).then(Commands.literal("dailyAmount")
                .then(Commands.argument("seconds", IntegerArgumentType.integer()).executes((resetDailyPT) -> {//chpvp increase
                            int secs = IntegerArgumentType.getInteger(resetDailyPT, "seconds");
                            return changeDailyPTAmount(resetDailyPT.getSource(),secs);
                })))
        ));

    }

    private static int addTime(CommandSourceStack source, int seconds, String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).removeTimePlayed(seconds);
        return 0;
    }

    private static int substractTime(CommandSourceStack source, int seconds,String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).addTimePlayed(seconds);
        return 0;
    }

    private static int setTime(CommandSourceStack source, int seconds,String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).setTimePlayed(seconds);
        return 0;
    }

    private static int showTime(CommandSourceStack source, String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        Supplier<Component> messageSupplier = () -> {
            String timePlayedToday = LocalTime.ofSecondOfDay(PlayerTimeManager.getTracker(uuid).getSecsPlayed())
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            return Component.translatable("cmds.timerpvp.timeplayedtoday", playername, Component.literal(timePlayedToday));
        };

        source.sendSuccess(messageSupplier, false);
        return 0;
    }
    private static int timerState(CommandSourceStack source) {
        if(PlayerTimeManager.isTimerEnabled()){
            source.sendSystemMessage(Component.translatable("cmds.timerpvp.timerenabled"));
        } else {
            source.sendSystemMessage(Component.translatable("cmds.timerpvp.timerdisabled"));
        }
        return 0;
    }

    private static int toggleTimer(CommandSourceStack source) {
        PlayerTimeManager.toggleTimer();
        BlockPos deathBP =  new BlockPos(0,100,0);
        source.getLevel().playSound(null, deathBP, SoundEvents.BELL_BLOCK , SoundSource.PLAYERS, 100.0f, 1.0f);
        source.getLevel().playSound(null, deathBP, SoundEvents.BELL_RESONATE , SoundSource.PLAYERS, 100.0f, 1.0f);

        MutableComponent message;

        if(PlayerTimeManager.isTimerEnabled()){
            message= Component.translatable("cmds.timerpvp.publictimerenabled");
        } else {
            message= Component.translatable("cmds.timerpvp.publictimerdisabled");
        }
        message.withStyle(ChatFormatting.BOLD);
        for (ServerPlayer p : source.getLevel().getServer().getPlayerList().getPlayers()) {
            p.sendSystemMessage(message, false);
        }
        return 0;
    }

    private static int changeResetTime(CommandSourceStack source, int hour, int min) throws CommandSyntaxException {
        if (hour <0 || min <0 || hour >24 || min >59){
            throw RESET_TIME_OUT_OF_RANGE.create();
        } else {
            PlayerTimeManager.setResetTime(hour,min);
            Supplier<Component> systemMessageSupplier = () -> {
                return Component.translatable("cmds.timerpvp.resettimeset",
                        Component.literal(String.valueOf(hour)),
                        Component.literal(String.valueOf(min)));
            };
            source.sendSuccess(systemMessageSupplier, false);
        }
        return 0;
    }


    //Toggle and show state of countdown
    private static int changeDailyPTAmount(CommandSourceStack source, int seconds) throws CommandSyntaxException {
        if (seconds < 60) {
            throw DAILY_AMOUNT_TO_LOW.create();
        } if (seconds > 99*3600){
            throw DAILY_AMOUNT_OUT_OF_RANGE.create();

        }else {
            PlayerTimeManager.setDailyTimeLimit(seconds);

            source.sendSystemMessage(Component.translatable("cmds.timerpvp.dailytimelimitset",
                    LocalTime.ofSecondOfDay(seconds).format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

            for (ServerPlayer p: source.getLevel().players()){ //update the timer for all online players GUIrender
                DailyTimeLimitHandler.sendToPlayer(new S2CDailyTimeLimit(seconds), p);

            }
        }
        return 0;
    }

    private static int showDailyLimit(CommandSourceStack source) {
        Supplier<Component> messageSupplier = () -> {
            String timeString = LocalTime.ofSecondOfDay(PlayerTimeManager.getDailyTimeLimit()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            return Component.translatable("cmds.timerpvp.showtimelimit", Component.literal(timeString));
        };

        source.sendSuccess(messageSupplier, false);
        return 0;
    }

    private static int showResetTime(CommandSourceStack source) {
        Supplier<Component> systemMessageSupplier = () -> {
            String resetTimeString = PlayerTimeManager.getResetTime().toString();
            return Component.translatable("cmds.timerpvp.showresettime" + resetTimeString);
        };

        source.sendSuccess(systemMessageSupplier, false);
        return 0;
    }

}
