package xyz.geik.ciftci.Utils.NPC.skin;

public enum SkinLayer {
    CAPE(0),
    HAT(6),
    JACKET(1),
    LEFT_PANTS(4),
    LEFT_SLEEVE(2),
    RIGHT_PANTS(5),
    RIGHT_SLEEVE(3);

    final int flag;

    SkinLayer(int offset) {
        this.flag = 1 << offset;
    }
}
