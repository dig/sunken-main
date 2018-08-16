package net.sunken.common.parties;

import lombok.Getter;
import lombok.Setter;
import redis.clients.johm.*;

import java.util.Set;
import java.util.UUID;

@Model
public class Party {

    static final String LEADER_KEY = "leaderUUID";
    static final String ALL_MEMBERS_KEY = "allMembers";

    @Id
    @Getter
    @Setter
    private Long id;
    @Attribute
    @Indexed
    @Setter
    private String leaderUUID;
    @CollectionSet(of = PartyPlayer.class)
    @Indexed
    @Getter
    @Setter
    private Set<PartyPlayer> allMembers;
    @Attribute
    @Getter
    @Setter
    private long createdAt;

    public Party(UUID leaderUUID, Set<PartyPlayer> allMembers, long createdAt) {
        this.leaderUUID = leaderUUID.toString();
        this.allMembers = allMembers;
        this.createdAt = createdAt;
    }

    public UUID getLeaderUUID() {
        return UUID.fromString(leaderUUID);
    }
}
