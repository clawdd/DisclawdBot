package org.clawd.data;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.clawd.data.items.Item;
import org.clawd.data.mobs.Mob;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.io.File;
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

    /**
     * This method replies to the '/biome' command, by building an embedded message
     * with all necessary information and buttons
     *
     * @param event Event
     */
    public void replyWithBiomeEmbedded(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        try {
            File imgFile = new File(biomeToImgPath.get(currentBiome));

            embedBuilder.setTitle(currentBiome.name());
            embedBuilder.setColor(Color.BLACK);
            embedBuilder.addField("Biome HP", currentBiomeHP + "/" + biomeToHP.get(currentBiome), false);
            embedBuilder.setImage("attachment://ore.png");

            event.replyEmbeds(embedBuilder.build())
                    .addFiles(FileUpload.fromData(imgFile, "ore.png"))
                    .addActionRow(
                            Button.primary(Constants.MINE_BUTTON_ID, "Mine")
                    ).queue();

        } catch (NullPointerException ex) {
            Main.logger.severe("Could not load image file: " + ex.getMessage());
        }
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
