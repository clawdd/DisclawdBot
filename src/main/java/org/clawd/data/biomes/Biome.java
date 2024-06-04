package org.clawd.data.biomes;

import org.clawd.data.enums.BiomeType;
import org.clawd.data.mobs.enums.MobSubType;

import java.util.Set;

public class Biome {

    private final BiomeType type;
    private final double biomeHP;
    private final String imgPath;
    private final boolean xpEnabled;
    private final Set<MobSubType> spawnableMobs;

    public Biome(BiomeType type, double biomeHP, String imgPath, boolean xpEnabled, Set<MobSubType> spawnableMobs) {
        this.type = type;
        this.biomeHP = biomeHP;
        this.imgPath = imgPath;
        this.xpEnabled = xpEnabled;
        this.spawnableMobs = spawnableMobs;
    }

    public BiomeType getType() {
        return type;
    }

    public double getBiomeHP() {
        return biomeHP;
    }

    public String getImgPath() {
        return imgPath;
    }

    public boolean isXpEnabled() {
        return xpEnabled;
    }

    public Set<MobSubType> getSpawnableMobs() {
        return spawnableMobs;
    }
}
