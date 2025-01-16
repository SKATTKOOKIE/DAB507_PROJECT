package users;

import com.google.gson.Gson;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a student user in the system.
 * Extends User class with student-specific attributes.
 */
public class Student extends User implements IStudent
{
    private String gender;
    private String type;

    // Getters and setters for student-specific fields
    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return super.toString() + String.format(", Gender: %s, Type: %s",
                gender, type);
    }

    public static List<Student> getByCourse(String courseName) throws IOException
    {
        var studentProcessor = new JsonProcessor("data/students.json");
        try
        {
            studentProcessor.processFile();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        Student[] allStudents = new Gson().fromJson(
                studentProcessor.getJsonContent().toString(),
                Student[].class
        );

        // If courseName is empty, return all students
        if (courseName == null || courseName.trim().isEmpty())
        {
            return Arrays.asList(allStudents);
        }

        // Otherwise filter by course name
        return Arrays.stream(allStudents)
                .filter(student -> courseName.equals(student.getCourse()))
                .collect(Collectors.toList());
    }

    @Override
    public void printDetailedInfo()
    {
        System.out.printf("- %s %s (ID: %d)\n",
                getFirstName(),
                getLastName(),
                getId());
        System.out.printf("  Type: %s\n", getType());
        System.out.printf("  Email: %s\n", getEmail());
        System.out.println();
    }
}