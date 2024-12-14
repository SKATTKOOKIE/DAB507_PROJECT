package users;

import users.User;

/**
 * Represents a student user in the system.
 * Extends User class with student-specific attributes.
 */
public class Student extends User
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
}