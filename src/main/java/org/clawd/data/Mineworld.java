package org.clawd.data;

import org.clawd.data.items.Item;
import org.clawd.data.items.UtilItem;
import org.clawd.data.mobs.Mob;

import java.util.List;

public class Mineworld {

    private final List<Item> itemList;
    private final List<Mob> mobList;

    public Mineworld(List<Item> itemList, List<Mob> mobList) {
        this.itemList = itemList;
        this.mobList = mobList;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public List<Mob> getMobList() {
        return mobList;
    }
}
