package users;

import com.google.gson.annotations.SerializedName;

/**
 * Abstract base class representing a user in the system.
 * Contains common attributes and functionality shared by all user types.
 * This class implements the IUser interface and provides the core implementation
 * for user management in the system.
 *
 * @see IUser
 */
public abstract class User implements IUser
{

    /**
     * Unique identifier for the user
     */
    private int id;

    /**
     * User's first name.
     * Mapped to "first_name" in JSON serialization.
     */
    @SerializedName("first_name")
    private String firstName;

    /**
     * User's last name.
     * Mapped to "last_name" in JSON serialization.
     */
    @SerializedName("last_name")
    private String lastName;

    /**
     * User's email address
     */
    private String email;

    /**
     * Department the user is associated with
     */
    private String department;

    /**
     * Course the user is associated with
     */
    private String course;

    /**
     * Gets the user's unique identifier.
     *
     * @return The unique ID of the user
     */
    @Override
    public int getId()
    {
        return id;
    }

    /**
     * Sets the user's unique identifier.
     *
     * @param id The unique ID to set for the user
     */
    @Override
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gets the user's first name.
     *
     * @return The first name of the user
     */
    @Override
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName The first name to set for the user
     */
    @Override
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return The last name of the user
     */
    @Override
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName The last name to set for the user
     */
    @Override
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * Gets the user's email address.
     *
     * @return The email address of the user
     */
    @Override
    public String getEmail()
    {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email The email address to set for the user
     */
    @Override
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Gets the department associated with the user.
     *
     * @return The department name of the user
     */
    @Override
    public String getDepartment()
    {
        return department;
    }

    /**
     * Sets the department associated with the user.
     *
     * @param department The department name to set for the user
     */
    @Override
    public void setDepartment(String department)
    {
        this.department = department;
    }

    /**
     * Gets the course associated with the user.
     *
     * @return The course name of the user
     */
    @Override
    public String getCourse()
    {
        return course;
    }

    /**
     * Sets the course associated with the user.
     *
     * @param course The course name to set for the user
     */
    @Override
    public void setCourse(String course)
    {
        this.course = course;
    }

    /**
     * Returns a string representation of the user.
     * Includes all base user information in a human-readable format.
     *
     * @return A string containing the user's ID, full name, email, and department
     * in the format: "ID: [id], Name: [firstName] [lastName], Email: [email], Department: [department]"
     */
    @Override
    public String toString()
    {
        return String.format("ID: %d, Name: %s %s, Email: %s, Department: %s",
                id, firstName, lastName, email, department);
    }
}