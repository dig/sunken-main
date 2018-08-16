package net.sunken.common.parties;

import lombok.Getter;
import lombok.Setter;
import redis.clients.johm.*;

import java.util.Set;

@Model

public class Party {

    static final String LEADER_KEY = "leader";

    @Id
    @Getter
    @Setter
    private Long id;
    @Attribute
    @Indexed
    @Getter
    @Setter
    private PartyPlayer leader;
    @CollectionSet(of = PartyPlayer.class)
    @Indexed
    @Getter
    @Setter
    private Set<PartyPlayer> allMembers;
    @Attribute
    @Getter
    @Setter
    private long createdAt;

    Party(PartyPlayer leader, Set<PartyPlayer> allMembers, long createdAt) {
        this.leader = leader;
        this.allMembers = allMembers;
        this.createdAt = createdAt;
    }
}
