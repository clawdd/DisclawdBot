package org.clawd.sql;

import org.clawd.main.Bot;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLItemHandler {
    //TODO maybe move to other class -> Inventory Handler
    /**
     * This method equips an item for a given user ID.
     *
     * @param userID The user ID
     * @param itemID The item ID to equip
     */
    public void equipItem(String userID, int itemID) {
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "UPDATE playertable SET equipedItemID = ? WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, itemID);
            preparedStatement.setString(2, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Main.logger.info("Equipped item " + itemID + " for user " + userID);
            } else {
                Main.logger.severe("Failed to equip item " + itemID + " for user " + userID);
            }
            preparedStatement.close();

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
    }

    /**
     * Replaces the equipped item ID with -1, to represent no item in slot currently
     *
     * @param userID The user ID
     */
    public void unequipItem(String userID) {
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "UPDATE playertable SET equipedItemID = ? WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, Constants.NO_ITEM_ID);
            preparedStatement.setString(2, userID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Main.logger.info("Unequipped item for user" + userID);
            } else {
                Main.logger.severe("Failed to unequip item for user" + userID);
            }
            preparedStatement.close();

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
    }

    /**
     * This method returns the equipped item ID, if no item is equipped this method returns -1
     *
     * @param userID The user ID
     * @return Either an item ID or -1
     */
    public int getEquippedItemFromUser(String userID) {
        int itemID = -1;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT equipedItemID FROM playertable WHERE userID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                itemID = resultSet.getInt("equipedItemID");
                Main.logger.info("Retrieved item ID: " + itemID + ". From user: " + userID);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return itemID;
    }
}
