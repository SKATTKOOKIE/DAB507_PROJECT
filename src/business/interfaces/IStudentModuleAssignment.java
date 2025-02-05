package business.interfaces;

import business.StudentModuleAssignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Defines the interface for managing student-to-module assignments within the educational system.
 * This interface provides functionality for:
 * <ul>
 *   <li>Managing student module enrollments</li>
 *   <li>Loading and saving enrollment data</li>
 *   <li>Generating initial module assignments</li>
 *   <li>Tracking enrollment history and changes</li>
 *   <li>Course-specific module assignment generation</li>
 * </ul>
 * <p>
 * The interface supports both individual enrollment operations and bulk data management,
 * ensuring consistent handling of student module registrations across the system.
 */
public interface IStudentModuleAssignment
{
    /**
     * Retrieves the unique identifier of the student.
     *
     * @return The student's ID
     */
    int getStudentId();

    /**
     * Retrieves the list of module IDs the student is enrolled in.
     *
     * @return List of enrolled module IDs
     */
    List<String> getModuleIds();

    /**
     * Retrieves the timestamp of the last enrollment update.
     *
     * @return The last update timestamp as a string
     */
    String getLastUpdated();

    /**
     * Loads all student-module assignments from persistent storage.
     *
     * @return Map of student IDs to their corresponding module assignments
     * @throws IOException If there is an error reading the assignment data
     */
    static Map<Integer, StudentModuleAssignment> loadAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Saves the current student-module assignments to persistent storage.
     *
     * @param assignments Map of student IDs to their corresponding module assignments
     * @throws IOException If there is an error saving the assignment data
     */
    static void saveAssignments(Map<Integer, StudentModuleAssignment> assignments) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Generates and saves initial module assignments for all students.
     * This method should be used for system initialization or bulk enrollment processing.
     *
     * @throws IOException If there is an error generating or saving the initial assignments
     */
    static void generateInitialAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Generates initial module assignments for a specific student based on their course.
     *
     * @param studentId  The ID of the student to generate assignments for
     * @param courseCode The course code to base the module assignments on
     * @throws IOException If there is an error generating or saving the assignments
     */
    static void generateInitialAssignments(int studentId, String courseCode) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Updates the module assignments for a specific student.
     *
     * @param studentId The ID of the student to update
     * @param moduleIds List of new module IDs to assign to the student
     * @throws IOException If there is an error updating the assignments
     */
    static void updateStudentAssignments(int studentId, List<String> moduleIds) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Retrieves the current module assignments for a specific student.
     *
     * @param studentId The ID of the student
     * @return List of module IDs currently assigned to the student
     * @throws IOException If there is an error retrieving the assignments
     */
    static List<String> getStudentAssignments(int studentId) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}