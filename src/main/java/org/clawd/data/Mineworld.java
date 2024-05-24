package org.clawd.data;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.clawd.data.enums.Biome;
import org.clawd.data.inventory.InventoryHandler;
import org.clawd.data.items.Item;
import org.clawd.data.items.WeaponItem;
import org.clawd.data.items.enums.ItemType;
import org.clawd.data.mobs.Mob;
import org.clawd.data.shop.Shop;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class Mineworld {
    // TODO - add timestamp to messages
    public final Shop shop;
    public final InventoryHandler inventoryHandler;
    private final List<Item> itemList;
    private final List<Mob> mobList;
    private final HashMap<Biome, Double> biomeToHP;
    private final HashMap<Biome, String> biomeToImgPath;
    private Biome currentBiome;
    private Double currentBiomeHP;
    private Double currentBiomeFullHP;
    private final Map<String, LocalDateTime> currentUserMap;
    private int currentUserMultiplier;

    public Mineworld(List<Item> itemList, List<Mob> mobList) {
        this.itemList = itemList;
        this.mobList = mobList;

        this.shop = new Shop(itemList);
        this.inventoryHandler = new InventoryHandler();
        this.biomeToHP = new HashMap<>();
        this.currentUserMap = new HashMap<>();

        biomeToHP.put(Biome.COAL, Constants.BIOM_COAL_HP);
        biomeToHP.put(Biome.IRON, Constants.BIOM_IRON_HP);
        biomeToHP.put(Biome.DIAMOND, Constants.BIOM_DIAMOND_HP);

        this.biomeToImgPath = new HashMap<>();

        biomeToImgPath.put(Biome.COAL, Constants.BIOME_COAL_IMG_PATH);
        biomeToImgPath.put(Biome.IRON, Constants.BIOME_IRON_IMG_PATH);
        biomeToImgPath.put(Biome.DIAMOND, Constants.BIOME_DIAMOND_IMG_PATH);

        this.currentBiome = generateBiome();
        this.currentBiomeFullHP = biomeToHP.get(currentBiome);
        this.currentBiomeHP = this.currentBiomeFullHP;

        this.currentUserMultiplier = 1;
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
            embedBuilder.setDescription("Active miners: " + this.currentUserMap.size() + " (Last " + Constants.MAX_MINE_NOT_INTERACTED_MINUTES + " minutes)");
            embedBuilder.addField("Biome HP", currentBiomeHP + "/" + this.currentBiomeFullHP, false);
            embedBuilder.setImage("attachment://ore.png");

            event.replyEmbeds(embedBuilder.build())
                    .addFiles(FileUpload.fromData(imgFile, "ore.png"))
                    .addActionRow(
                            Button.primary(Constants.MINE_BUTTON_ID, Constants.MINE_BUTTON_EMOJI)
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

            embedBuilder.setTitle(currentBiome.name());
            embedBuilder.setColor(Color.BLACK);
            embedBuilder.setDescription("Active miners: " + this.currentUserMap.size() + " (Last " + Constants.MAX_MINE_NOT_INTERACTED_MINUTES + " minutes)");
            embedBuilder.addField("Biome HP",  Main.generator.transformDouble(this.currentBiomeHP) + "/" + this.currentBiomeFullHP, false);;
            embedBuilder.setImage("attachment://ore.png");

            event.replyEmbeds(embedBuilder.build())
                    .addFiles(FileUpload.fromData(imgFile, "ore.png"))
                    .addActionRow(
                            Button.primary(Constants.MINE_BUTTON_ID, Constants.MINE_BUTTON_EMOJI)
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

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(currentBiome.name());
            embedBuilder.setColor(Color.BLACK);
            embedBuilder.setDescription("Active miners: " + this.currentUserMap.size() + " (Last " + Constants.MAX_MINE_NOT_INTERACTED_MINUTES + " minutes)");
            embedBuilder.addField("Biome HP", Main.generator.transformDouble(this.currentBiomeHP) + "/" + this.currentBiomeFullHP, false);
            embedBuilder.setImage("attachment://ore.png");

            event.editMessageEmbeds(embedBuilder.build())
                    .setFiles(FileUpload.fromData(imgFile, "ore.png"))
                    .setActionRow(Button.primary(Constants.MINE_BUTTON_ID, Constants.MINE_BUTTON_EMOJI))
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
        this.currentBiomeFullHP = biomeToHP.get(this.currentBiome);
        adjustCurrentBiomeHP();
        this.currentBiomeHP = currentBiomeFullHP;
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
        int itemID = Main.sqlHandler.sqlInventoryHandler.getEquippedItemIDFromUser(userID);
        double dmgMult = 1.0;
        Item item = getItemByID(itemID);

        if (item != null && item.getItemType().equals(ItemType.WEAPON)) {
            WeaponItem weaponItem = (WeaponItem) item;
            dmgMult = weaponItem.getDmgMultiplier();
        }
        //seems to be fixed with rounding the HP value and not transforming it to the form X.X before
        double totalDamage = Constants.BASE_DAMAGE_MULTIPLIER * dmgMult;
        double previousHP = this.currentBiomeHP;

        this.currentBiomeHP -= totalDamage;
        this.currentBiomeHP = Main.generator.roundDouble(this.currentBiomeHP, 1);
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

    /**
     * Add a user if interacted with the 'mine' button to the currentUserMap with a timestamp
     *
     * @param userID User ID
     */
    public void updateCurrentUserMultiplication(String userID) {
        int oldMapSize = this.currentUserMap.size();
        this.currentUserMap.put(userID, LocalDateTime.now());
        updateCurrentUserMap();
        int newMapSize = this.currentUserMap.size();
        if (oldMapSize != newMapSize)
            adjustCurrentBiomeHP();
    }

    /**
     * Updates the currentUserMap by removing users from the list that did not interact with the 'mine' button longer
     * than some minutes ago
     */
    private void updateCurrentUserMap() {
        Iterator<Map.Entry<String, LocalDateTime>> iterator = this.currentUserMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, LocalDateTime> entry = iterator.next();
            LocalDateTime lastInteractionTime = entry.getValue();

            if (lastInteractionTime.isBefore(LocalDateTime.now().minusMinutes(Constants.MAX_MINE_NOT_INTERACTED_MINUTES))) {
                iterator.remove();
            }
        }
    }

    /**
     * Adjusts the biome HP depending on how many unique users interacted with the mine button in the last minutes
     */
    private void adjustCurrentBiomeHP() {
        int previousUserMultiplier = this.currentUserMultiplier;
        this.currentUserMultiplier = currentUserMap.size();

        this.currentBiomeFullHP = Main.generator.roundDouble(biomeToHP.get(currentBiome) * currentUserMultiplier, 1);

        if (this.currentUserMultiplier < previousUserMultiplier) {
            double adjustment = (double) currentUserMultiplier / previousUserMultiplier;
            this.currentBiomeHP = Main.generator.roundDouble(this.currentBiomeHP * adjustment, 1);
        }
    }

    public Biome getCurrentBiome() {
        return this.currentBiome;
    }
}
