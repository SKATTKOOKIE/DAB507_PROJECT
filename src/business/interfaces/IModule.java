package business.interfaces;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;

/**
 * Defines the core functionality for Module management within the educational system.
 * This interface establishes the contract for handling module-related operations including:
 * <ul>
 *   <li>Basic module information access</li>
 *   <li>Course association management</li>
 *   <li>Data validation</li>
 *   <li>JSON data processing</li>
 *   <li>Module filtering and search operations</li>
 * </ul>
 */
public interface IModule
{
    /**
     * Retrieves the name of the module.
     *
     * @return The module name
     */
    String getName();

    /**
     * Retrieves the unique code identifier of the module.
     *
     * @return The module code
     */
    String getCode();

    /**
     * Retrieves the academic year of the module.
     *
     * @return The academic year
     */
    String getAcYear();

    /**
     * Retrieves the list of course codes associated with this module.
     *
     * @return List of associated course codes
     */
    List<String> getAssociatedCourses();

    /**
     * Checks if the module is associated with a specific course.
     *
     * @param courseCode The course code to check
     * @return true if the module is associated with the course, false otherwise
     */
    boolean isAssociatedWithCourse(String courseCode);

    /**
     * Validates the module's data.
     *
     * @return true if the module data is valid, false otherwise
     */
    boolean isValid();

    /**
     * Creates a list of Module objects from a JSON array.
     *
     * @param jsonArray The JSON array containing module data
     * @return List of Module objects
     */
    static List<Module> fromJsonArray(JsonArray jsonArray)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Retrieves all modules associated with a specific course.
     *
     * @param courseCode The course code to filter by
     * @return List of modules associated with the specified course
     * @throws IOException If there is an error accessing module data
     */
    static List<Module> getModulesForCourse(String courseCode) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Retrieves all modules in the system.
     *
     * @return List of all modules
     * @throws IOException If there is an error accessing module data
     */
    static List<Module> getAll() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Filters modules by academic year.
     *
     * @param modules List of modules to filter
     * @param year    Academic year to filter by
     * @return Filtered list of modules
     */
    static List<Module> filterByYear(List<Module> modules, String year)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Searches for modules by name.
     *
     * @param modules    List of modules to search through
     * @param searchTerm Term to search for in module names (case-insensitive)
     * @return List of modules matching the search term
     */
    static List<Module> searchByName(List<Module> modules, String searchTerm)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Filters modules by code prefix.
     *
     * @param modules    List of modules to filter
     * @param codePrefix Code prefix to filter by
     * @return Filtered list of modules
     */
    static List<Module> filterByCode(List<Module> modules, String codePrefix)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Retrieves a specific module by its code.
     *
     * @param code The code of the module to retrieve
     * @return The module with the specified code, or null if not found
     * @throws IOException If there is an error accessing module data
     */
    static Module getModuleByCode(String code) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}