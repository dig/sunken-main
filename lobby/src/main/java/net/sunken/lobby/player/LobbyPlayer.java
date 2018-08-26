package net.sunken.lobby.player;

import com.google.common.collect.ImmutableMap;
import net.sunken.common.Common;
import net.sunken.common.ServerInstance;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.server.ServerObjectCache;
import net.sunken.common.server.data.ServerObject;
import net.sunken.core.util.ScoreboardUtil;
import net.sunken.lobby.LobbyPlugin;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class LobbyPlayer extends AbstractPlayer {

    private static final String PARKOUR_FIELD = "parkours";
    private static final String PARKOUR_ID_FIELD = "id";
    private static final String PARKOUR_TIME_FIELD = "time";

    private HashMap<String, Long> parkourTimes;
    private ScoreboardUtil scoreboard;

    public LobbyPlayer(String uuid, String name, Document document, boolean firstJoin){
        super (uuid, name, document, firstJoin);

        this.parkourTimes = new HashMap<>();
        this.loadParkourTimes();

        this.scoreboard = null;
    }

    public Player toPlayer(){
        return Bukkit.getPlayer(this.getUUID());
    }

    public ChatColor getRankColour(){
        return ChatColor.valueOf(this.rank.getColour());
    }

    public void sendMessage(String message){
        this.toPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private List<Document> getPersistedParkourTimes(){
        if(this.playerDocument.containsKey(PARKOUR_FIELD)){
            return this.playerDocument.get(PARKOUR_FIELD, List.class);
        }

        return new ArrayList<Document>();
    }

    private void loadParkourTimes(){
        for(Document parkour : this.getPersistedParkourTimes()){
            parkourTimes.put(parkour.getString(PARKOUR_ID_FIELD),
                    parkour.getLong(PARKOUR_TIME_FIELD));
        }
    }

    public Long getBestTime(String id){
        if(this.parkourTimes.containsKey(id)){
            return this.parkourTimes.get(id);
        }

        return Long.MAX_VALUE;
    }

    public void updateParkourTime(String id, Long time){
        List<Document> parkours = this.getPersistedParkourTimes();
        boolean found = false;

        for(Document parkour : parkours) {
            if(parkour.getString(PARKOUR_ID_FIELD).equals(id)){
                parkour.put(PARKOUR_TIME_FIELD, time);

                found = true;
                break;
            }
        }

        if(!found){
            parkours.add(new Document(ImmutableMap.of(PARKOUR_ID_FIELD, id, PARKOUR_TIME_FIELD, time)));
        }

        this.playerDocument.put(PARKOUR_FIELD, parkours);
        this.parkourTimes.put(id, time);

        Bson updateDocument = new Document("$set", new Document(PARKOUR_FIELD, parkours));
        this.playerCollection.updateOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, this.uuid), updateDocument);
    }

    public void updateScoreboard(){
        boolean firstUpdate = false;
        ServerObjectCache serverCache = Common.getInstance().getServerCache();

        if (scoreboard == null) {
            scoreboard = new ScoreboardUtil(ChatColor.BLUE + "" + ChatColor.BOLD + "SUNKEN", this.getRankColour());
            firstUpdate = true;
        }

        int serverNum = serverCache.getServerNumber(ServerInstance.instance().getServerObject());
        int totalPlayers = 0;

        for (ServerObject server : Common.getInstance().getServerCache().getCache()) {
            totalPlayers += server.getPlayerCount();
        }

        scoreboard.add(ChatColor.WHITE + "   ", 10);
        scoreboard.add(ChatColor.GREEN + "" + ChatColor.BOLD + "You", 9);
        scoreboard.add(ChatColor.WHITE + "\u25AA Rank: " + this.getRankColour() + this.rank.getFriendlyName(), 8);
        scoreboard.add(ChatColor.WHITE + "\u25AA Gold: 0", 7);

        scoreboard.add(ChatColor.WHITE + "  ", 6);
        scoreboard.add(ChatColor.GOLD + "" + ChatColor.BOLD + "Network", 5);
        scoreboard.add(ChatColor.WHITE + "\u25AA Lobby: #" + serverNum, 4);
        scoreboard.add(ChatColor.WHITE + "\u25AA Players: " + totalPlayers, 3);

        scoreboard.add(ChatColor.WHITE + " ", 2);
        scoreboard.add(ChatColor.YELLOW + "www.sunken.net", 1);

        scoreboard.update();

        if (firstUpdate) {
            scoreboard.send(this.toPlayer());
        }
    }

}
