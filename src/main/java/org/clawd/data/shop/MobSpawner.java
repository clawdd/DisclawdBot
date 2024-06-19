package org.clawd.data.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.clawd.data.biomes.Biome;
import org.clawd.data.mobs.Mob;
import org.clawd.data.mobs.enums.MobSubType;
import org.clawd.data.mobs.enums.MobType;
import org.clawd.main.Main;
import org.clawd.tokens.Constants;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MobSpawner {
    private final Random RANDOM = new Random();
    public void spawnMob(MessageChannelUnion messageChannelUnion) {
        if (spawnNormalMob(messageChannelUnion)) return;
        if (spawnTraderMob(messageChannelUnion)) return;
        if (spawnBossMob(messageChannelUnion)) return;
    }

    private boolean spawnNormalMob(MessageChannelUnion messageChannelUnion) {
        Biome currentBiome = Main.mineworld.getCurrentBiome();
        Set<MobSubType> spawnableMobTypes = currentBiome.spawnableMobSubTypes();
        List<Mob> mobs = Main.mineworld.getMobList().stream()
                .filter(i -> i.getMobType().equals(MobType.NORMAL))
                .filter(i -> spawnableMobTypes.contains(i.getMobSubType())).toList();

        if (mobs.isEmpty())
            return false;

        int size = mobs.size();
        int selector = RANDOM.nextInt(size);
        Mob selectedMob = mobs.get(selector);

        if (RANDOM.nextDouble() > selectedMob.getSpawnChance())
            return false;

        EmbedBuilder embedBuilder = createMobEmbedded(selectedMob);
        try {
            File imgFile = new File(selectedMob.getImgPath());
            messageChannelUnion.sendMessageEmbeds(embedBuilder.build())
                    .addFiles(FileUpload.fromData(imgFile, "mob.png"))
                    .addActionRow(
                            Button.danger(Constants.HIT_BUTTON_ID + selectedMob.getUniqueID(), "Hit")
                    )
                    .queue();
            Main.LOG.info("Mob spawned: " + selectedMob.getName());
            return true;
        } catch (NullPointerException ex) {
            Main.LOG.severe("Could not load image file: " + ex.getMessage());
            return false;
        }
    }

    private boolean spawnTraderMob(MessageChannelUnion channelUnion) {
        //TODO
        return true;
    }

    private boolean spawnBossMob(MessageChannelUnion channelUnion) {
        //TODO
        return true;
    }

    private EmbedBuilder createMobEmbedded(Mob mob) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(mob.getName());
        embedBuilder.setColor(Color.RED);
        embedBuilder.setDescription(mob.getDescription());
        embedBuilder.setImage("attachment://mob.png");

        return embedBuilder;
    }
}
