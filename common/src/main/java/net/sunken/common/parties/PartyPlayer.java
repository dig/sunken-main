package net.sunken.common.parties;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.type.ServerType;
import redis.clients.johm.Attribute;
import redis.clients.johm.Id;
import redis.clients.johm.Model;

import java.util.UUID;

@Model
public class PartyPlayer {

    @Id
    @Getter
    @Setter
    private Long id;
    @Attribute
    @Getter
    @Setter
    private UUID uuid;
    @Attribute
    @Getter
    @Setter
    private String name;
    @Attribute
    @Getter
    @Setter
    private ServerType serverType;
    @Attribute
    @Getter
    @Setter
    private PlayerRank rank;

    PartyPlayer(UUID uuid, String name, ServerType serverType, PlayerRank rank) {
        this.uuid = uuid;
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
        return Objects.equal(this.uuid, other.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.uuid);
    }
}
