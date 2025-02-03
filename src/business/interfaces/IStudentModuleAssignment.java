package business.interfaces;

import business.StudentModuleAssignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IStudentModuleAssignment
{
    // Instance methods
    int getStudentId();

    List<String> getModuleIds();

    String getLastUpdated();

    // Static methods
    static Map<Integer, StudentModuleAssignment> loadAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void saveAssignments(Map<Integer, StudentModuleAssignment> assignments) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void generateInitialAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void generateInitialAssignments(int studentId, String courseCode) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void updateStudentAssignments(int studentId, List<String> moduleIds) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<String> getStudentAssignments(int studentId) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}