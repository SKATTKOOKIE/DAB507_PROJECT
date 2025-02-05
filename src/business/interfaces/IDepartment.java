package business.interfaces;

import business.Course;
import business.DepartmentId;
import business.DepartmentSummary;
import users.Student;
import users.Staff;

import java.util.List;

import business.Module;

/**
 * Interface defining core functionality for Department management.
 */
public interface IDepartment
{
    // Core methods
    DepartmentId getDepartmentId();

    String getName();

    void addModule(Module module);

    List<Module> getModules();

    List<Module> getModulesByYear(String year);

    List<Module> searchModulesByName(String searchTerm);

    List<Module> getModulesByCode(String codePrefix);

    List<Course> getCourses(List<Course> allCourses);

    DepartmentSummary createSummary(List<Student> students, List<Staff> staff, List<Course> courses);

    boolean isValid();

    // Object class method overrides
    boolean equals(Object o);

    int hashCode();

    String toString();
}