package org.clawd.data.mobs;

import org.clawd.data.DataObject;

public class Mob extends DataObject {
    private final String imgPath;
    public Mob(int uniqueID, String name, String desc, String imgPath) {
        super(uniqueID, name, desc);
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }
}
