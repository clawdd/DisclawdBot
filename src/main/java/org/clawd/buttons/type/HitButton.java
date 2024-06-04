package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.CustomButton;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

public class HitButton implements CustomButton {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();
        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            Main.sqlHandler.sqlEmbeddedHandler.replyToNewRegisteredUser(event);
        } else {
            String buttonID = event.getComponentId();
            int mobID = Integer.parseInt(buttonID.replace(Constants.HIT_BUTTON_ID, ""));

            // TODO

            event.deferEdit().queue();
            event.getMessage().delete().queue();

            Main.LOG.info("Executed '" + Constants.HIT_BUTTON_ID + "' button");
        }
    }
}
