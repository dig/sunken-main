package net.sunken.bungeecord.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.player.AbstractPlayer;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class BungeePlayer extends AbstractPlayer {

    public BungeePlayer(String uuid, String name, Document document, boolean firstJoin) {
        super(uuid, name, document, firstJoin);
    }

}
