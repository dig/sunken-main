package net.sunken.common;

import lombok.Getter;
import net.sunken.common.database.MongoConnection;
import net.sunken.common.database.RedisConnection;
import net.sunken.common.type.ServerType;

public abstract class Common {

    @Getter
    private ServerType type;

    @Getter
    public static Common instance;

    @Getter
    private MongoConnection mongo;
    @Getter
    private RedisConnection redis;

    public void onCommonLoad(ServerType type){
        this.type = type;
        this.instance = this;

        this.mongo = new MongoConnection("localhost", 27017, "username", "password");
        this.redis = new RedisConnection("localhost", 3306, "password");
    }

    public void onCommonDisable(){

    }

}
