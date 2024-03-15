package org.clawd.data;

import org.clawd.data.items.Item;
import org.clawd.data.mobs.Mob;

import java.util.ArrayList;

public class Mineworld {

    private final ArrayList<Item> itemList;
    private final ArrayList<Mob> mobList;

    public Mineworld(ArrayList<Item> itemList, ArrayList<Mob> mobList) {
        this.itemList = itemList;
        this.mobList = mobList;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public ArrayList<Mob> getMobList() {
        return mobList;
    }
}
