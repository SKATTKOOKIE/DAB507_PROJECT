package users;

import business.DepartmentId;
import testframework.*;

import java.io.IOException;
import java.util.List;

public class StaffTest extends BaseTest
{
    private Staff staff;

    private static final int TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Smith";
    private static final String TEST_EMAIL = "john.smith@test.com";
    private static final String TEST_GUID = "550e8400-e29b-41d4-a716-446655440000";
    private static final int TEST_WEEKLY_HOURS = 37;
    private static final int TEST_MAX_MODULES = 4;
    private static final String TEST_AVATAR = "avatar.png";
    private static final DepartmentId TEST_DEPARTMENT = DepartmentId.ECD;

    @Override
    protected void setup()
    {
        super.setup();
        staff = new Staff();
        staff.setId(TEST_ID);
        staff.setFirstName(TEST_FIRST_NAME);
        staff.setLastName(TEST_LAST_NAME);
        staff.setEmail(TEST_EMAIL);
        staff.setGuid(TEST_GUID);
        staff.setWeeklyHours(TEST_WEEKLY_HOURS);
        staff.setMaxModules(TEST_MAX_MODULES);
        staff.setAvatar(TEST_AVATAR);
        staff.setDepartmentId(TEST_DEPARTMENT);
    }

    // Basic Properties Tests
    public void testGetters()
    {
        Assert.assertEquals(TEST_GUID, staff.getGuid(), "GUID should match");
        Assert.assertEquals(TEST_WEEKLY_HOURS, staff.getWeeklyHours(), "Weekly hours should match");
        Assert.assertEquals(TEST_MAX_MODULES, staff.getMaxModules(), "Max modules should match");
        Assert.assertEquals(TEST_AVATAR, staff.getAvatar(), "Avatar should match");
        Assert.assertEquals(TEST_DEPARTMENT, staff.getDepartmentId(), "Department should match");
        Assert.assertEquals(TEST_DEPARTMENT.getDepartmentName(), staff.getDepartment(), "Department name should match");
    }

    public void testSetters()
    {
        Staff newStaff = new Staff();

        newStaff.setGuid(TEST_GUID);
        Assert.assertEquals(TEST_GUID, newStaff.getGuid(), "GUID should be updated");

        newStaff.setWeeklyHours(TEST_WEEKLY_HOURS);
        Assert.assertEquals(TEST_WEEKLY_HOURS, newStaff.getWeeklyHours(), "Weekly hours should be updated");

        newStaff.setMaxModules(TEST_MAX_MODULES);
        Assert.assertEquals(TEST_MAX_MODULES, newStaff.getMaxModules(), "Max modules should be updated");

        newStaff.setAvatar(TEST_AVATAR);
        Assert.assertEquals(TEST_AVATAR, newStaff.getAvatar(), "Avatar should be updated");

        newStaff.setDepartmentId(TEST_DEPARTMENT);
        Assert.assertEquals(TEST_DEPARTMENT, newStaff.getDepartmentId(), "Department should be updated");
    }

    // Department Tests
    public void testGetDepartmentIdWithNull()
    {
        Staff nullDeptStaff = new Staff();
        Assert.assertEquals(DepartmentId.UNKNOWN, nullDeptStaff.getDepartmentId(), "Null department should return UNKNOWN");
    }

    public void testGetDepartmentIdWithEmpty()
    {
        Staff emptyDeptStaff = new Staff();
        emptyDeptStaff.setDepartment("");
        Assert.assertEquals(DepartmentId.UNKNOWN, emptyDeptStaff.getDepartmentId(), "Empty department should return UNKNOWN");
    }

    public void testGetDepartmentIdWithInvalid()
    {
        Staff invalidDeptStaff = new Staff();
        invalidDeptStaff.setDepartment("Invalid Department");
        Assert.assertEquals(DepartmentId.UNKNOWN, invalidDeptStaff.getDepartmentId(), "Invalid department should return UNKNOWN");
    }

    // Data Retrieval Tests
    public void testGetByNullDepartment() throws IOException
    {
        List<Staff> staffList = Staff.getByDepartment(null);
        Assert.assertNotNull(staffList, "Staff list should not be null");
    }

    public void testGetByEmptyDepartment() throws IOException
    {
        List<Staff> staffList = Staff.getByDepartment("");
        Assert.assertNotNull(staffList, "Staff list should not be null");
    }

    public void testGetByUnknownDepartment() throws IOException
    {
        List<Staff> staffList = Staff.getByDepartmentId(DepartmentId.UNKNOWN);
        Assert.assertNotNull(staffList, "Staff list should not be null");
    }

    // Output Format Tests
    public void testToString()
    {
        String result = staff.toString();

        Assert.assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
        Assert.assertTrue(result.contains("Name: " + TEST_FIRST_NAME + " " + TEST_LAST_NAME),
                "toString should contain full name");
        Assert.assertTrue(result.contains("Email: " + TEST_EMAIL), "toString should contain email");
        Assert.assertTrue(result.contains("GUID: " + TEST_GUID), "toString should contain GUID");
        Assert.assertTrue(result.contains("Weekly Hours: " + TEST_WEEKLY_HOURS),
                "toString should contain weekly hours");
        Assert.assertTrue(result.contains("Max Modules: " + TEST_MAX_MODULES),
                "toString should contain max modules");
    }

    public void testPrintDetailedInfo()
    {
        outputStream.reset();
        staff.printDetailedInfo();
        String output = outputStream.toString();

        Assert.assertTrue(output.contains(TEST_FIRST_NAME), "Output should contain first name");
        Assert.assertTrue(output.contains(TEST_LAST_NAME), "Output should contain last name");
        Assert.assertTrue(output.contains(String.valueOf(TEST_ID)), "Output should contain ID");
        Assert.assertTrue(output.contains(TEST_DEPARTMENT.getDepartmentName()),
                "Output should contain department name");
        Assert.assertTrue(output.contains(String.valueOf(TEST_WEEKLY_HOURS)),
                "Output should contain weekly hours");
        Assert.assertTrue(output.contains(String.valueOf(TEST_MAX_MODULES)),
                "Output should contain max modules");
        Assert.assertTrue(output.contains(TEST_EMAIL), "Output should contain email");
    }

    public static void main(String[] args)
    {
        new StaffTest().runTests();
    }
}