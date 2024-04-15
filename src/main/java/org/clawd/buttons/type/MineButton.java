package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.Button;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;

public class MineButton implements Button {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();

        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler handler = new SQLEmbeddedHandler();
            handler.replyToNewRegisteredUser(event);
        } else {
            // TODO
            Main.mineworld.updateBiome(event);
            Main.logger.info("Executed 'mine' button");
        }
    }
}
