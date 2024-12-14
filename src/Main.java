import business.Department;
import com.google.gson.Gson;
import file_handling.JsonProcessor;
import users.Staff;
import users.Student;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            // Create department
            Department theatreDept = new Department("Theatre", "THE001");

            // Get filtered users for Theatre department
            List<Student> theatreStudents = getStudentsByDepartment("Theatre");
            List<Staff> theatreStaff = getStaffByDepartment("Theatre");

            // Print department information using helper function
            printDetailedDepartmentInfo(theatreStudents, theatreStaff, theatreDept.getName());

            // Print summary
            printDepartmentSummary(theatreDept, theatreStudents.size(), theatreStaff.size());

        } catch (IOException e) {
            System.err.println("Error processing JSON files: " + e.getMessage());
        }
    }

    /**
     * Gets students filtered by department
     * @param departmentName Name of the department to filter by
     * @return List of filtered students
     * @throws IOException if file processing fails
     */
    private static List<Student> getStudentsByDepartment(String departmentName) throws IOException {
        JsonProcessor studentProcessor = new JsonProcessor("data/students.json");
        studentProcessor.processFile();
        Student[] allStudents = new Gson().fromJson(
                studentProcessor.getJsonContent().toString(),
                Student[].class
        );

        return Arrays.stream(allStudents)
                .filter(student -> departmentName.equals(student.getDepartment()))
                .collect(Collectors.toList());
    }

    /**
     * Gets staff filtered by department
     * @param departmentName Name of the department to filter by
     * @return List of filtered staff
     * @throws IOException if file processing fails
     */
    private static List<Staff> getStaffByDepartment(String departmentName) throws IOException {
        JsonProcessor staffProcessor = new JsonProcessor("data/staff.json");
        staffProcessor.processFile();
        Staff[] allStaff = new Gson().fromJson(
                staffProcessor.getJsonContent().toString(),
                Staff[].class
        );

        return Arrays.stream(allStaff)
                .filter(staff -> departmentName.equals(staff.getDepartment()))
                .collect(Collectors.toList());
    }

    /**
     * Prints summary information for a department
     * @param department Department object
     * @param studentCount Number of students
     * @param staffCount Number of staff
     */
    private static void printDepartmentSummary(Department department, int studentCount, int staffCount) {
        System.out.println("\nDepartment Summary:");
        System.out.println("==================");
        System.out.println("Department: " + department.getName() + " [" + department.getDepartmentId() + "]");
        System.out.println("Total Students: " + studentCount);
        System.out.println("Total Staff: " + staffCount);
    }

    /**
     * Helper method to print detailed user information
     * @param students List of students to print
     * @param staff List of staff to print
     * @param department Department name
     */
    private static void printDetailedDepartmentInfo(List<Student> students, List<Staff> staff, String department) {
        System.out.println("\nDetailed " + department + " Department Information");
        System.out.println("=".repeat(department.length() + 30));

        // Print student details
        System.out.println("\nStudents:");
        students.forEach(student -> {
            System.out.printf("- %s %s (ID: %d)\n",
                    student.getFirstName(),
                    student.getLastName(),
                    student.getId());
            System.out.printf("  Type: %s\n", student.getType());
            System.out.printf("  Email: %s\n", student.getEmail());
            System.out.println();
        });

        // Print staff details
        System.out.println("Staff:");
        staff.forEach(staffMember -> {
            System.out.printf("- %s %s (ID: %d)\n",
                    staffMember.getFirstName(),
                    staffMember.getLastName(),
                    staffMember.getId());
            System.out.printf("  Weekly Hours: %d\n", staffMember.getWeeklyHours());
            System.out.printf("  Max Modules: %d\n", staffMember.getMaxModules());
            System.out.printf("  Email: %s\n", staffMember.getEmail());
            System.out.println();
        });
    }
}