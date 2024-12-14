package users;

import com.google.gson.annotations.SerializedName;
import users.User;

/**
 * Represents a staff member in the system.
 * Extends User class with staff-specific attributes.
 */
public class Staff extends User
{
    private String guid;
    @SerializedName("weekly_hours")
    private int weeklyHours;
    @SerializedName("max_modules")
    private int maxModules;
    private String avatar;

    // Getters and setters for staff-specific fields
    public String getGuid()
    {
        return guid;
    }

    public void setGuid(String guid)
    {
        this.guid = guid;
    }

    public int getWeeklyHours()
    {
        return weeklyHours;
    }

    public void setWeeklyHours(int weeklyHours)
    {
        this.weeklyHours = weeklyHours;
    }

    public int getMaxModules()
    {
        return maxModules;
    }

    public void setMaxModules(int maxModules)
    {
        this.maxModules = maxModules;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    @Override
    public String toString()
    {
        return super.toString() + String.format(
                ", GUID: %s, Weekly Hours: %d, Max Modules: %d",
                guid, weeklyHours, maxModules);
    }
}