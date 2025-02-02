package business;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import business.interfaces.IDepartment;
import users.Student;
import users.Staff;

public class Department implements IDepartment
{
    private final DepartmentId departmentId;
    private List<Module> modules;

    public Department(DepartmentId departmentId)
    {
        this.departmentId = departmentId != null ? departmentId : DepartmentId.UNKNOWN;
        this.modules = new ArrayList<>();
    }

    public Department(String departmentName)
    {
        this(DepartmentId.fromString(departmentName));
    }

    public DepartmentId getDepartmentId()
    {
        return departmentId;
    }

    public String getName()
    {
        return departmentId.getDepartmentName();
    }

    public void addModule(Module module)
    {
        if (module != null)
        {
            modules.add(module);
        }
    }

    public List<Module> getModules()
    {
        return new ArrayList<>(modules);
    }

    public List<Module> getModulesByYear(String year)
    {
        return Module.filterByYear(modules, year);
    }

    public List<Module> searchModulesByName(String searchTerm)
    {
        return Module.searchByName(modules, searchTerm);
    }

    public List<Module> getModulesByCode(String codePrefix)
    {
        return Module.filterByCode(modules, codePrefix);
    }

    public List<Course> getCourses(List<Course> allCourses)
    {
        return allCourses.stream()
                .filter(course -> this.departmentId.equals(course.getDepartmentId()))
                .collect(Collectors.toList());
    }

    public DepartmentSummary createSummary(List<Student> students, List<Staff> staff, List<Course> courses)
    {
        return new DepartmentSummary(
                this.departmentId,
                students.size(),
                staff.size(),
                students,
                staff,
                courses
        );
    }

    public boolean isValid()
    {
        return departmentId != DepartmentId.UNKNOWN;
    }

    @Override
    public String toString()
    {
        return String.format("Department: %s [%s] (Number of modules: %d)",
                getName(), departmentId.toString(), modules.size());
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
        Department that = (Department) o;
        return Objects.equals(departmentId, that.departmentId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(departmentId);
    }

    public static List<Department> searchByName(List<Department> departments, String searchTerm)
    {
        return departments.stream()
                .filter(d -> d.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }
}