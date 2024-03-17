package org.clawd.parser;

import org.clawd.data.DataObject;
import org.clawd.data.items.Item;
import org.clawd.data.items.UtilItem;
import org.clawd.data.items.ItemType;
import org.clawd.data.items.WeaponItem;
import org.clawd.data.mobs.Mob;

public class ObjectFactory {
    /**
     * Factory method to create Item object
     *
     * @param uniqueID Unique item ID
     * @param name Item name
     * @param desc Item description
     * @param itemType Item type
     * @param dropChance Drop chance
     * @param xpMultiplier XP-Multiplier
     * @param goldMultiplier Gold-Multiplier
     *
     * @return A utility item data object
     */
    public Item createUtilityItem(
            int uniqueID,
            String name,
            String desc,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double goldMultiplier
    ) {
        return new UtilItem(uniqueID, name, desc, itemType, dropChance, xpMultiplier, goldMultiplier);
    }

    /**
     *
     * @param uniqueID Unique item ID
     * @param name Item name
     * @param desc Item description
     * @param itemType Item type
     * @param dropChance Drop chance
     * @param xpMultiplier XP-Multiplier
     * @param dmgMultiplier Damage-Multiplier
     *
     * @return A weapon item data object
     */
    public Item createWeaponItem(
            int uniqueID,
            String name,
            String desc,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double dmgMultiplier
    ) {
        return new WeaponItem(uniqueID, name, desc, itemType, dropChance, xpMultiplier, dmgMultiplier);
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
