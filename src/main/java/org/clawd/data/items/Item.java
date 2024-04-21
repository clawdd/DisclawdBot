package org.clawd.data.items;

import org.clawd.data.DataObject;
import org.clawd.data.items.enums.ItemType;

public abstract class Item extends DataObject {

    private String emoji;
    protected int price;
    private final double dropChance;
    private final double xpMultiplier;
    protected final int reqLvl;
    private final ItemType itemType;

    public Item(
            int uniqueID,
            String name,
            String desc,
            String emoji,
            int reqLvl,
            ItemType itemType,
            double dropChance,
            double xpMultiplier
    ) {
        super(uniqueID, name, desc);
        this.emoji = emoji;
        this.price = 0;
        this.dropChance = dropChance;
        this.xpMultiplier = xpMultiplier;
        this.reqLvl = reqLvl;
        this.itemType = itemType;
    }

    public int getPrice(){
        return price;
    }

    /**
     *
     *
     * @param perkOne The first perk of an item
     * @param perkTwo The second perk of an item
     *
     * @return The price following a formula:
     *             - ((perkOne * 10) mod 10) * 300 + ((perkTwo * 10) mod 10) * 300 + (lvlReq/0.2)^2
     */
    protected int calculatePrice (double perkOne, double perkTwo) {
        double xpMultDif = (perkOne * 10) % 10;
        double goldMultDif = (perkTwo * 10) % 10;

        int firstPerkGoldIncrease = (int) xpMultDif * 300;
        int scdPerkGoldIncrease = (int) goldMultDif * 300;

        int additionalIncrease = 0;
        if (firstPerkGoldIncrease > 0 && scdPerkGoldIncrease > 0)
            additionalIncrease = 200;

        int formulaResult = (int) ((this.reqLvl / 0.2)  * (this.reqLvl / 0.2));

        return firstPerkGoldIncrease + scdPerkGoldIncrease + additionalIncrease + formulaResult;
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

    public String getEmoji() {
        return  emoji;
    }

    public int getReqLvl() {
        return reqLvl;
    }
}
