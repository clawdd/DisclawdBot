package org.clawd.data.inventory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.tokens.Constants;


import java.util.Objects;

//TODO make the inventory class an object to be stored in an list that gets updated overtime
public class InventoryHandler {

    public final InventoryCache inventoryCache = new InventoryCache();
    private final SQLInventoryHandler sqlInventoryHandler = new SQLInventoryHandler();
    private Button nextButton = Button.secondary(Constants.NEXT_INV_BUTTON_ID, Constants.NEXT_BUTTON_EMOJI);
    private final Button homeButton = Button.secondary(Constants.HOME_INV_BUTTON_ID, Constants.HOME_BUTTON_EMOJI);
    private Button backButton = Button.secondary(Constants.BACK_INV_BUTTON_ID, Constants.BACK_BUTTON_EMOJI);

    public void replyWithInventoryFirstEmbedded(SlashCommandInteractionEvent event) {
        Inventory inventory = this.inventoryCache.addInventory(event);
        if (inventory.getInventoryPages().size() < 2)
            this.nextButton = this.nextButton.asDisabled();
        event.replyEmbeds(inventory.getInventoryPages().getFirst().build())
                .addActionRow(
                        this.backButton.asDisabled(),
                        this.homeButton,
                        this.nextButton
                )
                .setEphemeral(true).queue();
    }

    public void replyToNextInvPage(ButtonInteractionEvent event, boolean back) {
        Inventory inventory = this.inventoryCache.addInventory(event);
        String footer = Objects.requireNonNull(event.getMessage().getEmbeds().get(0).getFooter()).getText();
        String[] parts = footer.split("/");
        int currentPage = Integer.parseInt(parts[0].substring(6).strip());

        if (back) {
            currentPage--;
        } else {
            currentPage++;
        }

        currentPage = Math.max(1, Math.min(currentPage, inventory.getInventoryPages().size())) - 1;

        EmbedBuilder embedBuilder = inventory.getInventoryPages().get(currentPage);
        this.backButton = this.backButton.asEnabled();
        this.nextButton = this.nextButton.asEnabled();

        if (currentPage == 0) {
            this.backButton = this.backButton.asDisabled();
        } else if (currentPage == inventory.getInventoryPages().size() - 1) {
            this.nextButton = this.nextButton.asDisabled();
        }

        InteractionHook hook = event.editMessageEmbeds(embedBuilder.build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
    }

    public void updateToFirstEmbedded(ButtonInteractionEvent event) {
        Inventory inventory = this.inventoryCache.forceInventoryUpdate(event);

        this.nextButton = this.nextButton.asEnabled();
        this.backButton = this.backButton.asDisabled();

        if (inventory.getInventoryPages().size() < 2)
            this.nextButton = this.nextButton.asDisabled();

        InteractionHook hook = event.editMessageEmbeds(inventory.getInventoryPages().getFirst().build()).complete();
        hook.editOriginalComponents(ActionRow.of(this.backButton, this.homeButton, this.nextButton)).queue();
    }
}
