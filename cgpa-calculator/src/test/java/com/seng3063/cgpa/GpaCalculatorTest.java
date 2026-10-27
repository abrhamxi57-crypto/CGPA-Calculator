package com.seng3063.cgpa;

import com.seng3063.cgpa.model.Course;
import com.seng3063.cgpa.service.DbuGradingPolicy;
import com.seng3063.cgpa.service.GpaCalculator;
import com.seng3063.cgpa.service.GradingPolicy;
import com.seng3063.cgpa.util.NumberFormatter;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GpaCalculator.
 * Tests GPA/CGPA calculation logic independently of the GUI.
 */
class GpaCalculatorTest {

    private GpaCalculator calculator;
    private GradingPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new DbuGradingPolicy();
        calculator = new GpaCalculator(policy);
    }

    // ==================== Constructor Tests ====================

    @Test
    @DisplayName("Constructor rejects null grading policy")
    void constructorRejectsNullPolicy() {
        assertThrows(IllegalArgumentException.class,
                () -> new GpaCalculator(null));
    }

    // ==================== Grade Course Tests ====================

    @Test
    @DisplayName("gradeCourse sets letter grade, grade point, and weighted GP")
    void gradeCourseSetAllFields() {
        Course course = new Course("CS101", "Intro to Programming", 5, 92.0);
        calculator.gradeCourse(course);

        assertEquals("A", course.getLetterGrade());
        assertEquals(4.0, course.getGradePoint(), 0.001);
        assertEquals(20.0, course.getWeightedGradePoint(), 0.001);
    }

    @Test
    @DisplayName("gradeCourse rejects null course")
    void gradeCourseRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.gradeCourse(null));
    }

    // ==================== Single Course GPA Tests ====================

    @Test
    @DisplayName("GPA for one course with A grade equals 4.0")
    void gpaSingleCourseA() {
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 5, 95) // A → 4.0
        );
        assertEquals(4.0, calculator.calculateGpa(courses), 0.001);
    }

    @Test
    @DisplayName("GPA for one course with F grade equals 0.0")
    void gpaSingleCourseF() {
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 3, 30) // F → 0.0
        );
        assertEquals(0.0, calculator.calculateGpa(courses), 0.001);
    }

    // ==================== Multiple Course Tests ====================

    @Test
    @DisplayName("GPA for assignment example: CS101(5,A) + CS102(4,B) + CS103(5,C-)")
    void gpaAssignmentExample() {
        /*
         * The assignment gives this example:
         * CS101: 5 credits × 4.0 GP = 20 WGP
         * CS102: 4 credits × 3.0 GP = 12 WGP
         * CS103: 5 credits × 1.0 GP = 5 WGP (D grade in DBU scale)
         * Total credits = 14, Total WGP = 37
         * CGPA = 37 / 14 = 2.642857... ≈ 2.64
         *
         * Using the provisional DBU scale:
         * A = 4.0 (score ≥ 90), B = 3.0 (score ≥ 75), D = 1.0 (score ≥ 45)
         */
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 5, 92), // A → 4.0 → WGP = 20
                new CourseData("CS102", 4, 78), // B → 3.0 → WGP = 12
                new CourseData("CS103", 5, 47) // D → 1.0 → WGP = 5
        );

        assertEquals(14, calculator.calculateTotalCredits(courses));
        assertEquals(37.0, calculator.calculateTotalWeightedGradePoints(courses), 0.001);

        double gpa = calculator.calculateGpa(courses);
        assertEquals(2.64, NumberFormatter.round(gpa, 2), 0.001);
    }

    @Test
    @DisplayName("GPA with different credit hour values")
    void gpaDifferentCreditHours() {
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 1, 95), // A → 4.0 → WGP = 4
                new CourseData("CS102", 3, 78), // B → 3.0 → WGP = 9
                new CourseData("CS103", 5, 65) // C+ → 2.5 → WGP = 12.5
        );

        assertEquals(9, calculator.calculateTotalCredits(courses));
        assertEquals(25.5, calculator.calculateTotalWeightedGradePoints(courses), 0.001);

        // 25.5 / 9 = 2.8333...
        double gpa = calculator.calculateGpa(courses);
        assertEquals(2.83, NumberFormatter.round(gpa, 2), 0.001);
    }

    @Test
    @DisplayName("GPA with all courses having the same grade")
    void gpaAllSameGrade() {
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 3, 92), // A → 4.0
                new CourseData("CS102", 4, 95), // A → 4.0
                new CourseData("CS103", 5, 100) // A → 4.0
        );

        // All A grades → GPA should be exactly 4.0
        assertEquals(4.0, calculator.calculateGpa(courses), 0.001);
    }

    // ==================== Total Credits Tests ====================

    @Test
    @DisplayName("Total credits calculated correctly")
    void totalCreditsCorrect() {
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 5, 90),
                new CourseData("CS102", 4, 80),
                new CourseData("CS103", 3, 70));
        assertEquals(12, calculator.calculateTotalCredits(courses));
    }

    @Test
    @DisplayName("Total credits for single course")
    void totalCreditsSingleCourse() {
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 5, 90));
        assertEquals(5, calculator.calculateTotalCredits(courses));
    }

    // ==================== Total Weighted Grade Points Tests ====================

    @Test
    @DisplayName("Total weighted GP calculated correctly")
    void totalWeightedGpCorrect() {
        List<Course> courses = createGradedCourses(
                new CourseData("CS101", 5, 92), // 5 × 4.0 = 20
                new CourseData("CS102", 4, 78) // 4 × 3.0 = 12
        );
        assertEquals(32.0, calculator.calculateTotalWeightedGradePoints(courses), 0.001);
    }

    // ==================== Empty / Null Collections ====================

    @Test
    @DisplayName("GPA returns 0 for null course list")
    void gpaReturnsZeroForNull() {
        assertEquals(0.0, calculator.calculateGpa(null), 0.001);
    }

    @Test
    @DisplayName("GPA returns 0 for empty course list")
    void gpaReturnsZeroForEmpty() {
        assertEquals(0.0, calculator.calculateGpa(new ArrayList<>()), 0.001);
    }

    @Test
    @DisplayName("Total credits returns 0 for null list")
    void totalCreditsZeroForNull() {
        assertEquals(0, calculator.calculateTotalCredits(null));
    }

    @Test
    @DisplayName("Total credits returns 0 for empty list")
    void totalCreditsZeroForEmpty() {
        assertEquals(0, calculator.calculateTotalCredits(Collections.emptyList()));
    }

    @Test
    @DisplayName("Total weighted GP returns 0 for null list")
    void totalWeightedGpZeroForNull() {
        assertEquals(0.0, calculator.calculateTotalWeightedGradePoints(null), 0.001);
    }

    @Test
    @DisplayName("Total weighted GP returns 0 for empty list")
    void totalWeightedGpZeroForEmpty() {
        assertEquals(0.0,
                calculator.calculateTotalWeightedGradePoints(Collections.emptyList()), 0.001);
    }

    // ==================== Display Rounding Tests ====================

    @ParameterizedTest
    @DisplayName("GPA rounds correctly for display")
    @CsvSource({
            "2.6428571, 2.64",
            "3.9999999, 4.00",
            "0.0,       0.00",
            "1.555,     1.56",
            "1.545,     1.55",
            "2.005,     2.01"
    })
    void gpaRoundsCorrectlyForDisplay(double rawGpa, String expected) {
        assertEquals(expected, NumberFormatter.formatGpa(rawGpa));
    }

    // ==================== Helper Methods ====================

    /**
     * Helper record for creating test course data.
     */
    private record CourseData(String code, int credits, double score) {
    }

    /**
     * Creates a list of courses, grades them all, and returns them.
     */
    private List<Course> createGradedCourses(CourseData... data) {
        List<Course> courses = new ArrayList<>();
        for (CourseData cd : data) {
            Course course = new Course(cd.code, cd.code + " Title", cd.credits, cd.score);
            calculator.gradeCourse(course);
            courses.add(course);
        }
        return courses;
    }
}
