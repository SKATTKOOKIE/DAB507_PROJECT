package business.interfaces;

import business.DepartmentId;

/**
 * Defines the interface for generating and accessing department summary information.
 * This interface provides functionality for:
 * <ul>
 *   <li>Generating basic and detailed department summaries</li>
 *   <li>Displaying department information</li>
 *   <li>Accessing department statistics and identifiers</li>
 * </ul>
 * <p>
 * Implementing classes should provide both concise and comprehensive views
 * of department information, suitable for different reporting needs.
 */
public interface IDepartmentSummary
{
    /**
     * Generates a basic summary of the department's information.
     * This typically includes the department name and key statistics.
     *
     * @return A formatted string containing the basic department summary
     */
    String getBasicSummary();

    /**
     * Generates detailed information about the department.
     * This includes comprehensive information about students, staff,
     * courses, and other relevant department details.
     *
     * @return A formatted string containing detailed department information
     */
    String getDetailedInfo();

    /**
     * Prints the basic department summary to the standard output.
     * This method should format and display the basic summary in a
     * user-friendly manner.
     */
    void printSummary();

    /**
     * Prints detailed department information to the standard output.
     * This method should format and display comprehensive department
     * information in a structured and readable format.
     */
    void printDetailedInfo();

    /**
     * Retrieves the unique identifier for the department.
     *
     * @return The department's unique identifier
     */
    DepartmentId getDepartmentId();

    /**
     * Retrieves the total number of students in the department.
     *
     * @return The count of students enrolled in the department
     */
    int getStudentCount();

    /**
     * Retrieves the total number of staff members in the department.
     *
     * @return The count of staff members working in the department
     */
    int getStaffCount();
}