package org.clawd.main;

import net.dv8tion.jda.api.OnlineStatus;
import org.clawd.tokens.Tokens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static Logger logger;

    public static void main(String[] args) {
        logger = Logger.getLogger(Tokens.LOGGER_NAME);
        Bot bot = Bot.getInstance();
        run(bot);
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

                    logger.severe("Bot shutdown");
                    return;
                }
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }
}