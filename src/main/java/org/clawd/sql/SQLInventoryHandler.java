package org.clawd.sql;

import org.clawd.main.Bot;
import org.clawd.main.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLInventoryHandler {

    public void addItemToPlayer(String userID, int itemID) {
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "INSERT INTO inventory (userID, itemID) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, itemID);

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
    }

    public boolean isItemInUserInventory(String userID, int itemID) {
        boolean itemInInventory = false;
        try {
            Connection connection = Bot.getInstance().getSQLConnection();
            String sqlQuery = "SELECT * FROM inventory WHERE userID = ? AND itemID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, itemID);
            ResultSet resultSet = preparedStatement.executeQuery();

            itemInInventory = resultSet.next();
            resultSet.close();
        } catch (SQLException ex) {
            Main.logger.severe("Some SQL error occurred: " + ex.getMessage());
        }
        return itemInInventory;
    }
}
