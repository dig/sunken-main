package net.sunken.common.parties.service;

import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.service.status.PartyCreateStatus;
import net.sunken.common.parties.data.PartyPlayer;

import java.util.UUID;

public interface PartyService {

    Party getPartyByUUID(UUID uuid);

    PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite);

    void leaveParty(UUID leaving);
}
