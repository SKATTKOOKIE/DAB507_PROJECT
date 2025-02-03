package business;

import testframework.*;

import java.util.Arrays;
import java.util.List;

public class ModuleTest extends BaseTest
{
    private Module module;
    private static final String TEST_NAME = "Introduction to Programming";
    private static final String TEST_CODE = "CS101";
    private static final String TEST_AC_YEAR = "2023";
    private static final List<String> TEST_ASSOCIATED_COURSES = Arrays.asList("BSCS", "BSIT");

    @Override
    protected void setup()
    {
        super.setup();
        module = new Module(TEST_NAME, TEST_CODE, TEST_AC_YEAR, TEST_ASSOCIATED_COURSES);
    }

    public void testGetters()
    {
        Assert.assertEquals(TEST_NAME, module.getName(), "Module name should match");
        Assert.assertEquals(TEST_CODE, module.getCode(), "Module code should match");
        Assert.assertEquals(TEST_AC_YEAR, module.getAcYear(), "Academic year should match");
        Assert.assertEquals(TEST_ASSOCIATED_COURSES, module.getAssociatedCourses(), "Associated courses should match");
    }

    public void testIsAssociatedWithCourse()
    {
        Assert.assertTrue(module.isAssociatedWithCourse("BSCS"),
                "Module should be associated with BSCS");
        Assert.assertTrue(module.isAssociatedWithCourse("BSIT"),
                "Module should be associated with BSIT");
        Assert.assertFalse(module.isAssociatedWithCourse("BSEE"),
                "Module should not be associated with BSEE");
    }

    public void testFilterByYear()
    {
        List<Module> modules = Arrays.asList(
                new Module("Module 1", "M1", "2022", Arrays.asList("C1", "C2")),
                new Module("Module 2", "M2", "2023", Arrays.asList("C3", "C4")),
                new Module("Module 3", "M3", "2023", Arrays.asList("C5", "C6"))
        );

        List<Module> filteredModules = Module.filterByYear(modules, "2023");
        Assert.assertEquals(2, filteredModules.size(),
                "Should return 2 modules for the year 2023");
    }

    public void testSearchByName()
    {
        List<Module> modules = Arrays.asList(
                new Module("Programming Fundamentals", "CS101", "2023", Arrays.asList("BSCS", "BSIT")),
                new Module("Data Structures", "CS201", "2023", Arrays.asList("BSCS")),
                new Module("Database Systems", "IT301", "2023", Arrays.asList("BSIT"))
        );

        List<Module> searchResults = Module.searchByName(modules, "programming");
        Assert.assertEquals(1, searchResults.size(),
                "Should return 1 module with 'programming' in the name");
    }

    public void testToString()
    {
        String result = module.toString();
        Assert.assertTrue(result.contains(TEST_NAME) &&
                        result.contains(TEST_CODE) &&
                        result.contains(TEST_AC_YEAR),
                "toString should contain module name, code, and academic year");
    }

    public static void main(String[] args)
    {
        new ModuleTest().runTests();
    }
}