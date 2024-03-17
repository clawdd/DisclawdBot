package org.clawd.data;

import org.clawd.data.items.ItemType;

public abstract class DataObject {
    private final int uniqueID;
    private final String name;
    private final String desc;

    public DataObject(int uniqueID, String name, String desc) {
        this.uniqueID = uniqueID;
        this.name = name;
        this.desc = desc;
    }

    public int getUniqueID() {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }
}
