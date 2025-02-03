package testframework;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Base test class providing common functionality for all test classes.
 */
public abstract class BaseTest {
    protected final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    protected final PrintStream originalOut = System.out;

    /**
     * Setup method to be called before each test.
     * Override this method in test classes to add specific setup logic.
     */
    protected void setup() {
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Cleanup method to be called after each test.
     * Override this method in test classes to add specific cleanup logic.
     */
    protected void cleanup() {
        System.setOut(originalOut);
    }

    /**
     * Runs all tests in the class.
     */
    public void runTests() {
        new TestRunner(outputStream, originalOut).runTests(this);
    }
}