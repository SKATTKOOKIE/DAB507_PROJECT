package business.interfaces;

import business.Course;
import business.Department;
import business.DepartmentId;

import java.io.IOException;
import java.util.List;

/**
 * Interface defining core functionality for Course management.
 */
public interface ICourse
{
    // Basic getters and setters
    String getCourseTitle();

    void setCourseTitle(String courseTitle);

    String getCourseId();

    void setCourseId(String courseId);

    DepartmentId getDepartmentId();

    void setDepartmentId(DepartmentId departmentId);

    // Course code operations
    String getCourseCode();

    boolean hasValidCourseCode();

    // Static methods that should be defined
    static List<Course> getAll() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<Course> getCoursesByDepartment(Department department) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<Course> filterByIdPrefix(List<Course> courses, String prefix)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static String getCourseCodeFromTitle(String courseTitle) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void displayAllCourses() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void displayCoursesByDepartment(DepartmentId department) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void searchCoursesByTitle(String searchTerm) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}