package net.sunken.minigameapi.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

/**
 * Created by Digital on 21/01/2018.
 *
 * Tons of shit for fireworks
 */
public class FireworkUtil {

    public static Firework spawnRandomFirework(Location l) {
        World w = l.getWorld();

        Firework fw = (Firework) w.spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        fw.setFireworkMeta(randomizeMeta(fwm));

        return fw;
    }

    public static Firework spawnFirework(Location l, FireworkEffect.Type type, Color mainColor, Color fade, Boolean flicker, Boolean trail) {
        World w = l.getWorld();
        Firework fw = (Firework) w.spawnEntity(l, EntityType.FIREWORK);

        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().flicker(flicker).withColor(mainColor).withFade(fade).with(type).trail(trail).build();
        fwm.addEffect(effect);
        fwm.setPower(2);
        fw.setFireworkMeta(fwm);

        return fw;
    }

    public static FireworkMeta randomizeMeta(FireworkMeta original) {
        //Get the type
        Random r = new Random();

        int rt = r.nextInt(5) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) {
            type = FireworkEffect.Type.BALL;
        }
        if (rt == 2) {
            type = FireworkEffect.Type.BALL_LARGE;
        }
        if (rt == 3) {
            type = FireworkEffect.Type.BURST;
        }
        if (rt == 4) {
            type = FireworkEffect.Type.CREEPER;
        }
        if (rt == 5) {
            type = FireworkEffect.Type.STAR;
        }

        //Get our random colours
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColorByID(r1i);
        Color c2 = getColorByID(r2i);

        //Create our effect with this
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

        //Then apply the effect to the meta
        original.addEffect(effect);

        //Generate some random power and set it
        int rp = r.nextInt(2) + 1;
        original.setPower(rp);

        return original;
    }

    private static Color getColorByID(int i) {
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
        }

        return c;
    }

}
