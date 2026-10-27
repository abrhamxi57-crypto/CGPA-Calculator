# 🎓 CGPA Calculator

**SENG3063 — Object-Oriented Programming and GUI Review**  
**Lab Exercise 01 — CGPA Calculator Desktop Application**

A modern JavaFX desktop application for calculating Cumulative Grade Point Average (CGPA) with academic status determination, built to demonstrate OOP principles and GUI design.

---

## 📋 Lab Requirements Implemented

| # | Requirement | Status |
|---|---|:---:|
| 1 | Course data capture (code, title, credits, score) | ✅ |
| 2 | Grading logic with configurable policy | ✅ |
| 3 | Weighted grade point calculation | ✅ |
| 4 | Total credit hours calculation | ✅ |
| 5 | Total weighted grade points calculation | ✅ |
| 6 | CGPA calculation with 2-decimal display | ✅ |
| 7 | Academic status determination (4 levels) | ✅ |
| 8 | JavaFX GUI with TableView | ✅ |
| 9 | Input validation | ✅ |
| 10 | OOP principles demonstration | ✅ |
| 11 | JUnit 5 automated tests | ✅ |
| 12 | Maven build system | ✅ |
| 13 | CSS-styled dark theme | ✅ |

---

## ✨ Features

- **Course Management**: Add, edit, and remove courses with full validation
- **Real-time Calculations**: GPA, total credits, and weighted GP update instantly
- **Academic Status**: Color-coded status display (Pass, Warning, Readmission, Dismissal)
- **Modern Dark Theme**: Professional UI with rounded cards, gradients, and hover effects
- **Configurable Grading Scale**: Centralized DBU grading policy, easy to update
- **Comprehensive Validation**: Prevents invalid data with clear error messages
- **Confirmation Dialogs**: Prevents accidental course deletion
- **Empty State**: Helpful guidance when no courses are entered
- **Keyboard Accessible**: Full keyboard navigation support

---

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java (OpenJDK) | 25 | Core language |
| JavaFX | 21.0.5 | GUI framework |
| Apache Maven | 3.9.8 | Build system & dependency management |
| JUnit 5 | 5.11.4 | Unit testing |
| CSS | — | JavaFX styling |

> **Note on Java version**: The assignment specifies JDK 27, which has not been released yet. JDK 25 (latest available) is fully compatible with all lab requirements.

---

## 📦 Prerequisites

### Ubuntu Linux

1. **Java JDK 25** (or later):
   ```bash
   sudo apt update
   sudo apt install openjdk-25-jdk
   java --version
   ```

2. **Apache Maven 3.9.x**:
   ```bash
   sudo apt install maven
   mvn --version
   ```

   Or download manually:
   ```bash
   curl -fsSL https://repo1.maven.org/maven2/org/apache/maven/apache-maven/3.9.8/apache-maven-3.9.8-bin.tar.gz -o /tmp/maven.tar.gz
   tar -xzf /tmp/maven.tar.gz -C ~/
   export PATH="$HOME/apache-maven-3.9.8/bin:$PATH"
   ```

3. **Verify installations**:
   ```bash
   java --version     # Should show OpenJDK 25.x
   mvn --version      # Should show Apache Maven 3.9.x
   ```

---

## 📁 Project Structure

```
cgpa-calculator/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
├── .gitignore                       # Git ignore rules
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── module-info.java     # Java module descriptor
    │   │   └── com/seng3063/cgpa/
    │   │       ├── MainApp.java                    # Application entry point
    │   │       ├── model/
    │   │       │   └── Course.java                 # Course data model
    │   │       ├── service/
    │   │       │   ├── GradingPolicy.java           # Grading interface (ABSTRACTION)
    │   │       │   ├── DbuGradingPolicy.java        # DBU implementation (INHERITANCE)
    │   │       │   ├── GpaCalculator.java            # GPA calculation service
    │   │       │   └── AcademicStatusService.java    # Academic standing
    │   │       ├── controller/
    │   │       │   └── MainController.java          # JavaFX controller
    │   │       ├── validation/
    │   │       │   └── CourseValidator.java          # Input validation
    │   │       └── util/
    │   │           └── NumberFormatter.java          # Number formatting
    │   └── resources/com/seng3063/cgpa/
    │       ├── main-view.fxml       # FXML layout
    │       └── styles.css           # Dark theme CSS
    └── test/java/com/seng3063/cgpa/
        ├── GpaCalculatorTest.java           # GPA calculation tests
        ├── AcademicStatusServiceTest.java   # Academic status tests
        ├── CourseValidatorTest.java          # Validation tests
        └── GradingPolicyTest.java           # Grading policy tests
```

---

## 🚀 How to Run

### Run the Application
```bash
export PATH="$HOME/apache-maven-3.9.8/bin:$PATH"  # if Maven is manually installed
cd cgpa-calculator
mvn clean javafx:run
```
This compiles the project and launches the JavaFX desktop application.

### Run Unit Tests
```bash
mvn clean test
```
This runs all JUnit 5 tests without launching the GUI.

### Build Only (no run)
```bash
mvn clean compile
```

---

## 📊 Example Calculations

### Example 1: Three-Course Semester

| Course | Credits | Score | Grade | Grade Point | Weighted GP |
|--------|--------:|------:|-------|------------:|------------:|
| CS101  | 5       | 92    | A     | 4.00        | 20.00       |
| CS102  | 4       | 78    | B     | 3.00        | 12.00       |
| CS103  | 5       | 47    | D     | 1.00        | 5.00        |

**Calculations:**
- Total Credits = 5 + 4 + 5 = **14**
- Total Weighted GP = 20 + 12 + 5 = **37.00**
- CGPA = 37 ÷ 14 = **2.64**
- Academic Status: **Pass** (GPA ≥ 2.00)

### Understanding the Terminology

| Term | Definition | Example |
|------|-----------|---------|
| **Grade Point (GP)** | Numeric value assigned to a letter grade | A = 4.0, B = 3.0 |
| **Weighted Grade Point (WGP)** | Credit hours × Grade Point | 5 credits × 4.0 = 20.0 |
| **GPA / CGPA** | Total WGP ÷ Total Credits | 37 ÷ 14 = 2.64 |
| **Academic Status** | Standing based on GPA thresholds | GPA ≥ 2.0 = Pass |

---

## 🎓 OOP Principles Demonstrated

### 1. Encapsulation
- **`Course.java`**: All fields are `private` with controlled access through getters/setters
- **`CourseValidator.java`**: Validation logic is centralized, preventing invalid data from reaching the model
- JavaFX properties enable automatic UI data binding

### 2. Abstraction
- **`GradingPolicy.java`** (interface): Defines the contract for grade conversion without specifying implementation
- **`GpaCalculator.java`**: Encapsulates the GPA formula in one place
- **`AcademicStatusService.java`**: Isolates status determination logic
- Business logic is completely independent of the GUI

### 3. Inheritance
- **`DbuGradingPolicy.java`** implements `GradingPolicy`: Provides the DBU-specific grading scale
- Different grading policies can extend or implement the same interface

### 4. Polymorphism
- **`GradingPolicy`** interface allows different grading scales to be plugged in
- `GpaCalculator` works with any `GradingPolicy` implementation
- Swapping grading policies requires no changes to the calculator or GUI

---

## 📐 GPA and Academic Status Formulas

### GPA Calculation
```
Weighted Grade Point = Credit Hours × Grade Point
Total Credits        = Σ (Credit Hours for all courses)
Total Weighted GP    = Σ (Weighted Grade Point for all courses)
CGPA                 = Total Weighted GP ÷ Total Credits
```

### Academic Status Thresholds
| GPA Range | Status |
|-----------|--------|
| GPA ≥ 2.00 | ✅ Pass |
| 1.75 ≤ GPA < 2.00 | ⚠️ Warning |
| 1.50 ≤ GPA < 1.75 | 🔶 Readmission |
| GPA < 1.50 | 🔴 Academic Dismissal |

---

## 📏 Grading Scale Configuration

> ⚠️ **PROVISIONAL**: The grade boundaries below are based on a commonly used Ethiopian university grading scale. **Verify against the official DBU grading policy** before any academic use.

| Score Range | Letter Grade | Grade Point |
|:-----------:|:------------:|:-----------:|
| 90 – 100 | A | 4.00 |
| 85 – 89 | A- | 3.75 |
| 80 – 84 | B+ | 3.50 |
| 75 – 79 | B | 3.00 |
| 70 – 74 | B- | 2.75 |
| 65 – 69 | C+ | 2.50 |
| 60 – 64 | C | 2.00 |
| 50 – 59 | C- | 1.75 |
| 45 – 49 | D | 1.00 |
| 40 – 44 | D- | 0.75 |
| 0 – 39 | F | 0.00 |

**To update the grading scale**: Modify the `GRADE_TABLE` array in [`DbuGradingPolicy.java`](src/main/java/com/seng3063/cgpa/service/DbuGradingPolicy.java). No other files need to change.

### Values Requiring Verification
- The assignment example states C = 1.0, but most Ethiopian university scales map C to 2.0 and D to 1.0. This implementation uses C = 2.0 following common practice.
- The exact score boundaries for each grade must be confirmed against the official DBU grading table.

---

## 🔧 Troubleshooting

### JavaFX not found
```
Error: JavaFX runtime components are missing
```
**Fix**: Ensure the Maven JavaFX plugin is configured in `pom.xml` and run with:
```bash
mvn clean javafx:run
```

### Module system errors
```
Error: module not found
```
**Fix**: Ensure `module-info.java` is present in `src/main/java/`.

### Maven not found
```
bash: mvn: command not found
```
**Fix**: Install Maven or add it to your PATH:
```bash
export PATH="$HOME/apache-maven-3.9.8/bin:$PATH"
```

### Tests fail with NoClassDefFoundError
```bash
mvn clean test
```
Maven will download all required dependencies automatically on first run.

### Display issues on Ubuntu (Wayland)
If the GUI appears blank, try:
```bash
export GDK_BACKEND=x11
mvn clean javafx:run
```

---

## 📝 License

This project is created for academic purposes as part of the SENG3063 course at Debre Berhan University.
