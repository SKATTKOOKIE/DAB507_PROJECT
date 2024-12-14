package business;

import com.google.gson.*;

import java.lang.reflect.Type;

public class CourseDeserializer implements JsonDeserializer<Course>
{
    @Override
    public Course deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        JsonObject jsonObject = json.getAsJsonObject();
        Course course = new Course();

        // Get the first entry (key-value pair) from the JsonObject
        String firstKey = jsonObject.keySet().iterator().next();
        String secondKey = jsonObject.keySet().stream()
                .skip(1)
                .findFirst()
                .orElse(null);

        if (firstKey != null && secondKey != null)
        {
            String courseTitle = jsonObject.get(firstKey).getAsString();
            course.setCourseTitle(courseTitle);
            course.setCourseId(jsonObject.get(secondKey).getAsString());

            // DepartmentMatcher.findDepartmentId now returns DepartmentId enum
            DepartmentId departmentId = DepartmentMatcher.findDepartmentId(courseTitle);
            course.setDepartmentId(departmentId);
        }

        return course;
    }
}