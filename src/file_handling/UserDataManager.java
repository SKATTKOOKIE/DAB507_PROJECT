package file_handling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.JsonElement;
import users.Staff;
import users.Student;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDataManager
{
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static void addStudent(Student newStudent) throws IOException
    {
        // Read existing students
        JsonProcessor processor = new JsonProcessor(FilePathHandler.STUDENTS_FILE.getNormalisedPath());
        processor.processFile();
        JsonElement content = (JsonElement) processor.getJsonContent();

        // Convert existing JSON to List of Students
        List<Student> students = new ArrayList<>();
        if (content.isJsonArray())
        {
            Student[] existingStudents = gson.fromJson(content, Student[].class);
            students.addAll(Arrays.asList(existingStudents));
        }

        // Add new student
        students.add(newStudent);

        // Write back to file
        try (FileWriter writer = new FileWriter(FilePathHandler.STUDENTS_FILE.getNormalisedPath()))
        {
            gson.toJson(students, writer);
        }
    }

    public static void addStaff(Staff newStaff) throws IOException
    {
        // Read existing staff
        JsonProcessor processor = new JsonProcessor(FilePathHandler.STAFF_FILE.getNormalisedPath());
        processor.processFile();
        JsonElement content = (JsonElement) processor.getJsonContent();

        // Convert existing JSON to List of Staff
        List<Staff> staffList = new ArrayList<>();
        if (content.isJsonArray())
        {
            Staff[] existingStaff = gson.fromJson(content, Staff[].class);
            staffList.addAll(Arrays.asList(existingStaff));
        }

        // Add new staff member
        staffList.add(newStaff);

        // Write back to file
        try (FileWriter writer = new FileWriter(FilePathHandler.STAFF_FILE.getNormalisedPath()))
        {
            gson.toJson(staffList, writer);
        }
    }

    public static void validateUser(Object user) throws IllegalArgumentException
    {
        if (user instanceof Student)
        {
            Student student = (Student) user;
            if (isNullOrEmpty(student.getFirstName()) ||
                    isNullOrEmpty(student.getLastName()) ||
                    isNullOrEmpty(student.getEmail()) ||
                    isNullOrEmpty(student.getType()) ||
                    isNullOrEmpty(student.getGender()) ||
                    isNullOrEmpty(student.getCourse()))
            {
                throw new IllegalArgumentException("All student fields must be filled, including course selection");
            }
        }
        else if (user instanceof Staff)
        {
            Staff staff = (Staff) user;
            if (isNullOrEmpty(staff.getFirstName()) ||
                    isNullOrEmpty(staff.getLastName()) ||
                    isNullOrEmpty(staff.getEmail()) ||
                    isNullOrEmpty(staff.getGuid()) ||
                    staff.getWeeklyHours() <= 0 ||
                    staff.getMaxModules() <= 0)
            {
                throw new IllegalArgumentException("All staff fields must be filled and numeric values must be greater than 0");
            }
        }
    }

    private static boolean isNullOrEmpty(String str)
    {
        return str == null || str.trim().isEmpty();
    }
}