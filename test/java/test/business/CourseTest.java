package business;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Course Class Tests")
class CourseTest
{
    private Course course;

    // Test constants
    private static final String TEST_COURSE_TITLE = "Introduction to Programming";
    private static final String TEST_COURSE_ID = "CS101";
    private static final DepartmentId TEST_DEPARTMENT = DepartmentId.ECD;

    // Console output capture
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup()
    {
        System.setOut(new PrintStream(outputStream));
        System.out.println("\n=== Starting Course Test ===");

        course = new Course();
        course.setCourseTitle(TEST_COURSE_TITLE);
        course.setCourseId(TEST_COURSE_ID);
        course.setDepartmentId(TEST_DEPARTMENT);

        System.out.println("✓ Test setup completed");
    }

    @AfterEach
    void cleanup()
    {
        System.setOut(originalOut);
    }

    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests
    {

        @Test
        @DisplayName("Course getters should return correct values")
        void testGetters()
        {
            assertEquals(TEST_COURSE_TITLE, course.getCourseTitle(), "Course title should match");
            assertEquals(TEST_COURSE_ID, course.getCourseId(), "Course ID should match");
            assertEquals(TEST_DEPARTMENT, course.getDepartmentId(), "Department should match");
        }

        @Test
        @DisplayName("Course setters should update values")
        void testSetters()
        {
            Course newCourse = new Course();

            newCourse.setCourseTitle(TEST_COURSE_TITLE);
            assertEquals(TEST_COURSE_TITLE, newCourse.getCourseTitle(), "Course title should be updated");

            newCourse.setCourseId(TEST_COURSE_ID);
            assertEquals(TEST_COURSE_ID, newCourse.getCourseId(), "Course ID should be updated");

            newCourse.setDepartmentId(TEST_DEPARTMENT);
            assertEquals(TEST_DEPARTMENT, newCourse.getDepartmentId(), "Department should be updated");
        }
    }

    @Nested
    @DisplayName("Department Handling Tests")
    class DepartmentTests
    {

        @Test
        @DisplayName("getDepartmentId should handle null department")
        void testGetDepartmentIdWithNull()
        {
            Course nullDeptCourse = new Course();
            assertEquals(DepartmentId.UNKNOWN, nullDeptCourse.getDepartmentId(),
                    "Null department should return UNKNOWN");
        }
    }

    @Nested
    @DisplayName("Data Retrieval Tests")
    class DataRetrievalTests
    {

        @Test
        @DisplayName("getAll should return list of courses")
        void testGetAll()
        {
            assertDoesNotThrow(() ->
            {
                List<Course> courses = Course.getAll();
                assertNotNull(courses, "Course list should not be null");
            }, "getAll should not throw exception");
        }
    }

    @Nested
    @DisplayName("Output Format Tests")
    class OutputTests
    {

        @Test
        @DisplayName("toString should include course properties")
        void testToString()
        {
            String result = course.toString();

            assertTrue(result.contains(TEST_COURSE_TITLE), "toString should contain course title");
            assertTrue(result.contains(TEST_COURSE_ID), "toString should contain course ID");
            assertTrue(result.contains(TEST_DEPARTMENT.toString()), "toString should contain department");
        }
    }
}