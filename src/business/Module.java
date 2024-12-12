package business;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a module in the system.
 * Contains all module-related data and operations.
 */
public class Module
{
    private final String name;
    private final String code;
    private final String acYear;

    /**
     * Constructs a Module with the specified parameters.
     *
     * @param name   Module name
     * @param code   Module code
     * @param acYear Academic year
     */
    public Module(String name, String code, String acYear)
    {
        this.name = name != null ? name.trim() : "";
        this.code = code != null ? code.trim() : "";
        this.acYear = acYear != null ? acYear.trim() : "";
    }

    /**
     * Creates a list of Modules from a JsonArray.
     *
     * @param jsonArray JsonArray containing module data
     * @return List of Module objects
     */
    public static List<Module> fromJsonArray(JsonArray jsonArray)
    {
        List<Module> modules = new ArrayList<>();

        for (JsonElement element : jsonArray)
        {
            JsonObject moduleObj = element.getAsJsonObject();

            String name = moduleObj.get("name").getAsString();
            String code = moduleObj.get("code").getAsString();
            String acYear = moduleObj.get("ac_year").getAsString();

            Module module = new Module(name, code, acYear);
            if (module.isValid())
            {
                modules.add(module);
            }
        }

        return modules;
    }

    // Getters
    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    public String getAcYear()
    {
        return acYear;
    }

    /**
     * Checks if this is a current year module.
     */
    public boolean isCurrentYear()
    {
        return acYear != null && acYear.equals("20");
    }

    /**
     * Checks if this is a previous year module.
     */
    public boolean isPreviousYear()
    {
        return acYear != null && acYear.equals("19");
    }

    /**
     * Filters a list of modules by academic year.
     */
    public static List<Module> filterByYear(List<Module> modules, String year)
    {
        return modules.stream()
                .filter(m -> year.equals(m.getAcYear()))
                .collect(Collectors.toList());
    }

    /**
     * Searches modules by name.
     */
    public static List<Module> searchByName(List<Module> modules, String searchTerm)
    {
        return modules.stream()
                .filter(m -> m.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Filters modules by code prefix.
     */
    public static List<Module> filterByCode(List<Module> modules, String codePrefix)
    {
        return modules.stream()
                .filter(m -> m.getCode().startsWith(codePrefix))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the module has valid data.
     */
    public boolean isValid()
    {
        return name != null && !name.trim().isEmpty() &&
                code != null && !code.trim().isEmpty() &&
                acYear != null && !acYear.trim().isEmpty();
    }

    @Override
    public String toString()
    {
        return String.format("%s (%s) - Year %s", name, code, acYear);
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
        Module module = (Module) o;
        return Objects.equals(name, module.name) &&
                Objects.equals(code, module.code) &&
                Objects.equals(acYear, module.acYear);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, code, acYear);
    }
}