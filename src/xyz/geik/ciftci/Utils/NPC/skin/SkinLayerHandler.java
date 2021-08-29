package xyz.geik.ciftci.Utils.NPC.skin;

public class SkinLayerHandler {

    private boolean cape = true, hat = true, jacket = true, leftPants = true, leftSleeve = true, rightPants = true, rightSleeve = true;

    public void setLayer(SkinLayer layer, boolean value) {
        switch (layer) {
            case CAPE:
                cape = value;
            case JACKET:
                jacket = value;
            case LEFT_SLEEVE:
                leftSleeve = value;
            case RIGHT_SLEEVE:
                rightSleeve = value;
            case LEFT_PANTS:
                leftPants = value;
            case RIGHT_PANTS:
                rightPants = value;
            case HAT:
                hat = value;
        }
    }

    public boolean isVisible(SkinLayer layer) {
        switch (layer) {
            case CAPE:
                return cape;
            case JACKET:
                return jacket;
            case LEFT_SLEEVE:
                return leftSleeve;
            case RIGHT_SLEEVE:
                return rightSleeve;
            case LEFT_PANTS:
                return leftPants;
            case RIGHT_PANTS:
                return rightPants;
            case HAT:
                return hat;
            default:
                return false;
        }
    }

    public byte getFlags() {
        int flags = 0xFF;
        for(SkinLayer layer : SkinLayer.values()) {
            if (!isVisible(layer)) {
                flags &= ~layer.flag;
            }
        }
        return (byte) flags;
    }


}
