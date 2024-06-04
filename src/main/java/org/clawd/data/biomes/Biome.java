package org.clawd.data.biomes;

import org.clawd.data.enums.BiomeType;
import org.clawd.data.mobs.enums.MobSubType;

import java.util.Set;

public record Biome(BiomeType type, double biomeHP, String imgPath, boolean xpEnabled, Set<MobSubType> spawnableMobTypes) {
}
