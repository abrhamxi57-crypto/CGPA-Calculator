package com.seng3063.cgpa;

import com.seng3063.cgpa.service.AcademicStatusService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AcademicStatusService.
 * Tests all boundary values specified in the assignment:
 *   GPA >= 2.00  → Pass
 *   1.75 <= GPA < 2.00  → Warning
 *   1.50 <= GPA < 1.75  → Readmission
 *   GPA < 1.50  → Academic Dismissal
 */
class AcademicStatusServiceTest {

    private AcademicStatusService service;

    @BeforeEach
    void setUp() {
        service = new AcademicStatusService();
    }

    // ==================== Exact Boundary Tests ====================

    @ParameterizedTest
    @DisplayName("Academic status at exact boundary values")
    @CsvSource({
            "4.00, true, Pass",
            "2.00, true, Pass",
            "1.99, true, Warning",
            "1.75, true, Warning",
            "1.74, true, Readmission",
            "1.50, true, Readmission",
            "1.49, true, Academic Dismissal",
            "0.00, true, Academic Dismissal"
    })
    void academicStatusAtBoundaries(double gpa, boolean hasCourses, String expectedStatus) {
        assertEquals(expectedStatus, service.determineStatus(gpa, hasCourses));
    }

    // ==================== Interior Value Tests ====================

    @Test
    @DisplayName("Pass status for high GPA (3.50)")
    void passHighGpa() {
        assertEquals("Pass", service.determineStatus(3.50, true));
    }

    @Test
    @DisplayName("Warning status for mid-range GPA (1.85)")
    void warningMidRange() {
        assertEquals("Warning", service.determineStatus(1.85, true));
    }

    @Test
    @DisplayName("Readmission status for GPA 1.60")
    void readmissionMidRange() {
        assertEquals("Readmission", service.determineStatus(1.60, true));
    }

    @Test
    @DisplayName("Dismissal status for GPA 1.00")
    void dismissalLowGpa() {
        assertEquals("Academic Dismissal", service.determineStatus(1.00, true));
    }

    // ==================== No Courses Test ====================

    @Test
    @DisplayName("Returns 'No Courses Entered' when no courses exist")
    void noCourses() {
        assertEquals("No Courses Entered", service.determineStatus(0.0, false));
    }

    @Test
    @DisplayName("Returns 'No Courses Entered' even with non-zero GPA when flag is false")
    void noCoursesIgnoresGpa() {
        assertEquals("No Courses Entered", service.determineStatus(4.0, false));
    }

    // ==================== Style Class Tests ====================

    @Test
    @DisplayName("getStatusStyleClass returns correct CSS class suffixes")
    void statusStyleClasses() {
        assertEquals("pass", service.getStatusStyleClass("Pass"));
        assertEquals("warning", service.getStatusStyleClass("Warning"));
        assertEquals("readmission", service.getStatusStyleClass("Readmission"));
        assertEquals("dismissal", service.getStatusStyleClass("Academic Dismissal"));
        assertEquals("none", service.getStatusStyleClass("No Courses Entered"));
    }

    // ==================== Edge Cases ====================

    @Test
    @DisplayName("Perfect 4.0 GPA is Pass")
    void perfectGpa() {
        assertEquals("Pass", service.determineStatus(4.0, true));
    }

    @Test
    @DisplayName("Zero GPA with courses is Academic Dismissal")
    void zeroGpaWithCourses() {
        assertEquals("Academic Dismissal", service.determineStatus(0.0, true));
    }

    @Test
    @DisplayName("GPA just above Pass threshold (2.01)")
    void justAbovePass() {
        assertEquals("Pass", service.determineStatus(2.01, true));
    }

    @Test
    @DisplayName("GPA just below Pass threshold (1.999)")
    void justBelowPass() {
        assertEquals("Warning", service.determineStatus(1.999, true));
    }
}
