package com.coldstoragecoins.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class is used as
 *
 * @author DreamWorksSoftwares
 * @version 1.0
 * @since 11 Aug,2018
 */
public class Util {
    private static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String getProfileImage(String email) {
        String image = "http://gravatar.com/avatar/";
        String md = md5(email);
        return image + md;
    }

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    public static String currencySymbol(String currency) {
        if (currency.equalsIgnoreCase("USD")) {
            return "$";
        } else if (currency.equalsIgnoreCase("EUR")) {
            return "€";
        } else if (currency.equalsIgnoreCase("GBP")) {
            return "£";
        } else {
            return currency;
        }
    }

}
