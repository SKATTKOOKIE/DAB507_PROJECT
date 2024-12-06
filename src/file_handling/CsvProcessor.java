package file_handling;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.*;

/**
 * Processes CSV files and converts them to JSON format.
 * This class is final to prevent further inheritance.
 */
public final class CsvProcessor extends FileProcessor
{
    private JsonArray jsonContent;
    private final Gson gson;
    private List<String> headers;

    /**
     * Constructs a CsvProcessor with the specified file path.
     * @param filePath Path to the CSV file to be processed
     */
    public CsvProcessor(String filePath)
    {
        super(filePath);
        this.gson = new Gson();
        this.headers = new ArrayList<>();
    }

    @Override
    public void processFile() throws IOException
    {
        validateFile();
        readFile();
        parseContent();
    }

    @Override
    protected void validateFile()
    {
        if (!filePath.endsWith(".csv")) {
            throw new IllegalArgumentException("File must be a CSV file");
        }
    }

    @Override
    protected void readFile() throws IOException
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String headerLine = reader.readLine();
            if (headerLine != null)
            {
                headers = Arrays.asList(headerLine.split(","));
            }

            jsonContent = new JsonArray();
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] values = line.split(",");
                JsonObject row = new JsonObject();

                for (int i = 0; i < headers.size() && i < values.length; i++)
                {
                    row.addProperty(headers.get(i).trim(), values[i].trim());
                }
                jsonContent.add(row);
            }
        }
    }

    @Override
    protected void parseContent()
    {
        // Additional parsing if needed after JSON conversion
    }

    /**
     * Returns the CSV data converted to JSON format.
     * Each row is represented as a JsonObject within a JsonArray.
     * @return JsonArray containing the converted CSV data
     */
    public JsonArray getJsonContent()
    {
        return jsonContent;
    }

    /**
     * Returns the CSV headers.
     * @return List of column headers from the CSV
     */
    public List<String> getHeaders()
    {
        return new ArrayList<>(headers);
    }
}