package org.clawd.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.clawd.commands.type.slashcommand.*;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CommandManager extends ListenerAdapter {

    private final HashMap<String, SlashCommand> commands;

    public CommandManager() {
        this.commands = new HashMap<>();

        this.commands.put(Constants.BIOME_COMMAND_ID, new BiomeCommand());
        this.commands.put(Constants.STATS_COMMAND_ID, new StatsCommand());
        this.commands.put(Constants.SHOP_COMMAND_ID, new ShopCommand());
        this.commands.put(Constants.ITEM_COMMAND_ID, new ItemCommand());
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
