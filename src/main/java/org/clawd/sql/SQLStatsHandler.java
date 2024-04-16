package org.clawd.sql;

import org.clawd.main.Bot;
import org.clawd.main.Main;

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
                xpCount = resultSet.getDouble("xpCount");
                Main.logger.info("Retrieved the total XP amount: " + xpCount + ". From user:"+ userID);
            }

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return xpCount;
    }

    public double getGoldCountFromUser(String userID) {
        double goldCount = 0.0;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT goldCount FROM playertable WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                goldCount = resultSet.getDouble("goldCount");
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
                Main.logger.info("Updated mine count for user: " + userID + ". New mineCount: " + newCount);
            } else {
                Main.logger.warning("Failed to update mine count for user " + userID);
            }
        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
    }
}
