package net.sunken.common.parties.data;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.type.ServerType;

import java.util.UUID;

public class PartyPlayer {

    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private UUID uniqueId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private ServerType serverType;
    @Getter
    @Setter
    private PlayerRank rank;

    public PartyPlayer(UUID uuid, String name, ServerType serverType, PlayerRank rank) {
        this.uniqueId = uuid;
        this.name = name;
        this.serverType = serverType;
        this.rank = rank;
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
