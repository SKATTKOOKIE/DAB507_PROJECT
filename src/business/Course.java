package business;

import business.interfaces.ICourse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a course in the system.
 * Contains course-related data and operations, with integration to Department functionality.
 */
public class Course implements ICourse
{
    private String courseTitle;
    private String courseId;
    private DepartmentId departmentId;
    private static final String DATA_FILE = FilePathHandler.COURSES_FILE.getNormalisedPath();

    // Default constructor
    public Course()
    {
        this.departmentId = DepartmentId.UNKNOWN;
    }

    // Getters and Setters
    public String getCourseTitle()
    {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle)
    {
        this.courseTitle = courseTitle;
    }

    public String getCourseId()
    {
        return courseId;
    }

    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
    }

    public DepartmentId getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(DepartmentId departmentId)
    {
        this.departmentId = departmentId != null ? departmentId : DepartmentId.UNKNOWN;
    }

    @Override
    public String toString()
    {
        return String.format("%s (ID: %s) [Department: %s]",
                courseTitle,
                courseId,
                departmentId);
    }

    /**
     * Retrieves all courses from the JSON file.
     *
     * @return List of Course objects
     * @throws IOException if there is an error reading the file
     */
    public static List<Course> getAll() throws IOException
    {
        JsonProcessor courseProcessor = new JsonProcessor(DATA_FILE);
        courseProcessor.processFile();

        JsonObject jsonContent = (JsonObject) courseProcessor.getJsonContent();
        JsonArray coursesJson = jsonContent.getAsJsonArray("courses");

        if (coursesJson == null)
        {
            throw new IOException("No courses array found in JSON file");
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Course.class, new CourseDeserialiser(DATA_FILE))
                .create();

        List<Course> courses = new ArrayList<>();
        coursesJson.forEach(jsonElement ->
        {
            Course course = gson.fromJson(jsonElement, Course.class);
            courses.add(course);
        });

        return courses;
    }

    /**
     * Displays all courses grouped by department.
     *
     * @throws IOException if there is an error reading the file
     */
    /**
     * Gets courses for a specific department.
     *
     * @param department The department to get courses for
     * @return List of courses in the department
     * @throws IOException if there is an error reading the file
     */
    public static List<Course> getCoursesByDepartment(Department department) throws IOException
    {
        if (department == null)
        {
            return new ArrayList<>();
        }

        List<Course> allCourses = getAll();
        return allCourses.stream()
                .filter(course -> department.getDepartmentId() == course.getDepartmentId())
                .collect(Collectors.toList());
    }

    /**
     * Displays all courses grouped by their departments.
     *
     * @throws IOException if there is an error reading the file
     */
    public static void displayAllCourses() throws IOException
    {
        List<Course> courses = getAll();

        if (courses.isEmpty())
        {
            System.out.println("No courses found.");
            return;
        }

        System.out.println("\nCourse List:");
        System.out.println("============");

        // Group courses by department
        Map<DepartmentId, List<Course>> coursesByDepartment = courses.stream()
                .collect(Collectors.groupingBy(Course::getDepartmentId));

        // Display courses by department
        coursesByDepartment.forEach((dept, deptCourses) ->
        {
            System.out.printf("\n%s Department:%n", dept);
            System.out.println("=".repeat(20));

            deptCourses.forEach(course -> System.out.println(course.toString()));
        });

        System.out.printf("\nTotal Courses: %d%n", courses.size());
    }

    /**
     * Filters courses by their ID prefix (case-insensitive).
     *
     * @param courses List of courses to filter
     * @param prefix  The prefix to search for
     * @return Filtered list of courses
     */
    public static List<Course> filterByIdPrefix(List<Course> courses, String prefix)
    {
        return courses.stream()
                .filter(course -> course.getCourseId() != null)
                .filter(course -> course.getCourseId().toUpperCase().startsWith(prefix.toUpperCase()))
                .collect(Collectors.toList());
    }

    /**
     * Displays courses that match a specific department.
     *
     * @param department The department to filter by
     * @throws IOException if there is an error reading the file
     */
    public static void displayCoursesByDepartment(DepartmentId department) throws IOException
    {
        List<Course> courses = getAll();

        List<Course> departmentCourses = courses.stream()
                .filter(course -> course.getDepartmentId() == department)
                .collect(Collectors.toList());

        if (departmentCourses.isEmpty())
        {
            System.out.printf("No courses found for department: %s%n", department);
            return;
        }

        System.out.printf("\nCourses in %s Department:%n", department);
        System.out.println("=".repeat(30));

        departmentCourses.forEach(course -> System.out.println(course.toString()));
        System.out.printf("\nTotal Courses in Department: %d%n", departmentCourses.size());
    }

    /**
     * Search for courses by title (case-insensitive, partial match).
     *
     * @param searchTerm The term to search for in course titles
     * @throws IOException if there is an error reading the file
     */
    public static void searchCoursesByTitle(String searchTerm) throws IOException
    {
        List<Course> courses = getAll();

        List<Course> matchingCourses = courses.stream()
                .filter(course -> course.getCourseTitle().toLowerCase()
                        .contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        if (matchingCourses.isEmpty())
        {
            System.out.printf("No courses found matching: %s%n", searchTerm);
            return;
        }

        System.out.printf("\nCourses matching '%s':%n", searchTerm);
        System.out.println("=".repeat(30));

        matchingCourses.forEach(course -> System.out.println(course.toString()));
        System.out.printf("\nTotal Matching Courses: %d%n", matchingCourses.size());
    }

    public String getCourseCode()
    {
        if (courseId == null)
        {
            return "";
        }
        // Remove any non-alphanumeric characters and convert to uppercase
        return courseId.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
    }

    /**
     * Gets course code from course title
     *
     * @param courseTitle The full course title (e.g., "BA (Hons) Theatre")
     * @return The course code or empty string if not found
     */
    public static String getCourseCodeFromTitle(String courseTitle) throws IOException
    {
        List<Course> allCourses = getAll();
        return allCourses.stream()
                .filter(course -> course.getCourseTitle().equals(courseTitle))
                .map(Course::getCourseCode)
                .findFirst()
                .orElse("");
    }

    public boolean hasValidCourseCode()
    {
        String code = getCourseCode();
        return code != null && !code.isEmpty() && code.matches("[A-Z0-9]{4,5}");
    }
}