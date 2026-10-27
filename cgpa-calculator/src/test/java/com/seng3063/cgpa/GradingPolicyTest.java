package com.seng3063.cgpa;

import com.seng3063.cgpa.service.DbuGradingPolicy;
import com.seng3063.cgpa.service.GradingPolicy;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DbuGradingPolicy.
 * Tests every configured grade boundary, exact threshold scores,
 * and invalid score handling.
 * 
 * Provisional DBU Scale (verify against official policy):
 *   Score    Grade   GP
 *   90-100   A      4.00
 *   85-89    A-     3.75
 *   80-84    B+     3.50
 *   75-79    B      3.00
 *   70-74    B-     2.75
 *   65-69    C+     2.50
 *   60-64    C      2.00
 *   50-59    C-     1.75
 *   45-49    D      1.00
 *   40-44    D-     0.75
 *    0-39    F      0.00
 */
class GradingPolicyTest {

    private GradingPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new DbuGradingPolicy();
    }

    // ==================== Grade Boundary Tests ====================

    @ParameterizedTest
    @DisplayName("Correct letter grade for each score boundary")
    @CsvSource({
            // Exact lower boundaries
            "100, A",
            "90,  A",
            "89,  A-",
            "85,  A-",
            "84,  B+",
            "80,  B+",
            "79,  B",
            "75,  B",
            "74,  B-",
            "70,  B-",
            "69,  C+",
            "65,  C+",
            "64,  C",
            "60,  C",
            "59,  C-",
            "50,  C-",
            "49,  D",
            "45,  D",
            "44,  D-",
            "40,  D-",
            "39,  F",
            "0,   F",
    })
    void letterGradeAtBoundary(double score, String expectedGrade) {
        assertEquals(expectedGrade, policy.getLetterGrade(score));
    }

    @ParameterizedTest
    @DisplayName("Correct grade point for each score boundary")
    @CsvSource({
            "100, 4.00",
            "90,  4.00",
            "89,  3.75",
            "85,  3.75",
            "84,  3.50",
            "80,  3.50",
            "79,  3.00",
            "75,  3.00",
            "74,  2.75",
            "70,  2.75",
            "69,  2.50",
            "65,  2.50",
            "64,  2.00",
            "60,  2.00",
            "59,  1.75",
            "50,  1.75",
            "49,  1.00",
            "45,  1.00",
            "44,  0.75",
            "40,  0.75",
            "39,  0.00",
            "0,   0.00",
    })
    void gradePointAtBoundary(double score, double expectedGradePoint) {
        assertEquals(expectedGradePoint, policy.getGradePoint(score), 0.001);
    }

    // ==================== Interior Score Tests ====================

    @Test
    @DisplayName("Mid-range A grade (95)")
    void midRangeA() {
        assertEquals("A", policy.getLetterGrade(95));
        assertEquals(4.0, policy.getGradePoint(95), 0.001);
    }

    @Test
    @DisplayName("Mid-range C grade (62)")
    void midRangeC() {
        assertEquals("C", policy.getLetterGrade(62));
        assertEquals(2.0, policy.getGradePoint(62), 0.001);
    }

    // ==================== Edge Scores: 0 and 100 ====================

    @Test
    @DisplayName("Score of 0 returns F with 0.0 GP")
    void scoreZero() {
        assertEquals("F", policy.getLetterGrade(0));
        assertEquals(0.0, policy.getGradePoint(0), 0.001);
    }

    @Test
    @DisplayName("Score of 100 returns A with 4.0 GP")
    void scoreHundred() {
        assertEquals("A", policy.getLetterGrade(100));
        assertEquals(4.0, policy.getGradePoint(100), 0.001);
    }

    // ==================== Decimal Score Tests ====================

    @Test
    @DisplayName("Decimal score 89.5 falls in A- range")
    void decimalScoreInAminus() {
        assertEquals("A-", policy.getLetterGrade(89.5));
        assertEquals(3.75, policy.getGradePoint(89.5), 0.001);
    }

    @Test
    @DisplayName("Decimal score 59.9 falls in C- range")
    void decimalScoreInCminus() {
        assertEquals("C-", policy.getLetterGrade(59.9));
        assertEquals(1.75, policy.getGradePoint(59.9), 0.001);
    }

    // ==================== Invalid Score Tests ====================

    @ParameterizedTest
    @DisplayName("Invalid scores throw IllegalArgumentException")
    @ValueSource(doubles = {-1, -0.01, 100.01, 101, 200, -100})
    void invalidScoresThrow(double invalidScore) {
        assertThrows(IllegalArgumentException.class,
                () -> policy.getLetterGrade(invalidScore));
        assertThrows(IllegalArgumentException.class,
                () -> policy.getGradePoint(invalidScore));
    }

    @Test
    @DisplayName("NaN score throws IllegalArgumentException")
    void nanScoreThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> policy.getLetterGrade(Double.NaN));
        assertThrows(IllegalArgumentException.class,
                () -> policy.getGradePoint(Double.NaN));
    }

    @Test
    @DisplayName("Infinite score throws IllegalArgumentException")
    void infiniteScoreThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> policy.getLetterGrade(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class,
                () -> policy.getGradePoint(Double.NEGATIVE_INFINITY));
    }

    // ==================== Policy Name Test ====================

    @Test
    @DisplayName("Policy name is descriptive")
    void policyNameIsDescriptive() {
        assertNotNull(policy.getPolicyName());
        assertFalse(policy.getPolicyName().isBlank());
    }

    // ==================== Assignment Examples Verification ====================

    @Test
    @DisplayName("Assignment example: A = 4.0")
    void assignmentExampleA() {
        assertEquals(4.0, policy.getGradePoint(92), 0.001);
        assertEquals("A", policy.getLetterGrade(92));
    }

    @Test
    @DisplayName("Assignment example: A- = 3.75")
    void assignmentExampleAMinus() {
        assertEquals(3.75, policy.getGradePoint(87), 0.001);
        assertEquals("A-", policy.getLetterGrade(87));
    }

    @Test
    @DisplayName("Assignment example: F = 0.0")
    void assignmentExampleF() {
        assertEquals(0.0, policy.getGradePoint(20), 0.001);
        assertEquals("F", policy.getLetterGrade(20));
    }
}
