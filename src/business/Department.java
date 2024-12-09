package business;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a department in the system.
 * Contains all department-related data and operations.
 */
public class Department {
    private final String name;
    private List<Module> modules;

    /**
     * Constructs a Department with the specified parameters.
     * @param name Department name
     */
    public Department(String name) {
        this.name = name != null ? name.trim() : "";
        this.modules = new ArrayList<>();
    }

    /**
     * Creates a list of Departments from a JsonArray.
     * @param jsonArray JsonArray containing department data
     * @return List of Department objects
     */
    public static List<Department> fromJsonArray(JsonArray jsonArray) {
        List<Department> departments = new ArrayList<>();

        for (JsonElement element : jsonArray) {
            JsonObject departmentObj = element.getAsJsonObject();
            String name = departmentObj.get("name").getAsString();

            Department department = new Department(name);
            if (department.isValid()) {
                departments.add(department);
            }
        }

        return departments;
    }

    /**
     * Adds a module to this department.
     * @param module Module to add
     */
    public void addModule(Module module) {
        if (module != null) {
            modules.add(module);
        }
    }

    /**
     * Gets all modules in this department.
     * @return List of modules
     */
    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }

    /**
     * Gets modules filtered by academic year.
     * @param year Academic year to filter by
     * @return Filtered list of modules
     */
    public List<Module> getModulesByYear(String year) {
        return Module.filterByYear(modules, year);
    }

    /**
     * Searches for modules by name within this department.
     * @param searchTerm Term to search for
     * @return List of matching modules
     */
    public List<Module> searchModulesByName(String searchTerm) {
        return Module.searchByName(modules, searchTerm);
    }

    /**
     * Filters modules by code prefix within this department.
     * @param codePrefix Code prefix to filter by
     * @return Filtered list of modules
     */
    public List<Module> getModulesByCode(String codePrefix) {
        return Module.filterByCode(modules, codePrefix);
    }

    // Getters
    public String getName() {
        return name;
    }

    /**
     * Checks if the department has valid data.
     */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("Department: %s (Number of modules: %d)", name, modules.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department department = (Department) o;
        return Objects.equals(name, department.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Searches departments by name.
     * @param departments List of departments to search
     * @param searchTerm Term to search for
     * @return List of matching departments
     */
    public static List<Department> searchByName(List<Department> departments, String searchTerm) {
        return departments.stream()
                .filter(d -> d.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
}