package users;
import business.Department;
import java.util.UUID;

public abstract class User
{
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected Department department;
    protected UUID guid;

    public User(int id, String firstName, String lastName, String email, Department department, UUID guid)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.guid = guid;
    }

    public abstract String getUserType();

    // Getter methods for base properties
    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public Department getDepartment()
    {
        return department;
    }

    public UUID getGuid()
    {
        return guid;
    }
}