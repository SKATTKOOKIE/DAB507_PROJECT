package business;

import org.junit.jupiter.api.*;
import users.Student;
import users.Staff;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Department Class Tests")
class DepartmentTest
{
    private Department department;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    // Test constants
    private static final String TEST_DEPARTMENT_NAME = "Engineering Computing and Design";
    private static final DepartmentId TEST_DEPARTMENT_ID = DepartmentId.fromString(TEST_DEPARTMENT_NAME);
    private static final Module TEST_MODULE = new Module("Programming 101", "CS101", "2023", Arrays.asList("BSCS", "BSIT"));
    private static final Course TEST_COURSE = new Course();
    private static final Student TEST_STUDENT = new Student();
    private static final Staff TEST_STAFF = new Staff();

    @BeforeEach
    void setup()
    {
        System.setOut(new PrintStream(outputStream));
        outputStream.reset();
        System.out.println("\n=== Starting Department Test ===");

        department = new Department(TEST_DEPARTMENT_ID);
        TEST_COURSE.setCourseId("BSCS");
        TEST_COURSE.setCourseTitle("Bachelor of Science in Computer Science");
        TEST_COURSE.setDepartmentId(TEST_DEPARTMENT_ID);

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
        System.out.println("\n=== Department Tests Completed Successfully ===");
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
        @DisplayName("Department constructor should handle null DepartmentId")
        void testNullDepartmentIdConstructor()
        {
            Department nullDepartment = new Department((DepartmentId) null);
            assertEquals(DepartmentId.UNKNOWN, nullDepartment.getDepartmentId());
            printTestResult("Null DepartmentId constructor", true);
        }

        @Test
        @DisplayName("String constructor should create valid department")
        void testStringConstructor()
        {
            Department stringDepartment = new Department(TEST_DEPARTMENT_NAME);
            assertEquals(TEST_DEPARTMENT_NAME, stringDepartment.getName());
            printTestResult("String constructor", true);
        }

        @Test
        @DisplayName("Department getters should return correct values")
        void testGetters()
        {
            assertEquals(TEST_DEPARTMENT_ID, department.getDepartmentId());
            assertEquals(TEST_DEPARTMENT_NAME, department.getName());
            assertTrue(department.getModules().isEmpty());
            printTestResult("Department getters", true);
        }
    }

    @Nested
    @DisplayName("Module Management Tests")
    class ModuleManagementTests
    {

        @Test
        @DisplayName("addModule should add module to department")
        void testAddModule()
        {
            department.addModule(TEST_MODULE);
            assertEquals(1, department.getModules().size());
            assertTrue(department.getModules().contains(TEST_MODULE));
            printTestResult("Add module", true);
        }

        @Test
        @DisplayName("getModulesByCode should return correct modules")
        void testGetModulesByCode()
        {
            Module csModule = new Module("CS Module", "CS101", "2023", Arrays.asList("BSCS"));
            Module itModule = new Module("IT Module", "IT101", "2023", Arrays.asList("BSIT"));

            department.addModule(csModule);
            department.addModule(itModule);

            List<Module> csModules = department.getModulesByCode("CS");
            assertEquals(1, csModules.size());
            assertTrue(csModules.contains(csModule));

            printTestResult("Get modules by code", true);
        }
    }

    @Nested
    @DisplayName("Course Management Tests")
    class CourseManagementTests
    {

        @Test
        @DisplayName("getCourses should return department courses")
        void testGetCourses()
        {
            Course otherCourse = new Course();
            otherCourse.setDepartmentId(DepartmentId.fromString("Physics"));
            List<Course> allCourses = Arrays.asList(TEST_COURSE, otherCourse);

            List<Course> departmentCourses = department.getCourses(allCourses);
            assertEquals(1, departmentCourses.size());
            assertTrue(departmentCourses.contains(TEST_COURSE));

            printTestResult("Get department courses", true);
        }
    }

    @Nested
    @DisplayName("Department Summary Tests")
    class DepartmentSummaryTests
    {

        @Test
        @DisplayName("createSummary should generate correct summary")
        void testCreateSummary()
        {
            List<Student> students = Arrays.asList(TEST_STUDENT);
            List<Staff> staff = Arrays.asList(TEST_STAFF);
            List<Course> courses = Arrays.asList(TEST_COURSE);

            DepartmentSummary summary = department.createSummary(students, staff, courses);

            assertEquals(TEST_DEPARTMENT_ID, summary.getDepartmentId());
            assertEquals(1, summary.getStudentCount());
            assertEquals(1, summary.getStaffCount());

            String basicSummary = summary.getBasicSummary();
            assertTrue(basicSummary.contains(TEST_DEPARTMENT_NAME));
            assertTrue(basicSummary.contains("Total Students: 1"));
            assertTrue(basicSummary.contains("Total Staff: 1"));

            String detailedInfo = summary.getDetailedInfo();
            assertTrue(detailedInfo.contains(TEST_DEPARTMENT_NAME));
            assertTrue(detailedInfo.contains(TEST_STUDENT.toString()));
            assertTrue(detailedInfo.contains(TEST_STAFF.toString()));
            assertTrue(detailedInfo.contains(TEST_COURSE.toString()));

            printTestResult("Create department summary", true);
        }
    }

    @Nested
    @DisplayName("Object Override Tests")
    class ObjectOverrideTests
    {

        @Test
        @DisplayName("toString should include department properties")
        void testToString()
        {
            department.addModule(TEST_MODULE);
            String result = department.toString();

            assertTrue(result.contains(TEST_DEPARTMENT_NAME));
            assertTrue(result.contains(TEST_DEPARTMENT_ID.toString()));
            assertTrue(result.contains("Number of modules: 1"));

            printTestResult("toString", true);
        }

        @Test
        @DisplayName("equals and hashCode should be consistent")
        void testEqualsAndHashCode()
        {
            Department sameDepartment = new Department(TEST_DEPARTMENT_ID);
            Department differentDepartment = new Department("Physics");

            assertEquals(department, department);
            assertEquals(department, sameDepartment);
            assertNotEquals(department, differentDepartment);
            assertNotEquals(department, null);
            assertNotEquals(department, "Not a department");

            assertEquals(department.hashCode(), sameDepartment.hashCode());

            printTestResult("equals and hashCode", true);
        }
    }
}