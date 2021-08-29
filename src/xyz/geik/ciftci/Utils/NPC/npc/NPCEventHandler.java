package xyz.geik.ciftci.Utils.NPC.npc;

import org.bukkit.entity.Player;

public interface NPCEventHandler {

    void onInteract(NPC npc, Player player, InteractType type);

    enum InteractType {
        LEFT_CLICK, RIGHT_CLICK
    }
}
