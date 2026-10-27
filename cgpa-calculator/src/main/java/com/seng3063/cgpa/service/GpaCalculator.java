package com.seng3063.cgpa.service;

import com.seng3063.cgpa.model.Course;

import java.util.List;

/**
 * Calculates GPA/CGPA from a collection of courses.
 * 
 * Demonstrates ABSTRACTION:
 * - Encapsulates all GPA calculation logic in one place.
 * - The single formula (CGPA = Total Weighted GP / Total Credits) is
 *   implemented once and not duplicated elsewhere.
 * - Business logic is completely independent of the GUI.
 * 
 * This service uses a GradingPolicy (POLYMORPHISM) to determine
 * grade points, allowing different grading scales to be plugged in.
 */
public class GpaCalculator {

    private final GradingPolicy gradingPolicy;

    /**
     * Creates a GPA calculator with the specified grading policy.
     *
     * @param gradingPolicy the policy used to convert scores to grade points
     */
    public GpaCalculator(GradingPolicy gradingPolicy) {
        if (gradingPolicy == null) {
            throw new IllegalArgumentException("Grading policy must not be null");
        }
        this.gradingPolicy = gradingPolicy;
    }

    /**
     * Applies the grading policy to a single course:
     * - Sets the letter grade from the score.
     * - Sets the grade point from the score.
     * - Recalculates the weighted grade point (credits × grade point).
     *
     * @param course the course to grade
     */
    public void gradeCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course must not be null");
        }
        double score = course.getScore();
        course.setLetterGrade(gradingPolicy.getLetterGrade(score));
        course.setGradePoint(gradingPolicy.getGradePoint(score));
        course.recalculateWeightedGradePoint();
    }

    /**
     * Calculates the total credit hours across all courses.
     *
     * Formula: Total Credits = Σ (creditHours for each course)
     *
     * @param courses the list of courses
     * @return the sum of all credit hours, or 0 if the list is null/empty
     */
    public int calculateTotalCredits(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Course course : courses) {
            total += course.getCreditHours();
        }
        return total;
    }

    /**
     * Calculates the total weighted grade points across all courses.
     *
     * Formula: Total WGP = Σ (creditHours × gradePoint for each course)
     *
     * @param courses the list of courses
     * @return the sum of all weighted grade points, or 0.0 if the list is null/empty
     */
    public double calculateTotalWeightedGradePoints(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Course course : courses) {
            total += course.getWeightedGradePoint();
        }
        return total;
    }

    /**
     * Calculates the GPA/CGPA for the given courses.
     *
     * Formula: CGPA = Total Weighted Grade Points ÷ Total Credit Hours
     *
     * The calculation uses full precision internally.
     * Display rounding is handled by the presentation layer.
     *
     * @param courses the list of courses
     * @return the calculated GPA, or 0.0 if no valid courses are provided
     *         (never divides by zero)
     */
    public double calculateGpa(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return 0.0;
        }

        int totalCredits = calculateTotalCredits(courses);

        // Guard against division by zero (should not happen with validated data,
        // but defensive programming is important)
        if (totalCredits == 0) {
            return 0.0;
        }

        double totalWeightedGradePoints = calculateTotalWeightedGradePoints(courses);

        return totalWeightedGradePoints / totalCredits;
    }

    /**
     * Returns the grading policy used by this calculator.
     *
     * @return the current grading policy
     */
    public GradingPolicy getGradingPolicy() {
        return gradingPolicy;
    }
}
