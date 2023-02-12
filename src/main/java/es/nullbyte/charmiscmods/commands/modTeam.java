package es.nullbyte.charmiscmods.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.charmiscmods.commands.teams.TeamMgr;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;

/**
 * JAVA DOC:
 * Comando de equipos personalziado BASE.
 * Remember to check onServerStart on main (the command is registered there)
 * This implementation is pluraly exclusive: meaning that a given player can only be in one team
 */
public class modTeam {
    private static final int TEAM_NON_EXISTENT = 1;
    private static final int TEAM_ALREADY_EXISTENT = 2;
    private static final int MEMBER_ALREADY_ADDED = 3;
    private static final int MEMBER_NON_EXISTENT_ONTEAM = 4;

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType(Component.translatable("El equipo introducido ya existe"));
    private static final SimpleCommandExceptionType ERROR_TEAM_NON_EXISTENT = new SimpleCommandExceptionType(Component.translatable("El equipo introducido no existe"));
    private static final SimpleCommandExceptionType ERROR_ALREADY_ADDED = new SimpleCommandExceptionType(Component.translatable("Jugador ya está en equipo introducido"));
    private static final SimpleCommandExceptionType ERROR_UNAVAILABLE_PLAYER = new SimpleCommandExceptionType(Component.translatable("Jugador no reconocido (inexistente u offline)"));
    private static final SimpleCommandExceptionType ERROR_ON_VANILLA_INTERFACE = new SimpleCommandExceptionType(Component.translatable("Error durante la comunicación con la interfaz de equipos vainilla"));

    private static final int permissionLevel = 2;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands belows

        dispatcher.register(Commands.literal("team").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(permissionLevel);
        }).then(Commands.literal("clist").then(Commands.literal("teams").executes((teamsList) -> {//modteam list teams
            return teamList(teamsList.getSource());
        })).then(Commands.literal("players").then(Commands.argument("TeamName", StringArgumentType.string()).executes((teamPlayerList) -> {//modteam list players <TeamName>
            String teamName = StringArgumentType.getString(teamPlayerList, "TeamName");
            return playerList(teamPlayerList.getSource(),teamName);
        })))).then(Commands.literal("cadd").then(Commands.argument("TeamName", StringArgumentType.string()).executes((createteam) -> { //modTeam create <TeamName>
            String teamName = StringArgumentType.getString(createteam, "TeamName");
            return teamCreate(createteam.getSource(), teamName);
        }))).then(Commands.literal("cremove").then(Commands.argument("TeamName", StringArgumentType.string()).executes((removeteam) -> { //modTeam remove <TeamName>
            String teamName = StringArgumentType.getString(removeteam, "TeamName");
            return teamRemove(removeteam.getSource(), teamName);
        }))).then(Commands.literal("cjoin").then(Commands.argument("TeamName", StringArgumentType.string()).executes((join) -> {
            String teamName = StringArgumentType.getString(join, "TeamName");
            return joinTeam(join.getSource(), teamName, "");
        }).then(Commands.argument("PlayerName", StringArgumentType.string()).executes((joinP) -> {
            String teamName = StringArgumentType.getString(joinP, "TeamName");
            String playerName = StringArgumentType.getString(joinP, "PlayerName");
            return joinTeam(joinP.getSource(), teamName, playerName);
        })))).then(Commands.literal("cleave").executes((leave) -> {//modTeam leave <TeamName>
            return leaveTeam(leave.getSource(),"");
        }).then(Commands.argument("PlayerName", StringArgumentType.string()).executes((leavep) -> {//modTeam leave <TeamName>
            String playerName = StringArgumentType.getString(leavep, "PlayerName");
            return leaveTeam(leavep.getSource(), playerName);
        })))
        );
    }

    private static int teamCreate(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        //Custom team add
        int returnCode = TeamMgr.addTeam(teamName);
        if (returnCode == TEAM_ALREADY_EXISTENT) {
            throw ERROR_TEAM_ALREADY_EXISTS.create();
        }

        //Vanilla team add
        PlayerTeam addedTeam = source.getScoreboard().addPlayerTeam(teamName);
        if (addedTeam == null) {
            throw ERROR_ON_VANILLA_INTERFACE.create();
        }

        //Code exit
        source.sendSystemMessage(Component.literal(String.format("Equipo: " + teamName + " creado")));
        return returnCode;
    }

    private static int teamRemove(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        //Custom team remove
        int returnCode = TeamMgr.removeTeam(teamName);
        if (returnCode == TEAM_NON_EXISTENT) {
            source.sendSystemMessage(Component.literal(String.format("Equipo de nombre: " + teamName + " no existe")));
            throw ERROR_TEAM_NON_EXISTENT.create();
        }

        //Vanilla team remove
        PlayerTeam vanillaPlayerTeam = source.getScoreboard().getPlayerTeam(teamName);
        if (vanillaPlayerTeam == null) {
            throw ERROR_ON_VANILLA_INTERFACE.create();
        }
        source.getScoreboard().removePlayerTeam(vanillaPlayerTeam);

        source.sendSystemMessage(Component.literal(String.format("Equipo de nombre: " + teamName + " borrado")));
        return returnCode;
    }

    private static int teamList(CommandSourceStack source) {
        String teamString = TeamMgr.getTeamString();
        source.sendSystemMessage(Component.literal(String.format("TEAMS:\n\n" + teamString)));
        return 0;
    }

    private static int playerList(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        String teamPString = TeamMgr.getTeamMembersList(teamName);
        if (teamPString == null) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        }
        source.sendSystemMessage(Component.literal(String.format("MEMBERS OF:" + teamName + "\n" + teamPString)));

        return 0;
    }

    private static int joinTeam(CommandSourceStack source, String teamName, String playerName) throws CommandSyntaxException {
        //Custom player add
        Player joiner; //Player to join
        if (playerName.isEmpty()) { //If no playerargument provided, use command caster
            joiner = source.getPlayer();
        } else { //If playerargument provided, check the online players:
            joiner = source.getServer().getPlayerList().getPlayerByName(playerName);
        }
        if (joiner == null) { //If no player was found after checks, end command
            throw ERROR_UNAVAILABLE_PLAYER.create();
        }

        int returnCode = TeamMgr.addPlayerToTeam(joiner, teamName);
        if (returnCode == TEAM_NON_EXISTENT) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        } else if (returnCode == MEMBER_ALREADY_ADDED) {
            throw ERROR_ALREADY_ADDED.create();
        }

        //Check first if the player alredy has a saved team
        String lastTeam = joiner.getPersistentData().getString("playerchTeam");
        if (!lastTeam.isEmpty()) {//If they do, remove them from team
            TeamMgr.removePlayerFromTeam(joiner, lastTeam);
        }

        //Vanilla playeradd
        PlayerTeam vanillaPlayerTeam = source.getScoreboard().getPlayerTeam(teamName);
        if (vanillaPlayerTeam == null) {
            throw ERROR_ON_VANILLA_INTERFACE.create();
        }
        boolean added = source.getScoreboard().addPlayerToTeam(playerName, vanillaPlayerTeam);
        if (!added) {
            throw ERROR_ON_VANILLA_INTERFACE.create();
        }
        joiner.getPersistentData().putString("playerchTeam",teamName);
        source.sendSystemMessage(Component.literal(String.format("Jugador: " + joiner.getName().getString() + " se unió a equipo: " + teamName)));
        return 0;
    }

    private static int leaveTeam(CommandSourceStack source, String playerName) throws CommandSyntaxException {
        //Custom player remove
        Player joiner;
        if (playerName.isEmpty()) { //If no playerargument provided, use command caster
            joiner = source.getPlayer();
        } else { //If playerargument provided, check the online players:
            joiner = source.getServer().getPlayerList().getPlayerByName(playerName);
        }
        if (joiner == null) { //If no player was found after checks, end command
            throw ERROR_UNAVAILABLE_PLAYER.create();
        }

        String currentTeam = joiner.getPersistentData().getString("playerchTeam");
        if (currentTeam == null || currentTeam.isEmpty()){
            source.sendSystemMessage(Component.literal(String.format("Jugador borrado de equipo")));
            return 0;
        }
        int returnCode = TeamMgr.removePlayerFromTeam(joiner, currentTeam);

        if (returnCode == TEAM_NON_EXISTENT) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        } else if (returnCode == MEMBER_NON_EXISTENT_ONTEAM) {
            throw ERROR_ALREADY_ADDED.create();
        }
        joiner.getPersistentData().putString("playerchTeam","");

        //Vanilla palyeremove CHECK HERE
        PlayerTeam vanillaPlayerTeam = source.getScoreboard().getPlayerTeam(currentTeam);
        if (vanillaPlayerTeam == null) {
            throw ERROR_ON_VANILLA_INTERFACE.create();
        }
        source.getScoreboard().removePlayerFromTeam(playerName, vanillaPlayerTeam);
        source.sendSystemMessage(Component.literal(String.format("Jugador borrado de equipo")));

        return 0;

    }


}
