package users;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student Class Tests")
class StudentTest
{
    private Student student;

    // Test constants
    private static final int TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@test.com";
    private static final String TEST_GENDER = "Male";
    private static final String TEST_TYPE = "Full-Time";
    private static final String TEST_COURSE = "Computer Science";

    // Console output capture
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup()
    {
        System.setOut(new PrintStream(outputStream));
        System.out.println("\n=== Starting Test ===");

        student = new Student();
        student.setId(TEST_ID);
        student.setFirstName(TEST_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);
        student.setEmail(TEST_EMAIL);
        student.setGender(TEST_GENDER);
        student.setType(TEST_TYPE);
        student.setCourse(TEST_COURSE);

        System.out.println("✓ Test setup completed");
    }

    @AfterEach
    void cleanup()
    {
        System.setOut(originalOut);
    }

    private void printTestHeader(String testName)
    {
        System.setOut(originalOut);
        System.out.println("\nTesting " + testName + "...");
        System.setOut(new PrintStream(outputStream));
    }

    private void printTestSuccess(String testName)
    {
        System.setOut(originalOut);
        System.out.println("✓ " + testName + " test passed");
        System.setOut(new PrintStream(outputStream));
    }

    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests
    {

        @Test
        @DisplayName("Getter methods should return correct values")
        void testGetters()
        {
            printTestHeader("getter methods");

            assertEquals(TEST_ID, student.getId(), "ID should match");
            assertEquals(TEST_FIRST_NAME, student.getFirstName(), "First name should match");
            assertEquals(TEST_LAST_NAME, student.getLastName(), "Last name should match");
            assertEquals(TEST_EMAIL, student.getEmail(), "Email should match");
            assertEquals(TEST_GENDER, student.getGender(), "Gender should match");
            assertEquals(TEST_TYPE, student.getType(), "Type should match");
            assertEquals(TEST_COURSE, student.getCourse(), "Course should match");

            printTestSuccess("getter methods");
        }

        @Test
        @DisplayName("Setter methods should update values")
        void testSetters()
        {
            printTestHeader("setter methods");

            Student newStudent = new Student();

            newStudent.setId(TEST_ID);
            assertEquals(TEST_ID, newStudent.getId(), "ID should be updated");

            newStudent.setFirstName(TEST_FIRST_NAME);
            assertEquals(TEST_FIRST_NAME, newStudent.getFirstName(), "First name should be updated");

            newStudent.setLastName(TEST_LAST_NAME);
            assertEquals(TEST_LAST_NAME, newStudent.getLastName(), "Last name should be updated");

            newStudent.setEmail(TEST_EMAIL);
            assertEquals(TEST_EMAIL, newStudent.getEmail(), "Email should be updated");

            newStudent.setGender(TEST_GENDER);
            assertEquals(TEST_GENDER, newStudent.getGender(), "Gender should be updated");

            newStudent.setType(TEST_TYPE);
            assertEquals(TEST_TYPE, newStudent.getType(), "Type should be updated");

            newStudent.setCourse(TEST_COURSE);
            assertEquals(TEST_COURSE, newStudent.getCourse(), "Course should be updated");

            printTestSuccess("setter methods");
        }
    }

    @Nested
    @DisplayName("toString Method Tests")
    class ToStringTests
    {

        @Test
        @DisplayName("toString should include all properties")
        void testToStringComplete()
        {
            printTestHeader("toString with complete data");

            String result = student.toString();

            assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
            assertTrue(result.contains("Name: " + TEST_FIRST_NAME + " " + TEST_LAST_NAME),
                    "toString should contain full name");
            assertTrue(result.contains("Email: " + TEST_EMAIL), "toString should contain email");
            assertTrue(result.contains("Gender: " + TEST_GENDER), "toString should contain gender");
            assertTrue(result.contains("Type: " + TEST_TYPE), "toString should contain type");

            printTestSuccess("toString complete data");
        }

        @Test
        @DisplayName("toString should handle null values")
        void testToStringWithNulls()
        {
            printTestHeader("toString with null values");

            Student nullStudent = new Student();
            nullStudent.setId(TEST_ID); // ID should never be null

            String result = nullStudent.toString();

            assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
            assertTrue(result.contains("Name: null null"), "toString should display null for missing names");
            assertTrue(result.contains("Email: null"), "toString should display null for missing email");
            assertTrue(result.contains("Department: null"), "toString should display null for missing department");
            assertTrue(result.contains("Gender: null"), "toString should display null for missing gender");
            assertTrue(result.contains("Type: null"), "toString should display null for missing type");

            printTestSuccess("toString null values");
        }
    }

    @Nested
    @DisplayName("Data Retrieval Tests")
    class DataRetrievalTests
    {

        @Test
        @DisplayName("getByCourse should handle null course")
        void testGetByNullCourse()
        {
            printTestHeader("getByCourse with null");

            assertDoesNotThrow(() ->
            {
                List<Student> students = Student.getByCourse(null);
                assertNotNull(students, "Student list should not be null");
            }, "getByCourse should handle null course");

            printTestSuccess("getByCourse null");
        }

        @Test
        @DisplayName("getByCourse should handle empty course name")
        void testGetByEmptyCourse()
        {
            printTestHeader("getByCourse with empty string");

            assertDoesNotThrow(() ->
            {
                List<Student> students = Student.getByCourse("");
                assertNotNull(students, "Student list should not be null");
            }, "getByCourse should handle empty course name");

            printTestSuccess("getByCourse empty string");
        }

        @Test
        @DisplayName("getByCourse should filter by course")
        void testGetByValidCourse()
        {
            printTestHeader("getByCourse with valid course");

            assertDoesNotThrow(() ->
            {
                List<Student> students = Student.getByCourse(TEST_COURSE);
                assertNotNull(students, "Student list should not be null");
                students.forEach(s -> assertEquals(TEST_COURSE, s.getCourse(),
                        "All students should be from test course"));
            }, "getByCourse should handle valid course name");

            printTestSuccess("getByCourse valid course");
        }
    }

    @Nested
    @DisplayName("Print Methods Tests")
    class PrintMethodsTests
    {

        @Test
        @DisplayName("printDetailedInfo should output all information")
        void testPrintDetailedInfo()
        {
            printTestHeader("printDetailedInfo output");
            outputStream.reset(); // Clear previous output

            student.printDetailedInfo();
            String output = outputStream.toString();

            assertTrue(output.contains(TEST_FIRST_NAME), "Output should contain first name");
            assertTrue(output.contains(TEST_LAST_NAME), "Output should contain last name");
            assertTrue(output.contains(String.valueOf(TEST_ID)), "Output should contain ID");
            assertTrue(output.contains(TEST_TYPE), "Output should contain type");
            assertTrue(output.contains(TEST_EMAIL), "Output should contain email");

            printTestSuccess("printDetailedInfo");
        }

        @Test
        @DisplayName("printDetailedInfo should handle null values")
        void testPrintDetailedInfoWithNulls()
        {
            printTestHeader("printDetailedInfo with null values");
            outputStream.reset();

            Student nullStudent = new Student();
            nullStudent.setId(TEST_ID); // ID should never be null

            nullStudent.printDetailedInfo();
            String output = outputStream.toString();

            assertTrue(output.contains(String.valueOf(TEST_ID)), "Output should contain ID");
            assertTrue(output.contains("null"), "Output should contain null for missing values");

            // Verify output format
            String[] lines = output.split("\n");
            assertTrue(lines.length >= 3, "Output should have at least 3 lines");
            assertTrue(lines[0].contains(String.valueOf(TEST_ID)), "First line should contain ID");
            assertTrue(lines[1].contains("Type:"), "Second line should contain Type label");
            assertTrue(lines[2].contains("Email:"), "Third line should contain Email label");

            printTestSuccess("printDetailedInfo null values");
        }
    }
}