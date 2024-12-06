package users;

public enum StudentType
{
    FULL_TIME("Full time"),
    PART_TIME("Part time"),
    INTERNATIONAL("International"),
    DA("DA");

    private final String displayName;

    StudentType(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public static StudentType fromString(String text)
    {
        for (StudentType type : StudentType.values())
        {
            if (type.displayName.equalsIgnoreCase(text.trim()))
            {
                return type;
            }
        }
        throw new IllegalArgumentException("No student type found for: " + text);
    }
}
