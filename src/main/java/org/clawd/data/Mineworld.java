package org.clawd.data;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.clawd.data.enums.Biome;
import org.clawd.data.items.Item;
import org.clawd.data.items.WeaponItem;
import org.clawd.data.items.enums.ItemType;
import org.clawd.data.mobs.Mob;
import org.clawd.main.Main;
import org.clawd.sql.SQLInventoryHandler;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Mineworld {

    public final Shop shop;
    public final Inventory inventory;
    private final List<Item> itemList;
    private final List<Mob> mobList;
    private final HashMap<Biome, Double> biomeToHP;
    private final HashMap<Biome, String> biomeToImgPath;
    private Biome currentBiome;
    private Double currentBiomeHP;

    public Mineworld(List<Item> itemList, List<Mob> mobList) {
        this.itemList = itemList;
        this.mobList = mobList;

        this.shop = new Shop(itemList);
        this.inventory = new Inventory();

        this.biomeToHP = new HashMap<>();

        biomeToHP.put(Biome.COAL, Constants.BIOM_COAL_HP);
        biomeToHP.put(Biome.IRON, Constants.BIOM_IRON_HP);
        biomeToHP.put(Biome.DIAMOND, Constants.BIOM_DIAMOND_HP);

        this.biomeToImgPath = new HashMap<>();

        biomeToImgPath.put(Biome.COAL, Constants.BIOME_COAL_IMG_PATH);
        biomeToImgPath.put(Biome.IRON, Constants.BIOME_IRON_IMG_PATH);
        biomeToImgPath.put(Biome.DIAMOND, Constants.BIOME_DIAMOND_IMG_PATH);

        this.currentBiome = generateBiome();
        this.currentBiomeHP = biomeToHP.get(currentBiome);
    }

    /**
     * Selects a random biom from the biomes enum
     *
     * @return A biom
     */
    private Biome generateBiome() {
        List<Biome> biomeList = List.of(Biome.values());
        int size = biomeList.size();
        int selector = (int) (Math.random() * (size));

        Biome returnBiome = biomeList.get(selector);
        Main.LOG.info("The current biome is: " + returnBiome);

        return returnBiome;
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
                    )
                    .queue();

        } catch (NullPointerException ex) {
            Main.LOG.severe("Could not load image file: " + ex.getMessage());
        }
    }

    /**
     * This method replies to the 'mine' button, by building an embedded message
     * with all necessary information and buttons. This method overloads the method
     * with the SlashCommandInteractionEvent event param
     *
     * @param event Event
     */
    public void replyWithBiomeEmbedded(ButtonInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        try {
            File imgFile = new File(biomeToImgPath.get(currentBiome));
            Generator generator = new Generator();

            embedBuilder.setTitle(currentBiome.name());
            embedBuilder.setColor(Color.BLACK);
            embedBuilder.addField("Biome HP",  generator.transformDouble(this.currentBiomeHP) + "/" + biomeToHP.get(this.currentBiome), false);;
            embedBuilder.setImage("attachment://ore.png");

            event.replyEmbeds(embedBuilder.build())
                    .addFiles(FileUpload.fromData(imgFile, "ore.png"))
                    .addActionRow(
                            Button.primary(Constants.MINE_BUTTON_ID, "Mine")
                    ).queue();

        } catch (NullPointerException ex) {
            Main.LOG.severe("Could not load image file: " + ex.getMessage());
        }
    }

    /**
     * Updates the embedded message on a 'ButtonInteractionEvent'
     *
     * @param event Event
     */
    private void updateBiomeMsg(ButtonInteractionEvent event) {
        try {
            File imgFile = new File(biomeToImgPath.get(currentBiome));
            Generator generator = new Generator();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(currentBiome.name());
            embedBuilder.addField("Biome HP", generator.transformDouble(this.currentBiomeHP) + "/" + biomeToHP.get(this.currentBiome), false);
            embedBuilder.setImage("attachment://ore.png");

            event.editMessageEmbeds(embedBuilder.build())
                    .setFiles(FileUpload.fromData(imgFile, "ore.png"))
                    .queue();
            Main.LOG.info("Updated biome state.");
        } catch (NullPointerException ex) {
            Main.LOG.severe("Could not load image file: " + ex.getMessage());
        }
    }

    /**
     * Gets an item from the itemList by an item ID
     *
     * @param itemID The item ID we search with
     * @return The item matching the item ID, if not found null is returned
     */
    public Item getItemByID (int itemID) {
        Item returnItem = null;
        if (itemID < 0)
            return returnItem;

        for(Item item : itemList) {
            if (item.getUniqueID() == itemID)
                returnItem = item;
        }
        return returnItem;
    }

    /**
     * Wrapper method to update the biome on a 'ButtonInteractionEvent'
     * and the embedded message, such that the current state is displayed
     * correctly
     *
     * @param event Event
     */
    public void updateBiome(ButtonInteractionEvent event) {
        String userID = event.getUser().getId();
        damageBiome(userID);

        if (this.currentBiomeHP <= 0) {
            this.currentBiome = generateBiome();
            updateBiomeOnCompletion(event);
            return;
        }
        updateBiomeMsg(event);
    }

    /**
     * Updates the biome state and embedded message on a 'ButtonInteractionEvent', if
     * the biome has been completed by reaching HP of <= 0
     *
     * @param event Event
     */
    private void updateBiomeOnCompletion(ButtonInteractionEvent event) {
        event.getMessage().delete().queue();
        this.currentBiomeHP = biomeToHP.get(this.currentBiome);
        replyWithBiomeEmbedded(event);
        Main.LOG.info("Updated biome because of completion.");
    }

    /**
     * Calculates the damage done to a biome by a user and applies it to
     * the current HP of the current biome
     *
     * @param userID User ID
     */
    private void damageBiome(String userID) {
        // Remember you already wrote this line
        int itemID = new SQLInventoryHandler().getEquippedItemIDFromUser(userID);
        double dmgMult = 1.0;
        Item item = getItemByID(itemID);

        if (item != null && item.getItemType().equals(ItemType.WEAPON)) {
            WeaponItem weaponItem = (WeaponItem) item;
            dmgMult = weaponItem.getDmgMultiplier();
        }
        Generator generator = new Generator();
        //seems to be fixed with rounding the HP value and not transforming it to the form X.X before
        double totalDamage = Constants.BASE_DAMAGE_MULTIPLIER * dmgMult;
        double previousHP = this.currentBiomeHP;

        this.currentBiomeHP -= totalDamage;
        this.currentBiomeHP = generator.roundDouble(this.currentBiomeHP, 1);
        Main.LOG.info("Damage done to biome: " + this.currentBiome + "." +
                " Damage: " + totalDamage + ", " + previousHP + "->" + this.currentBiomeHP);
    }

    /**
     * Searches the item by the item name in the item list
     *
     * @param name The item name
     * @return Found item or null
     */
    public Item getItemByName(String name) {
        Item foundItem = null;
        for (Item item : itemList) {
            if (item.getName().replace(" ", "").equalsIgnoreCase(name.replace(" ", "")))
                foundItem = item;
        }
        return foundItem;
    }

    private Double getCurrentBiomeHP() {
        return this.currentBiomeHP;
    }

    private String getCurrentBiomeImgPath() {
        return biomeToImgPath.get(this.currentBiome);
    }

    public List<Item> getItemList() {
        return this.itemList;
    }

    public List<Mob> getMobList() {
        return this.mobList;
    }

    public Biome getCurrentBiome() {
        return this.currentBiome;
    }
}
