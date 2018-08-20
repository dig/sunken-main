package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.service.status.PartyInviteStatus;
import net.sunken.common.util.PlayerDetail;

/**
 * Sent when Master attempts to validate a party invite request,
 * this is then handled on the relevant BungeeCord(s) to send
 * messages based on the {@link PartyInviteStatus}
 */
public class PartyInviteValidatePacket extends Packet {

    @Getter
    private final PlayerDetail creator;
    @Getter
    private final PlayerDetail toInvite;
    @Getter
    private final PartyInviteStatus partyInviteStatus;

    public PartyInviteValidatePacket(PlayerDetail creator,
                                     PlayerDetail toInvite,
                                     PartyInviteStatus partyInviteStatus) {
        this.creator = creator;
        this.toInvite = toInvite;
        this.partyInviteStatus = partyInviteStatus;
    }
}
