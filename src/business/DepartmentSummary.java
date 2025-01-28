package business;

import users.Student;
import users.Staff;

import java.util.List;

public class DepartmentSummary
{
    private final DepartmentId departmentId;
    private final int studentCount;
    private final int staffCount;
    private final List<Student> students;
    private final List<Staff> staff;
    private final List<Course> courses;

    public DepartmentSummary(DepartmentId departmentId, int studentCount, int staffCount,
                             List<Student> students, List<Staff> staff, List<Course> courses)
    {
        this.departmentId = departmentId;
        this.studentCount = studentCount;
        this.staffCount = staffCount;
        this.students = students;
        this.staff = staff;
        this.courses = courses;
    }

    public String getBasicSummary()
    {
        return String.format("Department Summary for %s (%s):%n" +
                        "Total Students: %d%n" +
                        "Total Staff: %d%n",
                departmentId.getDepartmentName(), departmentId, studentCount, staffCount);
    }

    public String getDetailedInfo()
    {
        StringBuilder info = new StringBuilder();
        info.append("Department: ").append(departmentId.getDepartmentName()).append("\n");
        info.append("Department ID: ").append(departmentId).append("\n\n");

        info.append("Students:\n");
        students.forEach(student -> info.append("- ").append(student.toString()).append("\n"));

        info.append("\nStaff:\n");
        staff.forEach(staffMember -> info.append("- ").append(staffMember.toString()).append("\n"));

        info.append("\nCourses:\n");
        courses.forEach(course -> info.append("- ").append(course.toString()).append("\n"));

        return info.toString();
    }

    public void printSummary()
    {
        System.out.println(getBasicSummary());
    }

    public void printDetailedInfo()
    {
        System.out.println(getDetailedInfo());
    }
}