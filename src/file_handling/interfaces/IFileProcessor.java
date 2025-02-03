package file_handling.interfaces;

import java.io.IOException;

/**
 * Interface defining the contract for file processing operations.
 * Provides the base structure for different types of file processors.
 */
public interface IFileProcessor
{

    /**
     * Processes the file by executing the validation, reading and parsing steps.
     * This is the main method that orchestrates the file processing workflow.
     *
     * @throws IOException              if file operations fail
     * @throws IllegalArgumentException if file validation fails
     */
    void processFile() throws IOException;

    /**
     * Validates if the file meets the required format and existence criteria.
     *
     * @throws IllegalArgumentException if file validation fails
     */
    void validateFile();

    /**
     * Reads the contents of the file into memory.
     *
     * @throws IOException if file reading operations fail
     */
    void readFile() throws IOException;

    /**
     * Parses the file content into the appropriate data structure.
     *
     * @throws IOException if parsing operations fail
     */
    void parseContent() throws IOException;

    /**
     * Gets the path of the file being processed.
     *
     * @return String representing the file path
     */
    String getFilePath();
}