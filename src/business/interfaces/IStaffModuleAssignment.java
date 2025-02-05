package business.interfaces;

import business.StaffModuleAssignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Defines the interface for managing staff-to-module assignments within the educational system.
 * This interface provides functionality for:
 * <ul>
 *   <li>Accessing individual staff module assignments</li>
 *   <li>Loading and saving assignment data</li>
 *   <li>Managing and updating staff teaching assignments</li>
 *   <li>Tracking assignment history and changes</li>
 * </ul>
 * <p>
 * The interface supports both individual assignment operations and bulk data management,
 * ensuring consistent handling of staff teaching allocations across the system.
 */
public interface IStaffModuleAssignment
{
    /**
     * Retrieves the unique identifier of the staff member.
     *
     * @return The staff member's ID
     */
    int getStaffId();

    /**
     * Retrieves the list of module IDs assigned to the staff member.
     *
     * @return List of assigned module IDs
     */
    List<String> getModuleIds();

    /**
     * Retrieves the timestamp of the last assignment update.
     *
     * @return The last update timestamp as a string
     */
    String getLastUpdated();

    /**
     * Loads all staff-module assignments from persistent storage.
     *
     * @return Map of staff IDs to their corresponding module assignments
     * @throws IOException If there is an error reading the assignment data
     */
    static Map<Integer, StaffModuleAssignment> loadAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Saves the current staff-module assignments to persistent storage.
     *
     * @param assignments Map of staff IDs to their corresponding module assignments
     * @throws IOException If there is an error saving the assignment data
     */
    static void saveAssignments(Map<Integer, StaffModuleAssignment> assignments) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Generates and saves initial module assignments for staff members.
     * This method should be used for system initialisation or reset purposes.
     *
     * @throws IOException If there is an error generating or saving the initial assignments
     */
    static void generateInitialAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Updates the module assignments for a specific staff member.
     *
     * @param staffId   The ID of the staff member to update
     * @param moduleIds List of new module IDs to assign to the staff member
     * @throws IOException If there is an error updating the assignments
     */
    static void updateStaffAssignments(int staffId, List<String> moduleIds) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    /**
     * Retrieves the current module assignments for a specific staff member.
     *
     * @param staffId The ID of the staff member
     * @return List of module IDs currently assigned to the staff member
     * @throws IOException If there is an error retrieving the assignments
     */
    static List<String> getStaffAssignments(int staffId) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}