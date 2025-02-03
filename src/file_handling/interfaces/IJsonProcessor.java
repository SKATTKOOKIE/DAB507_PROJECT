package file_handling.interfaces;

import java.util.List;

/**
 * Interface defining JSON-specific processing operations.
 * Extends IFileProcessor to combine general file processing with JSON capabilities.
 */
public interface IJsonProcessor extends IFileProcessor
{

    /**
     * Converts the JSON array content to a List of specified type.
     *
     * @param <T>      The type to convert the JSON elements to
     * @param classOfT The class of type T
     * @return List of objects of type T
     * @throws IllegalStateException if the JSON content is not an array
     */
    <T> List<T> parseJsonToList(Class<T> classOfT);

    /**
     * Returns the parsed JSON content either as JsonArray or JsonObject.
     *
     * @return Object containing the parsed content (either JsonArray or JsonObject)
     */
    Object getJsonContent();

    /**
     * Checks if the parsed JSON content is an array.
     *
     * @return true if the content is a JSON array, false if it's a JSON object
     */
    boolean isArrayContent();
}