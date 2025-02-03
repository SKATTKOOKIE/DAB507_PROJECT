package users;

import testframework.*;
import business.DepartmentId;

/**
 * Refactored User test class using the custom testing framework
 */
public class UserTest extends BaseTest
{
    private static class TestUser extends User
    {
        @Override
        public void printDetailedInfo()
        {
            System.out.printf("Test User: %s %s%n", getFirstName(), getLastName());
        }
    }

    private TestUser user;

    // Test constants
    private static final int TEST_ID = 1;
    private static final String TEST_FIRST_NAME = "Jane";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "jane.doe@test.com";
    private static final String TEST_DEPARTMENT = String.valueOf(DepartmentId.ECD);
    private static final String TEST_COURSE = "Software Engineering";

    @Override
    protected void setup()
    {
        super.setup(); // Call base setup for output redirection
        user = new TestUser();
        user.setId(TEST_ID);
        user.setFirstName(TEST_FIRST_NAME);
        user.setLastName(TEST_LAST_NAME);
        user.setEmail(TEST_EMAIL);
        user.setDepartment(TEST_DEPARTMENT);
        user.setCourse(TEST_COURSE);
    }

    public void testGetters()
    {
        Assert.assertEquals(TEST_ID, user.getId(), "ID should match");
        Assert.assertEquals(TEST_FIRST_NAME, user.getFirstName(), "First name should match");
        Assert.assertEquals(TEST_LAST_NAME, user.getLastName(), "Last name should match");
        Assert.assertEquals(TEST_EMAIL, user.getEmail(), "Email should match");
        Assert.assertEquals(TEST_DEPARTMENT, user.getDepartment(), "Department should match");
        Assert.assertEquals(TEST_COURSE, user.getCourse(), "Course should match");
    }

    public void testSetters()
    {
        TestUser newUser = new TestUser();

        newUser.setId(TEST_ID);
        Assert.assertEquals(TEST_ID, newUser.getId(), "ID should be updated");

        newUser.setFirstName(TEST_FIRST_NAME);
        Assert.assertEquals(TEST_FIRST_NAME, newUser.getFirstName(), "First name should be updated");
    }

    public void testNullHandling()
    {
        TestUser nullUser = new TestUser();
        nullUser.setId(TEST_ID);

        Assert.assertNull(nullUser.getFirstName(), "First name should be null by default");
        Assert.assertNull(nullUser.getLastName(), "Last name should be null by default");
        Assert.assertNull(nullUser.getEmail(), "Email should be null by default");
    }

    public void testToString()
    {
        String result = user.toString();

        Assert.assertTrue(result.contains("ID: " + TEST_ID), "toString should contain ID");
        Assert.assertTrue(
                result.contains("Name: " + TEST_FIRST_NAME + " " + TEST_LAST_NAME),
                "toString should contain full name"
        );
    }

    public void testInterfaceImplementation()
    {
        Assert.assertTrue(user instanceof IUser, "User should implement IUser interface");

        IUser iUser = user;
        Assert.assertEquals(TEST_ID, iUser.getId(), "Interface getId should work");
        Assert.assertEquals(TEST_FIRST_NAME, iUser.getFirstName(), "Interface getFirstName should work");
    }

    // Main method to run the tests
    public static void main(String[] args)
    {
        new UserTest().runTests();
    }
}