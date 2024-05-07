package org.clawd.data;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.data.items.Item;
import org.clawd.data.items.UtilItem;
import org.clawd.data.items.WeaponItem;
import org.clawd.data.items.enums.ItemType;
import org.clawd.main.Main;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//TODO comment code
public class Inventory {
    private final SQLInventoryHandler sqlInventoryHandler = new SQLInventoryHandler();

    public Inventory() {

    }

    //TODO
    private List<EmbedBuilder> createPages() {
        List<EmbedBuilder> embedBuilders = new ArrayList<>();

        return embedBuilders;
    }

    //TODO
    public void replyWithInventoryCoreEmbedded(SlashCommandInteractionEvent event) {
        event.replyEmbeds(createCoreEmbedded(event).build()).setEphemeral(true).queue();
    }

    private EmbedBuilder createCoreEmbedded(SlashCommandInteractionEvent event) {
        String userID = event.getUser().getId();
        String userName = event.getUser().getName();
        String userAvatarURL = event.getUser().getAvatarUrl();

        double baseXP = Constants.BASE_XP_MULTIPLIER;
        double baseGold = Constants.BASE_GOLD_MULTIPLIER;
        double baseDMG = Constants.BASE_GOLD_MULTIPLIER;

        List<Item> userItemList = sqlInventoryHandler.getAllUserItems(userID);
        int inventoryPagesCount = (int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE) + 1;

        int equippedItemID = sqlInventoryHandler.getEquippedItemIDFromUser(userID);
        Item equippedItem = Main.mineworld.getItemByID(equippedItemID);

        double calculatedXPMult = 1.0;
        double calculatedGoldMult = 1.0;
        double calculatedDmgMult = 1.0;

        String equippedItemName = "Nothing equipped";

        if (equippedItem != null) {
            calculatedXPMult = equippedItem.getXpMultiplier() * baseXP;
            equippedItemName = equippedItem.getEmoji() + equippedItem.getName() + equippedItem.getEmoji();

            if (equippedItem.getItemType().equals(ItemType.UTILITY)) {
                UtilItem utilItem = (UtilItem) equippedItem;
                calculatedGoldMult = utilItem.getGoldMultiplier() * baseGold;

            } else if (equippedItem.getItemType().equals(ItemType.WEAPON)) {
                WeaponItem weaponItem = (WeaponItem) equippedItem;
                calculatedDmgMult = weaponItem.getDmgMultiplier() * baseDMG;
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your inventory - " + userName);
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setThumbnail(userAvatarURL);

        embedBuilder.addField(
                "Multipliers",
                "> XP: " + calculatedXPMult + " \n" +
                        "> Gold: " + calculatedGoldMult + " \n" +
                        "> Damage: " + calculatedDmgMult,
                false);
        embedBuilder.addField("Equipped Item", "> " + equippedItemName, false);

        embedBuilder.setFooter("Page: 1/" + inventoryPagesCount);

        return embedBuilder;
    }

    private EmbedBuilder createCoreEmbedded(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();
        String userName = event.getUser().getName();
        String userAvatarURL = event.getUser().getAvatarUrl();

        double baseXP = Constants.BASE_XP_MULTIPLIER;
        double baseGold = Constants.BASE_GOLD_MULTIPLIER;
        double baseDMG = Constants.BASE_GOLD_MULTIPLIER;

        List<Item> userItemList = sqlInventoryHandler.getAllUserItems(userID);
        int inventoryPagesCount = (int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE) + 1;

        int equippedItemID = sqlInventoryHandler.getEquippedItemIDFromUser(userID);
        Item equippedItem = Main.mineworld.getItemByID(equippedItemID);

        double calculatedXPMult = 1.0;
        double calculatedGoldMult = 1.0;
        double calculatedDmgMult = 1.0;

        String equippedItemName = "Nothing equipped";

        if (equippedItem != null) {
            calculatedXPMult = equippedItem.getXpMultiplier() * baseXP;
            equippedItemName = equippedItem.getEmoji() + equippedItem.getName() + equippedItem.getEmoji();

            if (equippedItem.getItemType().equals(ItemType.UTILITY)) {
                UtilItem utilItem = (UtilItem) equippedItem;
                calculatedGoldMult = utilItem.getGoldMultiplier() * baseGold;

            } else if (equippedItem.getItemType().equals(ItemType.WEAPON)) {
                WeaponItem weaponItem = (WeaponItem) equippedItem;
                calculatedDmgMult = weaponItem.getDmgMultiplier() * baseDMG;
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your inventory - " + userName);
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setThumbnail(userAvatarURL);

        embedBuilder.addField("Equipped Item", "> " + equippedItemName, false);
        embedBuilder.addField(
                "Multipliers",
                "> XP: " + calculatedXPMult + " \n" +
                        "> Gold: " + calculatedGoldMult + " \n" +
                        "> Damage: " + calculatedDmgMult,
                false);

        embedBuilder.setFooter("Page: 1/" + inventoryPagesCount);

        return embedBuilder;
    }

    //TODO
    private void replyToNextInvPage(ButtonInteractionEvent event, boolean back) {
        String userID = event.getUser().getId();
        List<Item> userItemList = sqlInventoryHandler.getAllUserItems(userID);
        int inventoryPagesCount = (int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE);
        List<EmbedBuilder> pages = createPages();

    }

    //TODO
    public double computeXPWithItem(double generatedXP) {

        return 0;
    }

    //TODO
    public int computeGoldWithItem(int generatedGold) {

        return 0;
    }
}
