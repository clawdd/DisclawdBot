package org.clawd.parser;

import org.clawd.data.items.Item;
import org.clawd.data.items.UtilItem;
import org.clawd.data.items.enums.ItemType;
import org.clawd.data.items.WeaponItem;
import org.clawd.data.mobs.BossMob;
import org.clawd.data.mobs.NormalMob;
import org.clawd.data.mobs.Mob;
import org.clawd.data.mobs.TradeMob;
import org.clawd.data.mobs.enums.MobSubType;
import org.clawd.data.mobs.enums.MobType;
import org.clawd.data.mobs.enums.TradeType;

public class Factory {
    /**
     * Factory method to create a utility item object
     *
     * @param uniqueID Item ID
     * @param name Name
     * @param desc Description
     * @param itemType Type
     * @param dropChance Drop chance
     * @param xpMultiplier XP-Multiplier
     * @param goldMultiplier Gold-Multiplier
     *
     * @return A utility item object
     */
    public Item createUtilityItem(
            int uniqueID,
            String name,
            String itemEmoji,
            String desc,
            String imgPath,
            int reqLvl,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double goldMultiplier
    ) {
        return new UtilItem(uniqueID, name, itemEmoji, desc, imgPath, reqLvl, itemType, dropChance, xpMultiplier, goldMultiplier);
    }

    /**
     * Factory method to create a weapon item object
     *
     * @param uniqueID Unique ID
     * @param name Name
     * @param desc Description
     * @param itemType Type
     * @param dropChance Drop chance
     * @param xpMultiplier XP-Multiplier
     * @param dmgMultiplier Damage-Multiplier
     *
     * @return A weapon item object
     */
    public Item createWeaponItem(
            int uniqueID,
            String name,
            String itemEmoji,
            String desc,
            String imgPath,
            int reqLvl,
            ItemType itemType,
            double dropChance,
            double xpMultiplier,
            double dmgMultiplier
    ) {
        return new WeaponItem(uniqueID, name, desc, itemEmoji, imgPath,  reqLvl, itemType, dropChance, xpMultiplier, dmgMultiplier);
    }

    /**
     * Factory method to create a normal mob object
     *
     * @param uniqueID Unique
     * @param name Name
     * @param desc Description
     * @param mobType Mob type
     * @param mobSubType Mob subtype
     * @param imgPath Image path
     * @param spawnChance Spawn-chance
     * @param xpDrop XP Drop
     * @param goldDrop Gold drop
     *
     * @return A normal mob object
     */
    public Mob createNormalMob(
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
        return new NormalMob(uniqueID, name, desc, mobType, mobSubType, imgPath, spawnChance, xpDrop, goldDrop);
    }

    /**
     * Factory method to create a boss mob object
     *
     * @param uniqueID Unique
     * @param name Name
     * @param desc Description
     * @param mobType Mob type
     * @param mobSubType Mob subtype
     * @param imgPath Image path
     * @param spawnChance Spawn-chance
     * @param xpDrop XP Drop
     * @param goldDrop Gold drop
     * @param specialDrop Special drop
     * @param health Health
     *
     * @return A boss mob object
     */
    public Mob createBossMob(
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
        return new BossMob(uniqueID, name, desc, mobType, mobSubType, imgPath, spawnChance, xpDrop, goldDrop, specialDrop, health);
    }

    /**
     * Factory method to create a trade mob object
     *
     * @param uniqueID Unique
     * @param name Name
     * @param desc Description
     * @param mobType Mob type
     * @param mobSubType Mob subtype
     * @param imgPath Image path
     * @param spawnChance Spawn-chance
     * @param tradeType Trade type
     *
     * @return A trade mob object
     */
    public Mob createTradeMob(
            int uniqueID,
            String name,
            String desc,
            MobType mobType,
            MobSubType mobSubType,
            String imgPath,
            double spawnChance,
            TradeType tradeType
    ) {
        return new TradeMob(uniqueID, name, desc, mobType, mobSubType, imgPath, spawnChance, tradeType);
    }
}
