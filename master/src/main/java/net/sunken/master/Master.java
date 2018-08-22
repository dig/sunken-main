package net.sunken.master;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.friend.packet.FriendAcceptPacket;
import net.sunken.common.friend.packet.FriendRequestPacket;
import net.sunken.common.packet.PacketHandlerRegistry;
import net.sunken.common.parkour.ParkourCacheUpdatePacket;
import net.sunken.common.parties.packet.PartyInviteSendPacket;
import net.sunken.common.player.packet.PlayerConnectPacket;
import net.sunken.common.player.packet.PlayerJoinPacket;
import net.sunken.common.player.packet.PlayerQuitPacket;
import net.sunken.master.friend.FriendAcceptHandler;
import net.sunken.master.friend.FriendManager;
import net.sunken.master.friend.FriendRequestHandler;
import net.sunken.master.parkour.ParkourCache;
import net.sunken.master.parkour.ParkourCacheHandler;
import net.sunken.master.party.PartyInviteRequestHandler;
import net.sunken.master.player.PlayerConnectHandler;
import net.sunken.master.player.PlayerJoinHandler;
import net.sunken.master.player.PlayerQuitHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Master {

    @Getter
    private static Master instance;

    @Getter
    private FriendManager friendManager;
    @Getter
    private ParkourCache parkourCache;

    public Master() {
        instance = this;
        this.onEnable();

        // Keep application running
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onEnable(){
        Common.getInstance().onCommonLoad(true);

        this.friendManager = new FriendManager();
        this.parkourCache = new ParkourCache();

        // Register packets
        PacketHandlerRegistry.registerHandler(ParkourCacheUpdatePacket.class, new ParkourCacheHandler());
        PacketHandlerRegistry.registerHandler(PlayerJoinPacket.class, new PlayerJoinHandler());
        PacketHandlerRegistry.registerHandler(PlayerQuitPacket.class, new PlayerQuitHandler());
        PacketHandlerRegistry.registerHandler(PlayerConnectPacket.class, new PlayerConnectHandler());
        PacketHandlerRegistry.registerHandler(PartyInviteSendPacket.class, new PartyInviteRequestHandler());
        PacketHandlerRegistry.registerHandler(FriendRequestPacket.class, new FriendRequestHandler());
        PacketHandlerRegistry.registerHandler(FriendAcceptPacket.class, new FriendAcceptHandler());
    }

    public void onDisable(){
        Common.getInstance().onCommonDisable();
    }

    public static void main(String[] args) {
        new Master();
    }
}
