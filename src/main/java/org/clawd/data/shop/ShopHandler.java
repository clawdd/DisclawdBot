package org.clawd.data.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.clawd.data.items.Item;
import org.clawd.tokens.Constants;

import java.util.*;
import java.util.List;

public class ShopHandler {
    private final ShopCore shopCore;
    private Button nextButton = Button.secondary(Constants.NEXT_SHOP_BUTTON_ID, Constants.NEXT_BUTTON_EMOJI);
    private final Button homeButton = Button.secondary(Constants.HOME_SHOP_BUTTON_ID, Constants.HOME_BUTTON_EMOJI);
    private Button backButton = Button.secondary(Constants.BACK_SHOP_BUTTON_ID, Constants.BACK_BUTTON_EMOJI);

    public ShopHandler(List<Item> itemList) {
        this.shopCore = new ShopCore(itemList);
    }

    /**
     * Replies with the first page of the shop
     *
     * @param event The SlashCommandInteractionEvent event
     */
    public void replyWithShopFirstEmbedded(SlashCommandInteractionEvent event) {

        EmbedBuilder embedBuilder = this.shopCore.getPages().getFirst();

        if (this.shopCore.getShopPagesCount() == 1)
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
     * @param event The ButtonInteractionEvent event
     * @param back  True if back button else false
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

        currentPage = Math.max(1, Math.min(currentPage, this.shopCore.getShopPagesCount())) - 1;

        EmbedBuilder embedBuilder = this.shopCore.getPages().get(currentPage);

        this.nextButton = this.nextButton.asEnabled();
        this.backButton = this.backButton.asEnabled();

        if (currentPage == 0) {
            this.backButton = this.backButton.asDisabled();
        } else if (currentPage == this.shopCore.getShopPagesCount() - 1) {
            this.nextButton = this.nextButton.asDisabled();
        }

        InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
    }

    /**
     * This method makes the embedded message jump back to the first page
     *
     * @param event The ButtonInteractionEvent event
     */
    public void updateToFirstEmbedded(ButtonInteractionEvent event) {
        EmbedBuilder embedBuilder;
        embedBuilder = this.shopCore.getPages().getFirst();
        this.nextButton = this.nextButton.asEnabled();
        this.backButton = this.backButton.asDisabled();

        InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
    }
}
