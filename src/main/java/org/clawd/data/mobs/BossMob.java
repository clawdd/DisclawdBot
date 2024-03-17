package org.clawd.data.mobs;

import org.clawd.data.mobs.enums.MobSubType;
import org.clawd.data.mobs.enums.MobType;

public class BossMob extends HostileMob {
    private final boolean specialDrop;
    private final double health;

    public BossMob(
            int uniqueID,
            String name,
            String desc,
            MobType mobType,
            MobSubType mobSubType,
            String imgPath,
            double spawnChance,
            double xpDrop,
            double goldDrop,
            boolean specialDrop,
            double health
    ) {
        super(uniqueID, name, desc, mobType, mobSubType, imgPath, spawnChance, xpDrop, goldDrop);
        this.specialDrop = specialDrop;
        this.health = health;
    }

    public boolean isSpecialDrop() {
        return specialDrop;
    }

    public double getHealth() {
        return health;
    }
}
