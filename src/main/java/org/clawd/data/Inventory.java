package org.clawd.data;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.data.items.Item;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.tokens.Constants;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private final String userID;
    private final List<Item> userItemList;
    private final List<EmbedBuilder> pages;
    private final int inventoryPagesCount;

    public Inventory(String userID) {
        SQLInventoryHandler sqlInventoryHandler = new SQLInventoryHandler();

        this.userID = userID;
        this.userItemList = sqlInventoryHandler.getAllUserItems(userID);
        this.pages = createPages();
        this.inventoryPagesCount = (int) Math.ceil((double) userItemList.size() / Constants.ITEMS_PER_PAGE);
    }

    //TODO
    private List<EmbedBuilder> createPages() {
        List<EmbedBuilder> embedBuilders = new ArrayList<>();

        return embedBuilders;
    }

    //TODO
    public void replyWithInventoryCoreEmbedded(SlashCommandInteractionEvent event) {

    }

    //TODO
    public void replyToNextInventoryPage(ButtonInteractionEvent event, boolean back) {

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
