package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.Packet;
import net.sunken.common.parties.status.PartyInviteStatus;
import net.sunken.common.util.PlayerDetail;

/**
 * Sent when Master has validated a party invite request,
 * this is then handled on the relevant BungeeCord(s) to send
 * messages based on the {@link PartyInviteStatus}
 */
public class PartyInviteValidatedPacket extends Packet {

    @Getter
    private final PlayerDetail creator;
    @Getter
    private final PlayerDetail toInvite;
    @Getter
    private final PartyInviteStatus partyInviteStatus;

    public PartyInviteValidatedPacket(PlayerDetail creator,
                                      PlayerDetail toInvite,
                                      PartyInviteStatus partyInviteStatus) {
        this.creator = creator;
        this.toInvite = toInvite;
        this.partyInviteStatus = partyInviteStatus;
    }
}
