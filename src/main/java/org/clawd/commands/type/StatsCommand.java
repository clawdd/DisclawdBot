package org.clawd.commands.type;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.clawd.commands.SlashCommand;
import org.clawd.data.Generator;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;
import org.clawd.sql.SQLStatsHandler;

import java.awt.*;

public class StatsCommand implements SlashCommand {
    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        String userID = event.getUser().getId();

        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler handler = new SQLEmbeddedHandler();
            handler.replyToNewRegisteredUser(event);
        } else {
            SQLStatsHandler sqlStatsHandler = new SQLStatsHandler();

            int minedCount = sqlStatsHandler.getMinedCountFromUser(userID);
            int mobKills = sqlStatsHandler.getMobKillsFromUser(userID);
            int bossKills = sqlStatsHandler.getBossKillsFromUser(userID);
            double xpCount = sqlStatsHandler.getXPCountFromUser(userID);
            int goldCount = sqlStatsHandler.getGoldCountFromUser(userID);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Your stats - " + event.getUser().getName() + " - lvl. " + new Generator().computeLevel(xpCount));
            embedBuilder.setColor(Color.GREEN);

            embedBuilder.addField("**Times Mined** :pick:", String.valueOf(minedCount), false);
            embedBuilder.addField("**Mob Kills** :skull:", String.valueOf(mobKills), false);
            embedBuilder.addField("**Bosses slain** :skull_crossbones:", String.valueOf(bossKills), false);
            embedBuilder.addField("**XP** :sparkles:", String.valueOf(xpCount), false);
            embedBuilder.addField("**Gold coins** :coin:", String.valueOf(goldCount), false);

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }
}
