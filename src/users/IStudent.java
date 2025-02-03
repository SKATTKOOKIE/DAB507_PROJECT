package users;

import java.io.IOException;
import java.util.List;

/**
 * Interface defining the contract for student-specific functionality.
 * This interface extends the base functionality provided by IUser with
 * methods specific to student management, such as gender and student type.
 * Classes implementing this interface must provide both the student-specific
 * functionality defined here and the base user functionality from IUser.
 *
 * @see IUser
 * @see Student
 * @see StudentType
 */
public interface IStudent extends IUser
{

    /**
     * Gets the student's gender.
     *
     * @return The gender of the student
     */
    String getGender();

    /**
     * Sets the student's gender.
     *
     * @param gender The gender to set for the student
     */
    void setGender(String gender);

    /**
     * Gets the student's type (e.g., full-time, part-time, international, or DA).
     *
     * @return The type of the student
     * @see StudentType for valid student types:
     * FULL_TIME ("Full time"),
     * PART_TIME ("Part time"),
     * INTERNATIONAL ("International"),
     * DA ("DA")
     */
    String getType();

    /**
     * Sets the student's type.
     *
     * @param type The type to set for the student. Should be one of the valid
     *             types as defined in StudentType enum
     * @see StudentType for valid student types
     */
    void setType(String type);

    /**
     * Returns a string representation of the student including all base user
     * information plus student-specific attributes.
     *
     * @return A string containing all student information in the format:
     * "[base user info], Gender: [gender], Type: [type]"
     */
    @Override
    String toString();

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
    static List<Student> getByCourse(String courseName) throws IOException
    {
        throw new UnsupportedOperationException("This method should be implemented by a concrete class");
    }
}