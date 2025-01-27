package business;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import file_handling.JsonProcessor;
import users.Staff;

/**
 * Manages the assignment of teaching modules to staff members.
 * This class handles the persistence and retrieval of module assignments,
 * including functionality to generate initial random assignments and update
 * existing assignments.
 */
public class StaffModuleAssignment
{
    /**
     * The file path where staff module assignments are stored
     */
    private static final String ASSIGNMENTS_FILE = "data/staff_module_assignments.json";

    /**
     * The unique identifier of the staff member
     */
    private final int staffId;

    /**
     * List of module IDs assigned to the staff member
     */
    private final List<String> moduleIds;

    /**
     * Timestamp of when the assignment was last updated
     */
    private final String lastUpdated;

    /**
     * Constructs a new StaffModuleAssignment instance.
     *
     * @param staffId   The unique identifier of the staff member
     * @param moduleIds List of module IDs to be assigned to the staff member.
     *                  If null, an empty list will be created
     */
    public StaffModuleAssignment(int staffId, List<String> moduleIds)
    {
        this.staffId = staffId;
        this.moduleIds = moduleIds != null ? moduleIds : new ArrayList<>();
        this.lastUpdated = new Date().toString();
    }

    /**
     * Gets the staff member's ID.
     *
     * @return The unique identifier of the staff member
     */
    public int getStaffId()
    {
        return staffId;
    }

    /**
     * Gets the list of module IDs assigned to the staff member.
     *
     * @return List of assigned module IDs
     */
    public List<String> getModuleIds()
    {
        return moduleIds;
    }

    /**
     * Gets the timestamp of when the assignment was last updated.
     *
     * @return String representation of the last update timestamp
     */
    public String getLastUpdated()
    {
        return lastUpdated;
    }

    /**
     * Loads all staff module assignments from the JSON storage file.
     * Creates a new empty map if the file doesn't exist.
     *
     * @return Map of staff IDs to their corresponding module assignments
     * @throws IOException If there is an error reading from the file
     */
    public static Map<Integer, StaffModuleAssignment> loadAssignments() throws IOException
    {
        File file = new File(ASSIGNMENTS_FILE);
        if (!file.exists())
        {
            return new HashMap<>();
        }

        JsonProcessor processor = new JsonProcessor(ASSIGNMENTS_FILE);
        processor.processFile();
        JsonObject root = (JsonObject) processor.getJsonContent();
        JsonArray assignmentsArray = root.getAsJsonArray("assignments");

        Map<Integer, StaffModuleAssignment> assignments = new HashMap<>();
        Gson gson = new GsonBuilder().create();

        if (assignmentsArray != null)
        {
            assignmentsArray.forEach(element ->
            {
                StaffModuleAssignment assignment = gson.fromJson(element, StaffModuleAssignment.class);
                assignments.put(assignment.getStaffId(), assignment);
            });
        }

        return assignments;
    }

    /**
     * Saves all staff module assignments to the JSON storage file.
     *
     * @param assignments Map of staff IDs to their corresponding module assignments
     * @throws IOException If there is an error writing to the file
     */
    public static void saveAssignments(Map<Integer, StaffModuleAssignment> assignments) throws IOException
    {
        JsonObject root = new JsonObject();
        JsonArray assignmentsArray = new JsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        assignments.values().forEach(assignment ->
                assignmentsArray.add(gson.toJsonTree(assignment)));

        root.add("assignments", assignmentsArray);

        try (FileWriter writer = new FileWriter(ASSIGNMENTS_FILE))
        {
            gson.toJson(root, writer);
        }
    }

    /**
     * Generates initial random module assignments for all staff members.
     * Assignments are based on:
     * - Staff department matching course department
     * - Staff member's maximum module limit
     * - Available modules in department courses
     *
     * @throws IOException If there is an error accessing the storage file
     */
    public static void generateInitialAssignments() throws IOException
    {
        List<Staff> allStaff = Staff.getByDepartment("");
        Map<Integer, StaffModuleAssignment> assignments = new HashMap<>();

        for (Staff staff : allStaff)
        {
            try
            {
                // Get courses for staff's department
                List<Course> departmentCourses = Course.getAll().stream()
                        .filter(course ->
                        {
                            DepartmentId deptId = course.getDepartmentId();
                            String deptName = deptId != null ? deptId.getDepartmentName() : "";
                            return deptName.equals(staff.getDepartment());
                        })
                        .collect(Collectors.toList());

                // Get all modules for department courses
                Set<Module> availableModules = new HashSet<>();
                for (Course course : departmentCourses)
                {
                    List<Module> courseModules = Module.getModulesForCourse(course.getCourseCode());
                    availableModules.addAll(courseModules);
                }

                // Randomly select modules up to staff's max_modules
                List<String> moduleIds = new ArrayList<>(availableModules).stream()
                        .limit(staff.getMaxModules())
                        .map(Module::getCode)
                        .collect(Collectors.toList());

                // Create assignment
                StaffModuleAssignment assignment = new StaffModuleAssignment(
                        staff.getId(), moduleIds);
                assignments.put(staff.getId(), assignment);

            }
            catch (Exception e)
            {
                System.err.println("Error generating assignments for staff " + staff.getId() + ": " + e.getMessage());
            }
        }

        // Save all assignments
        saveAssignments(assignments);
    }

    /**
     * Updates the module assignments for a specific staff member.
     *
     * @param staffId   The unique identifier of the staff member
     * @param moduleIds List of new module IDs to be assigned
     * @throws IOException If there is an error accessing the storage file
     */
    public static void updateStaffAssignments(int staffId, List<String> moduleIds) throws IOException
    {
        Map<Integer, StaffModuleAssignment> assignments = loadAssignments();
        StaffModuleAssignment updated = new StaffModuleAssignment(staffId, moduleIds);
        assignments.put(staffId, updated);
        saveAssignments(assignments);
    }

    /**
     * Retrieves the current module assignments for a specific staff member.
     *
     * @param staffId The unique identifier of the staff member
     * @return List of assigned module IDs. Returns an empty list if no assignments exist
     * @throws IOException If there is an error accessing the storage file
     */
    public static List<String> getStaffAssignments(int staffId) throws IOException
    {
        Map<Integer, StaffModuleAssignment> assignments = loadAssignments();
        StaffModuleAssignment assignment = assignments.get(staffId);
        return assignment != null ? assignment.getModuleIds() : new ArrayList<>();
    }
}