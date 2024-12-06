import file_handling.JsonProcessor;
import file_handling.CsvProcessor;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            JsonProcessor jsonProcessor = new JsonProcessor("data/staff.json");
            jsonProcessor.processFile();
            System.out.println("JSON Data: " + jsonProcessor.getJsonContent().toString());

            CsvProcessor csvProcessor = new CsvProcessor("data/Courses.csv");
            csvProcessor.processFile();
            System.out.println("CSV Headers: " + csvProcessor.getHeaders());
            System.out.println("CSV Data as JSON: " + csvProcessor.getJsonContent().toString());

        }
        catch (IOException e)
        {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
}