import business.CourseDeserializer;
import business.Department;
import business.Course;
import business.DepartmentId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import file_handling.JsonProcessor;
import file_handling.CsvProcessor;
import users.Staff;
import users.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            // Create department
            Department theatreDept = new Department("Theatre", DepartmentId.THE);

            // Get filtered users for Theatre department
            List<Student> theatreStudents = Student.getByDepartment("Theatre");
            List<Staff> theatreStaff = Staff.getByDepartment("Theatre");
            List<Course> allCourses = Course.getAll();

            // Print department information
            theatreDept.printDetailedInfo(theatreStudents, theatreStaff);

            // Get and print course information
            List<Course> theatreCourses = theatreDept.filterCoursesByDepartment(allCourses);
            theatreDept.printCourseInfo(theatreCourses);

            Department childCareDept = new Department("Childhood", DepartmentId.CHI);
            List<Course> childCareCourses = childCareDept.filterCoursesByDepartment(allCourses);
            childCareDept.printCourseInfo(childCareCourses);

            // Print summary
            theatreDept.printSummary(theatreStudents.size(), theatreStaff.size());
        }
        catch (IOException e)
        {
            System.err.println("Error processing JSON files: " + e.getMessage());
        }
    }
}