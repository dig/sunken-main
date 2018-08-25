package net.sunken.master.party;

import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.packet.SendPlayerBungeeMessagePacket;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.MustBeInPartyPacket;
import net.sunken.common.parties.packet.request.MPartyChatRequestPacket;
import net.sunken.common.player.AbstractPlayer;

import java.util.Set;
import java.util.UUID;

/** Requests for promoting party players are handled here. */
public class PartyChatHandler extends PacketHandler<MPartyChatRequestPacket> {

    private static final DataManager dataManager = Common.getInstance().getDataManager();

    @Override
    public void onReceive(MPartyChatRequestPacket packet) {
        UUID requester = packet.getRequester();
        String chatMessage = packet.getMessage();

        Party party = PartyManager.getPartyByPlayer(requester);
        if (party == null) {
            PacketUtil.sendPacket(new MustBeInPartyPacket(requester));
            return;
        }

        AbstractPlayer requesterPlayer = dataManager.getOnlinePlayers().get(requester);

        Set<PartyPlayer> allMembers = party.getAllMembers();
        for (PartyPlayer member : allMembers) {
            SendPlayerBungeeMessagePacket chatPacket = new SendPlayerBungeeMessagePacket(member.getUniqueId(),
                    "[PARTY CHAT] " + requesterPlayer.getName() + ": " + chatMessage);
        }
    }
}
