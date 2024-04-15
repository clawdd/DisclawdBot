package org.clawd.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.clawd.buttons.ButtonManager;
import org.clawd.commands.CommandManager;
import org.clawd.tokens.BotTokens;
import org.clawd.tokens.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class Bot {

    private static Bot INSTANCE;
    private final JDA jda;
    private Connection connection;

    private Bot() {

        Properties botProperties = loadProperties();

        establishSQLConnection();

        jda = JDABuilder.create(BotTokens.BOT_TOKEN, GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS)).build();
        jda.getPresence().setActivity(Activity.customStatus("The cake is a lie! " + botProperties.get("version")));

        addListeners();
        upsertCommands();

        Main.logger.info("Bot finished loading");
    }

    /**
     * Calls the private constructor
     *
     * @return Bot instance
     */
    public static Bot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Bot();
        }
        return INSTANCE;
    }

    /**
     * Creates a properties object using the bot.properties file
     *
     * @return Properties object
     */
    private Properties loadProperties() {
        Properties botProps = new Properties();
        try {
            String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();

            String configPath = rootPath + Constants.PROPERTIES_FILE_NAME;
            botProps.load(new FileInputStream(configPath));

        } catch (NullPointerException | IOException ex) {
            Main.logger.severe("Error loading properties file: " + ex.getMessage());
        }
        return botProps;
    }

    /**
     * Establishes connection to the SQL database
     */
    public void establishSQLConnection() {
        try {
            Class.forName(Constants.SQL_CLASS_NAME);
            connection = DriverManager.getConnection("jdbc:mysql://" + BotTokens.JDBC_URL, BotTokens.SQL_USERNAME, "");

            if (connection != null && !connection.isClosed())
                Main.logger.info("Successfully connected to SQL Database");

        } catch (ClassNotFoundException | SQLException ex) {
            Main.logger.severe("Could not connect to SQL Database " + ex.getMessage());
        }
    }

    /**
     * Closes connection to the SQL database
     */
    public void closeSQLConnection() {
        try {
            if (connection != null && !connection.isClosed())
                this.connection.close();

            Main.logger.info("SQL connection closed successfully");

        } catch (SQLException ex) {
            Main.logger.severe("Could not close SQL connection" + ex.getMessage());
        }
    }

    private void addListeners() {
        jda.addEventListener(new CommandManager());
        jda.addEventListener(new ButtonManager());

        Main.logger.info("Added listeners");
    }

    private void upsertCommands() {
        jda.upsertCommand(Constants.BIOME_COMMAND_ID, "What do I have to mine?")
                .setGuildOnly(true)
                .queue();

        Main.logger.info("Added commands");
    }

    public Connection getSQLConnection() {
        return this.connection;
    }

    public JDA getJda() {
        return this.jda;
    }
}
