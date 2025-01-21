package users;

/**
 * Interface defining the base contract for all users in the system.
 * Provides methods for managing common user attributes such as ID, name,
 * email, department, and course information. This interface serves as the
 * foundation for specific user types (e.g., students, staff) in the system.
 */
public interface IUser
{

    /**
     * Gets the user's unique identifier.
     *
     * @return The unique ID of the user
     */
    int getId();

    /**
     * Sets the user's unique identifier.
     *
     * @param id The unique ID to set for the user
     */
    void setId(int id);

    /**
     * Gets the user's first name.
     *
     * @return The first name of the user
     */
    String getFirstName();

    /**
     * Sets the user's first name.
     *
     * @param firstName The first name to set for the user
     */
    void setFirstName(String firstName);

    /**
     * Gets the user's last name.
     *
     * @return The last name of the user
     */
    String getLastName();

    /**
     * Sets the user's last name.
     *
     * @param lastName The last name to set for the user
     */
    void setLastName(String lastName);

    /**
     * Gets the user's email address.
     *
     * @return The email address of the user
     */
    String getEmail();

    /**
     * Sets the user's email address.
     *
     * @param email The email address to set for the user
     */
    void setEmail(String email);

    /**
     * Gets the department associated with the user.
     *
     * @return The department name of the user
     */
    String getDepartment();

    /**
     * Sets the department associated with the user.
     *
     * @param department The department name to set for the user
     */
    void setDepartment(String department);

    /**
     * Gets the course associated with the user.
     *
     * @return The course name of the user
     */
    String getCourse();

    /**
     * Sets the course associated with the user.
     *
     * @param course The course name to set for the user
     */
    void setCourse(String course);

    /**
     * Prints detailed information about the user.
     * This method should output all relevant user information
     * in a human-readable format, typically used for debugging
     * or administrative purposes.
     */
    void printDetailedInfo();
}