package net.sunken.common.parties.data;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.sunken.common.player.PlayerRank;

import java.io.Serializable;
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
