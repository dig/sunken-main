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
    @Setter
    private String uuid;
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

    public PartyPlayer(UUID uuid, String name, ServerType serverType, PlayerRank rank) {
        this.uuid = uuid.toString();
        this.name = name;
        this.serverType = serverType;
        this.rank = rank;
    }

    public UUID getUUID() {
        return UUID.fromString(this.uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        PartyPlayer other = (PartyPlayer) obj;
        return Objects.equal(this.getUUID(), other.getUUID());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.uuid);
    }
}
