package org.clawd.buttons.type;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.clawd.buttons.CustomButton;
import org.clawd.data.items.Item;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.sql.SQLStatsHandler;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.util.List;

public class BuyButton implements CustomButton {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();

        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler sqlEmbeddedHandler = new SQLEmbeddedHandler();
            sqlEmbeddedHandler.replyToNewRegisteredUser(event);
        } else {

            List<MessageEmbed> embeds = event.getMessage().getEmbeds();
            String embeddedTitle = embeds.getFirst().getTitle();
            String itemName = embeddedTitle.replace(":mag:", "").strip();
            Item item = Main.mineworld.shop.getItemByName(itemName);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Couldn't buy item");
            embedBuilder.setColor(Color.ORANGE);

            if (item == null) {
                embedBuilder.setDescription("Something went wrong, the cake is a lie!");
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                return;
            }
            SQLStatsHandler sqlStatsHandler = new SQLStatsHandler();
            SQLInventoryHandler sqlInventoryHandler = new SQLInventoryHandler();

            int itemPrice = item.getPrice();
            int itemID = item.getUniqueID();

            sqlStatsHandler.changeGoldCount(userID, -itemPrice);
            sqlInventoryHandler.addItemToPlayer(userID, itemID);
            embedBuilder.setDescription("You successfully acquired " + item.getEmoji() + "**" + itemName + "**" + item.getEmoji() + " !");

            event.editComponents(
                    ActionRow.of(
                            Button.success(Constants.BUY_BUTTON_ID, "Buy").asDisabled(),
                            Button.success(Constants.EQUIP_BUTTON_ID, "Equip").asEnabled()
                    )
            ).queue();
            InteractionHook hook = event.getHook();
            hook.sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            Main.logger.info("Executed '"+ Constants.BUY_BUTTON_ID +"' button");
        }
    }

    private void updateItemMessage(InteractionHook event) {
        event.editOriginalComponents(
                ActionRow.of(
                        Button.success(Constants.BUY_BUTTON_ID, "Buy").asDisabled(),
                        Button.success(Constants.EQUIP_BUTTON_ID, "Equip").asEnabled()
                )
        ).queue();
    }
}
