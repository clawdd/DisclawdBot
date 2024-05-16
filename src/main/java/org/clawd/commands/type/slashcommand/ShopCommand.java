package org.clawd.commands.type.slashcommand;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

public class ShopCommand implements SlashCommand {
    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {
        Main.mineworld.shop.replyWithShopFirstEmbedded(event);
        Main.LOG.info("Executed '"+ Constants.SHOP_COMMAND_ID +"' command");

    }
}
