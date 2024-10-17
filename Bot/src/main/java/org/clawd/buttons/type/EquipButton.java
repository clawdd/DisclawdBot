package org.clawd.buttons.type;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.CustomButton;
import org.clawd.data.items.Item;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.util.List;

public class EquipButton implements CustomButton {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();
        List<MessageEmbed> embeds = event.getMessage().getEmbeds();
        String title = embeds.getFirst().getTitle();
        String searchTerm = title.replace(":mag:", "");
        Item item = Main.mineworld.getItemByName(searchTerm);

        Main.sqlHandler.sqlInventoryHandler.equipItem(userID, item.getUniqueID());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setDescription("You're now wielding **" + item.getEmoji() + item.getName() + item.getEmoji() + "**!");

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        Main.LOG.info("Executed '"+ Constants.EQUIP_BUTTON_ID  +"' button");
    }
}
