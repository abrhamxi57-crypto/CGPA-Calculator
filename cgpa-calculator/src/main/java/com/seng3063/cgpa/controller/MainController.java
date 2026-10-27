package com.seng3063.cgpa.controller;

import com.seng3063.cgpa.model.Course;
import com.seng3063.cgpa.service.AcademicStatusService;
import com.seng3063.cgpa.service.DbuGradingPolicy;
import com.seng3063.cgpa.service.GpaCalculator;
import com.seng3063.cgpa.service.GradingPolicy;
import com.seng3063.cgpa.util.NumberFormatter;
import com.seng3063.cgpa.validation.CourseValidator;
import com.seng3063.cgpa.validation.CourseValidator.ValidationResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 * Controller for the main CGPA Calculator view.
 * 
 * Connects the JavaFX GUI to the business logic services.
 * Handles all user interactions: adding, editing, removing courses,
 * and updating the dashboard statistics.
 * 
 * Demonstrates SEPARATION OF CONCERNS:
 * - The controller delegates grading to GpaCalculator.
 * - Validation is handled by CourseValidator.
 * - Academic status is determined by AcademicStatusService.
 * - The controller only coordinates between GUI and services.
 */
public class MainController {

    // ========== Dashboard Labels ==========
    @FXML private Label totalCoursesLabel;
    @FXML private Label totalCreditsLabel;
    @FXML private Label totalWeightedGpLabel;
    @FXML private Label cgpaLabel;
    @FXML private Label academicStatusLabel;
    @FXML private VBox academicStatusCard;

    // ========== Input Fields ==========
    @FXML private TextField courseCodeField;
    @FXML private TextField courseTitleField;
    @FXML private TextField creditHoursField;
    @FXML private TextField scoreField;
    @FXML private Label validationLabel;

    // ========== Buttons ==========
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button removeButton;
    @FXML private Button clearFieldsButton;
    @FXML private Button resetAllButton;

    // ========== Table ==========
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, String> titleColumn;
    @FXML private TableColumn<Course, Integer> creditsColumn;
    @FXML private TableColumn<Course, Double> scoreColumn;
    @FXML private TableColumn<Course, String> gradeColumn;
    @FXML private TableColumn<Course, Double> gradePointColumn;
    @FXML private TableColumn<Course, Double> weightedGpColumn;

    // ========== Empty State ==========
    @FXML private VBox emptyStateBox;

    // ========== Services ==========
    private final GradingPolicy gradingPolicy = new DbuGradingPolicy();
    private final GpaCalculator gpaCalculator = new GpaCalculator(gradingPolicy);
    private final AcademicStatusService statusService = new AcademicStatusService();
    private final CourseValidator validator = new CourseValidator();

    // ========== Data ==========
    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private Course selectedCourse = null;

    /**
     * Initializes the controller after FXML loading.
     * Sets up table columns, bindings, and event handlers.
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableSelection();
        courseTable.setItems(courses);

        // Initial state: disable update/remove until a course is selected
        updateButton.setDisable(true);
        removeButton.setDisable(true);

        updateDashboard();
        updateEmptyState();
    }

    /**
     * Configures TableView columns to bind to Course properties.
     */
    private void setupTableColumns() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<>("creditHours"));

        // Score column with 1 decimal place formatting
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : NumberFormatter.format(item, 1));
            }
        });

        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("letterGrade"));

        // Grade point column with 2 decimal places
        gradePointColumn.setCellValueFactory(new PropertyValueFactory<>("gradePoint"));
        gradePointColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : NumberFormatter.formatGradePoint(item));
            }
        });

        // Weighted GP column with 2 decimal places
        weightedGpColumn.setCellValueFactory(new PropertyValueFactory<>("weightedGradePoint"));
        weightedGpColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : NumberFormatter.format(item, 2));
            }
        });
    }

    /**
     * Listens for table row selection to enable edit/delete and
     * populate input fields with the selected course's data.
     */
    private void setupTableSelection() {
        courseTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedCourse = newSelection;
                    boolean hasSelection = newSelection != null;
                    updateButton.setDisable(!hasSelection);
                    removeButton.setDisable(!hasSelection);

                    if (hasSelection) {
                        populateFields(newSelection);
                    }
                }
        );
    }

    /**
     * Fills the input fields with data from the selected course.
     */
    private void populateFields(Course course) {
        courseCodeField.setText(course.getCourseCode());
        courseTitleField.setText(course.getCourseTitle());
        creditHoursField.setText(String.valueOf(course.getCreditHours()));
        scoreField.setText(NumberFormatter.format(course.getScore(), 1));
        clearValidation();
    }

    // ==================== Button Handlers ====================

    /**
     * Handles the "Add Course" button click.
     * Validates input, creates a new Course, grades it, and adds to the list.
     */
    @FXML
    private void handleAddCourse() {
        String code = courseCodeField.getText();
        String title = courseTitleField.getText();
        String credits = creditHoursField.getText();
        String score = scoreField.getText();

        ValidationResult result = validator.validateNewCourse(
                code, title, credits, score, courses);

        if (!result.isValid()) {
            showValidation(result.getErrorsAsString());
            return;
        }

        // Create the course with validated/parsed values
        Course course = new Course(
                code.trim(),
                title.trim(),
                validator.parseCreditHours(credits),
                validator.parseScore(score)
        );

        // Apply grading policy
        gpaCalculator.gradeCourse(course);

        // Add to observable list (TableView updates automatically)
        courses.add(course);

        clearFields();
        updateDashboard();
        updateEmptyState();
        showSuccess("Course '" + course.getCourseCode() + "' added successfully.");
    }

    /**
     * Handles the "Update Selected Course" button click.
     * Validates input and updates the selected course's data.
     */
    @FXML
    private void handleUpdateCourse() {
        if (selectedCourse == null) {
            showValidation("Please select a course to update.");
            return;
        }

        String code = courseCodeField.getText();
        String title = courseTitleField.getText();
        String credits = creditHoursField.getText();
        String score = scoreField.getText();

        ValidationResult result = validator.validateUpdateCourse(
                code, title, credits, score, courses, selectedCourse);

        if (!result.isValid()) {
            showValidation(result.getErrorsAsString());
            return;
        }

        // Update the selected course
        selectedCourse.setCourseCode(code.trim());
        selectedCourse.setCourseTitle(title.trim());
        selectedCourse.setCreditHours(validator.parseCreditHours(credits));
        selectedCourse.setScore(validator.parseScore(score));

        // Re-apply grading policy
        gpaCalculator.gradeCourse(selectedCourse);

        // Force table refresh
        courseTable.refresh();

        updateDashboard();
        showSuccess("Course '" + selectedCourse.getCourseCode() + "' updated successfully.");
    }

    /**
     * Handles the "Remove Selected Course" button click.
     * Asks for confirmation before removing.
     */
    @FXML
    private void handleRemoveCourse() {
        if (selectedCourse == null) {
            showValidation("Please select a course to remove.");
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Course");
        confirm.setHeaderText("Remove " + selectedCourse.getCourseCode() + "?");
        confirm.setContentText("Are you sure you want to remove '"
                + selectedCourse.getCourseCode() + " - "
                + selectedCourse.getCourseTitle() + "'?");

        Optional<ButtonType> response = confirm.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            String removedCode = selectedCourse.getCourseCode();
            courses.remove(selectedCourse);
            selectedCourse = null;
            clearFields();
            updateDashboard();
            updateEmptyState();
            showSuccess("Course '" + removedCode + "' removed.");
        }
    }

    /**
     * Handles the "Clear Fields" button click.
     * Clears all input fields and deselects the table.
     */
    @FXML
    private void handleClearFields() {
        clearFields();
        courseTable.getSelectionModel().clearSelection();
        selectedCourse = null;
    }

    /**
     * Handles the "Reset All" button click.
     * Confirms, then removes all courses and resets the dashboard.
     */
    @FXML
    private void handleResetAll() {
        if (courses.isEmpty()) {
            showValidation("No courses to reset.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Reset All Courses");
        confirm.setHeaderText("Remove all courses?");
        confirm.setContentText("This will permanently remove all "
                + courses.size() + " course(s). This action cannot be undone.");

        Optional<ButtonType> response = confirm.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            courses.clear();
            selectedCourse = null;
            clearFields();
            updateDashboard();
            updateEmptyState();
            showSuccess("All courses have been removed.");
        }
    }

    // ==================== UI Update Methods ====================

    /**
     * Recalculates and updates all dashboard statistics.
     * Called after every course add/edit/remove operation.
     */
    private void updateDashboard() {
        int totalCourses = courses.size();
        int totalCredits = gpaCalculator.calculateTotalCredits(courses);
        double totalWeightedGp = gpaCalculator.calculateTotalWeightedGradePoints(courses);
        double gpa = gpaCalculator.calculateGpa(courses);
        boolean hasCourses = !courses.isEmpty();
        String status = statusService.determineStatus(gpa, hasCourses);

        totalCoursesLabel.setText(String.valueOf(totalCourses));
        totalCreditsLabel.setText(String.valueOf(totalCredits));
        totalWeightedGpLabel.setText(NumberFormatter.format(totalWeightedGp, 2));
        cgpaLabel.setText(hasCourses ? NumberFormatter.formatGpa(gpa) : "0.00");

        // Update academic status with appropriate styling
        academicStatusLabel.setText(status);
        String styleClass = statusService.getStatusStyleClass(status);
        academicStatusCard.getStyleClass().removeAll(
                "status-pass", "status-warning", "status-readmission",
                "status-dismissal", "status-none");
        academicStatusCard.getStyleClass().add("status-" + styleClass);
    }

    /**
     * Shows/hides the empty state message based on course count.
     */
    private void updateEmptyState() {
        boolean empty = courses.isEmpty();
        emptyStateBox.setVisible(empty);
        emptyStateBox.setManaged(empty);
        courseTable.setVisible(!empty);
        courseTable.setManaged(!empty);
    }

    /**
     * Clears all input fields and validation messages.
     */
    private void clearFields() {
        courseCodeField.clear();
        courseTitleField.clear();
        creditHoursField.clear();
        scoreField.clear();
        clearValidation();
    }

    /**
     * Displays a validation error message.
     */
    private void showValidation(String message) {
        validationLabel.setText(message);
        validationLabel.getStyleClass().removeAll("success-label", "error-label");
        validationLabel.getStyleClass().add("error-label");
        validationLabel.setVisible(true);
        validationLabel.setManaged(true);
    }

    /**
     * Displays a success message.
     */
    private void showSuccess(String message) {
        validationLabel.setText(message);
        validationLabel.getStyleClass().removeAll("success-label", "error-label");
        validationLabel.getStyleClass().add("success-label");
        validationLabel.setVisible(true);
        validationLabel.setManaged(true);
    }

    /**
     * Clears the validation/success message.
     */
    private void clearValidation() {
        validationLabel.setText("");
        validationLabel.setVisible(false);
        validationLabel.setManaged(false);
    }
}
