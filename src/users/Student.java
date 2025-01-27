package users;

import com.google.gson.Gson;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a student user in the system.
 * Extends the base User class with student-specific attributes and functionality.
 * Students have additional properties such as gender and type (e.g., full-time, part-time).
 * This class provides methods to manage student data and retrieve students by course.
 *
 * @see User
 * @see IStudent
 */
public class Student extends User implements IStudent
{

    /**
     * The gender of the student.
     * This field stores the student's gender identification.
     */
    private String gender;

    /**
     * The type of student (e.g., full-time, part-time, international).
     * This field indicates the student's enrollment status or category.
     *
     * @see StudentType for valid student types
     */
    private String type;

    /**
     * Gets the student's gender.
     *
     * @return The gender of the student
     */
    public String getGender()
    {
        return gender;
    }

    /**
     * Sets the student's gender.
     *
     * @param gender The gender to set for the student
     */
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    /**
     * Gets the student's type (e.g., full-time, part-time, international).
     *
     * @return The type of the student
     * @see StudentType for valid student types
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the student's type.
     *
     * @param type The type to set for the student
     * @see StudentType for valid student types
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns a string representation of the student.
     * Includes all base user information plus student-specific attributes.
     *
     * @return A string containing all student information in the format:
     * "[base user info], Gender: [gender], Type: [type]"
     */
    @Override
    public String toString()
    {
        return super.toString() + String.format(", Gender: %s, Type: %s",
                gender, type);
    }

    /**
     * Retrieves a list of students filtered by course name.
     * Loads student data from a JSON file and filters based on the provided course name.
     * If no course name is provided, returns all students.
     *
     * @param courseName The name of the course to filter by, or empty/null for all students
     * @return A list of students enrolled in the specified course, or all students if no course specified
     * @throws IOException      If there is an error reading or processing the student data file
     * @throws RuntimeException If there is an error processing the JSON file
     */
    public static List<Student> getByCourse(String courseName) throws IOException
    {
        var studentProcessor = new JsonProcessor(FilePathHandler.STUDENTS_FILE.getNormalisedPath());
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

    /**
     * Prints detailed information about the student to the console.
     * Includes the student's name, ID, type, and email address.
     * This method is primarily used for debugging and administrative purposes.
     */
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