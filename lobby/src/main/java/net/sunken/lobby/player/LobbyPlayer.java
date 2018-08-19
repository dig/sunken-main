package net.sunken.lobby.player;

import com.google.common.collect.ImmutableMap;
import net.sunken.common.Common;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.player.AbstractPlayer;
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

    public LobbyPlayer(String uuid, String name, Document document, boolean firstJoin){
        super (uuid, name, document, firstJoin);

        this.parkourTimes = new HashMap<>();
        this.loadParkourTimes();
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

}
