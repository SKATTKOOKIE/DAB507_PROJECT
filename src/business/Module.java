package business;

import business.interfaces.IModule;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an academic module within the system.
 * This class handles the storage and manipulation of module data including
 * module name, code, academic year, and associated courses.
 * <p>
 * The class provides functionality for:
 * <ul>
 *   <li>Creating and managing module information</li>
 *   <li>JSON serialisation and deserialisation</li>
 *   <li>Filtering and searching modules</li>
 *   <li>Course association management</li>
 * </ul>
 */
public class Module implements IModule
{
    /**
     * Module instance variables:
     * <ul>
     *   <li>{@code name} - The full name of the module</li>
     *   <li>{@code code} - The unique identifier code for the module</li>
     *   <li>{@code acYear} - The academic year in which the module is taught</li>
     *   <li>{@code associatedCourses} - List of course codes associated with this module</li>
     * </ul>
     */
    private final String name;
    private final String code;
    private final String acYear;
    private final List<String> associatedCourses;

    /**
     * Constructs a new Module instance.
     *
     * @param name              The name of the module
     * @param code              The unique module code
     * @param acYear            The academic year
     * @param associatedCourses List of associated course codes
     */
    public Module(String name, String code, String acYear, List<String> associatedCourses)
    {
        this.name = name != null ? name.trim() : "";
        this.code = code != null ? code.trim() : "";
        this.acYear = acYear != null ? acYear.trim() : "";
        this.associatedCourses = associatedCourses != null ? associatedCourses : new ArrayList<>();
    }

    /**
     * Creates a list of Module objects from a JSON array.
     *
     * @param jsonArray The JSON array containing module data
     * @return List of valid Module objects
     */
    public static List<Module> fromJsonArray(JsonArray jsonArray)
    {
        List<Module> modules = new ArrayList<>();

        for (JsonElement element : jsonArray)
        {
            JsonObject moduleObj = element.getAsJsonObject();

            String name = moduleObj.get("module_name").getAsString();
            String code = moduleObj.get("module_code").getAsString();
            String acYear = moduleObj.get("ac_year").getAsString();

            List<String> associatedCourses = new ArrayList<>();
            if (moduleObj.has("associated_courses"))
            {
                JsonArray coursesArray = moduleObj.getAsJsonArray("associated_courses");
                for (JsonElement courseElement : coursesArray)
                {
                    associatedCourses.add(courseElement.getAsString());
                }
            }

            Module module = new Module(name, code, acYear, associatedCourses);
            if (module.isValid())
            {
                modules.add(module);
            }
        }

        return modules;
    }

    /**
     * @return The name of the module
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The module code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @return The academic year
     */
    public String getAcYear()
    {
        return acYear;
    }

    /**
     * @return List of associated course codes
     */
    public List<String> getAssociatedCourses()
    {
        return associatedCourses;
    }

    /**
     * Checks if this module is associated with a specific course.
     *
     * @param courseCode The course code to check
     * @return true if the module is associated with the course, false otherwise
     */
    public boolean isAssociatedWithCourse(String courseCode)
    {
        return associatedCourses.contains(courseCode);
    }

    /**
     * Retrieves all modules associated with a specific course.
     *
     * @param courseCode The course code to filter by
     * @return List of modules associated with the specified course
     * @throws IOException If there is an error reading the modules file
     */
    public static List<Module> getModulesForCourse(String courseCode) throws IOException
    {
        List<Module> allModules = getAll();
        return allModules.stream()
                .filter(module -> module.isAssociatedWithCourse(courseCode))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all modules from the system.
     *
     * @return List of all modules in the system
     * @throws IOException If there is an error reading the modules file
     */
    public static List<Module> getAll() throws IOException
    {
        JsonProcessor processor = new JsonProcessor(FilePathHandler.MODULES_FILE.getNormalisedPath());
        processor.processFile();
        JsonElement root = (JsonElement) processor.getJsonContent();

        if (!root.isJsonObject())
        {
            throw new IOException("Root element is not a JsonObject");
        }

        JsonObject rootObj = root.getAsJsonObject();
        JsonArray modulesJson = rootObj.getAsJsonArray("modules");
        return Module.fromJsonArray(modulesJson);
    }

    /**
     * Checks if the module has all required fields populated.
     *
     * @return true if the module is valid, false otherwise
     */
    public boolean isValid()
    {
        return name != null && !name.trim().isEmpty() &&
                code != null && !code.trim().isEmpty() &&
                acYear != null && !acYear.trim().isEmpty();
    }

    /**
     * @return String representation of the module
     */
    @Override
    public String toString()
    {
        return String.format("%s (%s) - Year %s", name, code, acYear);
    }

    /**
     * Checks if this module is equal to another object.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Module module = (Module) o;
        return Objects.equals(name, module.name) &&
                Objects.equals(code, module.code) &&
                Objects.equals(acYear, module.acYear);
    }

    /**
     * @return Hash code for the module
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(name, code, acYear);
    }

    /**
     * Filters modules by academic year.
     *
     * @param modules List of modules to filter
     * @param year    Academic year to filter by
     * @return Filtered list of modules
     */
    public static List<Module> filterByYear(List<Module> modules, String year)
    {
        if (modules == null || year == null)
        {
            return new ArrayList<>();
        }

        return modules.stream()
                .filter(module -> module.getAcYear().equals(year))
                .collect(Collectors.toList());
    }

    /**
     * Searches for modules by name.
     *
     * @param modules    List of modules to search through
     * @param searchTerm Term to search for (case-insensitive)
     * @return List of modules whose names contain the search term
     */
    public static List<Module> searchByName(List<Module> modules, String searchTerm)
    {
        if (modules == null || searchTerm == null)
        {
            return new ArrayList<>();
        }

        return modules.stream()
                .filter(module -> module.getName().toLowerCase()
                        .contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Filters modules by code prefix.
     *
     * @param modules    List of modules to filter
     * @param codePrefix Code prefix to filter by
     * @return Filtered list of modules
     */
    public static List<Module> filterByCode(List<Module> modules, String codePrefix)
    {
        if (modules == null || codePrefix == null)
        {
            return new ArrayList<>();
        }

        return modules.stream()
                .filter(module -> module.getCode().startsWith(codePrefix))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a module by its code.
     *
     * @param code The code of the module to retrieve
     * @return The module with the specified code, or null if not found
     * @throws IOException If there is an error reading the modules file
     */
    public static Module getModuleByCode(String code) throws IOException
    {
        List<Module> allModules = getAll();
        return allModules.stream()
                .filter(module -> module.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}