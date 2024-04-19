package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.Button;
import org.clawd.data.Generator;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;
import org.clawd.sql.SQLStatsHandler;

public class MineButton implements Button {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();

        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            SQLEmbeddedHandler sqlEmbeddedHandler = new SQLEmbeddedHandler();
            sqlEmbeddedHandler.replyToNewRegisteredUser(event);
        } else {
            // TODO
            Generator generator = new Generator();
            double generatedXP = generator.generateXP();
            int generatedGold = generator.generateGold();

            Main.mineworld.updateBiome(event);

            SQLStatsHandler sqlStatsHandler = new SQLStatsHandler();
            sqlStatsHandler.incrementMineCount(userID);
            sqlStatsHandler.incrementXPCount(userID, generatedXP);
            sqlStatsHandler.incrementGoldCount(userID, generatedGold);

            Main.logger.info("Executed 'mine' button");
        }
    }
}
