package org.clawd.buttons.type;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.Button;
import org.clawd.data.Generator;
import org.clawd.data.items.Item;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.sql.SQLStatsHandler;

import java.awt.*;
import java.util.List;

public class BuyButton implements Button {
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
            int itemLvl = item.getReqLvl();
            int itemID = item.getUniqueID();

            int userGold = sqlStatsHandler.getGoldCountFromUser(userID);
            int userLvl = new Generator().computeLevel(sqlStatsHandler.getXPCountFromUser(userID));

            if (itemPrice > userGold || itemLvl > userLvl) {
                embedBuilder.setDescription("You dont have enough gold in your pockets! Or your level is to low :/");
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                return;
            }

            if (sqlInventoryHandler.isItemInUserInventory(userID, itemID)) {
                embedBuilder.setDescription("You already have this Item :face_with_monocle:");
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                return;
            }

            sqlStatsHandler.changeGoldCount(userID, -itemPrice);
            sqlInventoryHandler.addItemToPlayer(userID, itemID);
            embedBuilder.setDescription("You successfully acquired " + item.getEmoji() + "**" + itemName + "**" + item.getEmoji() + " !");
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }
}
