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

public class StudentModuleAssignment {
    private static final String ASSIGNMENTS_FILE = "data/student_module_assignments.json";
    private final int studentId;
    private final List<String> moduleIds;
    private final String lastUpdated;

    public StudentModuleAssignment(int studentId, List<String> moduleIds) {
        this.studentId = studentId;
        this.moduleIds = moduleIds != null ? moduleIds : new ArrayList<>();
        this.lastUpdated = new Date().toString();
    }

    // Getters
    public int getStudentId() {
        return studentId;
    }

    public List<String> getModuleIds() {
        return moduleIds;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    // Load all assignments from JSON file
    public static Map<Integer, StudentModuleAssignment> loadAssignments() throws IOException {
        File file = new File(ASSIGNMENTS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        JsonProcessor processor = new JsonProcessor(ASSIGNMENTS_FILE);
        processor.processFile();
        JsonObject root = (JsonObject) processor.getJsonContent();
        JsonArray assignmentsArray = root.getAsJsonArray("assignments");

        Map<Integer, StudentModuleAssignment> assignments = new HashMap<>();
        Gson gson = new GsonBuilder().create();

        if (assignmentsArray != null) {
            assignmentsArray.forEach(element -> {
                StudentModuleAssignment assignment = gson.fromJson(element, StudentModuleAssignment.class);
                assignments.put(assignment.getStudentId(), assignment);
            });
        }

        return assignments;
    }

    // Save all assignments to JSON file
    public static void saveAssignments(Map<Integer, StudentModuleAssignment> assignments) throws IOException {
        JsonObject root = new JsonObject();
        JsonArray assignmentsArray = new JsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        assignments.values().forEach(assignment ->
                assignmentsArray.add(gson.toJsonTree(assignment)));

        root.add("assignments", assignmentsArray);

        try (FileWriter writer = new FileWriter(ASSIGNMENTS_FILE)) {
            gson.toJson(root, writer);
        }
    }

    // Generate initial assignments for all students
    public static void generateInitialAssignments() throws IOException {
        List<Student> allStudents = Student.getByCourse("");
        Map<Integer, StudentModuleAssignment> assignments = new HashMap<>();

        for (Student student : allStudents) {
            try {
                String courseCode = Course.getCourseCodeFromTitle(student.getCourse());
                if (courseCode != null && !courseCode.isEmpty()) {
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
            } catch (Exception e) {
                System.err.println("Error generating assignments for student " + student.getId() + ": " + e.getMessage());
            }
        }

        // Save all assignments
        saveAssignments(assignments);
    }

    // Generate initial assignments for a specific student
    public static void generateInitialAssignments(int studentId, String courseCode) throws IOException {
        Map<Integer, StudentModuleAssignment> assignments = loadAssignments();

        try {
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
        } catch (Exception e) {
            System.err.println("Error generating assignments for student " + studentId + ": " + e.getMessage());
            throw new IOException(e);
        }
    }

    // Update assignments for a specific student
    public static void updateStudentAssignments(int studentId, List<String> moduleIds) throws IOException {
        Map<Integer, StudentModuleAssignment> assignments = loadAssignments();
        StudentModuleAssignment updated = new StudentModuleAssignment(studentId, moduleIds);
        assignments.put(studentId, updated);
        saveAssignments(assignments);
    }

    // Get assignments for a specific student
    public static List<String> getStudentAssignments(int studentId) throws IOException {
        Map<Integer, StudentModuleAssignment> assignments = loadAssignments();
        StudentModuleAssignment assignment = assignments.get(studentId);
        return assignment != null ? assignment.getModuleIds() : new ArrayList<>();
    }
}