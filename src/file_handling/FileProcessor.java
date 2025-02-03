package file_handling;

import java.io.IOException;

import file_handling.interfaces.IFileProcessor;

/**
 * Abstract base class for processing different types of files.
 * Implements template method pattern for file processing workflow.
 */
public abstract class FileProcessor implements IFileProcessor
{
    protected String filePath;

    /**
     * Constructs a FileProcessor with the specified file path.
     *
     * @param filePath Path to the file to be processed
     */
    public FileProcessor(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * Template method that defines the file processing algorithm.
     * Subclasses should not override this method.
     *
     * @throws IOException if file operations fail
     */
    public abstract void processFile() throws IOException;

    /**
     * Validates if the file meets the required format and existence criteria.
     *
     * @throws IllegalArgumentException if file validation fails
     */
    public abstract void validateFile();

    /**
     * Reads the contents of the file into memory.
     *
     * @throws IOException if file reading operations fail
     */
    public abstract void readFile() throws IOException;

    /**
     * Parses the file content into the appropriate data structure.
     *
     * @throws IOException if parsing operations fail
     */
    public abstract void parseContent() throws IOException;
}