package business.interfaces;

import com.google.gson.JsonArray;

import java.io.IOException;
import java.util.List;

public interface IModule
{
    String getName();

    String getCode();

    String getAcYear();

    List<String> getAssociatedCourses();

    boolean isAssociatedWithCourse(String courseCode);

    boolean isValid();

    static List<Module> fromJsonArray(JsonArray jsonArray)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<Module> getModulesForCourse(String courseCode) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<Module> getAll() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<Module> filterByYear(List<Module> modules, String year)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<Module> searchByName(List<Module> modules, String searchTerm)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<Module> filterByCode(List<Module> modules, String codePrefix)
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static Module getModuleByCode(String code) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}