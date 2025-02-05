package testframework;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Base test class providing common functionality for all test classes.
 * This abstract class implements basic test infrastructure including:
 * - Output stream management for test isolation
 * - Setup and cleanup hooks for test lifecycle management
 * - Test execution functionality
 * <p>
 * Test classes should extend this class to inherit the testing framework capabilities.
 */
public abstract class BaseTest
{
    /**
     * Stream to capture test output
     */
    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * Original system output stream preserved for restoration
     */
    protected final PrintStream originalOut = System.out;

    /**
     * Setup method to be called before each test.
     * This method redirects system output to a captured stream for test isolation.
     * Test classes should override this method to add specific setup logic by calling
     * super.setup() first, then adding their own initialization code.
     */
    protected void setup()
    {
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Cleanup method to be called after each test.
     * This method restores the original system output stream.
     * Test classes should override this method to add specific cleanup logic by
     * calling super.cleanup() after their cleanup code.
     */
    protected void cleanup()
    {
        System.setOut(originalOut);
    }

    /**
     * Runs all tests in the class using the TestRunner.
     * This method will execute all methods in the class that start with "test".
     * Test results will be captured and reported through the TestRunner.
     */
    public void runTests()
    {
        new TestRunner(outputStream, originalOut).runTests(this);
    }
}