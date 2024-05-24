package org.clawd.tokens;

import net.dv8tion.jda.api.entities.emoji.Emoji;

public final class Constants {
    public static final String LOGGER_NAME = "DISCLAWD BOT";
    public static final String SQL_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String PROPERTIES_FILE_NAME = "bot.properties";

    /*
     * JSON String constants
     */

    public static final String ITEMS_JSON_FILEPATH = "src/main/resources/jsonfiles/items.json";
    public static final String ITEMS_JSON_ITEMS = "items";
    public static final String MOBS_JSON_FILEPATH = "src/main/resources/jsonfiles/mobs.json";
    public static final String MOBS_JSON_MOBS = "mobs";

    /*
     * THRESHOLDS for item params
     */

    public static final double ITEM_DROP_CHANCE_UPPER_B = 1;
    public static final double XP_MULTIPLIER_LOWER_B = 1;
    public static final double GOLD_MULTIPLIER_LOWER_B = 0;
    public static final double DMG_MULTIPLIER_LOWER_B = 0;

    /*
     * THRESHOLDS for mob params
     */

    public static final double MOB_SPAWN_CHANCE_LOWER_B = 0;
    public static final double MOB_SPAWN_CHANCE_UPPER_B = 1;
    public static final double XP_DROP_AMOUNT_LOWER_B = 1;
    public static final double GOLD_DROP_AMOUNT_LOWER_B = 1;

    /*
     * Biom HP
     */

    // Adjusted for testing
    public static final double BIOM_COAL_HP = 10.0;
    public static final double BIOM_IRON_HP = 10.0;
    public static final double BIOM_DIAMOND_HP = 10.0;

    /*
     * Ore image path's
     */

    public static final String BIOME_COAL_IMG_PATH = "src/main/resources/images/biome/coal.png";
    public static final String BIOME_IRON_IMG_PATH = "src/main/resources/images/biome/iron.png";
    public static final String BIOME_DIAMOND_IMG_PATH = "src/main/resources/images/biome/diamond.png";

    /*
     * Command Id's
     */

    public static final String BIOME_COMMAND_ID = "biome";
    public static final String INV_COMMAND_ID = "inventory";
    public static final String SHOP_COMMAND_ID = "shop";
    public static final String ITEM_COMMAND_ID = "item";
    public static final String ITEM_COMMAND_OPTION_ID = "name";
    public static final String HELP_COMMAND_ID = "help";

    /*
     * Button Id's
     */

    public static final String MINE_BUTTON_ID = "mine";
    public static final String NEXT_SHOP_BUTTON_ID = "next-shop";
    public static final String BACK_SHOP_BUTTON_ID = "back-shop";
    public static final String HOME_SHOP_BUTTON_ID = "home-shop";
    public static final String NEXT_INV_BUTTON_ID = "next-inv";
    public static final String HOME_INV_BUTTON_ID = "home-inv";
    public static final String BACK_INV_BUTTON_ID = "back-inv";
    public static final String BUY_BUTTON_ID = "buy";
    public static final String EQUIP_BUTTON_ID = "Equip";

    /*
     * Button emoji's
     */

    public static final Emoji MINE_BUTTON_EMOJI = Emoji.fromUnicode("U+1F4A5");
    public static final Emoji NEXT_BUTTON_EMOJI = Emoji.fromUnicode("U+25B6");
    public static final Emoji HOME_BUTTON_EMOJI = Emoji.fromUnicode("U+1F504");
    public static final Emoji BACK_BUTTON_EMOJI = Emoji.fromUnicode("U+25C0");


    /*
     * Base stat constants
     */

    public static final double BASE_DAMAGE_MULTIPLIER = 1.0;
    public static final double BASE_XP_MULTIPLIER = 1.0;
    public static final double BASE_GOLD_MULTIPLIER = 1.0;

    /*
     * Others
     */

    public static final int NO_ITEM_ID = -1;
    public static final int ITEMS_PER_PAGE = 3;
    public static final String PATH_LOGO_IMG = "src/main/resources/images/disclawd_logo.png";
    public static final int MAX_MINE_NOT_INTERACTED_MINUTES = 2;
    public static final long CACHE_EXPIRY_MINUTES = 5;
    public static final int CACHE_PERIOD_MINUTES = 2;

    /*
     * Help command description
     */

    public static final String HELP_COMMAND_DESC = ">>> Hi there again :sloth: Welcome to the **Helpcenter**! From here" +
            " I will try to guide you trough the basics of **Disclawd**, this sounds great right? So let's begin!";
    public static final String HELP_WHAT_IS = ">>> What is **Disclawd**? Simple put, it is a clicker game for Discord. Ore's" +
            " from a pretty familiar game are to be mined! While doings this collect, **XP :sparkles:**, **GOLD :coin:** and Items. Also do not" +
            " forget to fight the **monsters** that can spawn!";
    public static final String HELP_ABOUT_ME = ">>> I'm a Mediainformatics student from germany. I like to develop applications that people" +
            " can interact with and enjoy playing around. This bot is only one way to express a passion for games, art and coding!" +
            " If there is any kind of feedback you would like to give, feel free to message me :mailbox:";
    public static final String HELP_END_TEXT = ">>> But now enough of reading! On the next page you will find the most important commands" +
            " and more to get started. Have fun! ~ max";
}
