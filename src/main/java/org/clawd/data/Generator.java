package org.clawd.data;

import org.clawd.data.enums.Biome;
import org.clawd.main.Main;

import java.util.Arrays;
import java.util.List;

public class Generator {

    private final List<Biome> xpBiomes = Arrays.asList(Biome.COAL, Biome.DIAMOND);

    /**
     * Used to generate XP
     *
     * @return XP as double
     */
    public double generateXP() {
        Biome biome = Main.mineworld.getCurrentBiome();

        if (!xpBiomes.contains(biome))
            return 0d;

        double generatedXP = (Math.random() * 5) + 1;
        double transformedXP = transformDouble(generatedXP);
        Main.logger.info("Generated XP: " + transformedXP);
        return transformedXP;
    }

    /**
     * Used to generate gold
     *
     * @return Gold as int
     */
    public int generateGold() {
        int generatedGold = (int) (Math.random() * 3) + 1;
        Main.logger.info("Generated gold: " + generatedGold);
        return generatedGold;
    }

    /**
     * Transforms a double, by cutting of decimal places
     *
     * @param initialValue An initial double
     * @return The double after the transformation
     */
    public double transformDouble(double initialValue) {
        return ((int) (initialValue * 10)) / 10d;
    }
}
