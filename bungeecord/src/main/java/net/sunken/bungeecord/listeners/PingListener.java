package net.sunken.bungeecord.listeners;

import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.sunken.bungeecord.BungeeMain;
import net.sunken.bungeecord.Constants;
import net.sunken.bungeecord.util.MessageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PingListener implements Listener {

    private static BufferedImage favicon = null;

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        if(favicon == null){
            favicon = this.getFavicon();
        }

        ServerPing serverPing = event.getResponse();
        String message = MessageUtil.getCenteredMessage(Constants.MOTD_TOP_LINE, MessageUtil.CENTER_PX_BUNGEE)
                + "\n" + MessageUtil.getCenteredMessage(Constants.MOTD_BOTTOM_LINE, MessageUtil.CENTER_PX_BUNGEE);
        serverPing.setDescriptionComponent(new TextComponent(message));
        serverPing.setFavicon(Favicon.create(favicon));
        event.setResponse(serverPing);
    }

    private BufferedImage getFavicon(){
        BufferedImage img = null;
        try{
            img = ImageIO.read(new File(BungeeMain.getInstance().getDataFolder(), "favicon.png"));
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return img;
    }
}
