package business.interfaces;

import business.Course;
import business.DepartmentId;
import business.DepartmentSummary;
import users.Student;
import users.Staff;
import business.Module;

import java.util.List;

/**
 * Defines the core functionality for Department management within the educational system.
 * This interface establishes the contract for handling department-related operations including:
 * <ul>
 *   <li>Department identification and basic information management</li>
 *   <li>Module management and filtering capabilities</li>
 *   <li>Course association handling</li>
 *   <li>Department summary generation</li>
 *   <li>Data validation and comparison operations</li>
 * </ul>
 */
public interface IDepartment
{
    /**
     * Retrieves the unique identifier for the department.
     *
     * @return The department's unique identifier
     */
    DepartmentId getDepartmentId();

    /**
     * Retrieves the name of the department.
     *
     * @return The department name
     */
    String getName();

    /**
     * Adds a module to the department's module list.
     *
     * @param module The module to add to the department
     */
    void addModule(Module module);

    /**
     * Retrieves all modules associated with the department.
     *
     * @return List of all modules in the department
     */
    List<Module> getModules();

    /**
     * Retrieves modules filtered by academic year.
     *
     * @param year The academic year to filter by
     * @return List of modules for the specified year
     */
    List<Module> getModulesByYear(String year);

    /**
     * Searches for modules by name within the department.
     *
     * @param searchTerm The term to search for in module names
     * @return List of modules matching the search term
     */
    List<Module> searchModulesByName(String searchTerm);

    /**
     * Retrieves modules filtered by their code prefix.
     *
     * @param codePrefix The prefix to filter module codes by
     * @return List of modules with matching code prefix
     */
    List<Module> getModulesByCode(String codePrefix);

    /**
     * Retrieves courses associated with this department from a list of all courses.
     *
     * @param allCourses List of all courses in the system
     * @return List of courses associated with this department
     */
    List<Course> getCourses(List<Course> allCourses);

    /**
     * Creates a summary of the department including students, staff, and courses.
     *
     * @param students List of students in the department
     * @param staff    List of staff members in the department
     * @param courses  List of courses offered by the department
     * @return A DepartmentSummary object containing the compiled information
     */
    DepartmentSummary createSummary(List<Student> students, List<Staff> staff, List<Course> courses);

    /**
     * Validates the department's data.
     *
     * @return true if the department data is valid, false otherwise
     */
    boolean isValid();

    /**
     * Compares this department with another object for equality.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    boolean equals(Object o);

    /**
     * Generates a hash code for the department.
     *
     * @return The hash code value
     */
    int hashCode();

    /**
     * Provides a string representation of the department.
     *
     * @return A string containing the department's information
     */
    String toString();
}