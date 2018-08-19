package net.sunken.common.parties.data;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@ToString
public class Party implements Serializable {

    @Getter
    @Setter
    private UUID partyUUID;
    @Getter
    @Setter
    private UUID leaderUniqueId;
    @Getter
    @Setter
    private Set<PartyPlayer> allMembers;
    @Getter
    @Setter
    private long createdAt;

    public Party(UUID partyUUID,
                 UUID leaderUniqueId,
                 Set<PartyPlayer> allMembers,
                 long createdAt) {

        this.partyUUID = partyUUID;
        this.leaderUniqueId = leaderUniqueId;
        this.allMembers = allMembers;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != getClass()) return false;
        Party other = (Party) obj;
        return Objects.equal(this.leaderUniqueId, other.leaderUniqueId)
                && Objects.equal(this.createdAt, other.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.leaderUniqueId, this.createdAt);
    }
}
