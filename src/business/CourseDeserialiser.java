package business;

import com.google.gson.*;
import file_handling.JsonProcessor;

import java.lang.reflect.Type;

/**
 * Custom deserialiser for Course objects from JSON format.
 * This class handles the conversion of JSON data into Course objects,
 * managing the parsing of course details and their associated department information.
 * <p>
 * The class provides functionality for:
 * <ul>
 *   <li>Reading course data from JSON files</li>
 *   <li>Converting JSON elements to Course objects</li>
 *   <li>Handling department associations</li>
 *   <li>Managing error cases during deserialisation</li>
 * </ul>
 */
public class CourseDeserialiser implements JsonDeserializer<Course>
{
    /**
     * Class instance variables:
     * <ul>
     *   <li>{@code jsonProcessor} - Handles the processing and reading of JSON files</li>
     * </ul>
     */
    private final JsonProcessor jsonProcessor;

    /**
     * Constructs a new CourseDeserialiser with a specified JSON file path.
     *
     * @param filePath The path to the JSON file containing course data
     */
    public CourseDeserialiser(String filePath)
    {
        this.jsonProcessor = new JsonProcessor(filePath);
    }

    /**
     * Deserialises a JSON element into a Course object.
     * This method processes the JSON data and creates a new Course instance with
     * the extracted information, including course details and department association.
     *
     * @param json    The JsonElement to deserialise
     * @param typeOfT The type of the object to deserialise into
     * @param context The deserialisation context
     * @return A newly created Course object with the deserialised data
     * @throws JsonParseException If there is an error during JSON processing or parsing
     */
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

    /**
     * Safely extracts a string value from a JsonObject.
     * This helper method handles null checks and provides safe access to JSON values.
     *
     * @param jsonObject The JsonObject to extract the value from
     * @param key        The key of the value to extract
     * @return The string value if it exists and is not null, otherwise null
     */
    private String getStringValue(JsonObject jsonObject, String key)
    {
        JsonElement element = jsonObject.get(key);
        return (element != null && !element.isJsonNull()) ? element.getAsString() : null;
    }
}