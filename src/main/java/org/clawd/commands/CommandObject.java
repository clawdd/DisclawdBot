package org.clawd.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandObject {

    /**
     * Function get overridden by concrete slash command
     *
     * @param event The event to be handled
     */
    public void executeCommand(SlashCommandInteractionEvent event);
}
