package org.clawd.data;

import org.clawd.data.items.UtilItem;
import org.clawd.data.mobs.Mob;

import java.util.List;

public class Mineworld {

    private final List<UtilItem> itemList;
    private final List<Mob> mobList;

    public Mineworld(List<UtilItem> itemList, List<Mob> mobList) {
        this.itemList = itemList;
        this.mobList = mobList;
    }

    public List<UtilItem> getItemList() {
        return itemList;
    }

    public List<Mob> getMobList() {
        return mobList;
    }
}
