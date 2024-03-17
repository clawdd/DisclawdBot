package org.clawd.data.mobs;

import org.clawd.data.mobs.enums.MobSubType;
import org.clawd.data.mobs.enums.MobType;

public class HostileMob extends Mob {
    private final double xpDrop;
    private final double goldDrop;

    public HostileMob(
            int uniqueID,
            String name,
            String desc,
            MobType mobType,
            MobSubType mobSubType,
            String imgPath,
            double spawnChance,
            double xpDrop,
            double goldDrop
    ) {
        super(uniqueID, name, desc, mobType, mobSubType, imgPath, spawnChance);
        this.xpDrop = xpDrop;
        this.goldDrop = goldDrop;
    }

    public double getXpDrop() {
        return xpDrop;
    }

    public double getGoldDrop() {
        return goldDrop;
    }
}
