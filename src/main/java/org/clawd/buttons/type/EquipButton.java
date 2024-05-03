package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.CustomButton;

public class EquipButton implements CustomButton {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        event.reply("Not implemented yet.").setEphemeral(true).queue();
    }
}
