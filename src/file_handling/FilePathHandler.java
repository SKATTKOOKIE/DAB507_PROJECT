package file_handling;

import java.io.File;

/**
 * Manages file paths for system data files.
 * This enum provides centralised management of file paths for various data files
 * used throughout the system, ensuring consistent file access and path handling.
 * <p>
 * The enum constants represent paths to:
 * <ul>
 *   <li>Student records</li>
 *   <li>Staff information</li>
 *   <li>Course and department data</li>
 *   <li>Module information</li>
 *   <li>Assignment mappings</li>
 * </ul>
 */
public enum FilePathHandler
{
    /**
     * Path to the JSON file containing student data
     */
    STUDENTS_FILE("data/students.json"),

    /**
     * Path to the JSON file containing staff data
     */
    STAFF_FILE("data/staff.json"),

    /**
     * Path to the JSON file containing course and department mappings
     */
    COURSES_FILE("data/courses_with_departments.json"),

    /**
     * Path to the JSON file containing department information
     */
    DEPARTMENTS_FILE("data/departments.json"),

    /**
     * Path to the JSON file containing module information and associations
     */
    MODULES_FILE("data/associated_modules.json"),

    /**
     * Path to the JSON file containing student-module assignments
     */
    ASSIGNED_STUDENTS_FILE("data/student_module_assignments.json"),

    /**
     * Path to the JSON file containing staff-module assignments
     */
    ASSIGNED_STAFF_FILE("data/staff_module_assignments.json");

    /**
     * The file path stored for each enum constant
     */
    private String path;

    /**
     * Initialises a file path.
     * Note: This method appears to be unused and may be deprecated.
     *
     * @param path The file path to set
     */
    void FilePaths(String path)
    {
        this.path = path;
    }

    /**
     * Constructs a new FilePathHandler with the specified path.
     *
     * @param path The file path for this enum constant
     */
    FilePathHandler(String path)
    {
        this.path = path;
    }

    /**
     * Retrieves the raw file path.
     *
     * @return The original file path string
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Retrieves the file path with system-specific directory separators.
     * Converts forward slashes to the system's directory separator for cross-platform compatibility.
     *
     * @return The normalised file path using system-specific separators
     */
    public String getNormalisedPath()
    {
        return path.replace("/", File.separator);
    }

    /**
     * Creates a new file path by appending additional path components.
     * Joins the base path with additional components using the system's directory separator.
     *
     * @param additional Variable number of path components to append
     * @return The combined file path using system-specific separators
     */
    public String resolve(String... additional)
    {
        StringBuilder result = new StringBuilder(path);
        for (String part : additional)
        {
            result.append(File.separator).append(part);
        }
        return result.toString();
    }
}