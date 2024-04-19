package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.Button;
import org.clawd.main.Main;

public class ShopNextButton implements Button {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        Main.mineworld.shop.replyToNextShopPage(event,false);
    }
}
