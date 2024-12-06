package business;

public enum Department
{
    ENGINEERING("Engineering"),
    CHILDHOOD("Childhood"),
    DANCE("Dance"),
    CREATIVE("Creative"),
    ENGINEERING_COMPUTING("Engineering Computing and Design"),
    FINE_ART("Fine Art"),
    HUMANITIES("Humanities"),
    LAW("Law"),
    NURSING("Nursing"),
    OUTDOOR_ADVENTURE("Outdoor Adventure Education"),
    PHYSIOTHERAPY("Physiotherapy"),
    SCHOOL_OF_MUSIC("School of Music"),
    SOCIAL_WORK("Social Work"),
    THEATRE("Theatre"),
    NONE("");

    private final String displayName;

    Department(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public static Department fromString(String text)
    {
        if (text == null || text.trim().isEmpty())
        {
            return NONE;
        }

        for (Department dept : Department.values())
        {
            if (dept.displayName.equalsIgnoreCase(text.trim()))
            {
                return dept;
            }
        }
        throw new IllegalArgumentException("No department found for: " + text);
    }

    public boolean isAcademic() {
        return this != NONE;
    }
}