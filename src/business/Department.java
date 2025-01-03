package business;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import users.Student;
import users.Staff;

/**
 * Represents a department in the system.
 * Contains all department-related data and operations.
 */
public class Department
{
    private final String name;
    private final DepartmentId departmentId;
    private List<Module> modules;

    /**
     * Constructs a Department with the specified parameters.
     *
     * @param name         Department name
     * @param departmentId Department identifier
     */
    public Department(String name, DepartmentId departmentId)
    {  // Updated constructor
        this.name = name != null ? name.trim() : "";
        this.departmentId = departmentId != null ? departmentId : DepartmentId.UNKNOWN;
        this.modules = new ArrayList<>();
    }

    public DepartmentId getDepartmentId()
    {
        return departmentId;
    }

    public String getDepartmentIdString()
    {
        return departmentId.toString();
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
        }
    }

    /**
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
    public String getName()
    {
        return name;
    }

    /**
     * Checks if the department has valid data.
     */
    public boolean isValid()
    {
        return name != null && !name.trim().isEmpty()
                && departmentId != null && departmentId != DepartmentId.UNKNOWN;
    }

    @Override
    public String toString()
    {
        return String.format("Department: %s [%s] (Number of modules: %d)",
                name, departmentId.toString(), modules.size());
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
    }

    public void printSummary(int studentCount, int staffCount)
    {
        System.out.println("\nDepartment Summary:");
        System.out.println("==================");
        System.out.println("Department: " + name + " [" + departmentId + "]");
        System.out.println("Total Students: " + studentCount);
        System.out.println("Total Staff: " + staffCount);
    }

    public void printDetailedInfo(List<Student> students, List<Staff> staff)
    {
        System.out.println("\nDetailed " + name + " Department Information");
        System.out.println("=".repeat(name.length() + 30));

        // Print student details
        System.out.println("\nStudents:");
        students.forEach(Student::printDetailedInfo);

        // Print staff details
        System.out.println("Staff:");
        staff.forEach(Staff::printDetailedInfo);
    }

    public List<Course> filterCoursesByDepartment(List<Course> allCourses)
    {
        return allCourses.stream()
                .filter(course -> this.departmentId.equals(course.getDepartmentId()))
                .collect(Collectors.toList());
    }

    public String printCourseInfo(List<Course> courses)
    {
        System.out.println("\nCourses for " + name + " Department:");
        System.out.println("========");
        courses.forEach(Course::printInfo);
        System.out.println();
        return null;
    }

    public String getDetailedInfo(List<Student> students, List<Staff> staff) {
        StringBuilder info = new StringBuilder();
        info.append("Department: ").append(name).append("\n");
        info.append("Department ID: ").append(departmentId).append("\n\n");

        info.append("Students:\n");
        for (Student student : students) {
            info.append("- ").append(student.toString()).append("\n");
        }

        info.append("\nStaff:\n");
        for (Staff staffMember : staff) {
            info.append("- ").append(staffMember.toString()).append("\n");
        }

        return info.toString();
    }

    public String getSummary(int studentCount, int staffCount) {
        return String.format("Department Summary for %s (%s):\n" +
                        "Total Students: %d\n" +
                        "Total Staff: %d\n",
                name, departmentId, studentCount, staffCount);
    }

    public String getCourseInfo(List<Course> courses) {
        StringBuilder info = new StringBuilder();
        info.append("Courses for ").append(name).append(" Department:\n");
        for (Course course : courses) {
            info.append("- ").append(course.toString()).append("\n");
        }
        return info.toString();
    }

}