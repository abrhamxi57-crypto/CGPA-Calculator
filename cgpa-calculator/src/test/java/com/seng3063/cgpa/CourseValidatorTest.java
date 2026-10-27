package com.seng3063.cgpa;

import com.seng3063.cgpa.model.Course;
import com.seng3063.cgpa.validation.CourseValidator;
import com.seng3063.cgpa.validation.CourseValidator.ValidationResult;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CourseValidator.
 * Tests all validation rules for course input data.
 */
class CourseValidatorTest {

    private CourseValidator validator;
    private List<Course> existingCourses;

    @BeforeEach
    void setUp() {
        validator = new CourseValidator();
        existingCourses = new ArrayList<>();
    }

    // ==================== Course Code Validation ====================

    @ParameterizedTest
    @DisplayName("Missing course code is rejected")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void missingCourseCodeRejected(String code) {
        ValidationResult result = validator.validateNewCourse(
                code, "Title", "3", "80", existingCourses);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.contains("Course code")));
    }

    @Test
    @DisplayName("Duplicate course code is rejected (case-insensitive)")
    void duplicateCourseCodeRejected() {
        existingCourses.add(new Course("CS101", "Existing", 3, 80));

        ValidationResult result = validator.validateNewCourse(
                "cs101", "New Title", "4", "90", existingCourses);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.contains("already exists")));
    }

    @Test
    @DisplayName("Duplicate code allowed when updating the same course")
    void duplicateCodeAllowedForSameCourse() {
        Course existing = new Course("CS101", "Existing", 3, 80);
        existingCourses.add(existing);

        // Updating CS101 with the same code should be valid
        ValidationResult result = validator.validateUpdateCourse(
                "CS101", "Updated Title", "4", "90",
                existingCourses, existing);
        assertTrue(result.isValid());
    }

    @Test
    @DisplayName("Valid course code is accepted")
    void validCourseCodeAccepted() {
        List<String> errors = validator.validateCourseCode(
                "CS101", existingCourses, null);
        assertTrue(errors.isEmpty());
    }

    // ==================== Course Title Validation ====================

    @ParameterizedTest
    @DisplayName("Missing course title is rejected")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t"})
    void missingCourseTitleRejected(String title) {
        ValidationResult result = validator.validateNewCourse(
                "CS101", title, "3", "80", existingCourses);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.contains("Course title")));
    }

    @Test
    @DisplayName("Valid course title is accepted")
    void validCourseTitleAccepted() {
        List<String> errors = validator.validateCourseTitle("Intro to Programming");
        assertTrue(errors.isEmpty());
    }

    // ==================== Credit Hours Validation ====================

    @ParameterizedTest
    @DisplayName("Invalid credit hours are rejected")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "abc", "-1", "0", "-5", "3.5", "NaN", "Infinity"})
    void invalidCreditHoursRejected(String credits) {
        ValidationResult result = validator.validateNewCourse(
                "CS101", "Title", credits, "80", existingCourses);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.toLowerCase().contains("credit")));
    }

    @Test
    @DisplayName("Zero credits are rejected")
    void zeroCreditHoursRejected() {
        List<String> errors = validator.validateCreditHours("0");
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream()
                .anyMatch(e -> e.contains("greater than zero")));
    }

    @Test
    @DisplayName("Negative credits are rejected")
    void negativeCreditHoursRejected() {
        List<String> errors = validator.validateCreditHours("-3");
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @DisplayName("Valid credit hours are accepted")
    @ValueSource(strings = {"1", "3", "5", "12"})
    void validCreditHoursAccepted(String credits) {
        List<String> errors = validator.validateCreditHours(credits);
        assertTrue(errors.isEmpty());
    }

    // ==================== Score Validation ====================

    @ParameterizedTest
    @DisplayName("Invalid scores are rejected")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "abc", "-1", "-0.01", "100.01", "101", "200", "NaN", "Infinity"})
    void invalidScoresRejected(String score) {
        ValidationResult result = validator.validateNewCourse(
                "CS101", "Title", "3", score, existingCourses);
        assertFalse(result.isValid());
        assertTrue(result.getErrors().stream()
                .anyMatch(e -> e.toLowerCase().contains("score")));
    }

    @Test
    @DisplayName("Score below 0 is rejected")
    void scoreBelowZeroRejected() {
        List<String> errors = validator.validateScore("-5");
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream()
                .anyMatch(e -> e.contains("negative")));
    }

    @Test
    @DisplayName("Score above 100 is rejected")
    void scoreAboveHundredRejected() {
        List<String> errors = validator.validateScore("105");
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream()
                .anyMatch(e -> e.contains("exceed 100")));
    }

    @ParameterizedTest
    @DisplayName("Valid scores (including boundary values) are accepted")
    @ValueSource(strings = {"0", "0.0", "50", "99.5", "100", "100.0"})
    void validScoresAccepted(String score) {
        List<String> errors = validator.validateScore(score);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Valid minimum score (0) is accepted")
    void validMinScore() {
        List<String> errors = validator.validateScore("0");
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Valid maximum score (100) is accepted")
    void validMaxScore() {
        List<String> errors = validator.validateScore("100");
        assertTrue(errors.isEmpty());
    }

    // ==================== Full Validation Happy Path ====================

    @Test
    @DisplayName("Fully valid course passes all validation")
    void fullyValidCourse() {
        ValidationResult result = validator.validateNewCourse(
                "CS101", "Intro to Programming", "5", "85.5",
                existingCourses);
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    // ==================== Multiple Errors ====================

    @Test
    @DisplayName("Multiple validation errors are reported together")
    void multipleErrorsReported() {
        ValidationResult result = validator.validateNewCourse(
                "", "", "abc", "200", existingCourses);
        assertFalse(result.isValid());
        // Should have at least 4 errors (code, title, credits, score)
        assertTrue(result.getErrors().size() >= 4);
    }

    // ==================== Whitespace Handling ====================

    @Test
    @DisplayName("Leading/trailing whitespace is handled")
    void whitespaceHandled() {
        ValidationResult result = validator.validateNewCourse(
                "  CS101  ", "  Intro  ", "  3  ", "  80  ", existingCourses);
        assertTrue(result.isValid());
    }

    // ==================== Parse Methods ====================

    @Test
    @DisplayName("parseCreditHours returns correct integer")
    void parseCreditHoursCorrect() {
        assertEquals(5, validator.parseCreditHours("5"));
        assertEquals(3, validator.parseCreditHours("  3  "));
    }

    @Test
    @DisplayName("parseScore returns correct double")
    void parseScoreCorrect() {
        assertEquals(85.5, validator.parseScore("85.5"), 0.001);
        assertEquals(100.0, validator.parseScore(" 100 "), 0.001);
    }

    // ==================== Non-numeric Input ====================

    @Test
    @DisplayName("Non-numeric credit hours show helpful message")
    void nonNumericCreditsMessage() {
        List<String> errors = validator.validateCreditHours("five");
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream()
                .anyMatch(e -> e.contains("valid number")));
    }

    @Test
    @DisplayName("Non-numeric score shows helpful message")
    void nonNumericScoreMessage() {
        List<String> errors = validator.validateScore("eighty");
        assertFalse(errors.isEmpty());
        assertTrue(errors.stream()
                .anyMatch(e -> e.contains("valid number")));
    }
}
