package users;

import business.DepartmentId;
import com.google.gson.Gson;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Staff extends User implements IStaffMember
{
    private String guid;
    private int weeklyHours;
    private int maxModules;
    private String avatar;
    private static List<Staff> cachedStaff = null;

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
        // Always read fresh from file
        JsonProcessor staffProcessor = new JsonProcessor(FilePathHandler.STAFF_FILE.getNormalisedPath());
        staffProcessor.processFile();

        // Create a custom GSON instance with field name mapping
        Gson gson = new com.google.gson.GsonBuilder()
                .setFieldNamingStrategy(field ->
                {
                    if (field.getName().equals("weeklyHours"))
                    {
                        return "weekly_hours";
                    }
                    if (field.getName().equals("maxModules"))
                    {
                        return "max_modules";
                    }
                    if (field.getName().equals("firstName"))
                    {
                        return "first_name";
                    }
                    if (field.getName().equals("lastName"))
                    {
                        return "last_name";
                    }
                    return field.getName();
                })
                .create();

        Staff[] allStaff = gson.fromJson(
                staffProcessor.getJsonContent().toString(),
                Staff[].class
        );

        cachedStaff = Arrays.asList(allStaff);

        // If empty department name, return all staff
        if (departmentName == null || departmentName.trim().isEmpty())
        {
            return cachedStaff;
        }

        // Otherwise filter by department name
        return cachedStaff.stream()
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