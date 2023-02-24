package xyz.geik.farmer.helpers;

import static xyz.geik.farmer.helpers.Settings.numberFormat;

public class Formatter {

    public static String coolFormat(int iteration) {
        return coolFormat(iteration, 0);
    }

    private static String coolFormat(double n, int iteration) {
        double d = ((long) n / 100) / 10.0;
        boolean isRound = (d * 10) %10 == 0;//true if the decimal part is equal to 0 (then it's trimmed anyway)
        //this determines the class, i.e. 'k', 'm' etc
        //this decides whether to trim the decimals
        // (int) d * 10 / 10 drops the decimal
        return d < 1000? //this determines the class, i.e. 'k', 'm' etc
                (d > 99.9 || isRound || d > 9.99 ? //this decides whether to trim the decimals
                        (int) d * 10 / 10 : d + "" // (int) d * 10 / 10 drops the decimal
                ) + "" + numberFormat[iteration]
                : coolFormat(d, iteration+1);

    }
}
