package business.interfaces;

import business.StaffModuleAssignment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IStaffModuleAssignment
{
    // Instance methods
    int getStaffId();

    List<String> getModuleIds();

    String getLastUpdated();

    // Static methods
    static Map<Integer, StaffModuleAssignment> loadAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void saveAssignments(Map<Integer, StaffModuleAssignment> assignments) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void generateInitialAssignments() throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static void updateStaffAssignments(int staffId, List<String> moduleIds) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }

    static List<String> getStaffAssignments(int staffId) throws IOException
    {
        throw new UnsupportedOperationException("Method must be implemented");
    }
}