package com.seng3063.cgpa.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class for consistent number formatting across the application.
 * 
 * Uses BigDecimal for precise rounding to avoid floating-point display artifacts
 * (e.g., 2.6399999999 displaying instead of 2.64).
 */
public final class NumberFormatter {

    // Private constructor prevents instantiation of utility class
    private NumberFormatter() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Formats a double value to the specified number of decimal places.
     * Uses HALF_UP rounding (standard mathematical rounding).
     *
     * @param value         the value to format
     * @param decimalPlaces the number of decimal places
     * @return the formatted string
     */
    public static String format(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(value)
                .setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.toPlainString();
    }

    /**
     * Rounds a double to the specified number of decimal places.
     * Returns the rounded value as a double.
     *
     * @param value         the value to round
     * @param decimalPlaces the number of decimal places
     * @return the rounded double
     */
    public static double round(double value, int decimalPlaces) {
        BigDecimal bd = BigDecimal.valueOf(value)
                .setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Formats a GPA value to 2 decimal places.
     *
     * @param gpa the GPA value
     * @return the formatted GPA string (e.g., "2.64")
     */
    public static String formatGpa(double gpa) {
        return format(gpa, 2);
    }

    /**
     * Formats a grade point value to 2 decimal places.
     *
     * @param gradePoint the grade point value
     * @return the formatted grade point string (e.g., "4.00")
     */
    public static String formatGradePoint(double gradePoint) {
        return format(gradePoint, 2);
    }
}
