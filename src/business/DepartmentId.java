package business;

public enum DepartmentId
{
    ENG("Engineering"),
    CHI("Childhood"),
    DAN("Dance"),
    CRE("Creative"),
    ECD("Engineering Computing and Design"),
    FIA("Fine Art"),
    HUM("Humanities"),
    LAW("Law"),
    NUR("Nursing"),
    OAE("Outdoor Adventure Education"),
    PHY("Physiotherapy"),
    SOM("School of Music"),
    SOW("Social Work"),
    THE("Theatre"),
    UNKNOWN("Unknown Department");

    private final String departmentName;

    DepartmentId(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }
}