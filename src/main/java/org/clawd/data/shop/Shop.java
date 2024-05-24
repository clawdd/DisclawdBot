package org.clawd.data.shop;

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
import org.clawd.tokens.Constants;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Shop {
    private final List<Item> itemList;
    private final List<EmbedBuilder> pages;
    private final int shopPagesCount;

    private Button nextButton = Button.secondary(Constants.NEXT_SHOP_BUTTON_ID, Constants.NEXT_BUTTON_EMOJI);
    private final Button homeButton = Button.secondary(Constants.HOME_SHOP_BUTTON_ID, Constants.HOME_BUTTON_EMOJI);
    private Button backButton = Button.secondary(Constants.BACK_SHOP_BUTTON_ID, Constants.BACK_BUTTON_EMOJI);

    public Shop(List<Item> itemList) {
        this.itemList = itemList.stream().filter(i -> i.getDropChance() == 0).toList();
        this.shopPagesCount = (int) Math.ceil((double) itemList.size() / Constants.ITEMS_PER_PAGE);
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

            int startIndex = i * Constants.ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + Constants.ITEMS_PER_PAGE, this.itemList.size());

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
                                        + ":black_small_square: Price: " + itemPrice + " Coins" + "\n"
                                        + ":black_small_square: Required lvl. " + item.getReqLvl(),
                        true);
            }
            embedBuilders.add(embedBuilder);
        }
        return embedBuilders;
    }

    /**
     * Replies with the first page of the shop
     *
     * @param event The event
     */
    public void replyWithShopFirstEmbedded(SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = pages.getFirst();

        if (this.shopPagesCount == 1)
            this.nextButton = this.nextButton.asDisabled();

        event.replyEmbeds(embedBuilder.build())
                .addActionRow(
                        this.backButton.asDisabled(),
                        this.homeButton,
                        this.nextButton
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
        String footer = Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter()).getText();
        String[] parts = footer.split("/");
        int currentPage = Integer.parseInt(parts[0].substring(6).strip());

        if (back) {
            currentPage--;
        } else {
            currentPage++;
        }

        currentPage = Math.max(1, Math.min(currentPage, this.shopPagesCount)) - 1;

        EmbedBuilder embedBuilder = pages.get(currentPage);

        this.nextButton = this.nextButton.asEnabled();
        this.backButton = this.backButton.asEnabled();

        if (currentPage == 0) {
            this.backButton = this.backButton.asDisabled();
        } else if (currentPage == this.shopPagesCount - 1) {
            this.nextButton = this.nextButton.asDisabled();
        }

        InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
    }

    public void updateToFirstEmbedded(ButtonInteractionEvent event) {
        EmbedBuilder embedBuilder;
        embedBuilder = pages.getFirst();
        this.nextButton = this.nextButton.asEnabled();
        this.backButton = this.backButton.asDisabled();

        InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
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
            Main.LOG.severe("Item '" + itemName + "' cannot by found by name.");
        }
        return result;
    }
}
