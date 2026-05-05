package com.psl.util;

public class Validator {

    public static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validatePrice(String price) {
        return isNumeric(price) && Double.parseDouble(price) > 0;
    }
}