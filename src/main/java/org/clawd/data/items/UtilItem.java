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
        calculatePrice();
    }

    //TODO comment the code
    private void calculatePrice () {
        double xpMultDif = this.getXpMultiplier() - 1.0;
        double goldMultDif = this.getGoldMultiplier() - 1.0;

        int firstPerkGoldIncrease = ((int) (xpMultDif * 10)) * 300;
        int scdPerkGoldIncrease = ((int) (goldMultDif * 10)) * 300;

        int additionalIncrease = 0;
        if (firstPerkGoldIncrease > 0 && scdPerkGoldIncrease > 0)
            additionalIncrease = 200;

        int formulaResult = (int) ((this.reqLvl / 0.2)  * (this.reqLvl / 0.2));

        this.price = firstPerkGoldIncrease + scdPerkGoldIncrease + additionalIncrease + formulaResult;
    }

    public double getGoldMultiplier() {
        return goldMultiplier;
    }
}
