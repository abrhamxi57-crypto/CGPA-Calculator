package com.seng3063.cgpa.service;

/**
 * Abstract interface for grading policies.
 * 
 * Demonstrates ABSTRACTION and POLYMORPHISM:
 * - Defines the contract for converting a numerical score to a letter grade
 *   and a grade point, without specifying the implementation.
 * - Different grading policies (e.g., DBU, standard US, European) can implement
 *   this interface and be substituted at runtime without modifying the GPA
 *   calculator or the GUI.
 * 
 * INHERITANCE is shown through the abstract base class pattern:
 * - Subclasses provide their own grade boundaries and point mappings.
 */
public interface GradingPolicy {

    /**
     * Converts a numerical score (0–100) to a letter grade.
     *
     * @param score the numerical score (0–100 inclusive)
     * @return the letter grade string (e.g., "A", "B+", "C", "F")
     * @throws IllegalArgumentException if score is outside 0–100
     */
    String getLetterGrade(double score);

    /**
     * Converts a numerical score (0–100) to a grade point.
     *
     * @param score the numerical score (0–100 inclusive)
     * @return the grade point value (e.g., 4.0, 3.75, 3.0)
     * @throws IllegalArgumentException if score is outside 0–100
     */
    double getGradePoint(double score);

    /**
     * Returns a human-readable name for this grading policy.
     *
     * @return the policy name (e.g., "DBU Grading Scale (Provisional)")
     */
    String getPolicyName();
}
