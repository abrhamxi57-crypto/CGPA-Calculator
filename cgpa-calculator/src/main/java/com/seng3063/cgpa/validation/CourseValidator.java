package com.seng3063.cgpa.validation;

import com.seng3063.cgpa.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates course input data before it enters the system.
 * 
 * Demonstrates ENCAPSULATION:
 * - Validation logic is centralized, preventing invalid data from
 *   reaching the model or calculation services.
 * - Each validation method returns a list of human-readable error messages.
 * 
 * All string inputs are trimmed. Numeric inputs are checked for NaN,
 * infinity, and range violations.
 */
public class CourseValidator {

    /**
     * Result of a validation operation.
     * Contains a boolean validity flag and a list of error messages.
     */
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;

        public ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors != null ? errors : new ArrayList<>();
        }

        public boolean isValid() {
            return valid;
        }

        public List<String> getErrors() {
            return errors;
        }

        /**
         * Returns all errors as a single string, separated by newlines.
         */
        public String getErrorsAsString() {
            return String.join("\n", errors);
        }
    }

    /**
     * Validates all fields for a new course entry.
     *
     * @param courseCode    the course code input
     * @param courseTitle   the course title input
     * @param creditHoursStr the credit hours input as a string
     * @param scoreStr      the score input as a string
     * @param existingCourses the current list of courses (for duplicate check)
     * @return a ValidationResult with any error messages
     */
    public ValidationResult validateNewCourse(String courseCode, String courseTitle,
                                               String creditHoursStr, String scoreStr,
                                               List<Course> existingCourses) {
        return validateCourse(courseCode, courseTitle, creditHoursStr, scoreStr,
                existingCourses, null);
    }

    /**
     * Validates all fields for updating an existing course.
     * Allows the same course code if it matches the course being edited.
     *
     * @param courseCode    the course code input
     * @param courseTitle   the course title input
     * @param creditHoursStr the credit hours input as a string
     * @param scoreStr      the score input as a string
     * @param existingCourses the current list of courses (for duplicate check)
     * @param editingCourse   the course being edited (its code is excluded from duplicate check)
     * @return a ValidationResult with any error messages
     */
    public ValidationResult validateUpdateCourse(String courseCode, String courseTitle,
                                                  String creditHoursStr, String scoreStr,
                                                  List<Course> existingCourses,
                                                  Course editingCourse) {
        return validateCourse(courseCode, courseTitle, creditHoursStr, scoreStr,
                existingCourses, editingCourse);
    }

    /**
     * Core validation logic for course data.
     *
     * @param courseCode       raw course code input
     * @param courseTitle      raw course title input
     * @param creditHoursStr  raw credit hours input
     * @param scoreStr        raw score input
     * @param existingCourses list of current courses
     * @param editingCourse   if non-null, the course being edited (excluded from dup check)
     * @return validation result
     */
    private ValidationResult validateCourse(String courseCode, String courseTitle,
                                             String creditHoursStr, String scoreStr,
                                             List<Course> existingCourses,
                                             Course editingCourse) {
        List<String> errors = new ArrayList<>();

        // Validate course code
        errors.addAll(validateCourseCode(courseCode, existingCourses, editingCourse));

        // Validate course title
        errors.addAll(validateCourseTitle(courseTitle));

        // Validate credit hours
        errors.addAll(validateCreditHours(creditHoursStr));

        // Validate score
        errors.addAll(validateScore(scoreStr));

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Validates the course code:
     * - Must not be null, empty, or whitespace-only.
     * - Must be unique (case-insensitive) among existing courses.
     */
    public List<String> validateCourseCode(String courseCode,
                                            List<Course> existingCourses,
                                            Course editingCourse) {
        List<String> errors = new ArrayList<>();
        if (courseCode == null || courseCode.trim().isEmpty()) {
            errors.add("Course code is required.");
            return errors;
        }

        String trimmedCode = courseCode.trim();

        // Check for duplicates (case-insensitive), excluding the course being edited
        if (existingCourses != null) {
            for (Course existing : existingCourses) {
                if (existing == editingCourse) {
                    continue; // Skip the course being edited
                }
                if (existing.getCourseCode().equalsIgnoreCase(trimmedCode)) {
                    errors.add("Course code '" + trimmedCode + "' already exists.");
                    break;
                }
            }
        }

        return errors;
    }

    /**
     * Validates the course title:
     * - Must not be null, empty, or whitespace-only.
     */
    public List<String> validateCourseTitle(String courseTitle) {
        List<String> errors = new ArrayList<>();
        if (courseTitle == null || courseTitle.trim().isEmpty()) {
            errors.add("Course title is required.");
        }
        return errors;
    }

    /**
     * Validates credit hours:
     * - Must be a valid number.
     * - Must be a positive integer (greater than zero).
     * - Rejects zero, negative, NaN, infinity, and non-numeric input.
     */
    public List<String> validateCreditHours(String creditHoursStr) {
        List<String> errors = new ArrayList<>();
        if (creditHoursStr == null || creditHoursStr.trim().isEmpty()) {
            errors.add("Credit hours is required.");
            return errors;
        }

        try {
            double parsed = Double.parseDouble(creditHoursStr.trim());

            if (Double.isNaN(parsed) || Double.isInfinite(parsed)) {
                errors.add("Credit hours must be a valid number.");
                return errors;
            }

            // Check for non-integer values
            if (parsed != Math.floor(parsed)) {
                errors.add("Credit hours must be a whole number.");
                return errors;
            }

            int creditHours = (int) parsed;
            if (creditHours <= 0) {
                errors.add("Credit hours must be greater than zero.");
            }
        } catch (NumberFormatException e) {
            errors.add("Credit hours must be a valid number.");
        }

        return errors;
    }

    /**
     * Validates the score:
     * - Must be a valid number.
     * - Must be between 0 and 100 inclusive.
     * - Rejects negative values, values above 100, NaN, infinity, and non-numeric input.
     */
    public List<String> validateScore(String scoreStr) {
        List<String> errors = new ArrayList<>();
        if (scoreStr == null || scoreStr.trim().isEmpty()) {
            errors.add("Score is required.");
            return errors;
        }

        try {
            double score = Double.parseDouble(scoreStr.trim());

            if (Double.isNaN(score) || Double.isInfinite(score)) {
                errors.add("Score must be a valid number.");
                return errors;
            }

            if (score < 0) {
                errors.add("Score must not be negative.");
            } else if (score > 100) {
                errors.add("Score must not exceed 100.");
            }
        } catch (NumberFormatException e) {
            errors.add("Score must be a valid number.");
        }

        return errors;
    }

    /**
     * Parses a validated credit hours string to an integer.
     * Call only after validation passes.
     */
    public int parseCreditHours(String creditHoursStr) {
        return (int) Double.parseDouble(creditHoursStr.trim());
    }

    /**
     * Parses a validated score string to a double.
     * Call only after validation passes.
     */
    public double parseScore(String scoreStr) {
        return Double.parseDouble(scoreStr.trim());
    }
}
