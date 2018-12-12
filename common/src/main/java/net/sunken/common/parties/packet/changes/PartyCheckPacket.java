package net.sunken.common.parties.packet.changes;

import lombok.Getter;
import net.sunken.common.packet.PlayerPacket;
import net.sunken.common.parties.data.PartyPlayer;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class PartyCheckPacket extends PlayerPacket {

    @Getter
    private boolean inParty;

    @Getter
    private Set<PartyPlayer> playersInParty;

    @Getter
    private Collection<UUID> requests;

    public PartyCheckPacket(UUID playerTargeted, Boolean inParty, Set<PartyPlayer> playersInParty, Collection<UUID> requests) {
        super(playerTargeted);
        this.inParty = inParty;
        this.playersInParty = playersInParty;
        this.requests = requests;
    }
}
