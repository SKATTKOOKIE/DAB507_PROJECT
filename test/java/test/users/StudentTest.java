package users;

import testframework.*;

import java.io.IOException;
import java.util.List;

/**
 * Test class for the Student entity.
 * Tests functionality specific to Student objects including:
 * - Basic property getters and setters
 * - String representation
 * - Course-based queries
 * - Information display formatting
 * <p>
 * Extends BaseTest to utilise the custom testing framework.
 */
public class StudentTest extends BaseTest
{
    /**
     * The student instance used for testing
     */
    private Student student;

    /**
     * Constants used for test data
     */
    private static final int TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@test.com";
    private static final String TEST_GENDER = "Male";
    private static final String TEST_TYPE = "Full-Time";
    private static final String TEST_COURSE = "Computer Science";

    /**
     * Sets up the test environment before each test.
     * Creates a new Student instance and initialises it with test data.
     */
    @Override
    protected void setup()
    {
        super.setup();
        student = new Student();
        student.setId(TEST_ID);
        student.setFirstName(TEST_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);
        student.setEmail(TEST_EMAIL);
        student.setGender(TEST_GENDER);
        student.setType(TEST_TYPE);
        student.setCourse(TEST_COURSE);
    }

    /**
     * Tests the getter methods of the Student class.
     * Verifies that all properties return their expected values.
     */
    public void testGetters()
    {
        Assert.assertEquals(TEST_ID, student.getId(), "ID should match");
        Assert.assertEquals(TEST_FIRST_NAME, student.getFirstName(), "First name should match");
        Assert.assertEquals(TEST_LAST_NAME, student.getLastName(), "Last name should match");
        Assert.assertEquals(TEST_EMAIL, student.getEmail(), "Email should match");
        Assert.assertEquals(TEST_GENDER, student.getGender(), "Gender should match");
        Assert.assertEquals(TEST_TYPE, student.getType(), "Type should match");
        Assert.assertEquals(TEST_COURSE, student.getCourse(), "Course should match");
    }

    /**
     * Tests the setter methods of the Student class.
     * Creates a new Student instance and verifies that properties can be correctly set.
     */
    public void testSetters()
    {
        Student newStudent = new Student();

        newStudent.setId(TEST_ID);
        Assert.assertEquals(TEST_ID, newStudent.getId(), "ID should be updated");

        newStudent.setFirstName(TEST_FIRST_NAME);
        Assert.assertEquals(TEST_FIRST_NAME, newStudent.getFirstName(), "First name should be updated");

        newStudent.setLastName(TEST_LAST_NAME);
        Assert.assertEquals(TEST_LAST_NAME, newStudent.getLastName(), "Last name should be updated");

        newStudent.setEmail(TEST_EMAIL);
        Assert.assertEquals(TEST_EMAIL, newStudent.getEmail(), "Email should be updated");

        newStudent.setGender(TEST_GENDER);
        Assert.assertEquals(TEST_GENDER, newStudent.getGender(), "Gender should be updated");

        newStudent.setType(TEST_TYPE);
        Assert.assertEquals(TEST_TYPE, newStudent.getType(), "Type should be updated");

        newStudent.setCourse(TEST_COURSE);
        Assert.assertEquals(TEST_COURSE, newStudent.getCourse(), "Course should be updated");
    }

    /**
     * Tests the toString method with a fully populated Student object.
     * Verifies that all fields are properly included in the string representation.
     */
    public void testToStringComplete()
    {
        String result = student.toString();

        Assert.assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
        Assert.assertTrue(result.contains("Name: " + TEST_FIRST_NAME + " " + TEST_LAST_NAME),
                "toString should contain full name");
        Assert.assertTrue(result.contains("Email: " + TEST_EMAIL), "toString should contain email");
        Assert.assertTrue(result.contains("Gender: " + TEST_GENDER), "toString should contain gender");
        Assert.assertTrue(result.contains("Type: " + TEST_TYPE), "toString should contain type");
    }

    /**
     * Tests the toString method with a Student object containing null values.
     * Verifies that null fields are properly handled in the string representation.
     */
    public void testToStringWithNulls()
    {
        Student nullStudent = new Student();
        nullStudent.setId(TEST_ID);

        String result = nullStudent.toString();

        Assert.assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
        Assert.assertTrue(result.contains("Name: null null"), "toString should display null for missing names");
        Assert.assertTrue(result.contains("Email: null"), "toString should display null for missing email");
        Assert.assertTrue(result.contains("Department: null"), "toString should display null for missing department");
        Assert.assertTrue(result.contains("Gender: null"), "toString should display null for missing gender");
        Assert.assertTrue(result.contains("Type: null"), "toString should display null for missing type");
    }

    /**
     * Tests retrieving students with a null course parameter.
     *
     * @throws IOException if there is an error accessing the data source
     */
    public void testGetByNullCourse() throws IOException
    {
        List<Student> students = Student.getByCourse(null);
        Assert.assertNotNull(students, "Student list should not be null");
    }

    /**
     * Tests retrieving students with an empty course parameter.
     *
     * @throws IOException if there is an error accessing the data source
     */
    public void testGetByEmptyCourse() throws IOException
    {
        List<Student> students = Student.getByCourse("");
        Assert.assertNotNull(students, "Student list should not be null");
    }

    /**
     * Tests retrieving students with a valid course parameter.
     * Verifies that all returned students are from the specified course.
     *
     * @throws IOException if there is an error accessing the data source
     */
    public void testGetByValidCourse() throws IOException
    {
        List<Student> students = Student.getByCourse(TEST_COURSE);
        Assert.assertNotNull(students, "Student list should not be null");
        for (Student s : students)
        {
            Assert.assertEquals(TEST_COURSE, s.getCourse(), "All students should be from test course");
        }
    }

    /**
     * Tests the printDetailedInfo method with a fully populated Student object.
     * Verifies that all relevant information is included in the output.
     */
    public void testPrintDetailedInfo()
    {
        outputStream.reset();
        student.printDetailedInfo();
        String output = outputStream.toString();

        Assert.assertTrue(output.contains(TEST_FIRST_NAME), "Output should contain first name");
        Assert.assertTrue(output.contains(TEST_LAST_NAME), "Output should contain last name");
        Assert.assertTrue(output.contains(String.valueOf(TEST_ID)), "Output should contain ID");
        Assert.assertTrue(output.contains(TEST_TYPE), "Output should contain type");
        Assert.assertTrue(output.contains(TEST_EMAIL), "Output should contain email");
    }

    /**
     * Tests the printDetailedInfo method with a Student object containing null values.
     * Verifies that null fields are properly handled in the output.
     */
    public void testPrintDetailedInfoWithNulls()
    {
        outputStream.reset();
        Student nullStudent = new Student();
        nullStudent.setId(TEST_ID);

        nullStudent.printDetailedInfo();
        String output = outputStream.toString();

        Assert.assertTrue(output.contains(String.valueOf(TEST_ID)), "Output should contain ID");
        Assert.assertTrue(output.contains("null"), "Output should contain null for missing values");

        String[] lines = output.split("\n");
        Assert.assertTrue(lines.length >= 3, "Output should have at least 3 lines");
        Assert.assertTrue(lines[0].contains(String.valueOf(TEST_ID)), "First line should contain ID");
        Assert.assertTrue(lines[1].contains("Type:"), "Second line should contain Type label");
        Assert.assertTrue(lines[2].contains("Email:"), "Third line should contain Email label");
    }

    /**
     * Main method to run the test suite.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        new StudentTest().runTests();
    }
}