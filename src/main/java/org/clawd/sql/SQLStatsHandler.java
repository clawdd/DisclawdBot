package org.clawd.sql;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.clawd.data.Generator;
import org.clawd.main.Bot;
import org.clawd.main.Main;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLStatsHandler {

    /**
     * This method returns the number of times a user has mined any biome
     *
     * @param userID The user ID
     * @return 'minedCount' from the SQL database
     */
    public int getMinedCountFromUser(String userID) {
        int minedCount = 0;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT minedCount FROM playertable WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                minedCount = resultSet.getInt("minedCount");
                Main.logger.info("Retrieved the amount of times mined: " + minedCount + ". From user:" + userID);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return minedCount;
    }

    public double getXPCountFromUser(String userID) {
        double xpCount = 0.0;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT xpCount FROM playertable WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double retrievedXP = resultSet.getDouble("xpCount");
                // Call to transformDouble() to hopefully fix 'precision issue' where doubles have to
                // many decimal places
                xpCount = new Generator().transformDouble(retrievedXP);
                Main.logger.info("Retrieved the total XP amount: " + xpCount + ". From user:"+ userID);
            }

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return xpCount;
    }

    public int getGoldCountFromUser(String userID) {
        int goldCount = 0;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT goldCount FROM playertable WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                goldCount = resultSet.getInt("goldCount");
                Main.logger.info("Retrieved the total gold amount: " + goldCount + ". From user:"+ userID);
            }

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return goldCount;
    }

    public int getMobKillsFromUser(String userID) {
        int mobKills = 0;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT mobKills FROM playertable WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                mobKills = resultSet.getInt("mobKills");
                Main.logger.info("Retrieved the amount of mob kills: " + mobKills + ". From user:" + userID);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return mobKills;
    }

    public int getBossKillsFromUser(String userID) {
        int bossKills = 0;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT bossKills FROM playertable WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bossKills = resultSet.getInt("bossKills");
                Main.logger.info("Retrieved the amount of boss kills: " + bossKills + ". From user:" + userID);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return bossKills;
    }

    public void incrementMineCount(String userID) {
        int currentCount = this.getMinedCountFromUser(userID);
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "UPDATE playertable SET minedCount = ? WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            int newCount = currentCount + 1;
            preparedStatement.setInt(1, newCount);
            preparedStatement.setString(2, userID);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                Main.logger.info("Updated mine count for user: " + userID + ". New mine count: " + newCount);
            } else {
                Main.logger.warning("Failed to update mine count for user " + userID);
            }
        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
    }

    public void incrementXPCount(String userID, double xp) {
        if (xp == 0)
            return;

        double currentXP = this.getXPCountFromUser(userID);
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "UPDATE playertable SET xpCount = ? WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            // Again call to transformDouble() to fix 'precision' issue, before updating the XP count
            // in the database
            double newCount = new Generator().transformDouble(currentXP + xp);

            preparedStatement.setDouble(1, newCount);
            preparedStatement.setString(2, userID);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                Main.logger.info("Updated XP count for user: " + userID + ". New XP count: " + newCount);
            } else {
                Main.logger.warning("Failed to update XP count for user " + userID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeGoldCount(String userID, int gold) {
        int currentGold = this.getGoldCountFromUser(userID);
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "UPDATE playertable SET goldCount = ? WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            int newCount = currentGold + gold;

            preparedStatement.setInt(1, newCount);
            preparedStatement.setString(2, userID);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                Main.logger.info("Updated gold count for user: " + userID + ". New gold count: " + newCount);
            } else {
                Main.logger.warning("Failed to update gold count for user " + userID);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO comment
    public void replyToUserLevelUp(
            double userCurrentXP,
            double userUpdatedXP,
            ButtonInteractionEvent event
    ) {
        Generator generator = new Generator();
        int userCurrentLvl = generator.computeLevel(userCurrentXP);
        int userUpdatedLvl = generator.computeLevel(userUpdatedXP);

        if (userUpdatedLvl > userCurrentLvl) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(":mushroom: Level UP! :mushroom:");
            embedBuilder.setColor(Color.GREEN);
            embedBuilder.addField(
                    "You reached level " + userUpdatedLvl,
                    ":black_small_square: With **" + userUpdatedXP + "** XP",
                    false
            );
            event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        };
    }
}
