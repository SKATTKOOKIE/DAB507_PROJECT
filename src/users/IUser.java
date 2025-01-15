package users;

public interface IUser
{
    int getId();

    void setId(int id);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String email);

    String getDepartment();

    void setDepartment(String department);

    void printDetailedInfo();
}
