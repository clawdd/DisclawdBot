package org.clawd.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.clawd.buttons.type.*;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ButtonManager extends ListenerAdapter {

    private final HashMap<String, CustomButton> buttons;

    public ButtonManager () {
        this.buttons = new HashMap<>();

        this.buttons.put(Constants.MINE_BUTTON_ID, new MineButton());
        this.buttons.put(Constants.NEXT_SHOP_BUTTON_ID, new ShopNextButton());
        this.buttons.put(Constants.BACK_SHOP_BUTTON_ID, new ShopBackButton());
        this.buttons.put(Constants.CLOSE_BUTTON_ID, new ShopCloseButton());
        this.buttons.put(Constants.BUY_BUTTON_ID, new BuyButton());
        this.buttons.put(Constants.EQUIP_BUTTON_ID, new EquipButton());
    }

    /**
     * Receives a button interaction event and calls on the corresponding
     * button class executeButton() method
     *
     * @param event Received event
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String buttonID = event.getComponentId();
        Main.logger.info("Received button interaction: " + buttonID);

        CustomButton button = buttons.get(buttonID);
        button.executeButton(event);
    }
}
