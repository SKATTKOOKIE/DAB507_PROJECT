package users;

public enum StudentType
{
    FULL_TIME("Full time"),
    PART_TIME("Part time"),
    INTERNATIONAL("International"),
    DA("DA");

    private final String value;

    StudentType(String value)
    {
        this.value = value;
    }

    public static StudentType fromString(String text)
    {
        for (StudentType type : StudentType.values())
        {
            if (type.value.equalsIgnoreCase(text))
            {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }

    @Override
    public String toString()
    {
        return value;
    }
}