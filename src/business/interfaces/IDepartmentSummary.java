package business.interfaces;

import business.DepartmentId;

public interface IDepartmentSummary
{
    String getBasicSummary();

    String getDetailedInfo();

    void printSummary();

    void printDetailedInfo();

    DepartmentId getDepartmentId();

    int getStudentCount();

    int getStaffCount();
}