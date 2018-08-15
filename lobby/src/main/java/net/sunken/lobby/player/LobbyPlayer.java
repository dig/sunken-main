package net.sunken.lobby.player;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.sunken.common.achievements.Achievement;
import net.sunken.common.achievements.AchievementRegistry;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.lobby.LobbyPlugin;
import net.sunken.lobby.parkour.Parkour;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LobbyPlayer extends AbstractPlayer {

    private static final String PARKOUR_FIELD = "parkours";
    private static final String PARKOUR_ID_FIELD = "id";
    private static final String PARKOUR_TIME_FIELD = "time";

    private HashMap<String, Long> parkourTimes;

    public LobbyPlayer(Player player){
        super(
                player.getUniqueId().toString(),
                player.getName()
        );

        this.parkourTimes = new HashMap<>();
        this.loadParkourTimes();
    }

    public Player toPlayer(){
        return Bukkit.getPlayer(this.getUUID());
    }

    public ChatColor getRankColour(){
        return ChatColor.valueOf(this.rank.getColour());
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

        return (long) 0;
    }

    public void updateParkourTime(String id, Long time){
        List<Document> parkours = this.getPersistedParkourTimes();

        if(this.parkourTimes.containsKey(id)){
            for(Document parkour : parkours) {
                if(parkour.getString(PARKOUR_ID_FIELD).equals(id)){
                    parkour.remove(PARKOUR_TIME_FIELD);
                    parkour.put(PARKOUR_TIME_FIELD, time);
                    break;
                }
            }
        }
        else{
            parkours.add(new Document(ImmutableMap.of(PARKOUR_ID_FIELD, id, PARKOUR_TIME_FIELD, time)));
        }

        this.parkourTimes.put(id, time);
        this.playerCollection.replaceOne(new Document(UUID_FIELD, this.uuid), playerDocument);
    }

}
