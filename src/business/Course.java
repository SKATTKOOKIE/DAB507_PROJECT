package business;

public class Course
{
   protected String id;
   protected String courseName;

    public Course(String id, String courseName)
    {
        this.id = id;
        this.courseName = courseName;
    }

    public String getCourseName()
    {
        return courseName;
    }

    public String getId()
    {
        return id;
    }
}
