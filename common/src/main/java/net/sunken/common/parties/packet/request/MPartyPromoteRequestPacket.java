package net.sunken.common.parties.packet.request;

import lombok.Getter;

import java.util.UUID;

/** Sent when a player wants to promote a party member to leader */
public class MPartyPromoteRequestPacket extends MPartyRequestPacket {

    @Getter
    private String toPromote; // name of the person to promote

    public MPartyPromoteRequestPacket(UUID requesting, String toPromote) {
        super(requesting);
        this.toPromote = toPromote;
    }
}
