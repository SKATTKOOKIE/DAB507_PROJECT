package business;


public class Module
{
    private String code;
    private String name;
    private int credits;
    private Department department;
    private Staff moduleLeader;
    private List<Staff> teachingStaff;
    private List<Student> enrolledStudents;

    public Module(String code, String name, int credits, Department department, Staff moduleLeader)
    {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.department = department;
        this.moduleLeader = moduleLeader;
    }

    public void addTeachingStaff(Staff staff)
    {
        if (staff.getWeeklyHours() > 0 && staff.getDepartment() == this.department)
        {
            teachingStaff.add(staff);
        }
    }

    public void enrollStudent(Student student)
    {
        // Add validation logic here
        enrolledStudents.add(student);
    }
}
