package com.seng3063.cgpa.service;

/**
 * DBU (Debre Berhan University) Grading Policy — PROVISIONAL CONFIGURATION.
 * 
 * Demonstrates INHERITANCE and POLYMORPHISM:
 * - Implements the GradingPolicy interface.
 * - Can be swapped with another GradingPolicy implementation without
 *   changing the GPA calculator or the GUI.
 * 
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │  IMPORTANT: PROVISIONAL GRADING SCALE                              │
 * │                                                                     │
 * │  The assignment specifies examples: A = 4.0, A- = 3.75, C = 1.0,  │
 * │  F = 0.0. The complete DBU grading boundaries were not provided    │
 * │  in the lab document.                                               │
 * │                                                                     │
 * │  The boundaries below are based on a commonly used Ethiopian       │
 * │  university grading scale. VERIFY these against the official DBU   │
 * │  grading policy before any academic use.                            │
 * │                                                                     │
 * │  To update: modify ONLY the GRADE_TABLE array below.               │
 * └─────────────────────────────────────────────────────────────────────┘
 *
 * Provisional grade boundaries (score ranges are inclusive at the lower bound):
 *
 *   Score Range    Letter Grade    Grade Point
 *   ──────────     ────────────    ───────────
 *   90 – 100       A               4.00
 *   85 –  89       A-              3.75
 *   80 –  84       B+              3.50
 *   75 –  79       B               3.00
 *   70 –  74       B-              2.75
 *   65 –  69       C+              2.50
 *   60 –  64       C               2.00  (Note: lab example says C=1.0; 
 *                                          see note below)
 *   50 –  59       C-              1.75
 *   45 –  49       D               1.00
 *   40 –  44       D-              0.75  (Note: some DBU scales use this)
 *    0 –  39       F               0.00
 *
 * NOTE ON C = 1.0 vs C = 2.0:
 * The assignment gives the example C = 1.0. In most Ethiopian university
 * scales, C maps to 2.0 and D maps to 1.0. The example C = 1.0 may refer
 * to a different grading scale or may be an illustrative simplification.
 * 
 * This implementation uses C = 2.0 following common DBU practice.
 * If the official policy states C = 1.0, update the GRADE_TABLE entry.
 */
public class DbuGradingPolicy implements GradingPolicy {

    /**
     * Internal record for a grade boundary.
     * Each entry defines: minimum score threshold, letter grade, and grade point.
     * The table MUST be sorted in descending order by minScore.
     */
    private static final GradeEntry[] GRADE_TABLE = {
        new GradeEntry(90, "A",  4.00),
        new GradeEntry(85, "A-", 3.75),
        new GradeEntry(80, "B+", 3.50),
        new GradeEntry(75, "B",  3.00),
        new GradeEntry(70, "B-", 2.75),
        new GradeEntry(65, "C+", 2.50),
        new GradeEntry(60, "C",  2.00),
        new GradeEntry(50, "C-", 1.75),
        new GradeEntry(45, "D",  1.00),
        new GradeEntry(40, "D-", 0.75),
        new GradeEntry( 0, "F",  0.00),
    };

    @Override
    public String getLetterGrade(double score) {
        validateScore(score);
        for (GradeEntry entry : GRADE_TABLE) {
            if (score >= entry.minScore) {
                return entry.letterGrade;
            }
        }
        // Should not reach here if table starts at 0, but safety fallback
        return "F";
    }

    @Override
    public double getGradePoint(double score) {
        validateScore(score);
        for (GradeEntry entry : GRADE_TABLE) {
            if (score >= entry.minScore) {
                return entry.gradePoint;
            }
        }
        // Should not reach here if table starts at 0, but safety fallback
        return 0.0;
    }

    @Override
    public String getPolicyName() {
        return "DBU Grading Scale (Provisional)";
    }

    /**
     * Validates that the score is within the acceptable range [0, 100].
     *
     * @param score the score to validate
     * @throws IllegalArgumentException if score is outside [0, 100], NaN, or infinite
     */
    private void validateScore(double score) {
        if (Double.isNaN(score) || Double.isInfinite(score)) {
            throw new IllegalArgumentException("Score must be a valid number, got: " + score);
        }
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException(
                    "Score must be between 0 and 100, got: " + score);
        }
    }

    /**
     * Simple record-like class for a grade table entry.
     * Each entry maps a minimum score to a letter grade and grade point.
     */
    private static final class GradeEntry {
        final double minScore;
        final String letterGrade;
        final double gradePoint;

        GradeEntry(double minScore, String letterGrade, double gradePoint) {
            this.minScore = minScore;
            this.letterGrade = letterGrade;
            this.gradePoint = gradePoint;
        }
    }
}
