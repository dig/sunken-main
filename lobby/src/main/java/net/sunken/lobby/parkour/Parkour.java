package net.sunken.lobby.parkour;

import lombok.Getter;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parkour.ParkourCacheUpdatePacket;
import net.sunken.common.player.PlayerRank;
import net.sunken.core.hologram.Hologram;
import net.sunken.lobby.LobbyPlugin;
import net.sunken.lobby.player.LobbyPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Parkour {

    @Getter
    private String id;
    @Getter
    private HashMap<UUID, Long> players;
    private HashMap<UUID, Integer> checkpoint;

    @Getter
    private Material mainBlock;
    @Getter
    private ArrayList<Material> allowedMaterials;

    @Getter
    private Location startLocation;
    @Getter
    private Location endLocation;
    @Getter
    private List<Location> checkpoints;
    private Location resetPoint;
    private Location leaderboardPos;

    private boolean timed;
    private boolean leaderboard;

    private Hologram hologram;

    public Parkour (String id, Material mainBlock, ArrayList<Material> allowedMaterials,
                    Location start, Location end, List<Location> checkpoints,
                    Location resetPoint, boolean timed){
        this.id = id;
        this.players = new HashMap<>();
        this.checkpoint = new HashMap<>();

        this.mainBlock = mainBlock;
        this.allowedMaterials = allowedMaterials;

        this.startLocation = start;
        this.endLocation = end;
        this.checkpoints = checkpoints;
        this.resetPoint = resetPoint;

        this.timed = timed;
        this.leaderboard = false;
    }

    public Parkour (String id, Material mainBlock, ArrayList<Material> allowedMaterials,
                    Location start, Location end, List<Location> checkpoints,
                    Location resetPoint, boolean timed, boolean leaderboard,
                    Location leaderboardPos){
        this(id, mainBlock, allowedMaterials, start, end, checkpoints, resetPoint, timed);

        this.leaderboard = leaderboard;
        this.leaderboardPos = leaderboardPos;
        this.hologram = null;
    }

    public void addPlayer(LobbyPlayer player){
        this.players.put(player.getUUID(), System.currentTimeMillis());
        this.checkpoint.put(player.getUUID(), 0);

        player.sendMessage("&aParkour started! Finish the course in as little time as possible.");
    }

    public void stopPlayer(LobbyPlayer player, boolean finished){
        if(finished){
            long time = System.currentTimeMillis() - this.players.get(player.getUUID());

            DecimalFormat df = new DecimalFormat("0.000");
            player.sendMessage("&aParkour finished! You finished in " + df.format(((double) time / 1000)) + " seconds.");

            if(time < player.getBestTime(this.id)){
                player.updateParkourTime(this.id, time);
                player.sendMessage("&a&lNew personal best!");

                // Check if we should update the leaderboard
                ParkourCache cache = LobbyPlugin.getInstance().getParkourCache();
                List<ParkourData> parkourData = cache.getBestTimes(this.id);

                if (parkourData != null) {
                    if(parkourData.size() < 10 || time <= parkourData.get((parkourData.size() - 1)).getTime()){
                        PacketUtil.sendPacket(new ParkourCacheUpdatePacket(this.id));
                    }
                } else {
                    PacketUtil.sendPacket(new ParkourCacheUpdatePacket(this.id));
                }
            }
        }

        checkpoint.remove(player.getUUID());
        players.remove(player.getUUID());
        player.toPlayer().teleport(this.resetPoint);
    }

    public void setCheckpoint(LobbyPlayer player, int index){
        long time = System.currentTimeMillis() - this.players.get(player.getUUID());

        if((index + 1) > checkpoint.get(player.getUUID())){
            checkpoint.put(player.getUUID(), (index + 1));

            DecimalFormat df = new DecimalFormat("0.000");
            player.sendMessage("&aYou reached Checkpoint #" + (index + 1) + " in " + df.format(((double) time / 1000)) + " seconds.");
        }
    }

    public void updateLeaderboard(){
        if(this.leaderboard){
            List<String> lines = new ArrayList<>();

            lines.add(ChatColor.BLUE + "" + ChatColor.BOLD + "Parkour Leaderboard");

            ParkourCache cache = LobbyPlugin.getInstance().getParkourCache();

            int x = 1;
            DecimalFormat df = new DecimalFormat("0.000");

            if (cache.getBestTimes(this.id) != null) {
                for(ParkourData ply : cache.getBestTimes(this.id)){
                    PlayerRank rank = ply.getRank();
                    ChatColor rankColor = ChatColor.valueOf(rank.getColour());

                    lines.add(ChatColor.AQUA + Integer.toString(x) + ". "
                            + rankColor + ply.getName()
                            + ChatColor.GRAY + " - "
                            + ChatColor.AQUA + df.format(((double) ply.getTime() / 1000)));

                    x++;
                }
            }

            if (this.hologram != null) {
                x = 0;
                for(String line : lines){
                    if (this.hologram.getLine(x) == null) {
                        this.hologram.addLine(line);
                    } else if (!this.hologram.getLine(x).equals(line)) {
                        this.hologram.updateLine(x, line);
                    }

                    x++;
                }
            } else {
                this.hologram = new Hologram(this.leaderboardPos, lines, 0.40);
            }
        }
    }

    public void cleanup(){
        if(this.leaderboard && this.hologram != null){
            this.hologram.removeAll();
        }
    }

}
