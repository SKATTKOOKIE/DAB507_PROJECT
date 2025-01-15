package business;

import java.util.HashMap;
import java.util.Map;

class DepartmentMatcher
{
    private static final Map<String, DepartmentId> DEPARTMENT_MAPPINGS = new HashMap<>();

    static
    {
        // Initialise direct mappings from JSON department strings to DepartmentId enums
        DEPARTMENT_MAPPINGS.put("Engineering", DepartmentId.ENG);
        DEPARTMENT_MAPPINGS.put("Childhood", DepartmentId.CHI);
        DEPARTMENT_MAPPINGS.put("Dance", DepartmentId.DAN);
        DEPARTMENT_MAPPINGS.put("Creative", DepartmentId.CRE);
        DEPARTMENT_MAPPINGS.put("Engineering Computing and Design", DepartmentId.ECD);
        DEPARTMENT_MAPPINGS.put("Fine Art", DepartmentId.FIA);
        DEPARTMENT_MAPPINGS.put("Humanities", DepartmentId.HUM);
        DEPARTMENT_MAPPINGS.put("Law", DepartmentId.LAW);
        DEPARTMENT_MAPPINGS.put("Nursing", DepartmentId.NUR);
        DEPARTMENT_MAPPINGS.put("Outdoor Adventure Education", DepartmentId.OAE);
        DEPARTMENT_MAPPINGS.put("Physiotherapy", DepartmentId.PHY);
        DEPARTMENT_MAPPINGS.put("School of Music", DepartmentId.SOM);
        DEPARTMENT_MAPPINGS.put("Social Work", DepartmentId.SOW);
        DEPARTMENT_MAPPINGS.put("Theatre", DepartmentId.THE);
    }

    public static DepartmentId findDepartmentId(String departmentName)
    {
        if (departmentName == null)
        {
            return DepartmentId.UNKNOWN;
        }

        return DEPARTMENT_MAPPINGS.getOrDefault(departmentName, DepartmentId.UNKNOWN);
    }
}