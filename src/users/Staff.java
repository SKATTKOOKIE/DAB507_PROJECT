package users;

import business.DepartmentId;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Staff extends User implements IStaffMember
{
    private String guid;
    @SerializedName("weekly_hours")
    private int weeklyHours;
    @SerializedName("max_modules")
    private int maxModules;
    private String avatar;

    // Add methods to work with DepartmentId
    public DepartmentId getDepartmentId()
    {
        String deptName = getDepartment();
        if (deptName == null || deptName.isEmpty())
        {
            return DepartmentId.UNKNOWN;
        }

        // Try to find matching department
        return Arrays.stream(DepartmentId.values())
                .filter(dept -> dept.getDepartmentName().equals(deptName))
                .findFirst()
                .orElse(DepartmentId.UNKNOWN);
    }

    public void setDepartmentId(DepartmentId departmentId)
    {
        setDepartment(departmentId.getDepartmentName());
    }

    // Original getters and setters
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

    public static List<Staff> getByDepartment(String departmentName) throws IOException
    {
        JsonProcessor staffProcessor = new JsonProcessor("data/staff.json");
        staffProcessor.processFile();
        Staff[] allStaff = new Gson().fromJson(
                staffProcessor.getJsonContent().toString(),
                Staff[].class
        );

        // If empty department name, return all staff
        if (departmentName == null || departmentName.trim().isEmpty())
        {
            return Arrays.asList(allStaff);
        }

        // Otherwise filter by department name
        return Arrays.stream(allStaff)
                .filter(staff -> departmentName.equals(staff.getDepartment()))
                .collect(Collectors.toList());
    }

    // Add a new method to get staff by DepartmentId
    public static List<Staff> getByDepartmentId(DepartmentId departmentId) throws IOException
    {
        if (departmentId == DepartmentId.UNKNOWN)
        {
            return getByDepartment("");
        }
        return getByDepartment(departmentId.getDepartmentName());
    }

    @Override
    public void printDetailedInfo()
    {
        System.out.printf("- %s %s (ID: %d)\n",
                getFirstName(),
                getLastName(),
                getId());
        System.out.printf("  Department: %s\n", getDepartmentId().getDepartmentName());
        System.out.printf("  Weekly Hours: %d\n", getWeeklyHours());
        System.out.printf("  Max Modules: %d\n", getMaxModules());
        System.out.printf("  Email: %s\n", getEmail());
        System.out.println();
    }
}