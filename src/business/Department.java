package business;

import java.io.IOException;
import java.util.List;

import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.List;

/**
 * Represents a department with a name and department ID.
 * Includes static methods for loading departments from JSON.
 */
public class Department
{
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
        }
    }

    /**
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
    public String getName()
    {
        return name;
    }

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
    }

    @Override
    public String toString()
    {
        return "Department{" +
                "name='" + name + '\'' +
                ", dep_id='" + dep_id + '\'' +
                '}';
    }
}