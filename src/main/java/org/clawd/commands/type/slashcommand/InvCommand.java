package org.clawd.commands.type.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;

public class InvCommand implements SlashCommand{

    //TODO
    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        String userID = event.getUser().getId();

        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler handler = new SQLEmbeddedHandler();
            handler.replyToNewRegisteredUser(event);
        } else {
            event.reply("Not implemented yet.").setEphemeral(true).queue();
        }
    }
}
