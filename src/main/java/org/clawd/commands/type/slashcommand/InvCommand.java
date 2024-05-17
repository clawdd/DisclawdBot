package org.clawd.commands.type.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;
import org.clawd.tokens.Constants;

public class InvCommand implements SlashCommand{

    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        String userID = event.getUser().getId();

        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler handler = new SQLEmbeddedHandler();
            handler.replyToNewRegisteredUser(event);
        } else {
            Main.mineworld.inventory.replyWithInventoryFirstEmbedded(event);
            Main.LOG.info("Executed '"+ Constants.INV_COMMAND_ID +"' button");
        }
    }
}
