package users;
import business.Department;
import java.util.UUID;

public abstract class Staff extends User
{
    protected int weeklyHours;
    protected int maxModules;
    protected String avatar;

    public Staff(int id, String firstName, String lastName, String email, Department department,
                 UUID guid, int weeklyHours, int maxModules, String avatar)
    {
        super(id, firstName, lastName, email, department, guid);
        this.weeklyHours = weeklyHours;
        this.maxModules = maxModules;
        this.avatar = avatar;
    }

    public int getWeeklyHours()
    {
        return weeklyHours;
    }
}