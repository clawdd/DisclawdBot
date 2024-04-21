package org.clawd.data.items;

import org.clawd.data.DataObject;
import org.clawd.data.items.enums.ItemType;

public abstract class Item extends DataObject {

    private final int price;
    private final double dropChance;
    private final double xpMultiplier;
    private final int reqLvl;
    private final ItemType itemType;

    public Item(
            int uniqueID,
            String name,
            int price,
            String desc,
            int reqLvl,
            ItemType itemType,
            double dropChance,
            double xpMultiplier
    ) {
        super(uniqueID, name, desc);
        this.price = price;
        this.dropChance = dropChance;
        this.xpMultiplier = xpMultiplier;
        this.reqLvl = reqLvl;
        this.itemType = itemType;
    }

    public int getPrice(){
        return price;
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

    public int getReqLvl() {
        return reqLvl;
    }
}
