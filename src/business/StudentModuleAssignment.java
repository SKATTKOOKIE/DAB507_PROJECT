package business;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.JsonProcessor;
import users.Student;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages the assignment of modules to students.
 * This class handles the persistence and retrieval of module assignments,
 * including functionality to generate initial assignments for new students
 * and update existing assignments.
 */
public class StudentModuleAssignment
{
    /**
     * The file path where student module assignments are stored
     */
    private static final String ASSIGNMENTS_FILE = "data/student_module_assignments.json";

    /**
     * The unique identifier of the student
     */
    private final int studentId;

    /**
     * List of module IDs assigned to the student
     */
    private final List<String> moduleIds;

    /**
     * Timestamp of when the assignment was last updated
     */
    private final String lastUpdated;

    /**
     * Constructs a new StudentModuleAssignment instance.
     *
     * @param studentId The unique identifier of the student
     * @param moduleIds List of module IDs to be assigned to the student.
     *                  If null, an empty list will be created
     */
    public StudentModuleAssignment(int studentId, List<String> moduleIds)
    {
        this.studentId = studentId;
        this.moduleIds = moduleIds != null ? moduleIds : new ArrayList<>();
        this.lastUpdated = new Date().toString();
    }

    /**
     * Gets the student's ID.
     *
     * @return The unique identifier of the student
     */
    public int getStudentId()
    {
        return studentId;
    }

    /**
     * Gets the list of module IDs assigned to the student.
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
     * Loads all student module assignments from the JSON storage file.
     * Creates a new empty map if the file doesn't exist.
     *
     * @return Map of student IDs to their corresponding module assignments
     * @throws IOException If there is an error reading from the file
     */
    public static Map<Integer, StudentModuleAssignment> loadAssignments() throws IOException
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

        Map<Integer, StudentModuleAssignment> assignments = new HashMap<>();
        Gson gson = new GsonBuilder().create();

        if (assignmentsArray != null)
        {
            assignmentsArray.forEach(element ->
            {
                StudentModuleAssignment assignment = gson.fromJson(element, StudentModuleAssignment.class);
                assignments.put(assignment.getStudentId(), assignment);
            });
        }

        return assignments;
    }

    /**
     * Saves all student module assignments to the JSON storage file.
     *
     * @param assignments Map of student IDs to their corresponding module assignments
     * @throws IOException If there is an error writing to the file
     */
    public static void saveAssignments(Map<Integer, StudentModuleAssignment> assignments) throws IOException
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
     * Generates initial module assignments for all students in the system.
     * Students are assigned all modules associated with their course.
     *
     * @throws IOException If there is an error accessing the storage file
     */
    public static void generateInitialAssignments() throws IOException
    {
        List<Student> allStudents = Student.getByCourse("");
        Map<Integer, StudentModuleAssignment> assignments = new HashMap<>();

        for (Student student : allStudents)
        {
            try
            {
                String courseCode = Course.getCourseCodeFromTitle(student.getCourse());
                if (courseCode != null && !courseCode.isEmpty())
                {
                    // Get all modules for the student's course
                    List<Module> courseModules = Module.getModulesForCourse(courseCode);

                    // Create default assignments with all available modules
                    List<String> moduleIds = courseModules.stream()
                            .map(Module::getCode)
                            .collect(Collectors.toList());

                    // Create assignment
                    StudentModuleAssignment assignment = new StudentModuleAssignment(
                            student.getId(), moduleIds);
                    assignments.put(student.getId(), assignment);
                }
            }
            catch (Exception e)
            {
                System.err.println("Error generating assignments for student " + student.getId() + ": " + e.getMessage());
            }
        }

        // Save all assignments
        saveAssignments(assignments);
    }

    /**
     * Generates initial module assignments for a specific student.
     * The student is assigned all modules associated with their course.
     *
     * @param studentId  The unique identifier of the student
     * @param courseCode The course code for which to generate module assignments
     * @throws IOException If there is an error accessing the storage file or generating assignments
     */
    public static void generateInitialAssignments(int studentId, String courseCode) throws IOException
    {
        Map<Integer, StudentModuleAssignment> assignments = loadAssignments();

        try
        {
            // Get all modules for the student's course
            List<Module> courseModules = Module.getModulesForCourse(courseCode);

            // Create default assignments with all modules
            List<String> moduleIds = courseModules.stream()
                    .map(Module::getCode)
                    .collect(Collectors.toList());

            // Create assignment
            StudentModuleAssignment assignment = new StudentModuleAssignment(studentId, moduleIds);
            assignments.put(studentId, assignment);

            // Save all assignments
            saveAssignments(assignments);
        }
        catch (Exception e)
        {
            System.err.println("Error generating assignments for student " + studentId + ": " + e.getMessage());
            throw new IOException(e);
        }
    }

    /**
     * Updates the module assignments for a specific student.
     *
     * @param studentId The unique identifier of the student
     * @param moduleIds List of new module IDs to be assigned
     * @throws IOException If there is an error accessing the storage file
     */
    public static void updateStudentAssignments(int studentId, List<String> moduleIds) throws IOException
    {
        Map<Integer, StudentModuleAssignment> assignments = loadAssignments();
        StudentModuleAssignment updated = new StudentModuleAssignment(studentId, moduleIds);
        assignments.put(studentId, updated);
        saveAssignments(assignments);
    }

    /**
     * Retrieves the current module assignments for a specific student.
     *
     * @param studentId The unique identifier of the student
     * @return List of assigned module IDs. Returns an empty list if no assignments exist
     * @throws IOException If there is an error accessing the storage file
     */
    public static List<String> getStudentAssignments(int studentId) throws IOException
    {
        Map<Integer, StudentModuleAssignment> assignments = loadAssignments();
        StudentModuleAssignment assignment = assignments.get(studentId);
        return assignment != null ? assignment.getModuleIds() : new ArrayList<>();
    }
}