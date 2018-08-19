package net.sunken.bungeecord.player;

import net.sunken.common.player.AbstractPlayer;
import org.bson.Document;

public class BungeePlayer extends AbstractPlayer {

    public BungeePlayer(String uuid, String name, Document document, boolean firstJoin) {
        super(uuid, name, document, firstJoin);
    }
}
