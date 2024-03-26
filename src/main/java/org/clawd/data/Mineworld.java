package org.clawd.data;

import org.clawd.data.items.Item;
import org.clawd.data.mobs.Mob;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

import java.util.HashMap;
import java.util.List;

public class Mineworld {

    private final List<Item> itemList;
    private final List<Mob> mobList;
    private final HashMap<Biome, Double> biomeToHP;
    private final HashMap<Biome, String> biomeToImgPath;
    private Biome currentBiome;
    private Double currentBiomeHP;

    public Mineworld(List<Item> itemList, List<Mob> mobList) {
        this.itemList = itemList;
        this.mobList = mobList;

        this.biomeToHP = new HashMap<>();

        biomeToHP.put(Biome.COAL, Constants.BIOM_COAL_HP);
        biomeToHP.put(Biome.IRON, Constants.BIOM_IRON_HP);
        biomeToHP.put(Biome.DIAMOND, Constants.BIOM_DIAMOND_HP);

        this.biomeToImgPath = new HashMap<>();

        biomeToImgPath.put(Biome.COAL, Constants.BIOME_COAL_IMG_PATH);
        biomeToImgPath.put(Biome.IRON, Constants.BIOME_IRON_IMG_PATH);
        biomeToImgPath.put(Biome.DIAMOND, Constants.BIOME_DIAMOND_IMG_PATH);

        this.currentBiome = generateStartBiome();
        this.currentBiomeHP = biomeToHP.get(currentBiome);
    }

    /**
     * Selects a random biom from the biomes enum
     *
     * @return A biom
     */
    private Biome generateStartBiome() {
        List<Biome> biomeList = List.of(Biome.values());
        int size = biomeList.size();
        int selector = (int) (Math.random() * (size - 1));

        Biome returnBiom = biomeList.get(selector);
        Main.logger.info("The starting biom is: " + returnBiom);

        return returnBiom;
    }

    private Double getCurrentBiomeHP() {
        return this.currentBiomeHP;
    }

    private String getCurrentBiomeImgPath() {
        return biomeToImgPath.get(currentBiome);
    }

    public List<Item> getItemList() {
        return this.itemList;
    }

    public List<Mob> getMobList() {
        return this.mobList;
    }
}
