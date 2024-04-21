package org.clawd.data.items;

import org.clawd.data.items.enums.ItemType;

public class UtilItem extends Item {
    private final double goldMultiplier;

    public UtilItem(
            int uniqueID,
            String name,
            int price,
            String desc,
            int reqLvl,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double goldMultiplier
    ) {
        super(uniqueID, name, price, desc, reqLvl, itemType, dropChance, xpMultiplier);
        this.goldMultiplier = goldMultiplier;
    }

    public double getGoldMultiplier() {
        return goldMultiplier;
    }
}
