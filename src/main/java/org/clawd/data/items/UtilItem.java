package org.clawd.data.items;

import org.clawd.data.items.enums.ItemType;

public class UtilItem extends Item {
    private final double goldMultiplier;

    public UtilItem(
            int uniqueID,
            String name,
            String desc,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double goldMultiplier
    ) {
        super(uniqueID, name, desc, itemType, dropChance, xpMultiplier);
        this.goldMultiplier = goldMultiplier;
    }

    public double getGoldMultiplier() {
        return goldMultiplier;
    }
}
