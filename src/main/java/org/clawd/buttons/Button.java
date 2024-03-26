package org.clawd.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface Button {

    /**
     * Function gets overridden by concrete button
     *
     * @param event The event to be handled
     */
    public void executeButton(ButtonInteractionEvent event);
}
