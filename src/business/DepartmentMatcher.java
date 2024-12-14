package business;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DepartmentMatcher
{
    private static final Map<DepartmentId, List<String>> DEPARTMENT_KEYWORDS = new HashMap<>();

    static
    {
        // Initialize keywords for each department
        DEPARTMENT_KEYWORDS.put(DepartmentId.THE, Arrays.asList(
                "theatre", "musical", "drama", "stage", "performance"
        ));
        DEPARTMENT_KEYWORDS.put(DepartmentId.CHI, Arrays.asList(
                "childhood", "early childhood", "early years"
        ));
        // Add more departments as needed
        DEPARTMENT_KEYWORDS.put(DepartmentId.SOM, Arrays.asList(
                "music", "orchestral", "vocal", "jazz"
        ));
        DEPARTMENT_KEYWORDS.put(DepartmentId.ECD, Arrays.asList(
                "computing", "computer", "design"
        ));
        // Can easily add more departments following the same pattern
    }

    public static DepartmentId findDepartmentId(String courseTitle)
    {
        String normalizedTitle = courseTitle.toLowerCase();

        for (Map.Entry<DepartmentId, List<String>> entry : DEPARTMENT_KEYWORDS.entrySet())
        {
            DepartmentId departmentId = entry.getKey();
            List<String> keywords = entry.getValue();

            if (keywords.stream()
                    .map(String::toLowerCase)
                    .anyMatch(normalizedTitle::contains))
            {
                return departmentId;
            }
        }

        return DepartmentId.UNKNOWN;
    }
}