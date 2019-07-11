package net.sunken.core.commands.party.handlers;

import net.sunken.common.packet.PacketHandler;
import net.sunken.common.parties.data.PartyPlayer;
import net.sunken.common.parties.packet.changes.PartyCheckPacket;
import net.sunken.core.commands.party.PartyCommand;
import net.sunken.core.inventory.ItemBuilder;
import net.sunken.core.inventory.Page;
import net.sunken.core.inventory.PageContainer;
import net.sunken.core.inventory.element.ActionableElement;
import net.sunken.core.inventory.element.Element;
import net.sunken.core.util.BungeeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class PartyCheckHandler extends PacketHandler<PartyCheckPacket> {

    @Override
    public void onReceive(PartyCheckPacket packet) {

        PageContainer container = new PageContainer();
        if (PartyCommand.getContainers().containsKey(packet.getPlayerTargeted())) {
            container = PartyCommand.getContainers().get(packet.getPlayerTargeted());
        }

        for (Page page : container.getPages().values()) {
            page.clearElements();
        }
            container.setInitialPage(partyMenu());
    }
//
//    private Page creationPage() {
//
//        Page.Builder creation = Page.builder("page-creation")
//                .title("Create A Party")
//                .size(27);
//
//        ItemBuilder createItem = new ItemBuilder(Material.BOOK)
//                .lore("&9Do you want to join friends?")
//                .lore("&9Then create a party and have them join!")
//                .name("&9&lCreate A Party");
//
//        creation.putElement(11, new ActionableElement(createItem.make(), context -> {
//            Player observer = context.getObserver();
//            return context;
//        }));
//
//        return creation.build();
//    }

    private Page partyMenu() {

        Page.Builder partyMenu = Page.builder("party-menu")
                .title("Party Menu")
                .size(54);

        return null;
    }

    private Page partyMembers(Set<PartyPlayer> partyPlayers) {

        int totalParty = partyPlayers.size();

        int index = 0;
        for (PartyPlayer player : partyPlayers) {
            int page = (int) Math.floor((index / 54) + 1);
            Page.Builder partyMembers = Page.builder("party-members")
                    .title("&9Party Members")
                    .size(54);

            // Add the heads of the players into this page.

            // Let's not use the entire 54 slots, let's use half, with half requests.

        }

        return null;
    }
}
