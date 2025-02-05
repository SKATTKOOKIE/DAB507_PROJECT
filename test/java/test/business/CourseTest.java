package business;

import testframework.*;

import java.util.List;

public class CourseTest extends BaseTest
{
    private Course course;
    private static final String TEST_COURSE_TITLE = "Introduction to Programming";
    private static final String TEST_COURSE_ID = "CS101";
    private static final DepartmentId TEST_DEPARTMENT = DepartmentId.ECD;

    @Override
    protected void setup()
    {
        super.setup();
        course = new Course();
        course.setCourseTitle(TEST_COURSE_TITLE);
        course.setCourseId(TEST_COURSE_ID);
        course.setDepartmentId(TEST_DEPARTMENT);
    }

    public void testGetters()
    {
        Assert.assertEquals(TEST_COURSE_TITLE, course.getCourseTitle(), "Course title should match");
        Assert.assertEquals(TEST_COURSE_ID, course.getCourseId(), "Course ID should match");
        Assert.assertEquals(TEST_DEPARTMENT, course.getDepartmentId(), "Department should match");
    }

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

    public void testGetDepartmentIdWithNull()
    {
        Course nullDeptCourse = new Course();
        Assert.assertEquals(DepartmentId.UNKNOWN, nullDeptCourse.getDepartmentId(),
                "Null department should return UNKNOWN");
    }

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

    public void testToString()
    {
        String result = course.toString();
        Assert.assertTrue(result.contains(TEST_COURSE_TITLE), "toString should contain course title");
        Assert.assertTrue(result.contains(TEST_COURSE_ID), "toString should contain course ID");
        Assert.assertTrue(result.contains(TEST_DEPARTMENT.toString()), "toString should contain department");
    }

    public static void main(String[] args)
    {
        new CourseTest().runTests();
    }
}