package org.clawd.data;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.clawd.data.items.Item;
import org.clawd.data.items.UtilItem;
import org.clawd.data.items.WeaponItem;
import org.clawd.data.items.enums.ItemType;
import org.clawd.main.Main;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.sql.SQLStatsHandler;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO make the inventory class an object to be stored in an list that gets updated overtime
public class Inventory {
    private final SQLInventoryHandler sqlInventoryHandler = new SQLInventoryHandler();
    private Button nextButton = Button.secondary(Constants.NEXT_INV_BUTTON_ID, Constants.NEXT_BUTTON_EMOJI);
    private final Button homeButton = Button.secondary(Constants.HOME_INV_BUTTON_ID, Constants.HOME_BUTTON_EMOJI);
    private Button backButton = Button.secondary(Constants.BACK_INV_BUTTON_ID, Constants.BACK_BUTTON_EMOJI);

    private List<EmbedBuilder> createPages(ButtonInteractionEvent event) {
        List<EmbedBuilder> embedBuilders = new ArrayList<>();
        String userID = event.getUser().getId();
        List<Item> userItemList = sqlInventoryHandler.getAllUserItems(userID);
        int inventoryPagesCount = (int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE);

        embedBuilders.add(createFirstEmbedded(event));

        for (int i = 0; i < inventoryPagesCount; i++) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Inventory - " + event.getUser().getName());
            embedBuilder.setColor(Color.GREEN);
            embedBuilder.setFooter("Page: " + (i + 2) + "/" + (inventoryPagesCount + 1));

            int startIndex = i * Constants.ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + Constants.ITEMS_PER_PAGE, userItemList.size());

            for (int j = startIndex; j < endIndex; j++) {
                //TODO - could be refactored into it's own method, since does almost the same as in the shop class
                Item item = userItemList.get(j);

                String itemName = item.getName();
                double itemXPMult = item.getXpMultiplier();
                String alternativeTxt;
                double alternativePerk;

                if (item.getItemType() == ItemType.UTILITY) {
                    UtilItem utilItem = (UtilItem) item;
                    alternativeTxt = ":black_small_square: Gold boost: ";
                    alternativePerk = utilItem.getGoldMultiplier();
                } else {
                    WeaponItem weaponItem = (WeaponItem) item;
                    alternativeTxt = ":black_small_square: Damage boost: ";
                    alternativePerk = weaponItem.getDmgMultiplier();
                }

                embedBuilder.addField(
                        item.getEmoji() + itemName + item.getEmoji(),
                        ":black_small_square: XP boost: " + itemXPMult + "\n"
                                + alternativeTxt + alternativePerk,
                        true);
            }
            embedBuilders.add(embedBuilder);
        }
        return embedBuilders;
    }

    public void replyWithInventoryFirstEmbedded(SlashCommandInteractionEvent event) {
        List<Item> userItemList = sqlInventoryHandler.getAllUserItems(event.getUser().getId());
        int inventoryPagesCount = ((int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE)) + 1;
        if (inventoryPagesCount == 1)
            this.nextButton = this.nextButton.asDisabled();
        event.replyEmbeds(createFirstEmbedded(event, inventoryPagesCount).build())
                .addActionRow(
                        this.backButton.asDisabled(),
                        this.homeButton,
                        this.nextButton
                )
                .setEphemeral(true).queue();
    }

    private EmbedBuilder createFirstEmbedded(SlashCommandInteractionEvent event, int inventoryPagesCount) {
        SQLStatsHandler sqlStatsHandler = new SQLStatsHandler();
        Generator generator = new Generator();
        String userID = event.getUser().getId();
        String userName = event.getUser().getName();
        String userAvatarURL = event.getUser().getAvatarUrl();

        int minedCount = sqlStatsHandler.getMinedCountFromUser(userID);
        int mobKills = sqlStatsHandler.getMobKillsFromUser(userID);
        int bossKills = sqlStatsHandler.getBossKillsFromUser(userID);
        double xpCount = sqlStatsHandler.getXPCountFromUser(userID);
        int goldCount = sqlStatsHandler.getGoldCountFromUser(userID);

        double baseXP = Constants.BASE_XP_MULTIPLIER;
        double baseGold = Constants.BASE_GOLD_MULTIPLIER;
        double baseDMG = Constants.BASE_GOLD_MULTIPLIER;

        int equippedItemID = sqlInventoryHandler.getEquippedItemIDFromUser(userID);
        Item equippedItem = Main.mineworld.getItemByID(equippedItemID);

        double calculatedXPMult = 1.0;
        double calculatedGoldMult = 1.0;
        double calculatedDmgMult = 1.0;

        String equippedItemName = "Nothing equipped";
        String itemEmoji = "";

        if (equippedItem != null) {
            calculatedXPMult = equippedItem.getXpMultiplier() * baseXP;
            equippedItemName = equippedItem.getName();
            itemEmoji = equippedItem.getEmoji();

            if (equippedItem.getItemType().equals(ItemType.UTILITY)) {
                UtilItem utilItem = (UtilItem) equippedItem;
                calculatedGoldMult = utilItem.getGoldMultiplier() * baseGold;

            } else if (equippedItem.getItemType().equals(ItemType.WEAPON)) {
                WeaponItem weaponItem = (WeaponItem) equippedItem;
                calculatedDmgMult = weaponItem.getDmgMultiplier() * baseDMG;
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Inventory - " + userName);
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setThumbnail(userAvatarURL);

        embedBuilder.setDescription("Level " + generator.computeLevel(xpCount));
        embedBuilder.addField(
                ":bar_chart:Stats",
                "> Times Mined: " + minedCount + " \n" +
                        "> Mob Kills: " + mobKills + " \n" +
                        "> Boss Kills: " + bossKills + " \n" +
                        "> XP: " + xpCount + " \n" +
                        "> Gold: " + goldCount,
                true);
        embedBuilder.addBlankField(true);
        embedBuilder.addField(
                itemEmoji +"Equipped Item",
                "> " + equippedItemName + " \n" +
                        ":dna:**Multipliers**" + " \n" +
                        "> XP: " + calculatedXPMult + " \n" +
                        "> Gold: " + calculatedGoldMult + " \n" +
                        "> Damage: " + calculatedDmgMult,
                true);
        embedBuilder.setFooter("Page: 1/" + inventoryPagesCount);
        return embedBuilder;
    }

    private EmbedBuilder createFirstEmbedded(ButtonInteractionEvent event) {
        SQLStatsHandler sqlStatsHandler = new SQLStatsHandler();
        Generator generator = new Generator();
        String userID = event.getUser().getId();
        String userName = event.getUser().getName();
        String userAvatarURL = event.getUser().getAvatarUrl();

        int minedCount = sqlStatsHandler.getMinedCountFromUser(userID);
        int mobKills = sqlStatsHandler.getMobKillsFromUser(userID);
        int bossKills = sqlStatsHandler.getBossKillsFromUser(userID);
        double xpCount = sqlStatsHandler.getXPCountFromUser(userID);
        int goldCount = sqlStatsHandler.getGoldCountFromUser(userID);

        double baseXP = Constants.BASE_XP_MULTIPLIER;
        double baseGold = Constants.BASE_GOLD_MULTIPLIER;
        double baseDMG = Constants.BASE_GOLD_MULTIPLIER;

        List<Item> userItemList = sqlInventoryHandler.getAllUserItems(userID);
        int inventoryPagesCount = ((int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE)) + 1;

        int equippedItemID = sqlInventoryHandler.getEquippedItemIDFromUser(userID);
        Item equippedItem = Main.mineworld.getItemByID(equippedItemID);

        double calculatedXPMult = 1.0;
        double calculatedGoldMult = 1.0;
        double calculatedDmgMult = 1.0;

        String equippedItemName = "Nothing equipped";
        String itemEmoji = "";

        if (equippedItem != null) {
            calculatedXPMult = equippedItem.getXpMultiplier() * baseXP;
            equippedItemName = equippedItem.getName();
            itemEmoji = equippedItem.getEmoji();

            if (equippedItem.getItemType().equals(ItemType.UTILITY)) {
                UtilItem utilItem = (UtilItem) equippedItem;
                calculatedGoldMult = utilItem.getGoldMultiplier() * baseGold;

            } else if (equippedItem.getItemType().equals(ItemType.WEAPON)) {
                WeaponItem weaponItem = (WeaponItem) equippedItem;
                calculatedDmgMult = weaponItem.getDmgMultiplier() * baseDMG;
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Inventory - " + userName);
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setThumbnail(userAvatarURL);

        embedBuilder.setDescription("Level " + generator.computeLevel(xpCount));
        embedBuilder.addField(
                ":bar_chart:Stats",
                "> Times Mined: " + minedCount + " \n" +
                        "> Mob Kills: " + mobKills + " \n" +
                        "> Boss Kills: " + bossKills + " \n" +
                        "> XP: " + xpCount + " \n" +
                        "> Gold: " + goldCount,
                true);
        embedBuilder.addBlankField(true);
        embedBuilder.addField(
                itemEmoji +"Equipped Item",
                "> " + equippedItemName + " \n" +
                        ":dna:**Multipliers**" + " \n" +
                        "> XP: " + calculatedXPMult + " \n" +
                        "> Gold: " + calculatedGoldMult + " \n" +
                        "> Damage: " + calculatedDmgMult,
                true);
        embedBuilder.setFooter("Page: 1/" + inventoryPagesCount);
        return embedBuilder;
    }

    public void replyToNextInvPage(ButtonInteractionEvent event, boolean back) {
        List<EmbedBuilder> pages = createPages(event);
        String footer = Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter()).getText();
        String[] parts = footer.split("/");
        int currentPage = Integer.parseInt(parts[0].substring(6).strip());

        if (back) {
            currentPage--;
        } else {
            currentPage++;
        }

        currentPage = Math.max(1, Math.min(currentPage, pages.size())) - 1;

        EmbedBuilder embedBuilder = pages.get(currentPage);

        this.backButton = this.backButton.asEnabled();
        this.nextButton = this.nextButton.asEnabled();

        if (currentPage == 0) {
            this.backButton = this.backButton.asDisabled();
        } else if (currentPage == pages.size() - 1) {
            this.nextButton = this.nextButton.asDisabled();
        }

        InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
    }

    public void updateToFirstEmbedded(ButtonInteractionEvent event) {
        EmbedBuilder embedBuilder;
        embedBuilder = createFirstEmbedded(event);

        List<Item> userItemList = sqlInventoryHandler.getAllUserItems(event.getUser().getId());
        int inventoryPagesCount = ((int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE)) + 1;

        this.nextButton = this.nextButton.asEnabled();
        this.backButton = this.backButton.asDisabled();

        if (inventoryPagesCount == 1)
            this.nextButton = this.nextButton.asDisabled();

        InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
    }
}
