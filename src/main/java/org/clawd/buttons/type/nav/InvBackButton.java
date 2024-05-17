package org.clawd.buttons.type.nav;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.CustomButton;
import org.clawd.data.Inventory;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;
import org.clawd.tokens.Constants;

public class InvBackButton implements CustomButton {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();
        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler sqlEmbeddedHandler = new SQLEmbeddedHandler();
            sqlEmbeddedHandler.replyToNewRegisteredUser(event);
        } else {
            Inventory inventory = new Inventory();
            inventory.replyToNextInvPage(event, true);
            Main.LOG.info("Executed '"+ Constants.BACK_INV_BUTTON_ID +"' button");
        }
    }
}
