package file_handling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Processes JSON files using GSON library.
 * Handles both JSON arrays and objects at the root level.
 * This class is final to prevent further inheritance.
 */
public final class JsonProcessor extends FileProcessor
{
    private JsonArray jsonArray;
    private JsonObject jsonObject;
    private final Gson gson;
    private boolean isArray;

    /**
     * Constructs a JsonProcessor with the specified file path.
     *
     * @param filePath Path to the JSON file to be processed
     */
    public JsonProcessor(String filePath)
    {
        super(filePath);
        this.gson = new Gson();
    }

    /**
     * Processes the JSON file by validating, reading, and parsing its content.
     *
     * @throws IOException if file operations fail
     */
    @Override
    public void processFile() throws IOException
    {
        validateFile();
        readFile();
        parseContent();
    }

    /**
     * Validates if the file has a .json extension.
     *
     * @throws IllegalArgumentException if file is not a JSON file
     */
    @Override
    protected void validateFile()
    {
        if (!filePath.endsWith(".json"))
        {
            throw new IllegalArgumentException("File must be a JSON file");
        }
    }

    /**
     * Reads the JSON file content and attempts to parse it as either
     * a JSON array or object.
     *
     * @throws IOException if file reading operations fail
     */
    @Override
    protected void readFile() throws IOException
    {
        try (FileReader reader = new FileReader(filePath))
        {
            try
            {
                jsonArray = gson.fromJson(reader, JsonArray.class);
                isArray = true;
            }
            catch (JsonSyntaxException e)
            {
                reader.close();
                try (FileReader newReader = new FileReader(filePath))
                {
                    jsonObject = gson.fromJson(newReader, JsonObject.class);
                    isArray = false;
                }
            }
        }
    }

    /**
     * Additional parsing operations if needed after initial read.
     */
    @Override
    protected void parseContent()
    {
        // Additional parsing if needed
    }

    /**
     * Converts the JSON array content to a List of specified type.
     *
     * @param <T>      The type to convert the JSON elements to
     * @param classOfT The class of type T
     * @return List of objects of type T
     * @throws IllegalStateException if the JSON content is not an array
     */
    public <T> List<T> parseJsonToList(Class<T> classOfT)
    {
        if (!isArray)
        {
            throw new IllegalStateException("JSON content is not an array");
        }

        return jsonArray.asList().stream()
                .map(element -> gson.fromJson(element, classOfT))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Returns the parsed JSON content either as JsonArray or JsonObject.
     *
     * @return Object containing the parsed content (either JsonArray or JsonObject)
     */
    public Object getJsonContent()
    {
        return isArray ? jsonArray : jsonObject;
    }


}