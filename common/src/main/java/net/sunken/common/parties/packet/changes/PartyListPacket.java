package net.sunken.common.parties.packet.changes;

import lombok.Getter;
import net.sunken.common.packet.PlayerPacket;
import net.sunken.common.parties.data.PartyPlayer;

import java.util.Set;
import java.util.UUID;

/** Sent by Master, contains a set of party players */
public class PartyListPacket extends PlayerPacket {

    @Getter
    private final Set<PartyPlayer> partyPlayers;

    @Getter
    private Boolean chat;

    public PartyListPacket(UUID playerTargeted, Set<PartyPlayer> partyPlayers, Boolean chat) {
        super(playerTargeted);
        this.partyPlayers = partyPlayers;
        this.chat = chat;
    }
}
