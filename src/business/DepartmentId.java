package business;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;
import users.Staff;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public enum DepartmentId
{
    ENG("Engineering"),
    CHI("Childhood"),
    DAN("Dance"),
    CRE("Creative"),
    ECD("Engineering Computing and Design"),
    FIA("Fine Art"),
    HUM("Humanities"),
    LAW("Law"),
    NUR("Nursing"),
    OAE("Outdoor Adventure Education"),
    PHY("Physiotherapy"),
    SOM("School of Music"),
    SOW("Social Work"),
    THE("Theatre"),
    UNKNOWN("Unknown Department");

    private final String departmentName;

    DepartmentId(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public static class StaffModuleAssignment
    {
        private static final String ASSIGNMENTS_FILE = FilePathHandler.ASSIGNED_STAFF_FILE.getNormalisedPath();
        private final int staffId;
        private final List<String> moduleIds;
        private final String lastUpdated;

        public StaffModuleAssignment(int staffId, List<String> moduleIds)
        {
            this.staffId = staffId;
            this.moduleIds = moduleIds != null ? moduleIds : new ArrayList<>();
            this.lastUpdated = new Date().toString();
        }

        // Getters
        public int getStaffId()
        {
            return staffId;
        }

        public List<String> getModuleIds()
        {
            return moduleIds;
        }

        public String getLastUpdated()
        {
            return lastUpdated;
        }

        // Load all assignments from JSON file
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

        // Save all assignments to JSON file
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

        // Generate random initial assignments for all staff
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

        // Update assignments for a specific staff member
        public static void updateStaffAssignments(int staffId, List<String> moduleIds) throws IOException
        {
            Map<Integer, StaffModuleAssignment> assignments = loadAssignments();
            StaffModuleAssignment updated = new StaffModuleAssignment(staffId, moduleIds);
            assignments.put(staffId, updated);
            saveAssignments(assignments);
        }

        // Get assignments for a specific staff member
        public static List<String> getStaffAssignments(int staffId) throws IOException
        {
            Map<Integer, StaffModuleAssignment> assignments = loadAssignments();
            StaffModuleAssignment assignment = assignments.get(staffId);
            return assignment != null ? assignment.getModuleIds() : new ArrayList<>();
        }
    }
}