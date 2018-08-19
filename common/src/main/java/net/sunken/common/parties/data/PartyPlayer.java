package net.sunken.common.parties.data;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.sunken.common.player.PlayerRank;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ToString
public class PartyPlayer implements Serializable {

    @Getter
    @Setter
    private UUID uniqueId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private PlayerRank rank;

    public PartyPlayer(UUID uuid, String name, PlayerRank rank) {
        this.uniqueId = uuid;
        this.name = name;
        this.rank = rank;
    }

    public PartyPlayer() {
    }

    public Map<String, String> toMap() {
        Map<String, String> details = new HashMap<>();
        details.put("name",  name);
        details.put("uuid", uniqueId.toString());
        details.put("player_rank", rank.name());
        return details;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        PartyPlayer other = (PartyPlayer) obj;
        return Objects.equal(this.uniqueId, other.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.uniqueId);
    }
}
