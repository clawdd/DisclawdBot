package org.clawd.data;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
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
import org.clawd.tokens.Constants;

import java.awt.*;
import java.util.*;
import java.util.List;

//TODO comment the code
public class Shop {
    private final List<Item> itemList;
    private final List<WeaponItem> weaponItemList;
    private final List<UtilItem> utilItemList;
    private final List<EmbedBuilder> pages;
    private final int shopPagesCount;

    public Shop(List<Item> itemList) {
        this.itemList = itemList.stream().filter(i -> i.getDropChance() == 0).toList();
        this.weaponItemList = populateWeaponList();
        this.utilItemList = populateUtilList();
        this.shopPagesCount = (int) Math.ceil((double) itemList.size() / Constants.ITEMS_PER_SHOP_PAGE);
        this.pages = createPages();
    }

    /**
     * Creates a list of embedded builders which do represent the single shop pages
     *
     * @return A list of all "pages"
     */
    private List<EmbedBuilder> createPages() {
        List<EmbedBuilder> embedBuilders = new ArrayList<>();
        // NumberFormat nf = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);

        for (int i = 0; i < this.shopPagesCount; i++) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(":label: Shop");
            embedBuilder.setDescription(">>> Remember, use '/item' to inspect and buy!");
            embedBuilder.setColor(Color.ORANGE);
            embedBuilder.setFooter("Page: " + (i + 1) + "/" + shopPagesCount);

            int startIndex = i * Constants.ITEMS_PER_SHOP_PAGE;
            int endIndex = Math.min(startIndex + Constants.ITEMS_PER_SHOP_PAGE, this.itemList.size());

            for (int j = startIndex; j < endIndex; j++) {
                Item item = itemList.get(j);

                String itemName = item.getName();
                int itemPrice = item.getPrice();
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
                                        + alternativeTxt + alternativePerk + "\n"
                                        + ":black_small_square: Price: " + itemPrice + " Coins" + "\n" //nf.format(itemPrice)
                                        + ":black_small_square: Required lvl. " + item.getReqLvl(),
                        true);
            }
            embedBuilders.add(embedBuilder);
        }
        return embedBuilders;
    }

    /**
     * Creates the core page for the shop
     *
     * @param event The event
     */
    public void replyWithShopCoreEmbedded(SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = pages.getFirst();

        Button nextButton = Button.secondary(Constants.NEXT_BUTTON_ID, Emoji.fromUnicode("U+25B6"));
        Button closeButton = Button.secondary(Constants.CLOSE_BUTTON_ID, Emoji.fromUnicode("U+274C"));
        Button backButton = Button.secondary(Constants.BACK_BUTTON_ID, Emoji.fromUnicode("U+25C0"));
        if (this.shopPagesCount == 1)
            nextButton = nextButton.asDisabled();

        event.replyEmbeds(embedBuilder.build())
                .addActionRow(
                        backButton.asDisabled(),
                        closeButton.asDisabled(),
                        nextButton
                )
                .setEphemeral(true)
                .queue();
    }

    /**
     * Replies if either the next page or back page button was pressed by a user. Retrieves the current page from the
     * page footer. Then calculates the next page and selects it. Generates correct buttons in each case and updates message
     * with new embed and buttons.
     *
     * @param event The event
     * @param back True if back button else false
     */
    public void replyToNextShopPage(ButtonInteractionEvent event, boolean back) {
        String footer = Objects.requireNonNull(event.getMessage().getEmbeds().getFirst().getFooter()).getText();
        String[] parts = footer.split("/");
        int currentPage = Integer.parseInt(parts[0].substring(6).strip());

        if (back) {
            currentPage--;
        } else {
            currentPage++;
        }

        event.getMessage().getContentStripped();
        currentPage = Math.max(1, Math.min(currentPage, this.shopPagesCount)) - 1;

        EmbedBuilder embedBuilder = pages.get(currentPage);

        Button nextButton = Button.secondary(Constants.NEXT_BUTTON_ID, Emoji.fromUnicode("U+25B6"));
        Button closeButton = Button.secondary(Constants.CLOSE_BUTTON_ID, Emoji.fromUnicode("U+274C"));
        Button backButton = Button.secondary(Constants.BACK_BUTTON_ID, Emoji.fromUnicode("U+25C0"));

        if (this.shopPagesCount == 1) {
            nextButton = nextButton.asDisabled();
            closeButton = closeButton.asDisabled();
            backButton = backButton.asDisabled();

            InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
            hook.editOriginalComponents(ActionRow.of(backButton, closeButton, nextButton)).queue();
        } else if (currentPage > 0 && currentPage < this.shopPagesCount - 1) {

            nextButton = nextButton.asEnabled();
            closeButton = closeButton.asDisabled();
            backButton = backButton.asEnabled();

            InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
            hook.editOriginalComponents(ActionRow.of(backButton, closeButton, nextButton)).queue();
        } else if (currentPage == 0) {

            nextButton = nextButton.asEnabled();
            closeButton = closeButton.asDisabled();
            backButton = backButton.asDisabled();

            InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
            hook.editOriginalComponents(ActionRow.of(backButton, closeButton, nextButton)).queue();
        } else if (currentPage == this.shopPagesCount - 1) {

            nextButton = nextButton.asDisabled();
            closeButton = closeButton.asDisabled();
            backButton = backButton.asEnabled();

            InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
            hook.editOriginalComponents(ActionRow.of(backButton, closeButton, nextButton)).queue();
        }
    }

    /**
     * Gets the item by its name
     *
     * @param itemName The name
     * @return The found item or null
     */
    public Item getItemByName(String itemName) {
        Item result = null;
        try {
            result = this.itemList.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).toList().getFirst();
        } catch (NoSuchElementException ex) {
            Main.logger.severe("Item '" + itemName + "' cannot by found by name.");
        }
        return result;
    }

    // might be unnecessary
    private List<WeaponItem> populateWeaponList() {
        List<WeaponItem> weaponItems = new ArrayList<>();
        for (Item item : this.itemList) {
            if (item.getItemType() == ItemType.WEAPON)
                weaponItems.add((WeaponItem) item);
        }
        return weaponItems;
    }

    private List<UtilItem> populateUtilList() {
        List<UtilItem> utilItems = new ArrayList<>();
        for (Item item : this.itemList) {
            if (item.getItemType() == ItemType.UTILITY)
                utilItems.add((UtilItem) item);
        }
        return utilItems;
    }
}
