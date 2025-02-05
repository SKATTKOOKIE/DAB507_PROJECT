package business;

import business.interfaces.IDepartmentSummary;
import users.Student;
import users.Staff;

import java.util.List;

/**
 * Represents a summary of a department's information within the educational institution.
 * This class provides comprehensive information about a department including its
 * staff, students, and courses.
 * <p>
 * The class offers functionality for:
 * <ul>
 *   <li>Generating basic and detailed department summaries</li>
 *   <li>Managing department statistics</li>
 *   <li>Accessing department members and courses</li>
 *   <li>Printing formatted department information</li>
 * </ul>
 */
public class DepartmentSummary implements IDepartmentSummary
{
    /**
     * Department instance variables:
     * <ul>
     *   <li>{@code departmentId} - Unique identifier and name information for the department</li>
     *   <li>{@code studentCount} - Total number of students in the department</li>
     *   <li>{@code staffCount} - Total number of staff members in the department</li>
     *   <li>{@code students} - List of all students enrolled in the department</li>
     *   <li>{@code staff} - List of all staff members working in the department</li>
     *   <li>{@code courses} - List of all courses offered by the department</li>
     * </ul>
     */
    private final DepartmentId departmentId;
    private final int studentCount;
    private final int staffCount;
    private final List<Student> students;
    private final List<Staff> staff;
    private final List<Course> courses;

    /**
     * Constructs a new DepartmentSummary instance with complete department information.
     *
     * @param departmentId Unique identifier for the department
     * @param studentCount Total number of students
     * @param staffCount   Total number of staff members
     * @param students     List of all students in the department
     * @param staff        List of all staff members in the department
     * @param courses      List of all courses offered by the department
     */
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

    /**
     * Generates a basic summary of the department including name and member counts.
     *
     * @return A formatted string containing the basic department summary
     */
    public String getBasicSummary()
    {
        return String.format("Department Summary for %s (%s):%n" +
                        "Total Students: %d%n" +
                        "Total Staff: %d%n",
                departmentId.getDepartmentName(), departmentId, studentCount, staffCount);
    }

    /**
     * Generates detailed information about the department including lists of all
     * students, staff members, and courses.
     *
     * @return A formatted string containing comprehensive department information
     */
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

    /**
     * Prints the basic department summary to the console.
     */
    public void printSummary()
    {
        System.out.println(getBasicSummary());
    }

    /**
     * Prints detailed department information to the console.
     */
    public void printDetailedInfo()
    {
        System.out.println(getDetailedInfo());
    }

    /**
     * @return The department's unique identifier
     */
    public DepartmentId getDepartmentId()
    {
        return departmentId;
    }

    /**
     * @return The total number of students in the department
     */
    public int getStudentCount()
    {
        return studentCount;
    }

    /**
     * @return The total number of staff members in the department
     */
    public int getStaffCount()
    {
        return staffCount;
    }
}