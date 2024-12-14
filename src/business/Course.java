package business;

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
}