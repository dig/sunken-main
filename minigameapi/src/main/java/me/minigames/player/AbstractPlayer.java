package me.minigames.player;

import lombok.Getter;
import me.minigames.MinigamePlugin;
import me.minigames.common.Rank;
import redis.clients.jedis.Jedis;

/**
 * Created by Digital on 26/01/2018.
 */
public abstract class AbstractPlayer {

    @Getter
    private String uuid;
    @Getter
    private String username;
    @Getter
    private Rank rank;

    public AbstractPlayer(String uuid) {
        this.uuid = uuid;

        // Load from redis cache
        Jedis jedis = MinigamePlugin.getInstance().getRedis().getConnection();
        this.username = jedis.hget(uuid, "username");
        this.rank = Rank.getById(Integer.parseInt(jedis.hget(uuid, "rank")));

        this.onLoad();
    }

    public abstract void onQuit();
    public abstract void onLoad();

}
