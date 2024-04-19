package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.Button;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;

public class ShopBackButton implements Button {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();
        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler sqlEmbeddedHandler = new SQLEmbeddedHandler();
            sqlEmbeddedHandler.replyToNewRegisteredUser(event);
        } else {
            Main.mineworld.shop.replyToNextShopPage(event, true);
        }
    }
}
