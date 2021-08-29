package xyz.geik.ciftci.Utils.NPC.util;

public class CoordinateUtil {
    private CoordinateUtil() {}

    public static int getFixedNumber(double value) {
        return (int) (value * 32.0D);
    }

    public static double getDouble(int value) {
        return value / 32.0D;
    }

    public static byte getByteAngle(float angle) {
        return (byte) (angle * 256.0F / 360.0F);
    }

    public static float getFloatAngle(byte angle) {
        return (angle * 360.F) / 256.0F;
    }
}
