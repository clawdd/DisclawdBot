package org.clawd.data.items;

import org.clawd.data.DataObject;
import org.clawd.data.items.enums.ItemType;

public abstract class Item extends DataObject {
    private final double dropChance;
    private final double xpMultiplier;
    private final ItemType itemType;

    public Item(
            int uniqueID,
            String name,
            String desc,
            ItemType itemType,
            double dropChance,
            double xpMultiplier
    ) {
        super(uniqueID, name, desc);
        this.dropChance = dropChance;
        this.xpMultiplier = xpMultiplier;
        this.itemType = itemType;
    }

    public double getDropChance() {
        return dropChance;
    }

    public double getXpMultiplier() {
        return xpMultiplier;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
