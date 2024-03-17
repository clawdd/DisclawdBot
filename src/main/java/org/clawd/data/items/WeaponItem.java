package org.clawd.data.items;

import org.clawd.data.DataObject;

public class WeaponItem extends Item {
    private final double dmgMultiplier;

    public WeaponItem(
            int uniqueID,
            String name,
            String desc,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double dmgMultiplier
    ) {
        super(uniqueID, name, desc, itemType, dropChance, xpMultiplier);
        this.dmgMultiplier = dmgMultiplier;
    }

    public double getDmgMultiplier() {
        return dmgMultiplier;
    }
}
