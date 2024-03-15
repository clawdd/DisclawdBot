package org.clawd.parser;

import org.clawd.data.items.Item;
import org.clawd.parser.exceptions.FailedItemsParseException;

import java.util.ArrayList;

public class JSONParser {

    private final ObjectFactory objectFactory = new ObjectFactory();

    public ArrayList<Item> parseItems() throws FailedItemsParseException {
        ArrayList<Item> items = getItemsFromJSON();

        boolean isItemListValid = validateItems(items);
        boolean isItemListEmpty = items.isEmpty();

        if (!isItemListValid || isItemListEmpty)
            throw new FailedItemsParseException(
                    "Could not parse items correctly:\n" +
                    "- valid items = " + isItemListValid + "\n" +
                    "- is item list empty = " + isItemListEmpty);

        return items;
    }

    private ArrayList<Item> getItemsFromJSON() {
        ArrayList<Item> items = new ArrayList<>();
        /*
        try {

        } catch ()  {

        }
        */
        return items;
    }

    private boolean validateItems(ArrayList<Item> items) {


        return true;
    }
}
