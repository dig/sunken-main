package net.sunken.master.party;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.status.PartyCreateStatus;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the creation, deletion and searching of parties.
 * The {@link net.sunken.common.parties.data.Party} references
 * are mutable and so modifying a party must be done by first
 * getting its instance.
 */
public final class PartyManager {

    /** leader UUID -> party */
    private static Map<UUID, Party> parties = Maps.newHashMap();

    /** Add the party to memory */
    public static PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer invitee) {
        if (parties.containsKey(leader.getUniqueId())) {
            return PartyCreateStatus.FAILED;
        }
        Party party = new Party(
                leader.getUniqueId(),
                Sets.newHashSet(leader, invitee),
                System.currentTimeMillis());
        parties.put(party.getLeaderUniqueId(), party);
        return PartyCreateStatus.SUCCESS;
    }

    /** Delete the party from memory */
    public static void deleteParty(Party party) {
        parties.remove(party.getLeaderUniqueId());
    }

    /** Returns a party if present with the leader UUID */
    @Nullable
    public static Party getPartyByLeader(UUID leaderUUID) {
        return parties.get(leaderUUID);
    }

    /**
     * Return the party the player is in or else null,
     * first checks if the player is a party leader in which case
     * the retrieval of the party is O(1), else iterate over each
     * party's member list
     */
    @Nullable
    public static Party getPartyByPlayer(UUID playerUUID) {
        Party byLeader = getPartyByLeader(playerUUID);
        if (byLeader != null) { // if the player is a party leader, we can fetch it quickly
            return byLeader;
        }
        // not a party leader, must be a member
        PartyPlayer equalityTestObject = new PartyPlayer();
        equalityTestObject.setUniqueId(playerUUID);
        for (Party party : parties.values()) {
            if (party.getAllMembers().contains(equalityTestObject)) {
                return party;
            }
        }
        return null;
    }

    /** Private constructor as it is a singleton */
    private PartyManager() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }
}
