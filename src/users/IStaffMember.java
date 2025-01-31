package users;

import business.DepartmentId;

import java.io.IOException;
import java.util.List;

/**
 * Interface defining the contract for staff-specific functionality.
 * This interface extends the base functionality provided by IUser with
 * methods specific to staff management, such as GUID, weekly hours,
 * and maximum modules.
 */
public interface IStaffMember extends IUser
{
    /**
     * Gets the staff member's GUID.
     *
     * @return The GUID of the staff member
     */
    String getGuid();

    /**
     * Sets the staff member's GUID.
     *
     * @param guid The GUID to set for the staff member
     */
    void setGuid(String guid);

    /**
     * Gets the staff member's weekly working hours.
     *
     * @return The weekly hours of the staff member
     */
    int getWeeklyHours();

    /**
     * Sets the staff member's weekly working hours.
     *
     * @param hours The weekly hours to set for the staff member
     */
    void setWeeklyHours(int hours);

    /**
     * Gets the maximum number of modules the staff member can teach.
     *
     * @return The maximum number of modules
     */
    int getMaxModules();

    /**
     * Sets the maximum number of modules the staff member can teach.
     *
     * @param modules The maximum number of modules to set
     */
    void setMaxModules(int modules);

    /**
     * Gets the staff member's avatar URL or identifier.
     *
     * @return The avatar of the staff member
     */
    String getAvatar();

    /**
     * Sets the staff member's avatar URL or identifier.
     *
     * @param avatar The avatar to set for the staff member
     */
    void setAvatar(String avatar);

    /**
     * Gets the department ID for the staff member.
     *
     * @return The DepartmentId of the staff member
     */
    DepartmentId getDepartmentId();

    /**
     * Sets the department ID for the staff member.
     *
     * @param departmentId The DepartmentId to set for the staff member
     */
    void setDepartmentId(DepartmentId departmentId);

    /**
     * Returns a string representation of the staff member.
     *
     * @return A string containing all staff information in the format:
     * "[base user info], GUID: [guid], Weekly Hours: [hours], Max Modules: [modules]"
     */
    @Override
    String toString();

    /**
     * Retrieves a list of staff filtered by department name.
     *
     * @param departmentName The name of the department to filter by, or empty/null for all staff
     * @return A list of staff in the specified department, or all staff if no department specified
     * @throws IOException If there is an error reading or processing the staff data file
     */
    static List<Staff> getByDepartment(String departmentName) throws IOException
    {
        throw new UnsupportedOperationException("This method should be implemented by a concrete class");
    }

    /**
     * Retrieves a list of staff filtered by department ID.
     *
     * @param departmentId The DepartmentId to filter by
     * @return A list of staff in the specified department
     * @throws IOException If there is an error reading or processing the staff data file
     */
    static List<Staff> getByDepartmentId(DepartmentId departmentId) throws IOException
    {
        throw new UnsupportedOperationException("This method should be implemented by a concrete class");
    }
}