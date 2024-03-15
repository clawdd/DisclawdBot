package org.clawd.parser;

import org.clawd.data.DataObject;
import org.clawd.data.items.Item;
import org.clawd.data.mobs.Mob;

public class ObjectFactory {
    /**
     * Factory method to create Item object
     *
     * @param uniqueID Unique item ID
     * @param name Item name
     * @param desc Item description
     * @param dropChance Drop chance
     * @param xpMultiplier XP-Multiplier
     *
     * @return A concrete Item data object
     */
    public DataObject createItem(int uniqueID, String name, String desc, double dropChance, double xpMultiplier) {
        return new Item(uniqueID, name, desc, dropChance, xpMultiplier);
    }

    /**
     * Factory method to create Mob object
     *
     * @param uniqueID Unique item ID
     * @param name Item name
     * @param desc Item description
     * @param imgPath Path to the concrete image
     *
     * @return A concrete Mob data object
     */
    public DataObject createMob(int uniqueID, String name, String desc, String imgPath) {
        return new Mob(uniqueID, name, desc, imgPath);
    }
}
