package users;

import business.DepartmentId;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Staff class.
 * Verifies the functionality of Staff-specific properties, department handling, data retrieval, and output formatting.
 */
@DisplayName("Staff Class Tests")
class StaffTest
{
    private Staff staff;

    // Test constants grouped together
    private static final int TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Smith";
    private static final String TEST_EMAIL = "john.smith@test.com";
    private static final String TEST_GUID = "550e8400-e29b-41d4-a716-446655440000";
    private static final int TEST_WEEKLY_HOURS = 37;
    private static final int TEST_MAX_MODULES = 4;
    private static final String TEST_AVATAR = "avatar.png";
    private static final DepartmentId TEST_DEPARTMENT = DepartmentId.ECD;

    // Output capture streams
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Setup method to initialize test data and redirect output.
     */
    @BeforeEach
    void setup()
    {
        System.setOut(new PrintStream(outputStream));
        System.out.println("\n=== Starting Staff Test ===");

        staff = new Staff();
        staff.setId(TEST_ID);
        staff.setFirstName(TEST_FIRST_NAME);
        staff.setLastName(TEST_LAST_NAME);
        staff.setEmail(TEST_EMAIL);
        staff.setGuid(TEST_GUID);
        staff.setWeeklyHours(TEST_WEEKLY_HOURS);
        staff.setMaxModules(TEST_MAX_MODULES);
        staff.setAvatar(TEST_AVATAR);
        staff.setDepartmentId(TEST_DEPARTMENT);

        System.out.println("✓ Test setup completed");
    }

    /**
     * Cleanup method to restore original output stream.
     */
    @AfterEach
    void cleanup()
    {
        System.setOut(originalOut);
    }

    /**
     * Utility method to print test header.
     *
     * @param testName the name of the test
     */
    private void printTestHeader(String testName)
    {
        System.setOut(originalOut);
        System.out.println("\nTesting " + testName + "...");
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Utility method to print test success message.
     *
     * @param testName the name of the test
     */
    private void printTestSuccess(String testName)
    {
        System.setOut(originalOut);
        System.out.println("✓ " + testName + " test passed");
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Nested test class for Staff-specific properties.
     */
    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests
    {
        /**
         * Test Staff-specific getter methods.
         */
        @Test
        @DisplayName("Staff-specific getters should return correct values")
        void testGetters()
        {
            printTestHeader("staff-specific getters");

            assertEquals(TEST_GUID, staff.getGuid(), "GUID should match");
            assertEquals(TEST_WEEKLY_HOURS, staff.getWeeklyHours(), "Weekly hours should match");
            assertEquals(TEST_MAX_MODULES, staff.getMaxModules(), "Max modules should match");
            assertEquals(TEST_AVATAR, staff.getAvatar(), "Avatar should match");
            assertEquals(TEST_DEPARTMENT, staff.getDepartmentId(), "Department should match");
            assertEquals(TEST_DEPARTMENT.getDepartmentName(), staff.getDepartment(), "Department name should match");

            printTestSuccess("staff-specific getters");
        }

        /**
         * Test Staff-specific setter methods.
         */
        @Test
        @DisplayName("Staff-specific setters should update values")
        void testSetters()
        {
            printTestHeader("staff-specific setters");

            Staff newStaff = new Staff();

            newStaff.setGuid(TEST_GUID);
            assertEquals(TEST_GUID, newStaff.getGuid(), "GUID should be updated");

            newStaff.setWeeklyHours(TEST_WEEKLY_HOURS);
            assertEquals(TEST_WEEKLY_HOURS, newStaff.getWeeklyHours(), "Weekly hours should be updated");

            newStaff.setMaxModules(TEST_MAX_MODULES);
            assertEquals(TEST_MAX_MODULES, newStaff.getMaxModules(), "Max modules should be updated");

            newStaff.setAvatar(TEST_AVATAR);
            assertEquals(TEST_AVATAR, newStaff.getAvatar(), "Avatar should be updated");

            newStaff.setDepartmentId(TEST_DEPARTMENT);
            assertEquals(TEST_DEPARTMENT, newStaff.getDepartmentId(), "Department should be updated");

            printTestSuccess("staff-specific setters");
        }
    }

    /**
     * Nested test class for department handling.
     */
    @Nested
    @DisplayName("Department Handling Tests")
    class DepartmentTests
    {
        /**
         * Test getDepartmentId method with null department.
         */
        @Test
        @DisplayName("getDepartmentId should handle null department")
        void testGetDepartmentIdWithNull()
        {
            printTestHeader("getDepartmentId with null");

            Staff nullDeptStaff = new Staff();
            assertEquals(DepartmentId.UNKNOWN, nullDeptStaff.getDepartmentId(), "Null department should return UNKNOWN");

            printTestSuccess("getDepartmentId null handling");
        }

        /**
         * Test getDepartmentId method with empty department.
         */
        @Test
        @DisplayName("getDepartmentId should handle empty department")
        void testGetDepartmentIdWithEmpty()
        {
            printTestHeader("getDepartmentId with empty string");

            Staff emptyDeptStaff = new Staff();
            emptyDeptStaff.setDepartment("");
            assertEquals(DepartmentId.UNKNOWN, emptyDeptStaff.getDepartmentId(), "Empty department should return UNKNOWN");

            printTestSuccess("getDepartmentId empty handling");
        }

        /**
         * Test getDepartmentId method with invalid department.
         */
        @Test
        @DisplayName("getDepartmentId should handle invalid department")
        void testGetDepartmentIdWithInvalid()
        {
            printTestHeader("getDepartmentId with invalid department");

            Staff invalidDeptStaff = new Staff();
            invalidDeptStaff.setDepartment("Invalid Department");
            assertEquals(DepartmentId.UNKNOWN, invalidDeptStaff.getDepartmentId(), "Invalid department should return UNKNOWN");

            printTestSuccess("getDepartmentId invalid handling");
        }
    }

    /**
     * Nested test class for data retrieval methods.
     */
    @Nested
    @DisplayName("Data Retrieval Tests")
    class DataRetrievalTests
    {
        /**
         * Test getByDepartment method with null department.
         */
        @Test
        @DisplayName("getByDepartment should handle null department")
        void testGetByNullDepartment()
        {
            printTestHeader("getByDepartment with null");

            assertDoesNotThrow(() ->
            {
                List<Staff> staffList = Staff.getByDepartment(null);
                assertNotNull(staffList, "Staff list should not be null");
            }, "getByDepartment should handle null department");

            printTestSuccess("getByDepartment null handling");
        }

        /**
         * Test getByDepartment method with empty department.
         */
        @Test
        @DisplayName("getByDepartment should handle empty department")
        void testGetByEmptyDepartment()
        {
            printTestHeader("getByDepartment with empty string");

            assertDoesNotThrow(() ->
            {
                List<Staff> staffList = Staff.getByDepartment("");
                assertNotNull(staffList, "Staff list should not be null");
            }, "getByDepartment should handle empty department");

            printTestSuccess("getByDepartment empty handling");
        }

        /**
         * Test getByDepartmentId method with UNKNOWN department.
         */
        @Test
        @DisplayName("getByDepartmentId should handle UNKNOWN department")
        void testGetByUnknownDepartment()
        {
            printTestHeader("getByDepartmentId with UNKNOWN");

            assertDoesNotThrow(() ->
            {
                List<Staff> staffList = Staff.getByDepartmentId(DepartmentId.UNKNOWN);
                assertNotNull(staffList, "Staff list should not be null");
            }, "getByDepartmentId should handle UNKNOWN department");

            printTestSuccess("getByDepartmentId UNKNOWN handling");
        }
    }

    /**
     * Nested test class for output formatting.
     */
    @Nested
    @DisplayName("Output Format Tests")
    class OutputTests
    {
        /**
         * Test toString method output.
         */
        @Test
        @DisplayName("toString should include all properties")
        void testToString()
        {
            printTestHeader("toString output");

            String result = staff.toString();

            assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
            assertTrue(result.contains("Name: " + TEST_FIRST_NAME + " " + TEST_LAST_NAME), "toString should contain full name");
            assertTrue(result.contains("Email: " + TEST_EMAIL), "toString should contain email");
            assertTrue(result.contains("GUID: " + TEST_GUID), "toString should contain GUID");
            assertTrue(result.contains("Weekly Hours: " + TEST_WEEKLY_HOURS), "toString should contain weekly hours");
            assertTrue(result.contains("Max Modules: " + TEST_MAX_MODULES), "toString should contain max modules");

            printTestSuccess("toString");
        }

        /**
         * Test printDetailedInfo method output.
         */
        @Test
        @DisplayName("printDetailedInfo should display all information")
        void testPrintDetailedInfo()
        {
            printTestHeader("printDetailedInfo output");
            outputStream.reset();

            staff.printDetailedInfo();
            String output = outputStream.toString();

            assertTrue(output.contains(TEST_FIRST_NAME), "Output should contain first name");
            assertTrue(output.contains(TEST_LAST_NAME), "Output should contain last name");
            assertTrue(output.contains(String.valueOf(TEST_ID)), "Output should contain ID");
            assertTrue(output.contains(TEST_DEPARTMENT.getDepartmentName()), "Output should contain department name");
            assertTrue(output.contains(String.valueOf(TEST_WEEKLY_HOURS)), "Output should contain weekly hours");
            assertTrue(output.contains(String.valueOf(TEST_MAX_MODULES)), "Output should contain max modules");
            assertTrue(output.contains(TEST_EMAIL), "Output should contain email");

            printTestSuccess("printDetailedInfo");
        }
    }
}