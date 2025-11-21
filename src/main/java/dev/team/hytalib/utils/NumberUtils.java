package dev.team.hytalib.utils;


import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

/**
 * HytaLab Studio code @ 2025
 */
public class NumberUtils {

    public static boolean isDouble(@Nullable String value) {
        if (value == null) return false;
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isInt(@Nullable String value) {
        if (value == null) return false;
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isLong(@Nullable String value) {
        if (value == null) return false;
        try {
            Long.parseLong(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean isFloat(@Nullable String value) {
        if (value == null) return false;
        try {
            Float.parseFloat(value);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static int getIntOrDefault(@Nullable String value, int def) {
        return isInt(value) ? Integer.parseInt(value) : def;
    }

    public static double getDoubleOrDefault(@Nullable String value, double def) {
        return isDouble(value) ? Double.parseDouble(value) : def;
    }

    public static long getLongOrDefault(@Nullable String value, long def) {
        return isLong(value) ? Long.parseLong(value) : def;
    }

    public static float getFloatOrDefault(@Nullable String value, float def) {
        return isFloat(value) ? Float.parseFloat(value) : def;
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static boolean isBetween(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public static boolean isBetween(double value, double min, double max) {
        return value >= min && value <= max;
    }

    private static final DecimalFormat TWO_DECIMAL = new DecimalFormat("#.##");
    private static final DecimalFormat COMMAS = new DecimalFormat("#,###");

    public static String format2(double value) {
        return TWO_DECIMAL.format(value);
    }

    public static String formatCommas(long value) {
        return COMMAS.format(value);
    }

    public static double percent(double current, double max) {
        if (max == 0) return 0;
        return (current / max) * 100.0;
    }

    public static String percentFormatted(double current, double max) {
        return format2(percent(current, max)) + "%";
    }
}
