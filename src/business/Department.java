package business;

<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.io.IOException;
import java.util.List;

import file_handling.JsonProcessor;

import java.io.IOException;
>>>>>>> 32dafc942ac3a4fa1d6b6115aa8ac41faf18694f
import java.util.List;

/**
 * Represents a department with a name and department ID.
 * Includes static methods for loading departments from JSON.
 */
public class Department
{
<<<<<<< HEAD
    private final String name;
    private final String departmentId;
    private List<Module> modules;

    /**
     * Constructs a Department with the specified parameters.
     *
     * @param name         Department name
     * @param departmentId Department identifier
     */
    public Department(String name, String departmentId)
    {
        this.name = name != null ? name.trim() : "";
        this.departmentId = departmentId != null ? departmentId.trim() : "";
        this.modules = new ArrayList<>();
    }

    /**
     * Adds a module to this department.
     *
     * @param module Module to add
     */
    public void addModule(Module module)
    {
        if (module != null)
        {
            modules.add(module);
=======
    private String name;
    private String dep_id;

    /**
     * Default constructor required for JSON deserialization
     */
    public Department()
    {
    }

    /**
     * Constructs a Department with the specified name and department ID.
     *
     * @param name   The full name of the department
     * @param dep_id The department's identifier code
     */
    public Department(String name, String dep_id)
    {
        this.name = name;
        this.dep_id = dep_id;
    }

    /**
     * Loads all departments from a JSON file.
     *
     * @param filePath Path to the JSON file
     * @return List of Department objects
     * @throws IOException if file operations fail
     */
    public static List<Department> loadFromJson(String filePath) throws IOException
    {
        JsonProcessor jsonProcessor = new JsonProcessor(filePath);
        jsonProcessor.processFile();
        return jsonProcessor.parseJsonToList(Department.class);
    }

    /**
     * Displays all departments from a JSON file.
     *
     * @param filePath Path to the JSON file
     */
    public static void displayAll(String filePath)
    {
        try
        {
            List<Department> departments = loadFromJson(filePath);
            departments.forEach(System.out::println);
        }
        catch (IOException e)
        {
            System.err.println("Error loading departments: " + e.getMessage());
>>>>>>> 32dafc942ac3a4fa1d6b6115aa8ac41faf18694f
        }
    }

    /**
<<<<<<< HEAD
     * Gets all modules in this department.
     *
     * @return List of modules
     */
    public List<Module> getModules()
    {
        return new ArrayList<>(modules);
    }

    /**
     * Gets modules filtered by academic year.
     *
     * @param year Academic year to filter by
     * @return Filtered list of modules
     */
    public List<Module> getModulesByYear(String year)
    {
        return Module.filterByYear(modules, year);
    }

    /**
     * Searches for modules by name within this department.
     *
     * @param searchTerm Term to search for
     * @return List of matching modules
     */
    public List<Module> searchModulesByName(String searchTerm)
    {
        return Module.searchByName(modules, searchTerm);
    }

    /**
     * Filters modules by code prefix within this department.
     *
     * @param codePrefix Code prefix to filter by
     * @return Filtered list of modules
     */
    public List<Module> getModulesByCode(String codePrefix)
    {
        return Module.filterByCode(modules, codePrefix);
    }

    // Getters
=======
     * Finds and displays a department by its ID.
     *
     * @param filePath Path to the JSON file
     * @param depId    The department ID to search for
     */
    public static void displayById(String filePath, String depId)
    {
        try
        {
            List<Department> departments = loadFromJson(filePath);
            departments.stream()
                    .filter(dept -> dept.getDepId().equals(depId))
                    .findFirst()
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Department not found with ID: " + depId)
                    );
        }
        catch (IOException e)
        {
            System.err.println("Error loading departments: " + e.getMessage());
        }
    }

    /**
     * Finds and displays a department by its ID.
     *
     * @param filePath Path to the JSON file
     * @param name     The department name to search for
     */
    public static void displayByName(String filePath, String name)
    {
        try
        {
            List<Department> departments = loadFromJson(filePath);
            departments.stream()
                    .filter(dept -> dept.getName().equals(name))
                    .findFirst()
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.println("Department not found with name: " + name)
                    );
        }
        catch (IOException e)
        {
            System.err.println("Error loading departments: " + e.getMessage());
        }
    }

    // Getters and setters
>>>>>>> 32dafc942ac3a4fa1d6b6115aa8ac41faf18694f
    public String getName()
    {
        return name;
    }

<<<<<<< HEAD
    public String getDepartmentId()
    {
        return departmentId;
    }

    /**
     * Checks if the department has valid data.
     */
    public boolean isValid()
    {
        return name != null && !name.trim().isEmpty()
                && departmentId != null && !departmentId.trim().isEmpty();
=======
    public void setName(String name)
    {
        this.name = name;
    }

    public String getDepId()
    {
        return dep_id;
    }

    public void setDepId(String dep_id)
    {
        this.dep_id = dep_id;
>>>>>>> 32dafc942ac3a4fa1d6b6115aa8ac41faf18694f
    }

    @Override
    public String toString()
    {
<<<<<<< HEAD
        return String.format("Department: %s [%s] (Number of modules: %d)",
                name, departmentId, modules.size());
    }

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
        Department department = (Department) o;
        return Objects.equals(departmentId, department.departmentId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(departmentId);
    }

    /**
     * Searches departments by name.
     *
     * @param departments List of departments to search
     * @param searchTerm  Term to search for
     * @return List of matching departments
     */
    public static List<Department> searchByName(List<Department> departments, String searchTerm)
    {
        return departments.stream()
                .filter(d -> d.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
=======
        return "Department{" +
                "name='" + name + '\'' +
                ", dep_id='" + dep_id + '\'' +
                '}';
>>>>>>> 32dafc942ac3a4fa1d6b6115aa8ac41faf18694f
    }
}