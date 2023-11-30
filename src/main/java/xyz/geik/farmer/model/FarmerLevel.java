package xyz.geik.farmer.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Level object of farmer
 * Which contains capacity, required money, required perm etc.
 */
@Getter
@Setter
public class FarmerLevel {

    /**
     * Constructor of class
     */
    public FarmerLevel() {}

    /**
     * Level array which has all levels in it lower to higher
     */
    @Getter
    private static List<FarmerLevel> allLevels = new ArrayList<>();

    /**
     * Config name of level
     */
    private String dataName;

    /**
     * Capacity is item farmer can take
     * Required Money is required money for this level.
     */
    private long capacity, reqMoney;

    /**
     * Tax rate of this level
     */
    private double tax;

    /**
     * Required permission of this level
     */
    private String perm;

    /**
     * Main constructor of farmer level
     *
     * @param dataName data name of level
     * @param capacity capacity of level
     * @param reqMoney required money of level
     * @param tax tax amount of level
     * @param perm permission requirement of level
     */
    public FarmerLevel(String dataName, long capacity, long reqMoney, double tax, String perm) {
        this.dataName = dataName;
        this.capacity = capacity;
        this.reqMoney = reqMoney;
        this.tax = tax;
        this.perm = perm;
    }

    /**
     * Gets single level from farmerLevels by config name
     *
     * @param name of level
     * @return FarmerLevel object of level
     */
    public static @NotNull FarmerLevel getLevel(String name) {
        return getAllLevels().stream().filter(level -> (level.getDataName().equals(name))).findFirst().get();
    }
}
