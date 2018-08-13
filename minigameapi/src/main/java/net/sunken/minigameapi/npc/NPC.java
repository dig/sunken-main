package net.sunken.minigameapi.npc;

import lombok.Getter;
import net.minecraft.server.v1_13_R1.*;
import net.sunken.minigameapi.util.MojangUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.CraftServer;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC extends EntityPlayer {

    @Getter
    private String name;
    @Getter
    private String uuid;

    @Getter
    private boolean visible;
    private List<String> visibleTo;

    public NPC(String uuid, String name, Location location){
        // Entity player constructor
        super(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) location.getWorld()).getHandle(),
                MojangUtil.makeProfile(name, UUID.fromString(uuid)),
                new PlayerInteractManager(((CraftWorld) location.getWorld()).getHandle()));

        this.uuid = uuid;
        this.name = name;
        this.visible = true;
        this.visibleTo = new ArrayList<String>();

        setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        getBukkitEntity().setGameMode(GameMode.SURVIVAL);
    }

    public NPC(String uuid, String name, Location location, List<String> visibleTo){
        this(uuid, name, location);

        this.visible = false;
        this.visibleTo = visibleTo;
    }

    public void showToPlayer(Player player){
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((this.yaw * 256.0F) / 360.0F)));

        // Remove the npc from scoreboard for target player
        Bukkit.getScheduler().scheduleSyncDelayedTask(MinigamePlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(
                        new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, NPC.this));
            }
        }, 5);

        // Add to visible list
        if(!this.visible){
            visibleTo.add(player.getUniqueId().toString());
        }
    }

    public List<Player> getViewerList(){
        List<Player> list = new ArrayList<Player>();

        if(!this.visible){
            for(String uuid : this.visibleTo){
                Player target = Bukkit.getPlayer(UUID.fromString(uuid));

                if(target != null && target.isOnline()){
                    list.add(target);
                }
            }
        } else {
            list.addAll(Bukkit.getOnlinePlayers());
        }

        return list;
    }

    public void hideFromPlayer(Player player){
        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.getId());
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packet);
    }

    public void remove(){
        for(Player target : this.getViewerList()){
            hideFromPlayer(target);
        }

        getBukkitEntity().remove();
    }

    public void lookAtPoint(Location point) {
        if (this.getBukkitEntity().getWorld() != point.getWorld()) {
            return;
        }
        final Location npcLoc = ((LivingEntity) this.getBukkitEntity()).getEyeLocation();
        final double xDiff = point.getX() - npcLoc.getX();
        final double yDiff = point.getY() - npcLoc.getY();
        final double zDiff = point.getZ() - npcLoc.getZ();
        final double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        final double DistanceY = Math.sqrt(DistanceXZ * DistanceXZ + yDiff * yDiff);
        double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
        final double newPitch = Math.acos(yDiff / DistanceY) * 180 / Math.PI - 90;
        if (zDiff < 0.0) {
            newYaw = newYaw + Math.abs(180 - newYaw) * 2;
        }
        this.yaw = (float) (newYaw - 90);
        this.pitch = (float) newPitch;
        this.aP = (float) (newYaw - 90);
        this.updateRotation();
    }

    public void updateRotation(){
        PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(this, (byte) ((this.yaw * 256.0F) / 360.0F));
        this.sendPacket(packet);
    }

    public void sendPacket(Packet packet){
        for(Player target : this.getViewerList()){
            PlayerConnection connection = ((CraftPlayer) target).getHandle().playerConnection;
            connection.sendPacket(packet);
        }
    }

    public void setItem(EnumItemSlot slot, ItemStack item){
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(this.getId(), slot, CraftItemStack.asNMSCopy(item));
        this.sendPacket(packet);
    }
}
