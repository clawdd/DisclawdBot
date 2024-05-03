package org.clawd.commands.type.slashcommand;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.clawd.data.Generator;
import org.clawd.data.items.Item;
import org.clawd.data.items.UtilItem;
import org.clawd.data.items.WeaponItem;
import org.clawd.data.items.enums.ItemType;
import org.clawd.main.Main;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.sql.SQLStatsHandler;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.io.File;
import java.util.Objects;

public class ItemCommand implements SlashCommand{
    @Override
    public void executeCommand(SlashCommandInteractionEvent event) {

        String searchTerm = Objects.requireNonNull(event.getOption(Constants.ITEM_COMMAND_OPTION_ID)).getAsString();

        Item foundItem = Main.mineworld.getItemByName(searchTerm);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);

        if (foundItem == null) {
            embedBuilder.setDescription("Item **" + searchTerm + "** was not found, maybe you mistyped something :/");
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        } else {
            String priceCheck = ":black_small_square:";
            String lvlCheck = ":black_small_square:";

            String userID = event.getUser().getId();

            String itemDesc = foundItem.getDescription();
            int itemPrice = foundItem.getPrice();
            int itemLvl = foundItem.getReqLvl();
            double itemXPMult = foundItem.getXpMultiplier();

            String alternativeTxt;
            double alternativePerk;

            SQLStatsHandler sqlStatsHandler = new SQLStatsHandler();
            SQLInventoryHandler sqlInventoryHandler = new SQLInventoryHandler();
            int userLvl = new Generator().computeLevel(sqlStatsHandler.getXPCountFromUser(userID));
            int userGold = sqlStatsHandler.getGoldCountFromUser(userID);

            Button buyButton = Button.success(Constants.BUY_BUTTON_ID, "Buy");
            Button equipButton = Button.success(Constants.EQUIP_BUTTON_ID, "Equip");
            boolean isItemInInv = sqlInventoryHandler.isItemInUserInventory(userID, foundItem.getUniqueID());

            if (isItemInInv) {
                buyButton = buyButton.asDisabled();
            } else {
                if (userLvl < itemLvl ) {
                    buyButton = buyButton.asDisabled();
                    lvlCheck = ":x:";
                }
                if (userGold < itemPrice) {
                    buyButton = buyButton.asDisabled();
                    priceCheck = ":x:";
                }
                equipButton = equipButton.asDisabled();
            }

            if (foundItem.getItemType() == ItemType.UTILITY) {
                UtilItem utilItem = (UtilItem) foundItem;
                alternativeTxt = ":black_small_square: Gold boost: ";
                alternativePerk = utilItem.getGoldMultiplier();
            } else {
                WeaponItem weaponItem = (WeaponItem) foundItem;
                alternativeTxt = ":black_small_square: Damage boost: ";
                alternativePerk = weaponItem.getDmgMultiplier();
            }

            embedBuilder.setTitle(":mag: " + foundItem.getName() + " :mag: ");
            File imgFile = new File(foundItem.getImgPath());
            embedBuilder.setThumbnail("attachment://item.png")
                    .addField(
                    itemDesc,
                    ":black_small_square: XP boost: " + itemXPMult + "\n"
                            + alternativeTxt + alternativePerk + "\n"
                            + priceCheck + " Price: " + itemPrice + " Coins" + "\n"
                            + lvlCheck + " Required lvl. " + itemLvl
                    ,
                    false);

            event.replyEmbeds(embedBuilder.build())
                    .addFiles(FileUpload.fromData(imgFile, "item.png"))
                    .addActionRow(
                            buyButton,
                            equipButton
                    )
                    .setEphemeral(true)
                    .queue();
        }
    }
}
