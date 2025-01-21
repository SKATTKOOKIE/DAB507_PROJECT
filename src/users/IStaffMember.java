package users;

public interface IStaffMember
{
    String getGuid();

    void setGuid(String guid);

    int getWeeklyHours();

    void setWeeklyHours(int hours);

    int getMaxModules();

    void setMaxModules(int modules);
}
