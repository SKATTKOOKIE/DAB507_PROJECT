package business.interfaces;

import business.Course;
import business.Department;
import business.DepartmentId;

import java.io.IOException;
import java.util.List;

/**
 * Defines the core functionality for Course management within the system.
 * This interface establishes the contract for handling course-related operations including:
 * <ul>
 *   <li>Basic course information management</li>
 *   <li>Course code validation and processing</li>
 *   <li>Course retrieval and filtering operations</li>
 *   <li>Department-based course management</li>
 *   <li>Course display and search functionality</li>
 * </ul>
 */
public interface ICourse
{
    /**
     * Retrieves the title of the course.
     *
     * @return The course title
     */
    String getCourseTitle();

    /**
     * Sets the title of the course.
     *
     * @param courseTitle The new course title
     */
    void setCourseTitle(String courseTitle);

    /**
     * Retrieves the course identifier.
     *
     * @return The course ID
     */
    String getCourseId();

    /**
     * Sets the course identifier.
     *
     * @param courseId The new course ID
     */
    void setCourseId(String courseId);

    /**
     * Retrieves the department identifier associated with the course.
     *
     * @return The department ID
     */
    DepartmentId getDepartmentId();

    /**
     * Sets the department identifier for the course.
     *
     * @param departmentId The new department ID
     */
    void setDepartmentId(DepartmentId departmentId);

    /**
     * Retrieves the formatted course code.
     *
     * @return The course code
     */
    String getCourseCode();

    /**
     * Validates the course code format.
     *
     * @return true if the course code is valid, false otherwise
     */
    boolean hasValidCourseCode();

    /**
     * Retrieves all courses in the system.
     *
     * @return List of all courses
     * @throws IOException If there is an error accessing course data
     */
    static List<Course> getAll() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Retrieves all courses associated with a specific department.
     *
     * @param department The department to filter courses by
     * @return List of courses in the specified department
     * @throws IOException If there is an error accessing course data
     */
    static List<Course> getCoursesByDepartment(Department department) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Filters a list of courses by course ID prefix.
     *
     * @param courses List of courses to filter
     * @param prefix  The prefix to filter by
     * @return Filtered list of courses
     */
    static List<Course> filterByIdPrefix(List<Course> courses, String prefix)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Generates a course code from a course title.
     *
     * @param courseTitle The course title to generate code from
     * @return Generated course code
     * @throws IOException If there is an error accessing course data
     */
    static String getCourseCodeFromTitle(String courseTitle) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Displays all courses in the system.
     *
     * @throws IOException If there is an error accessing course data
     */
    static void displayAllCourses() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Displays all courses in a specific department.
     *
     * @param department The department to display courses for
     * @throws IOException If there is an error accessing course data
     */
    static void displayCoursesByDepartment(DepartmentId department) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Searches and displays courses by title.
     *
     * @param searchTerm The term to search for in course titles
     * @throws IOException If there is an error accessing course data
     */
    static void searchCoursesByTitle(String searchTerm) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}