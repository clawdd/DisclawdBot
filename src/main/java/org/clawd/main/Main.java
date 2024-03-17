package org.clawd.main;

import net.dv8tion.jda.api.OnlineStatus;
import org.clawd.data.Mineworld;
import org.clawd.data.items.Item;
import org.clawd.parser.ItemParser;
import org.clawd.parser.exceptions.FailedItemsParseException;
import org.clawd.tokens.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static Logger logger;
    public static Mineworld mineworld;

    public static void main(String[] args) {
        logger = Logger.getLogger(Constants.LOGGER_NAME);

        try {
            ItemParser itemParser = new ItemParser();
            List<Item> itemList = itemParser.parseItems();

            // 2nd argument is currently a placeholder
            mineworld = new Mineworld(itemList, new ArrayList<>());

            Bot bot = Bot.getInstance();
            run(bot);
        } catch (FailedItemsParseException ex) {
            logger.severe(ex.getMessage());
        }
    }

    /**
     * Infinite run loop
     * - Stop bot with --exit command inside the terminal
     *
     * @param bot Bot instance
     */
    private static void run(Bot bot) {
        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while ((line = reader.readLine()) != null) {
                if (line.equalsIgnoreCase("--exit")) {
                    bot.getJda().getPresence().setStatus(OnlineStatus.OFFLINE);
                    bot.getJda().shutdown();

                    bot.closeSQLConnection();

                    logger.info("Bot shutdown");
                    return;
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }
}