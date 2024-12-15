package business;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import file_handling.CsvProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Course
{
    private String courseTitle;
    private String courseId;
    private DepartmentId departmentId;

    // Default constructor
    public Course()
    {
        this.departmentId = DepartmentId.UNKNOWN;
    }

    public DepartmentId getDepartmentId()
    {
        return departmentId;
    }

    public void setDepartmentId(DepartmentId departmentId)
    {
        this.departmentId = departmentId != null ? departmentId : DepartmentId.UNKNOWN;
    }

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

    @Override
    public String toString()
    {
        return "Course{" +
                "courseTitle='" + courseTitle + '\'' +
                ", courseId='" + courseId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Course course = (Course) o;

        if (courseId != null ? !courseId.equals(course.courseId) : course.courseId != null)
        {
            return false;
        }
        if (courseTitle != null ? !courseTitle.equals(course.courseTitle) : course.courseTitle != null)
        {
            return false;
        }
        return departmentId == course.departmentId;  // Use == for enum comparison
    }

    @Override
    public int hashCode()
    {
        int result = courseTitle != null ? courseTitle.hashCode() : 0;
        result = 31 * result + (courseId != null ? courseId.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        return result;
    }

    public static List<Course> getAll() throws IOException
    {
        var courseProcessor = new CsvProcessor("data/Courses.csv");
        courseProcessor.processFile();
        JsonArray coursesJson = courseProcessor.getJsonContent();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Course.class, new CourseDeserializer())
                .create();

        List<Course> courses = new ArrayList<>();
        coursesJson.forEach(jsonElement ->
        {
            Course course = gson.fromJson(jsonElement, Course.class);
            courses.add(course);
        });

        return courses;
    }

    public void printInfo()
    {
        System.out.printf("- %s (ID: %s) [Dept: %s]\n",
                courseTitle,
                courseId,
                departmentId);
    }

    public static List<Course> filterByIdPrefix(List<Course> courses, String prefix)
    {
        return courses.stream()
                .filter(course -> course.getCourseId() != null)
                .filter(course -> course.getCourseId().toUpperCase().startsWith(prefix.toUpperCase()))
                .collect(Collectors.toList());
    }
}