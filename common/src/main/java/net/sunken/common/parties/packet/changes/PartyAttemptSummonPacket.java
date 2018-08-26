package net.sunken.common.parties.packet.changes;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.data.PartyPlayer;

import java.util.Set;
import java.util.UUID;

public class PartyAttemptSummonPacket extends Packet {

    @Getter
    private final String serverName;
    @Getter
    private final UUID requestingSummon;
    @Getter
    private final Set<PartyPlayer> partyPlayers;

    public PartyAttemptSummonPacket(String serverName, UUID requestingSummon, Set<PartyPlayer> partyPlayers) {
        this.serverName = serverName;
        this.requestingSummon = requestingSummon;
        this.partyPlayers = partyPlayers;
    }
}
