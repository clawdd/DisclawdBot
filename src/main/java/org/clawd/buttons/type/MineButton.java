package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.CustomButton;
import org.clawd.data.Generator;
import org.clawd.main.Main;
import org.clawd.sql.SQLEmbeddedHandler;
import org.clawd.sql.SQLStatsHandler;
import org.clawd.tokens.Constants;

public class MineButton implements CustomButton {
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

            double userCurrentXP = sqlStatsHandler.getXPCountFromUser(userID);
            sqlStatsHandler.incrementMineCount(userID);
            sqlStatsHandler.incrementXPCount(userID, generatedXP);
            sqlStatsHandler.changeGoldCount(userID, generatedGold);
            double userUpdatedXP = sqlStatsHandler.getXPCountFromUser(userID);

            sqlStatsHandler.replyToUserLevelUp(userCurrentXP,userUpdatedXP, event);

            Main.LOG.info("Executed '"+ Constants.MINE_BUTTON_ID  +"' button");
        }
    }
}
