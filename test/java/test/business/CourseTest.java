package business;

import testframework.*;

import java.util.List;

/**
 * Test class for the Course entity.
 * Contains unit tests to verify the functionality of Course class methods including
 * getters, setters, and other utility methods.
 */
public class CourseTest extends BaseTest
{
    private Course course;
    private static final String TEST_COURSE_TITLE = "Introduction to Programming";
    private static final String TEST_COURSE_ID = "CS101";
    private static final DepartmentId TEST_DEPARTMENT = DepartmentId.ECD;

    /**
     * Sets up the test environment before each test method.
     * Creates a new Course instance and initializes it with test data.
     */
    @Override
    protected void setup()
    {
        super.setup();
        course = new Course();
        course.setCourseTitle(TEST_COURSE_TITLE);
        course.setCourseId(TEST_COURSE_ID);
        course.setDepartmentId(TEST_DEPARTMENT);
    }

    /**
     * Tests the getter methods of the Course class.
     * Verifies that the get methods return the expected values that were set during setup.
     */
    public void testGetters()
    {
        Assert.assertEquals(TEST_COURSE_TITLE, course.getCourseTitle(), "Course title should match");
        Assert.assertEquals(TEST_COURSE_ID, course.getCourseId(), "Course ID should match");
        Assert.assertEquals(TEST_DEPARTMENT, course.getDepartmentId(), "Department should match");
    }

    /**
     * Tests the setter methods of the Course class.
     * Creates a new Course instance and verifies that values are correctly set.
     */
    public void testSetters()
    {
        Course newCourse = new Course();

        newCourse.setCourseTitle(TEST_COURSE_TITLE);
        Assert.assertEquals(TEST_COURSE_TITLE, newCourse.getCourseTitle(), "Course title should be updated");

        newCourse.setCourseId(TEST_COURSE_ID);
        Assert.assertEquals(TEST_COURSE_ID, newCourse.getCourseId(), "Course ID should be updated");

        newCourse.setDepartmentId(TEST_DEPARTMENT);
        Assert.assertEquals(TEST_DEPARTMENT, newCourse.getDepartmentId(), "Department should be updated");
    }

    /**
     * Tests the behavior of getDepartmentId when department is null.
     * Verifies that UNKNOWN is returned as the default value.
     */
    public void testGetDepartmentIdWithNull()
    {
        Course nullDeptCourse = new Course();
        Assert.assertEquals(DepartmentId.UNKNOWN, nullDeptCourse.getDepartmentId(),
                "Null department should return UNKNOWN");
    }

    /**
     * Tests the getAll static method of the Course class.
     * Verifies that the method returns a non-null list and doesn't throw exceptions.
     */
    public void testGetAll()
    {
        try
        {
            List<Course> courses = Course.getAll();
            Assert.assertNotNull(courses, "Course list should not be null");
        }
        catch (Exception e)
        {
            Assert.assertTrue(false, "getAll should not throw exception");
        }
    }

    /**
     * Tests the toString method of the Course class.
     * Verifies that the string representation contains all essential course information.
     */
    public void testToString()
    {
        String result = course.toString();
        Assert.assertTrue(result.contains(TEST_COURSE_TITLE), "toString should contain course title");
        Assert.assertTrue(result.contains(TEST_COURSE_ID), "toString should contain course ID");
        Assert.assertTrue(result.contains(TEST_DEPARTMENT.toString()), "toString should contain department");
    }

    /**
     * Main method to run the test suite.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        new CourseTest().runTests();
    }
}