package testframework;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom test runner for executing test methods and reporting results.
 */
public class TestRunner {
    private final ByteArrayOutputStream outputStream;
    private final PrintStream originalOut;
    private int passedTests = 0;
    private int failedTests = 0;
    private final List<String> failureMessages = new ArrayList<>();

    public TestRunner(ByteArrayOutputStream outputStream, PrintStream originalOut) {
        this.outputStream = outputStream;
        this.originalOut = originalOut;
    }

    public void runTests(Object testClass) {
        originalOut.println("=== Starting Tests for " + testClass.getClass().getSimpleName() + " ===\n");

        Method[] methods = testClass.getClass().getDeclaredMethods();

        // Run all test methods
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                runTestMethod(testClass, method);
            }
        }

        // Print final summary to original output
        printSummary();
    }

    private void runTestMethod(Object testClass, Method method) {
        try {
            // Call setup before each test method
            callSetupMethod(testClass);

            originalOut.println("\nRunning test: " + method.getName());
            method.setAccessible(true);
            method.invoke(testClass);
            passedTests++;
            originalOut.println("✓ " + method.getName() + " PASSED");

            // Call cleanup after each test method
            callCleanupMethod(testClass);
        } catch (Exception e) {
            failedTests++;
            String failureMessage = "✗ " + method.getName() + " FAILED: " +
                    getRootCause(e).getMessage();
            failureMessages.add(failureMessage);
            originalOut.println(failureMessage);

            // Still try to cleanup even if test failed
            try {
                callCleanupMethod(testClass);
            } catch (Exception ignored) {
                // Ignore cleanup failures after test failure
            }
        }
    }

    private void callSetupMethod(Object testClass) {
        try {
            Method setupMethod = testClass.getClass().getDeclaredMethod("setup");
            setupMethod.setAccessible(true);
            setupMethod.invoke(testClass);
        } catch (Exception e) {
            originalOut.println("Warning: Setup method failed: " + e.getMessage());
        }
    }

    private void callCleanupMethod(Object testClass) {
        try {
            Method cleanupMethod = testClass.getClass().getDeclaredMethod("cleanup");
            cleanupMethod.setAccessible(true);
            cleanupMethod.invoke(testClass);
        } catch (Exception ignored) {
            // Ignore cleanup failures
        }
    }

    private void printSummary() {
        originalOut.println("\n=== Test Summary ===");
        originalOut.println("Passed: " + passedTests);
        originalOut.println("Failed: " + failedTests);
        originalOut.println("Total: " + (passedTests + failedTests));

        if (!failureMessages.isEmpty()) {
            originalOut.println("\nFailures:");
            failureMessages.forEach(originalOut::println);
        }
    }

    private Throwable getRootCause(Exception e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }
}