package org.clawd.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.clawd.main.Main;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandManager extends ListenerAdapter {

    private final HashMap<String, SlashCommand> commands;

    public CommandManager() {
        this.commands = new HashMap<>();
    }

    /**
     * Receives slash command interaction event and calls on the
     * corresponding command class executeCommand() method
     *
     * @param event Received event
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        Main.logger.info("Received slash command: " + command);

        SlashCommand slashCommand = commands.get(command);
        slashCommand.executeCommand(event);
    }
}
