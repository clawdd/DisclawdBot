package org.clawd.buttons.type;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.buttons.CustomButton;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

public class MineButton implements CustomButton {
    @Override
    public void executeButton(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();

        if (!Main.sqlHandler.isUserRegistered(userID)) {
            Main.sqlHandler.registerUser(userID);
            Main.sqlHandler.sqlEmbeddedHandler.replyToNewRegisteredUser(event);
        } else {
            // TODO
            double generatedXP = Main.generator.generateXP();
            int generatedGold = Main.generator.generateGold();

            Main.mineworld.updateCurrentUserMultiplication(userID);
            Main.mineworld.updateBiome(event);


            double userCurrentXP = Main.sqlHandler.sqlStatsHandler.getXPCountFromUser(userID);
            Main.sqlHandler.sqlStatsHandler.incrementMineCount(userID);
            Main.sqlHandler.sqlStatsHandler.incrementXPCount(userID, generatedXP);
            Main.sqlHandler.sqlStatsHandler.changeGoldCount(userID, generatedGold);
            double userUpdatedXP = Main.sqlHandler.sqlStatsHandler.getXPCountFromUser(userID);

            Main.sqlHandler.sqlStatsHandler.replyToUserLevelUp(userCurrentXP,userUpdatedXP, event);

            Main.LOG.info("Executed '"+ Constants.MINE_BUTTON_ID  +"' button");
        }
    }
}
