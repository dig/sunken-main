package net.sunken.master.friend;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;

import java.util.UUID;

public class FriendManager {

    @Getter
    private Multimap<UUID, UUID> friendInvites;

    public FriendManager () {
        this.friendInvites = ArrayListMultimap.create();
    }

}
