package testframework;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom test runner for executing test methods and reporting results.
 * This class handles the execution of test methods, manages test lifecycle,
 * and provides detailed reporting of test results.
 * <p>
 * Features:
 * - Automatic test method discovery and execution
 * - Setup and cleanup method handling
 * - Detailed test reporting including pass/fail counts
 * - Failure message collection and reporting
 */
public class TestRunner
{
    /**
     * Stream for capturing test output
     */
    private final ByteArrayOutputStream outputStream;

    /**
     * Original system output stream
     */
    private final PrintStream originalOut;

    /**
     * Counter for passed tests
     */
    private int passedTests = 0;

    /**
     * Counter for failed tests
     */
    private int failedTests = 0;

    /**
     * Collection of failure messages for reporting
     */
    private final List<String> failureMessages = new ArrayList<>();

    /**
     * Constructs a new TestRunner with specified output streams.
     *
     * @param outputStream The stream to capture test output
     * @param originalOut  The original system output stream for reporting
     */
    public TestRunner(ByteArrayOutputStream outputStream, PrintStream originalOut)
    {
        this.outputStream = outputStream;
        this.originalOut = originalOut;
    }

    /**
     * Runs all test methods in the provided test class.
     * Discovers and executes all methods starting with "test".
     *
     * @param testClass The test class instance to run
     */
    public void runTests(Object testClass)
    {
        originalOut.println("=== Starting Tests for " + testClass.getClass().getSimpleName() + " ===\n");

        Method[] methods = testClass.getClass().getDeclaredMethods();

        // Run all test methods
        for (Method method : methods)
        {
            if (method.getName().startsWith("test"))
            {
                runTestMethod(testClass, method);
            }
        }

        // Print final summary to original output
        printSummary();
        System.exit(0);
    }

    /**
     * Executes a single test method with setup and cleanup.
     * Handles exceptions and records test results.
     *
     * @param testClass The test class instance
     * @param method    The test method to execute
     */
    private void runTestMethod(Object testClass, Method method)
    {
        try
        {
            callSetupMethod(testClass);

            originalOut.println("\nRunning test: " + method.getName());
            method.setAccessible(true);
            method.invoke(testClass);
            passedTests++;
            originalOut.println("✓ " + method.getName() + " PASSED");

            callCleanupMethod(testClass);
        }
        catch (Exception e)
        {
            failedTests++;
            String failureMessage = "✗ " + method.getName() + " FAILED: " +
                    getRootCause(e).getMessage();
            failureMessages.add(failureMessage);
            originalOut.println(failureMessage);

            try
            {
                callCleanupMethod(testClass);
            }
            catch (Exception ignored)
            {
                // Ignore cleanup failures after test failure
            }
        }
    }

    /**
     * Calls the setup method on the test class if it exists.
     *
     * @param testClass The test class instance
     */
    private void callSetupMethod(Object testClass)
    {
        try
        {
            Method setupMethod = testClass.getClass().getDeclaredMethod("setup");
            setupMethod.setAccessible(true);
            setupMethod.invoke(testClass);
        }
        catch (Exception e)
        {
            originalOut.println("Warning: Setup method failed: " + e.getMessage());
        }
    }

    /**
     * Calls the cleanup method on the test class if it exists.
     *
     * @param testClass The test class instance
     */
    private void callCleanupMethod(Object testClass)
    {
        try
        {
            Method cleanupMethod = testClass.getClass().getDeclaredMethod("cleanup");
            cleanupMethod.setAccessible(true);
            cleanupMethod.invoke(testClass);
        }
        catch (Exception ignored)
        {
            // Ignore cleanup failures
        }
    }

    /**
     * Prints a summary of test execution results.
     * Includes total tests run, passed tests, failed tests,
     * and details of any failures.
     */
    private void printSummary()
    {
        originalOut.println("\n=== Test Summary ===");
        originalOut.println("Passed: " + passedTests);
        originalOut.println("Failed: " + failedTests);
        originalOut.println("Total: " + (passedTests + failedTests));

        if (!failureMessages.isEmpty())
        {
            originalOut.println("\nFailures:");
            failureMessages.forEach(originalOut::println);
        }
    }

    /**
     * Gets the root cause of an exception by traversing the cause chain.
     *
     * @param e The exception to analyse
     * @return The root cause throwable
     */
    private Throwable getRootCause(Exception e)
    {
        Throwable cause = e;
        while (cause.getCause() != null)
        {
            cause = cause.getCause();
        }
        return cause;
    }
}