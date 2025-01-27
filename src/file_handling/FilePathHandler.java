package file_handling;

import java.io.File;

public enum FilePathHandler
{
    STUDENTS_FILE("data/students.json"),
    ASSIGNED_STUDENTS_FILE("data/student_module_assignments.json"),
    ASSIGNED_STAFF_FILE("data/staff_module_assignments.json");

    private String path;

    void FilePaths(String path)
    {
        this.path = path;
    }

    FilePathHandler(String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }

    // Helper method to get file path with system-specific separator
    public String getNormalisedPath()
    {
        return path.replace("/", File.separator);
    }

    // Helper method to create a new file path by appending to the base path
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