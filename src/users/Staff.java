package users;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a staff member in the system.
 * Extends AbstractUser class with staff-specific attributes.
 */
public class Staff extends User implements IStaffMember {
    private String guid;
    @SerializedName("weekly_hours")
    private int weeklyHours;
    @SerializedName("max_modules")
    private int maxModules;
    private String avatar;

    // Getters and setters for staff-specific fields
    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public int getMaxModules() {
        return maxModules;
    }

    public void setMaxModules(int maxModules) {
        this.maxModules = maxModules;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                ", GUID: %s, Weekly Hours: %d, Max Modules: %d",
                guid, weeklyHours, maxModules);
    }

    public static List<Staff> getByDepartment(String departmentName) throws IOException {
        JsonProcessor staffProcessor = new JsonProcessor("data/staff.json");
        staffProcessor.processFile();
        Staff[] allStaff = new Gson().fromJson(
                staffProcessor.getJsonContent().toString(),
                Staff[].class
        );

        return Arrays.stream(allStaff)
                .filter(staff -> departmentName.equals(staff.getDepartment()))
                .collect(Collectors.toList());
    }

    @Override
    public void printDetailedInfo() {
        System.out.printf("- %s %s (ID: %d)\n",
                getFirstName(),
                getLastName(),
                getId());
        System.out.printf("  Weekly Hours: %d\n", getWeeklyHours());
        System.out.printf("  Max Modules: %d\n", getMaxModules());
        System.out.printf("  Email: %s\n", getEmail());
        System.out.println();
    }
}