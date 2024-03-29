package net.sunken.core.npc;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;

public class NPCInteractEvent extends Event {

    public enum NPCInteractType {

        INTERACT(),
        INTERACT_AT(),
        ATTACK()
    }

    @Getter
    private final Player player;
    @Getter
    private final NPC target;
    @Getter
    private final EquipmentSlot hand;
    @Getter
    private final NPCInteractType type;

    public NPCInteractEvent(Player player, NPC target, EquipmentSlot hand, NPCInteractType type) {
        this.player = player;
        this.target = target;
        this.hand = hand;
        this.type = type;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
