package business;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Module Class Tests")
class ModuleTest
{
    private Module module;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    // Test constants
    private static final String TEST_NAME = "Introduction to Programming";
    private static final String TEST_CODE = "CS101";
    private static final String TEST_AC_YEAR = "2023";
    private static final List<String> TEST_ASSOCIATED_COURSES = Arrays.asList("BSCS", "BSIT");

    @BeforeEach
    void setup()
    {
        System.setOut(new PrintStream(outputStream));
        outputStream.reset();
        System.out.println("\n=== Starting Module Test ===");

        module = new Module(TEST_NAME, TEST_CODE, TEST_AC_YEAR, TEST_ASSOCIATED_COURSES);

        System.out.println("✓ Test setup completed");
    }

    @AfterEach
    void cleanup()
    {
        System.setOut(originalOut);
    }

    @AfterAll
    static void teardown()
    {
        System.out.println("\n=== Module Tests Completed Successfully ===");
    }

    private void printTestResult(String testName, boolean passed)
    {
        System.setOut(originalOut);
        if (passed)
        {
            System.out.printf("✓ %s passed%n", testName);
        }
        else
        {
            System.out.printf("✗ %s failed%n", testName);
        }
        System.setOut(new PrintStream(outputStream));
    }

    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests
    {

        @Test
        @DisplayName("Module getters should return correct values")
        void testGetters()
        {
            assertEquals(TEST_NAME, module.getName(), "Module name should match");
            assertEquals(TEST_CODE, module.getCode(), "Module code should match");
            assertEquals(TEST_AC_YEAR, module.getAcYear(), "Academic year should match");
            assertEquals(TEST_ASSOCIATED_COURSES, module.getAssociatedCourses(), "Associated courses should match");
            printTestResult("Module getters", true);
        }
    }

    @Nested
    @DisplayName("Course Association Tests")
    class CourseAssociationTests
    {

        @Test
        @DisplayName("isAssociatedWithCourse should return true for associated course")
        void testIsAssociatedWithCourse()
        {
            boolean result = module.isAssociatedWithCourse("BSCS") && module.isAssociatedWithCourse("BSIT");
            assertTrue(result, "Module should be associated with BSCS and BSIT");
            printTestResult("isAssociatedWithCourse (true)", result);
        }

        @Test
        @DisplayName("isAssociatedWithCourse should return false for non-associated course")
        void testIsNotAssociatedWithCourse()
        {
            boolean result = !module.isAssociatedWithCourse("BSEE");
            assertTrue(result, "Module should not be associated with BSEE");
            printTestResult("isAssociatedWithCourse (false)", result);
        }
    }

    @Nested
    @DisplayName("Data Retrieval Tests")
    class DataRetrievalTests
    {

        @Test
        @DisplayName("getAll should return list of modules")
        void testGetAll()
        {
            boolean passed = false;
            try
            {
                List<Module> modules = Module.getAll();
                passed = modules != null;
            }
            catch (IOException e)
            {
                // Exception handling
            }
            assertTrue(passed, "getAll should return a list of modules");
            printTestResult("getAll", passed);
        }
    }

    @Nested
    @DisplayName("Filtering and Searching Tests")
    class FilteringAndSearchingTests
    {

        @Test
        @DisplayName("filterByYear should return modules for the specified year")
        void testFilterByYear()
        {
            List<Module> modules = Arrays.asList(
                    new Module("Module 1", "M1", "2022", Arrays.asList("C1", "C2")),
                    new Module("Module 2", "M2", "2023", Arrays.asList("C3", "C4")),
                    new Module("Module 3", "M3", "2023", Arrays.asList("C5", "C6"))
            );

            List<Module> filteredModules = Module.filterByYear(modules, "2023");
            boolean passed = filteredModules.size() == 2;
            assertTrue(passed, "Should return 2 modules for the year 2023");
            printTestResult("filterByYear", passed);
        }

        @Test
        @DisplayName("searchByName should return modules with matching names")
        void testSearchByName()
        {
            List<Module> modules = Arrays.asList(
                    new Module("Programming Fundamentals", "CS101", "2023", Arrays.asList("BSCS", "BSIT")),
                    new Module("Data Structures", "CS201", "2023", Arrays.asList("BSCS")),
                    new Module("Database Systems", "IT301", "2023", Arrays.asList("BSIT"))
            );

            List<Module> searchResults = Module.searchByName(modules, "programming");
            boolean passed = searchResults.size() == 1;
            assertTrue(passed, "Should return 1 module with 'programming' in the name");
            printTestResult("searchByName", passed);
        }
    }

    @Nested
    @DisplayName("Output Format Tests")
    class OutputTests
    {

        @Test
        @DisplayName("toString should include module properties")
        void testToString()
        {
            String result = module.toString();

            boolean passed = result.contains(TEST_NAME) && result.contains(TEST_CODE) && result.contains(TEST_AC_YEAR);
            assertTrue(passed, "toString should contain module name, code, and academic year");
            printTestResult("toString", passed);
        }
    }
}