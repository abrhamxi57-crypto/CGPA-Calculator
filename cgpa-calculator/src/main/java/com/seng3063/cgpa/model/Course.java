package com.seng3063.cgpa.model;

import javafx.beans.property.*;

/**
 * Represents a single course with its academic data.
 * 
 * Demonstrates ENCAPSULATION:
 * - All fields are private.
 * - Data is accessed through controlled getters/setters.
 * - JavaFX properties enable automatic UI binding.
 * - Validation is enforced at the service layer before data reaches this model.
 * 
 * Each Course stores:
 * - Course code (e.g., "CS101")
 * - Course title (e.g., "Introduction to Programming")
 * - Credit hours (positive integer)
 * - Score (0–100)
 * - Letter grade (computed from score by the grading policy)
 * - Grade point (numeric value of the letter grade)
 * - Weighted grade point (credit hours × grade point)
 */
public class Course {

    private final StringProperty courseCode;
    private final StringProperty courseTitle;
    private final IntegerProperty creditHours;
    private final DoubleProperty score;
    private final StringProperty letterGrade;
    private final DoubleProperty gradePoint;
    private final DoubleProperty weightedGradePoint;

    /**
     * Constructs a Course with the student-entered data.
     * Grade fields are initialized to defaults and should be set
     * by the grading policy after construction.
     *
     * @param courseCode  the unique course identifier (e.g., "CS101")
     * @param courseTitle the descriptive course name
     * @param creditHours the number of credit hours (must be > 0)
     * @param score       the numerical score (0–100)
     */
    public Course(String courseCode, String courseTitle, int creditHours, double score) {
        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseTitle = new SimpleStringProperty(courseTitle);
        this.creditHours = new SimpleIntegerProperty(creditHours);
        this.score = new SimpleDoubleProperty(score);
        this.letterGrade = new SimpleStringProperty("");
        this.gradePoint = new SimpleDoubleProperty(0.0);
        this.weightedGradePoint = new SimpleDoubleProperty(0.0);
    }

    // ========== Course Code ==========

    public String getCourseCode() {
        return courseCode.get();
    }

    public void setCourseCode(String courseCode) {
        this.courseCode.set(courseCode);
    }

    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    // ========== Course Title ==========

    public String getCourseTitle() {
        return courseTitle.get();
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle.set(courseTitle);
    }

    public StringProperty courseTitleProperty() {
        return courseTitle;
    }

    // ========== Credit Hours ==========

    public int getCreditHours() {
        return creditHours.get();
    }

    public void setCreditHours(int creditHours) {
        this.creditHours.set(creditHours);
    }

    public IntegerProperty creditHoursProperty() {
        return creditHours;
    }

    // ========== Score ==========

    public double getScore() {
        return score.get();
    }

    public void setScore(double score) {
        this.score.set(score);
    }

    public DoubleProperty scoreProperty() {
        return score;
    }

    // ========== Letter Grade ==========

    public String getLetterGrade() {
        return letterGrade.get();
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade.set(letterGrade);
    }

    public StringProperty letterGradeProperty() {
        return letterGrade;
    }

    // ========== Grade Point ==========

    public double getGradePoint() {
        return gradePoint.get();
    }

    public void setGradePoint(double gradePoint) {
        this.gradePoint.set(gradePoint);
    }

    public DoubleProperty gradePointProperty() {
        return gradePoint;
    }

    // ========== Weighted Grade Point ==========

    /**
     * Weighted Grade Point = Credit Hours × Grade Point.
     * This value is recalculated whenever gradePoint or creditHours changes.
     */
    public double getWeightedGradePoint() {
        return weightedGradePoint.get();
    }

    public void setWeightedGradePoint(double weightedGradePoint) {
        this.weightedGradePoint.set(weightedGradePoint);
    }

    public DoubleProperty weightedGradePointProperty() {
        return weightedGradePoint;
    }

    /**
     * Recalculates the weighted grade point as:
     *   Weighted Grade Point = Credit Hours × Grade Point
     */
    public void recalculateWeightedGradePoint() {
        this.weightedGradePoint.set(this.creditHours.get() * this.gradePoint.get());
    }

    @Override
    public String toString() {
        return String.format("Course{code='%s', title='%s', credits=%d, score=%.1f, "
                        + "grade='%s', gp=%.2f, wgp=%.2f}",
                getCourseCode(), getCourseTitle(), getCreditHours(), getScore(),
                getLetterGrade(), getGradePoint(), getWeightedGradePoint());
    }
}
