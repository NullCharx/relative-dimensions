package es.nullbyte.charmiscmods.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.charmiscmods.commands.teams.TeamMgr;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class modTeam {
    private static int RETURN_OK = 0;
    private static int TEAM_NON_EXISTENT = 1;

    private static int TEAM_ALREADY_EXISTENT = 2;
    private static int MEMBER_ALREADY_ADDED = 3;
    private static int MEMBER_NON_EXISTENT_ONTEAM = 4;

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType(Component.translatable("commands.team.add.duplicate"));
    private static final SimpleCommandExceptionType ERROR_TEAM_NON_EXISTENT = new SimpleCommandExceptionType(Component.translatable("Team does not exists"));
    private static final SimpleCommandExceptionType ERROR_ALREADY_ADDED = new SimpleCommandExceptionType(Component.translatable("Player already added to the team"));
    private static final int permissionLevel = 2;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands below

        dispatcher.register(Commands.literal("chTeam").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(permissionLevel);
        }).then(Commands.literal("list").then(Commands.argument("listType", StringArgumentType.word()).executes((context) -> {//modTeam list (teams|players) [ - | teamName]
            String type = StringArgumentType.getString(context, "listType");
            if (type.equals("teams")) {
                return teamList(context.getSource());
            } else if (type.equals("players")) {
                return playerList(context.getSource(), context.getArgument("teamName", String.class));
            } else {
                return RETURN_OK;
            }
        }))).then(Commands.literal("create").then(Commands.argument("TeamName", StringArgumentType.string()).executes((createteam) -> { //modTeam create <TeamName>
            String teamName = StringArgumentType.getString(createteam, "TeamName");
            return teamCreate(createteam.getSource(), teamName);
        }))).then(Commands.literal("remove").then(Commands.argument("TeamName", StringArgumentType.string()).executes((removeteam) -> { //modTeam remove <TeamName>
            String teamName = StringArgumentType.getString(removeteam, "TeamName");
            return teamRemove(removeteam.getSource(), teamName);
        }))).then(Commands.literal("join").then(Commands.argument("TeamName", StringArgumentType.string()).executes((join) -> {//modTeam join <TeamName>
            String teamName = StringArgumentType.getString(join, "TeamName");
            return joinTeam(join.getSource(), teamName);
        }))).then(Commands.literal("leave").then(Commands.argument("PlayerName", StringArgumentType.string()).executes((leave) -> {//modTeam leave <TeamName>
            String playerName = StringArgumentType.getString(leave, "PlayerName");
            return leaveTeam(leave.getSource(),playerName);
        }))));

    }

    private static int teamCreate(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        int returnCode = TeamMgr.addTeam(teamName);
        if (returnCode == TEAM_ALREADY_EXISTENT) {
            throw ERROR_TEAM_ALREADY_EXISTS.create();
        }
        return returnCode;
    }

    private static int teamRemove(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        int returnCode = TeamMgr.removeTeam(teamName);
        if (returnCode == TEAM_NON_EXISTENT) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        }
        return returnCode;
    }

    private static int teamList(CommandSourceStack source) {
        String teamString = TeamMgr.getTeamString();
        source.sendSystemMessage(Component.literal(String.format("TEAMS:\n" + teamString)));
        return 0;
    }

    private static int playerList(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        String teamPString = TeamMgr.getTeamMembersList(teamName);
        if (teamPString == null) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        }
        source.sendSystemMessage(Component.literal(String.format("TEAMS:\n" + teamPString)));

        return 0;
    }

    private static int joinTeam(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        Player joiner = source.getPlayer();
        int returnCode = TeamMgr.addPlayerToTeam(joiner, teamName);
        if (returnCode == TEAM_ALREADY_EXISTENT) {
            throw ERROR_TEAM_ALREADY_EXISTS.create();
        } else if (returnCode == MEMBER_ALREADY_ADDED) {
            throw ERROR_ALREADY_ADDED.create();
        }
        //CHECK IF THE PLAYER HAS ALEREADY A TEAM!
        return 0;
    }

    private static int leaveTeam(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        Player joiner = source.getPlayer();
        int returnCode = TeamMgr.removePlayerFromTeam(joiner, teamName);
        if (returnCode == TEAM_NON_EXISTENT) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        } else if (returnCode == MEMBER_ALREADY_ADDED) {
            throw ERROR_ALREADY_ADDED.create();
        }
        //CHECK IF THE PLAYER HAS ALEREADY A TEAM!
        return 0;

    }
}