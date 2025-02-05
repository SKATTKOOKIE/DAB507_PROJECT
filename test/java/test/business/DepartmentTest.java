package business;

import testframework.*;
import users.Student;
import users.Staff;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the Department entity.
 * Contains comprehensive unit tests for Department class functionality including
 * constructors, getters, module management, course management, and department summaries.
 */
public class DepartmentTest extends BaseTest
{
    private Department department;
    private static final String TEST_DEPARTMENT_NAME = "Engineering Computing and Design";
    private static final DepartmentId TEST_DEPARTMENT_ID = DepartmentId.fromString(TEST_DEPARTMENT_NAME);
    private static final Module TEST_MODULE = new Module("Programming 101", "CS101", "2023", Arrays.asList("BSCS", "BSIT"));
    private static final Course TEST_COURSE = new Course();
    private static final Student TEST_STUDENT = new Student();
    private static final Staff TEST_STAFF = new Staff();

    /**
     * Sets up the test environment before each test method.
     * Initializes a Department instance and configures test course data.
     */
    @Override
    protected void setup()
    {
        super.setup();
        department = new Department(TEST_DEPARTMENT_ID);
        TEST_COURSE.setCourseId("BSCS");
        TEST_COURSE.setCourseTitle("Bachelor of Science in Computer Science");
        TEST_COURSE.setDepartmentId(TEST_DEPARTMENT_ID);
    }

    /**
     * Tests the Department constructor with null DepartmentId.
     * Verifies that UNKNOWN is returned as the default department ID.
     */
    public void testNullDepartmentIdConstructor()
    {
        Department nullDepartment = new Department((DepartmentId) null);
        Assert.assertEquals(DepartmentId.UNKNOWN, nullDepartment.getDepartmentId(),
                "Null department should return UNKNOWN");
    }

    /**
     * Tests the Department constructor with string parameter.
     * Verifies that the department name is correctly set from the constructor input.
     */
    public void testStringConstructor()
    {
        Department stringDepartment = new Department(TEST_DEPARTMENT_NAME);
        Assert.assertEquals(TEST_DEPARTMENT_NAME, stringDepartment.getName(),
                "Department name should match constructor input");
    }

    /**
     * Tests the getter methods of the Department class.
     * Verifies department ID, name, and initial empty modules list.
     */
    public void testGetters()
    {
        Assert.assertEquals(TEST_DEPARTMENT_ID, department.getDepartmentId(),
                "Department ID should match");
        Assert.assertEquals(TEST_DEPARTMENT_NAME, department.getName(),
                "Department name should match");
        Assert.assertTrue(department.getModules().isEmpty(),
                "Initial modules list should be empty");
    }

    /**
     * Tests module management functionality.
     * Verifies adding modules and retrieving modules by code.
     */
    public void testModuleManagement()
    {
        department.addModule(TEST_MODULE);
        Assert.assertEquals(1, department.getModules().size(),
                "Department should have one module");
        Assert.assertTrue(department.getModules().contains(TEST_MODULE),
                "Department should contain test module");

        Module csModule = new Module("CS Module", "CS102", "2023", Arrays.asList("BSCS"));
        Module itModule = new Module("IT Module", "IT101", "2023", Arrays.asList("BSIT"));

        department.addModule(csModule);
        department.addModule(itModule);

        List<Module> csModules = department.getModulesByCode("CS");
        Assert.assertEquals(2, csModules.size(),
                "Should find two CS modules");
        Assert.assertTrue(csModules.contains(csModule) && csModules.contains(TEST_MODULE),
                "Should contain both CS modules");
    }

    /**
     * Tests course management functionality.
     * Verifies filtering of courses by department.
     */
    public void testCourseManagement()
    {
        Course otherCourse = new Course();
        otherCourse.setDepartmentId(DepartmentId.fromString("Physics"));
        List<Course> allCourses = Arrays.asList(TEST_COURSE, otherCourse);

        List<Course> departmentCourses = department.getCourses(allCourses);
        Assert.assertEquals(1, departmentCourses.size(),
                "Should find one department course");
        Assert.assertTrue(departmentCourses.contains(TEST_COURSE),
                "Should contain the test course");
    }

    /**
     * Tests department summary functionality.
     * Verifies creation and content of basic and detailed department summaries.
     */
    public void testDepartmentSummary()
    {
        List<Student> students = Arrays.asList(TEST_STUDENT);
        List<Staff> staff = Arrays.asList(TEST_STAFF);
        List<Course> courses = Arrays.asList(TEST_COURSE);

        DepartmentSummary summary = department.createSummary(students, staff, courses);

        Assert.assertEquals(TEST_DEPARTMENT_ID, summary.getDepartmentId(),
                "Summary department ID should match");
        Assert.assertEquals(1, summary.getStudentCount(),
                "Should have one student");
        Assert.assertEquals(1, summary.getStaffCount(),
                "Should have one staff member");

        String basicSummary = summary.getBasicSummary();
        Assert.assertTrue(basicSummary.contains(TEST_DEPARTMENT_NAME),
                "Basic summary should contain department name");
        Assert.assertTrue(basicSummary.contains("Total Students: 1"),
                "Basic summary should show student count");
        Assert.assertTrue(basicSummary.contains("Total Staff: 1"),
                "Basic summary should show staff count");

        String detailedInfo = summary.getDetailedInfo();
        Assert.assertTrue(detailedInfo.contains(TEST_DEPARTMENT_NAME),
                "Detailed info should contain department name");
        Assert.assertTrue(detailedInfo.contains(TEST_STUDENT.toString()),
                "Detailed info should contain student info");
        Assert.assertTrue(detailedInfo.contains(TEST_STAFF.toString()),
                "Detailed info should contain staff info");
        Assert.assertTrue(detailedInfo.contains(TEST_COURSE.toString()),
                "Detailed info should contain course info");
    }

    /**
     * Tests the object override methods (toString, equals, hashCode).
     * Verifies proper implementation of object comparison and string representation.
     */
    public void testObjectOverrides()
    {
        department.addModule(TEST_MODULE);
        String result = department.toString();

        Assert.assertTrue(result.contains(TEST_DEPARTMENT_NAME),
                "toString should contain department name");
        Assert.assertTrue(result.contains(TEST_DEPARTMENT_ID.toString()),
                "toString should contain department ID");
        Assert.assertTrue(result.contains("Number of modules: 1"),
                "toString should show module count");

        Department sameDepartment = new Department(TEST_DEPARTMENT_ID);
        Department differentDepartment = new Department("Physics");

        Assert.assertTrue(department.equals(department),
                "Department should equal itself");
        Assert.assertTrue(department.equals(sameDepartment),
                "Department should equal same department");
        Assert.assertFalse(department.equals(differentDepartment),
                "Department should not equal different department");
        Assert.assertFalse(department.equals(null),
                "Department should not equal null");
        Assert.assertFalse(department.equals("Not a department"),
                "Department should not equal non-department");

        Assert.assertTrue(department.hashCode() == sameDepartment.hashCode(),
                "Equal departments should have same hash code");
    }

    /**
     * Main method to run the test suite.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        new DepartmentTest().runTests();
    }
}