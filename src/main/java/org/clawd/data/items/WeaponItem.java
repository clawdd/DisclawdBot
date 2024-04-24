package org.clawd.data.items;

import org.clawd.data.items.enums.ItemType;

public class WeaponItem extends Item {
    private final double dmgMultiplier;

    public WeaponItem(
            int uniqueID,
            String name,
            String emoji,
            String desc,
            String imgPath,
            int reqLvl,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double dmgMultiplier
    ) {
        super(uniqueID, name, desc, emoji, imgPath, reqLvl, itemType, dropChance, xpMultiplier);
        this.dmgMultiplier = dmgMultiplier;
        this.price = calculatePrice(xpMultiplier, dmgMultiplier);
    }

    public double getDmgMultiplier() {
        return dmgMultiplier;
    }
}
