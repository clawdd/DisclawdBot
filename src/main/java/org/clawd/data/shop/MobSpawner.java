package org.clawd.data.shop;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.interactions.InteractionHook;
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
import java.util.Set;

public class MobSpawner {
    public void spawnMob(InteractionHook hook) {

        double spawnChance = Math.random();
        // TODO
        spawnNormalMob(hook);
        // spawnTraderMob(channelUnion);
        // spawnBossMob(channelUnion);
    }

    private void spawnNormalMob(InteractionHook hook) {
        Biome currentBiome = Main.mineworld.getCurrentBiome();
        Set<MobSubType> spawnableMobTypes = currentBiome.spawnableMobTypes();
        List<Mob> mobs = Main.mineworld.getMobList().stream()
                .filter(i -> i.getMobType().equals(MobType.NORMAL))
                .filter(i -> spawnableMobTypes.contains(i.getMobSubType())).toList();

        if (mobs.isEmpty())
            return;

        int size = mobs.size();
        int selector = (int) (Math.random() * size);
        Mob selectedMob = mobs.get(selector);

        EmbedBuilder embedBuilder = createMobEmbedded(selectedMob);

        try {
            File imgFile = new File(selectedMob.getImgPath());
            hook.sendMessageEmbeds(embedBuilder.build())
                    .addFiles(FileUpload.fromData(imgFile, "mob.png"))
                    .addActionRow(
                            Button.danger(Constants.HIT_BUTTON_ID + selectedMob.getUniqueID(), "Hit")
                    )
                    .queue();
        } catch (NullPointerException ex) {
            Main.LOG.severe("Could not load image file: " + ex.getMessage());
        }
    }

    private void spawnTraderMob(MessageChannelUnion channelUnion) {
        //TODO
    }

    private void spawnBossMob(MessageChannelUnion channelUnion) {
        //TODO
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
