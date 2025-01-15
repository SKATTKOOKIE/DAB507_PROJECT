package business;

import com.google.gson.*;
import file_handling.JsonProcessor;

import java.lang.reflect.Type;

public class CourseDeserializer implements JsonDeserializer<Course>
{
    private JsonProcessor jsonProcessor;

    public CourseDeserializer(String filePath)
    {
        this.jsonProcessor = new JsonProcessor(filePath);
    }

    @Override
    public Course deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        try
        {
            jsonProcessor.processFile();
            JsonObject jsonObject = json.getAsJsonObject();
            Course course = new Course();

            // Get the course name and code using JsonProcessor's parsed content
            String name = getStringValue(jsonObject, "name");
            String code = getStringValue(jsonObject, "code");

            course.setCourseTitle(name);
            course.setCourseId(code);

            // Get the department and map it to DepartmentId
            String departmentName = getStringValue(jsonObject, "department");
            DepartmentId departmentId = DepartmentMatcher.findDepartmentId(departmentName);
            course.setDepartmentId(departmentId);

            return course;
        }
        catch (Exception e)
        {
            throw new JsonParseException("Error processing JSON file: " + e.getMessage());
        }
    }

    private String getStringValue(JsonObject jsonObject, String key)
    {
        JsonElement element = jsonObject.get(key);
        return (element != null && !element.isJsonNull()) ? element.getAsString() : null;
    }
}