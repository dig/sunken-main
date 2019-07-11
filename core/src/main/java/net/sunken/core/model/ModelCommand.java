package net.sunken.core.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;
import com.sk89q.minecraft.util.commands.playerrank.PlayerRankRequired;
import net.sunken.common.player.PlayerRank;
import net.sunken.core.Core;
import net.sunken.core.util.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ModelCommand {

    private static Cache<UUID, Model> selection;

    static {
        selection = CacheBuilder
                .newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Command(
            aliases = {"animate"},
            desc = "Animates your current model selection.",
            usage = "<animationName> <repeat> <reverse>",
            min = 3,
            max = 3)
    public static void animate(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        String args0 = args.getString(0);
        String args1 = args.getString(1);
        String args2 = args.getString(2);
        Model selected = selection.getIfPresent(player.getUniqueId());

        if (selected != null) {
            String animationName = args0;

            if (selected.getContainer().getAnimations().containsKey(animationName)) {
                boolean repeat = false;
                if (args1.equalsIgnoreCase("true")) {
                    repeat = true;
                }

                boolean reverse = false;
                if (args2.equalsIgnoreCase("true")) {
                    reverse = true;
                }

                selected.playAnimation(animationName, repeat, reverse);
                player.sendMessage("Playing animaton " + animationName);
            } else {
                player.sendMessage("Animation not found for this model.");
            }
        }
    }

    @Command(
            aliases = {"animations"},
            desc = "Lists all animations.",
            usage = "",
            min = 0,
            max = 0)
    public static void animations(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        Model selected = selection.getIfPresent(player.getUniqueId());

        if (selected != null) {
            player.sendMessage(selected.getContainer().getAnimations().keySet().toString());
        }
    }

    @Command(
            aliases = {"remove"},
            desc = "Removes current selection.",
            usage = "",
            min = 0,
            max = 0)
    public static void remove(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        Model selected = selection.getIfPresent(player.getUniqueId());

        if (selected != null) {
            selected.remove();
            selection.invalidate(player.getUniqueId());

            player.sendMessage("Removed model!");
        }
    }

    @Command(
            aliases = {"rawrotation"},
            desc = "Rotation test command.",
            usage = "",
            min = 2,
            max = 2)
    public static void rawrotation(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        Model selected = selection.getIfPresent(player.getUniqueId());

        String yaw = args.getString(0);
        String pitch = args.getString(1);

        if (selected != null) {
            for (LivingEntity entity : selected.getEntities().values()) {
                EntityUtil.setYaw(entity, Float.parseFloat(yaw));
                EntityUtil.setPitch(entity, Float.parseFloat(pitch));
            }

            player.sendMessage("yaw: " + yaw + " pitch: " + pitch);

        }
    }

    @Command(
            aliases = {"rotate"},
            desc = "Rotates selected model.",
            usage = "<angle>",
            min = 1,
            max = 1)
    public static void rotate(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        String args0 = args.getString(0);
        Model selected = selection.getIfPresent(player.getUniqueId());

        if (selected != null) {
            float yaw = Float.parseFloat(args0);
            selected.setRotation(yaw);

            player.sendMessage("Rotated to " + yaw);
        }
    }

    @Command(
            aliases = {"spawn"},
            desc = "Spawns a model.",
            usage = "<modelName>",
            min = 1,
            max = 1)
    public static void spawn(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        String args0 = args.getString(0);

        File file = new File(Core.getPlugin().getDataFolder(), "model" + File.separator + args0 + ".sunken");

        if (file.exists()) {
            ModelContainer container = new ModelContainer(file.getAbsolutePath());
            Model model = new Model(container, player.getLocation());

            selection.put(player.getUniqueId(), model);
            player.sendMessage("Model spawned on your location!");
        } else {
            player.sendMessage("Could not find model file.");
        }
    }

    @Command(
            aliases = {"test"},
            desc = "Test command.",
            usage = "<invisible> <offset> <rotation>",
            min = 3,
            max = 3)
    public static void test(final CommandContext args, final CommandSender sender) {
        if (!(sender instanceof Player)) return;

        Player player = (Player) sender;
        String args0 = args.getString(0);
        String args1 = args.getString(1);
        String args2 = args.getString(2);

        Minecart minecart = (Minecart) player.getWorld().spawnEntity(player.getLocation(), EntityType.MINECART);

        BlockData blockData = Bukkit.getServer().createBlockData(Material.DIAMOND_BLOCK);
        FallingBlock block = player.getWorld().spawnFallingBlock(player.getLocation(), blockData);
        block.setSilent(true);
        block.setDropItem(false);
        block.setHurtEntities(false);
        block.setFallDistance(0);
        block.setTicksLived(Integer.MAX_VALUE);

        EntityUtil.setYaw(block, Float.parseFloat(args2));
        EntityUtil.setYaw(minecart, Float.parseFloat(args2));

        minecart.addPassenger(block);

        // minecart.setGravity(false);
        minecart.setSilent(true);
        minecart.setDisplayBlockOffset(Integer.parseInt(args1));
    }

    public static class Parent {

        @Command(
                aliases = {"model"},
                desc = "Spawn a custom model.")
        @PlayerRankRequired(PlayerRank.DEVELOPER)
        @NestedCommand(ModelCommand.class)
        public static void parent(final CommandContext args, final CommandSender sender) {
        }
    }

}
