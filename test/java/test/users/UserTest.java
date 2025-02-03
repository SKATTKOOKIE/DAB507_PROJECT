package users;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the abstract User class.
 * Verifies the functionality of the User class and its interaction with the IUser interface.
 */
@DisplayName("User Abstract Class Tests")
class UserTest
{

    /**
     * Concrete implementation of User for testing purposes.
     */
    private static class TestUser extends User
    {
        @Override
        public void printDetailedInfo()
        {
            System.out.printf("Test User: %s %s%n", getFirstName(), getLastName());
        }
    }

    /**
     * The TestUser instance used for testing.
     */
    private TestUser user;

    /**
     * Test data constants:
     * - TEST_ID: The ID value used for testing.
     * - TEST_FIRST_NAME: The first name value used for testing.
     * - TEST_LAST_NAME: The last name value used for testing.
     * - TEST_EMAIL: The email value used for testing.
     * - TEST_DEPARTMENT: The department value used for testing.
     * - TEST_COURSE: The course value used for testing.
     */
    private static final int TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "Jane";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "jane.doe@test.com";
    private static final String TEST_DEPARTMENT = "Computer Science";
    private static final String TEST_COURSE = "Software Engineering";

    /**
     * Output streams for capturing console output:
     * - outputStream: ByteArrayOutputStream to capture the output.
     * - originalOut: The original System.out PrintStream.
     */
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Sets up the test environment before each test method.
     * Redirects System.out to capture console output and initialises the TestUser instance.
     */
    @BeforeEach
    void setup()
    {
        System.setOut(new PrintStream(outputStream));
        System.out.println("\n=== Starting User Test ===");

        user = new TestUser();
        user.setId(TEST_ID);
        user.setFirstName(TEST_FIRST_NAME);
        user.setLastName(TEST_LAST_NAME);
        user.setEmail(TEST_EMAIL);
        user.setDepartment(TEST_DEPARTMENT);
        user.setCourse(TEST_COURSE);

        System.out.println("✓ Test setup completed");
    }

    /**
     * Cleans up the test environment after each test method.
     * Restores System.out to the original PrintStream.
     */
    @AfterEach
    void cleanup()
    {
        System.setOut(originalOut);
    }

    /**
     * Prints the header for a test.
     *
     * @param testName The name of the test.
     */
    private void printTestHeader(String testName)
    {
        System.setOut(originalOut);
        System.out.println("\nTesting " + testName + "...");
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Prints the success message for a test.
     *
     * @param testName The name of the test.
     */
    private void printTestSuccess(String testName)
    {
        System.setOut(originalOut);
        System.out.println("✓ " + testName + " test passed");
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Nested test class for testing basic property functionality.
     */
    @Nested
    @DisplayName("Basic Property Tests")
    class BasicPropertyTests
    {

        /**
         * Tests the getter methods of the User class.
         * Verifies that the getter methods return the expected values.
         */
        @Test
        @DisplayName("Getter methods should return correct values")
        void testGetters()
        {
            printTestHeader("getter methods");

            assertEquals(TEST_ID, user.getId(), "ID should match");
            assertEquals(TEST_FIRST_NAME, user.getFirstName(), "First name should match");
            assertEquals(TEST_LAST_NAME, user.getLastName(), "Last name should match");
            assertEquals(TEST_EMAIL, user.getEmail(), "Email should match");
            assertEquals(TEST_DEPARTMENT, user.getDepartment(), "Department should match");
            assertEquals(TEST_COURSE, user.getCourse(), "Course should match");

            printTestSuccess("getter methods");
        }

        /**
         * Tests the setter methods of the User class.
         * Verifies that the setter methods update the values correctly.
         */
        @Test
        @DisplayName("Setter methods should update values")
        void testSetters()
        {
            printTestHeader("setter methods");

            TestUser newUser = new TestUser();

            newUser.setId(TEST_ID);
            assertEquals(TEST_ID, newUser.getId(), "ID should be updated");

            newUser.setFirstName(TEST_FIRST_NAME);
            assertEquals(TEST_FIRST_NAME, newUser.getFirstName(), "First name should be updated");

            newUser.setLastName(TEST_LAST_NAME);
            assertEquals(TEST_LAST_NAME, newUser.getLastName(), "Last name should be updated");

            newUser.setEmail(TEST_EMAIL);
            assertEquals(TEST_EMAIL, newUser.getEmail(), "Email should be updated");

            newUser.setDepartment(TEST_DEPARTMENT);
            assertEquals(TEST_DEPARTMENT, newUser.getDepartment(), "Department should be updated");

            newUser.setCourse(TEST_COURSE);
            assertEquals(TEST_COURSE, newUser.getCourse(), "Course should be updated");

            printTestSuccess("setter methods");
        }

        /**
         * Tests the handling of null values in the User class.
         * Verifies that the properties handle null values correctly.
         */
        @Test
        @DisplayName("Properties should handle null values")
        void testNullHandling()
        {
            printTestHeader("null value handling");

            TestUser nullUser = new TestUser();
            nullUser.setId(TEST_ID); // ID is primitive, can't be null

            assertNull(nullUser.getFirstName(), "First name should be null by default");
            assertNull(nullUser.getLastName(), "Last name should be null by default");
            assertNull(nullUser.getEmail(), "Email should be null by default");
            assertNull(nullUser.getDepartment(), "Department should be null by default");
            assertNull(nullUser.getCourse(), "Course should be null by default");

            printTestSuccess("null value handling");
        }
    }

    /**
     * Nested test class for testing string representation functionality.
     */
    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests
    {

        /**
         * Tests the toString method of the User class with complete data.
         * Verifies that the toString method includes all the properties.
         */
        @Test
        @DisplayName("toString should include all properties")
        void testToString()
        {
            printTestHeader("toString with complete data");

            String result = user.toString();

            assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
            assertTrue(result.contains("Name: " + TEST_FIRST_NAME + " " + TEST_LAST_NAME),
                    "toString should contain full name");
            assertTrue(result.contains("Email: " + TEST_EMAIL), "toString should contain email");
            assertTrue(result.contains("Department: " + TEST_DEPARTMENT),
                    "toString should contain department");

            printTestSuccess("toString complete data");
        }

        /**
         * Tests the toString method of the User class with null values.
         * Verifies that the toString method handles null values correctly.
         */
        @Test
        @DisplayName("toString should handle null values")
        void testToStringWithNulls()
        {
            printTestHeader("toString with null values");

            TestUser nullUser = new TestUser();
            nullUser.setId(TEST_ID); // ID is primitive, can't be null

            String result = nullUser.toString();

            assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
            assertTrue(result.contains("Name: null null"), "toString should handle null names");
            assertTrue(result.contains("Email: null"), "toString should handle null email");
            assertTrue(result.contains("Department: null"), "toString should handle null department");

            printTestSuccess("toString null values");
        }
    }

    /**
     * Nested test class for testing interface implementation functionality.
     */
    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceTests
    {

        /**
         * Tests the implementation of the IUser interface in the User class.
         * Verifies that the User class implements the IUser interface correctly.
         */
        @Test
        @DisplayName("User should implement IUser interface")
        void testInterfaceImplementation()
        {
            printTestHeader("IUser interface implementation");

            assertTrue(user instanceof IUser, "User should implement IUser interface");

            // Test interface method implementations
            IUser iUser = user;
            assertEquals(TEST_ID, iUser.getId(), "Interface getId should work");
            assertEquals(TEST_FIRST_NAME, iUser.getFirstName(), "Interface getFirstName should work");
            assertEquals(TEST_LAST_NAME, iUser.getLastName(), "Interface getLastName should work");
            assertEquals(TEST_EMAIL, iUser.getEmail(), "Interface getEmail should work");
            assertEquals(TEST_DEPARTMENT, iUser.getDepartment(), "Interface getDepartment should work");
            assertEquals(TEST_COURSE, iUser.getCourse(), "Interface getCourse should work");

            printTestSuccess("interface implementation");
        }

        /**
         * Tests the implementation of the abstract method in the User class.
         * Verifies that the abstract method is implemented correctly in the TestUser subclass.
         */
        @Test
        @DisplayName("Abstract method implementation should work")
        void testAbstractMethod()
        {
            printTestHeader("abstract method implementation");

            outputStream.reset();
            user.printDetailedInfo();
            String output = outputStream.toString();

            assertTrue(output.contains(TEST_FIRST_NAME), "Output should contain first name");
            assertTrue(output.contains(TEST_LAST_NAME), "Output should contain last name");

            printTestSuccess("abstract method");
        }
    }
}