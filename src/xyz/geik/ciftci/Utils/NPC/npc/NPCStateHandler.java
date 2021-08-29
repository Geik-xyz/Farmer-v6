package xyz.geik.ciftci.Utils.NPC.npc;

public class NPCStateHandler {
    private boolean fire, sneak;

    public boolean isOnFire() {
        return fire;
    }

    public void setOnFire(boolean fire) {
        this.fire = fire;
    }

    public boolean isSneaking() {
        return sneak;
    }

    public void setSneaking(boolean sneak) {
        this.sneak = sneak;
    }

    public int getId() {
        return fire && sneak ? 3 : sneak ? 2 : fire ? 1 : 0;
    }
}
