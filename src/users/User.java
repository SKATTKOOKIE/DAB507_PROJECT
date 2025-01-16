package users;

import com.google.gson.annotations.SerializedName;

/**
 * Base class representing a user in the system.
 * Contains common attributes shared by all user types.
 */
public abstract class User implements IUser
{
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String email;
    private String department;
    private String course;

    // Getters and setters
    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String getFirstName()
    {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Override
    public String getLastName()
    {
        return lastName;
    }

    @Override
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Override
    public String getEmail()
    {
        return email;
    }

    @Override
    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public String getDepartment()
    {
        return department;
    }

    @Override
    public void setDepartment(String department)
    {
        this.department = department;
    }

    @Override
    public String getCourse()
    {
        return course;
    }

    @Override
    public void setCourse(String course)
    {
        this.course = course;
    }

    @Override
    public String toString()
    {
        return String.format("ID: %d, Name: %s %s, Email: %s, Department: %s",
                id, firstName, lastName, email, department);
    }
}