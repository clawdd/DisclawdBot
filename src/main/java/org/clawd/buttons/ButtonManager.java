package org.clawd.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.clawd.buttons.type.MineButton;
import org.clawd.buttons.type.ShopBackButton;
import org.clawd.buttons.type.ShopNextButton;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ButtonManager extends ListenerAdapter {

    private final HashMap<String, Button> buttons;

    public ButtonManager () {
        this.buttons = new HashMap<>();

        this.buttons.put(Constants.MINE_BUTTON_ID, new MineButton());
        this.buttons.put(Constants.NEXT_BUTTON_ID, new ShopNextButton());
        this.buttons.put(Constants.BACK_BUTTON_ID, new ShopBackButton());
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

        Button button = buttons.get(buttonID);
        button.executeButton(event);
    }
}
