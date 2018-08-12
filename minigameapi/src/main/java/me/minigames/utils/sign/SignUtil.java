package me.minigames.utils.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.minigames.MinigamePlugin;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Digital on 22/01/2018.
 */
public class SignUtil {

    private static boolean listening = false;

    private static void listen(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(MinigamePlugin.getInstance(), PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                String[] signContents = event.getPacket().getStringArrays().read(0);

                new BukkitRunnable(){

                    @Override
                    public void run() {
                        Bukkit.getPluginManager().callEvent(new SignUpdateEvent(event.getPlayer(), signContents));
                    }

                }.runTask(MinigamePlugin.getInstance());
            }
                
        });

        listening = true;
    }

    public static void open(Player player, String[] lines){
        if(!listening){
            listen();
        }

        Material oldMat = player.getWorld().getBlockAt(player.getLocation().add(0, 3, 0)).getType();
        byte oldData = player.getWorld().getBlockAt(player.getLocation().add(0, 3, 0)).getData();

        player.sendBlockChange(player.getLocation().add(0, 3, 0), Material.WALL_SIGN, (byte)0);
        player.sendSignChange(player.getLocation().add(0, 3, 0), lines);

        PacketPlayOutOpenSignEditor packet = new PacketPlayOutOpenSignEditor(new BlockPosition(player.getLocation().getX(),
                player.getLocation().getY() + 3, player.getLocation().getZ()));

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packet);

        player.sendBlockChange(player.getLocation().add(0, 3, 0), oldMat, oldData);
    }

}
