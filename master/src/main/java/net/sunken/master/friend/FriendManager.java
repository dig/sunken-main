package net.sunken.master.friend;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FriendManager {

    /*
        Need to rethink how I am going to store friend invites as this method
        will not allow duplicates.
     */
    @Getter
    private Cache<UUID, UUID> friendInvites;

    public FriendManager () {
        this.friendInvites = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

}
