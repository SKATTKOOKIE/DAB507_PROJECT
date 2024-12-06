package users;
import business.Department;
import java.util.UUID;

public abstract class Student extends User
{
    protected String gender;
    protected StudentType type;

    public Student(int id, String firstName, String lastName, String email, Department department,
                   UUID guid, String gender, StudentType type)
    {
        super(id, firstName, lastName, email, department, guid);
        this.gender = gender;
        this.type = type;
    }
}