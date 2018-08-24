package net.sunken.master.player;

import lombok.Getter;
import lombok.Setter;
import net.sunken.common.player.AbstractPlayer;
import org.bson.Document;

public class MasterPlayer extends AbstractPlayer {

    @Getter
    @Setter
    private String serverName;

    public MasterPlayer(String uuid, String name, Document document, boolean firstJoin) {
        super(uuid, name, document, firstJoin);
        this.serverName = null;
    }
}
