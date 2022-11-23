package com.mindhub.homebanking.utils;

public final class CardUtils {

    private CardUtils() {
    }

    public static String getCardNumberVISA() {
        return (String) ("4" + (Math.random() * (1000 - 100) + 100))
                + "-" + (int) (Math.random() * (10000 - 1000) + 1000)
                + "-" + (int) (Math.random() * (10000 - 1000) + 1000)
                + "-" + (int) (Math.random() * (10000 - 1000) + 1000);
    }
    public static String getCardNumberMasterCard() {
        return  (String) ("5" + (Math.random() * (1000 - 100) + 100))
                + "-" + (int) (Math.random() * (10000 - 1000) + 1000)
                + "-" + (int) (Math.random() * (10000 - 1000) + 1000)
                + "-" + (int) (Math.random() * (10000 - 1000) + 1000);
    }


    public static int getCardNumberCVV() {
        return (int) (Math.random() * (1000 - 100) + 100);
    }

}
