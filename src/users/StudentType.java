package users;

/**
 * Enumeration of student types in the university system.
 * Defines the different categories of students that can be enrolled,
 * including full-time, part-time, international, and degree apprenticeship students.
 * Each type has an associated display value for user interfaces.
 */
public enum StudentType
{
    /**
     * Represents a full-time student
     */
    FULL_TIME("Full time"),

    /**
     * Represents a part-time student
     */
    PART_TIME("Part time"),

    /**
     * Represents an international student
     */
    INTERNATIONAL("International"),

    /**
     * Represents a degree apprenticeship student
     */
    DA("DA");

    /**
     * The display value for this student type.
     * This is the human-readable string used in user interfaces and reports.
     */
    private final String value;

    /**
     * Constructs a new StudentType enum constant.
     *
     * @param value The display value for this student type
     */
    StudentType(String value)
    {
        this.value = value;
    }

    /**
     * Converts a string representation to its corresponding StudentType enum constant.
     * The comparison is case-insensitive.
     *
     * @param text The string to convert to a StudentType
     * @return The corresponding StudentType enum constant
     * @throws IllegalArgumentException if no matching StudentType is found
     */
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

    /**
     * Returns the display value of this student type.
     * This is the human-readable string representation used in user interfaces and reports.
     *
     * @return The display value of this student type
     */
    @Override
    public String toString()
    {
        return value;
    }
}