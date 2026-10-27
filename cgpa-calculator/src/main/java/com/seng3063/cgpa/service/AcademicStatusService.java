package com.seng3063.cgpa.service;

/**
 * Determines academic standing based on the calculated GPA.
 * 
 * Uses the exact thresholds specified in the SENG3063 assignment:
 *   - GPA >= 2.00              → Pass
 *   - 1.75 <= GPA < 2.00       → Warning
 *   - 1.50 <= GPA < 1.75       → Readmission
 *   - GPA < 1.50               → Academic Dismissal
 *
 * The conditions are evaluated in descending order to prevent
 * overlapping threshold matches.
 * 
 * Demonstrates ABSTRACTION:
 * - Status determination is isolated from GPA calculation and GUI.
 * - Thresholds are clearly documented and testable.
 */
public class AcademicStatusService {

    /** GPA at or above this value: Pass */
    private static final double PASS_THRESHOLD = 2.00;
    /** GPA at or above this value (but below PASS): Warning */
    private static final double WARNING_THRESHOLD = 1.75;
    /** GPA at or above this value (but below WARNING): Readmission */
    private static final double READMISSION_THRESHOLD = 1.50;

    // Status string constants
    public static final String STATUS_PASS = "Pass";
    public static final String STATUS_WARNING = "Warning";
    public static final String STATUS_READMISSION = "Readmission";
    public static final String STATUS_DISMISSAL = "Academic Dismissal";
    public static final String STATUS_NO_COURSES = "No Courses Entered";

    /**
     * Determines academic status from the calculated GPA.
     * 
     * The conditions are evaluated from highest threshold to lowest
     * to ensure correct, non-overlapping classification.
     *
     * @param gpa            the calculated GPA
     * @param hasCourses     true if at least one valid course exists
     * @return the academic status string
     */
    public String determineStatus(double gpa, boolean hasCourses) {
        if (!hasCourses) {
            return STATUS_NO_COURSES;
        }

        // Evaluate thresholds in descending order (highest first)
        if (gpa >= PASS_THRESHOLD) {
            return STATUS_PASS;
        } else if (gpa >= WARNING_THRESHOLD) {
            return STATUS_WARNING;
        } else if (gpa >= READMISSION_THRESHOLD) {
            return STATUS_READMISSION;
        } else {
            return STATUS_DISMISSAL;
        }
    }

    /**
     * Returns a CSS style class suffix for the academic status.
     * Used to apply color-coded styling in the GUI.
     *
     * @param status the academic status string
     * @return a CSS-friendly class suffix (e.g., "pass", "warning")
     */
    public String getStatusStyleClass(String status) {
        return switch (status) {
            case STATUS_PASS -> "pass";
            case STATUS_WARNING -> "warning";
            case STATUS_READMISSION -> "readmission";
            case STATUS_DISMISSAL -> "dismissal";
            default -> "none";
        };
    }
}
