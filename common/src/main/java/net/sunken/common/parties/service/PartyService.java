package net.sunken.common.parties.service;

import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.service.status.PartyInviteStatus;

import java.util.UUID;

public interface PartyService {

    PartyInviteStatus validateInviteRequest(UUID creator, UUID invitee);

    Party getPartyByUUID(UUID uuid);

    PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite);

    void leaveParty(UUID leaving);
}
