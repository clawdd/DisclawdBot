package org.clawd.data.items;

import org.clawd.data.DataObject;

public class Item extends DataObject {
    private final double dropChance;
    private final double xpMultiplier;

    public Item(int uniqueID, String name, String desc, double dropChance, double xpMultiplier) {
        super(uniqueID, name, desc);
        this.dropChance = dropChance;
        this.xpMultiplier = xpMultiplier;
    }

    public double getDropChance() {
        return dropChance;
    }

    public double getXpMultiplier() {
        return xpMultiplier;
    }
}
