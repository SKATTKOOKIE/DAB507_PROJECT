package gui;

import gui.panels.DepartmentPanel;
import gui.panels.StaffListPanel;
import gui.panels.StudentListPanel;
import gui.templates.ChiUniProgressBar;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the DataManager class.
 * Verifies the behavior of data refresh operations and error handling.
 */
@DisplayName("DataManager Class Tests")
class DataManagerTest
{

    /**
     * The DataManager instance under test.
     */
    private DataManager dataManager;

    /**
     * Test properties:
     * - studentListPanel: Mock implementation of StudentListPanel for testing.
     * - staffListPanel: Mock implementation of StaffListPanel for testing.
     * - departmentPanel: Mock implementation of DepartmentPanel for testing.
     * - progressBar: Mock implementation of ChiUniProgressBar for testing.
     * - outputStream: ByteArrayOutputStream to capture console output during tests.
     * - originalOut: Stores the original System.out PrintStream to restore after tests.
     */
    private TestStudentListPanel studentListPanel;
    private TestStaffListPanel staffListPanel;
    private TestDepartmentPanel departmentPanel;
    private TestProgressBar progressBar;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Mock implementation of StudentListPanel for testing.
     */
    static class TestStudentListPanel extends StudentListPanel
    {
        private boolean refreshCalled = false;
        private RuntimeException exceptionToThrow = null;

        @Override
        public void refreshData()
        {
            if (exceptionToThrow != null)
            {
                throw exceptionToThrow;
            }
            refreshCalled = true;
        }

        public boolean wasRefreshCalled()
        {
            return refreshCalled;
        }

        public void setExceptionToThrow(RuntimeException e)
        {
            exceptionToThrow = e;
        }

        public void reset()
        {
            refreshCalled = false;
            exceptionToThrow = null;
        }
    }

    /**
     * Mock implementation of StaffListPanel for testing.
     */
    static class TestStaffListPanel extends StaffListPanel
    {
        private boolean refreshCalled = false;

        @Override
        public void refreshData()
        {
            refreshCalled = true;
        }

        public boolean wasRefreshCalled()
        {
            return refreshCalled;
        }

        public void reset()
        {
            refreshCalled = false;
        }
    }

    /**
     * Mock implementation of DepartmentPanel for testing.
     */
    static class TestDepartmentPanel extends DepartmentPanel
    {
        private boolean refreshCalled = false;

        @Override
        public void refreshData()
        {
            refreshCalled = true;
        }

        public boolean wasRefreshCalled()
        {
            return refreshCalled;
        }

        public void reset()
        {
            refreshCalled = false;
        }
    }

    /**
     * Mock implementation of ChiUniProgressBar for testing.
     */
    static class TestProgressBar extends ChiUniProgressBar
    {
        private boolean isVisible = false;
        private String currentMessage = "";

        public TestProgressBar()
        {
            super(new JFrame(), "Test", "Test");
        }

        @Override
        public void showProgress()
        {
            isVisible = true;
        }

        @Override
        public void hideProgress()
        {
            isVisible = false;
        }

        @Override
        public void updateMessage(String message)
        {
            currentMessage = message;
        }

        public boolean isProgressVisible()
        {
            return isVisible;
        }

        public String getCurrentMessage()
        {
            return currentMessage;
        }

        public void reset()
        {
            isVisible = false;
            currentMessage = "";
        }
    }

    /**
     * Sets up the test environment before each test method.
     * Initialises the DataManager instance and mock objects.
     */
    @BeforeEach
    void setup()
    {
        System.setOut(new PrintStream(outputStream));
        outputStream.reset();
        System.out.println("\n=== Starting DataManager Test ===");

        studentListPanel = new TestStudentListPanel();
        staffListPanel = new TestStaffListPanel();
        departmentPanel = new TestDepartmentPanel();
        progressBar = new TestProgressBar();

        dataManager = new DataManager(
                studentListPanel,
                staffListPanel,
                departmentPanel,
                progressBar
        );

        System.out.println("✓ Test setup completed");
    }

    /**
     * Cleans up the test environment after each test method.
     * Resets the mock objects and restores the original console output.
     */
    @AfterEach
    void cleanup()
    {
        System.setOut(originalOut);
        studentListPanel.reset();
        staffListPanel.reset();
        departmentPanel.reset();
        progressBar.reset();
    }

    /**
     * Prints a message indicating the completion of all tests.
     */
    @AfterAll
    static void teardown()
    {
        System.out.println("\n=== DataManager Tests Completed Successfully ===");
    }

    /**
     * Prints the result of a test.
     *
     * @param testName The name of the test.
     * @param passed   Indicates whether the test passed or failed.
     */
    private void printTestResult(String testName, boolean passed)
    {
        System.setOut(originalOut);
        System.out.printf("%s %s%n", passed ? "✓" : "✗", testName);
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Nested test class for specific data refresh tests.
     */
    @Nested
    @DisplayName("Specific Data Refresh Tests")
    class SpecificDataRefreshTests
    {

        /**
         * Tests the refresh of student data.
         *
         * @throws InterruptedException if the test is interrupted.
         */
        @Test
        @DisplayName("refreshSpecificData should handle STUDENTS refresh")
        void testRefreshStudentData() throws InterruptedException
        {
            CountDownLatch latch = new CountDownLatch(1);

            // Create a separate thread to check when refresh is called
            Thread checkThread = new Thread(() ->
            {
                while (!studentListPanel.wasRefreshCalled())
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                }
                latch.countDown();
            });
            checkThread.start();

            dataManager.refreshSpecificData(DataManager.DataType.STUDENTS);

            boolean completed = latch.await(2, TimeUnit.SECONDS);
            assertTrue(completed, "Student data refresh should complete");
            assertTrue(progressBar.isProgressVisible(), "Progress bar should be shown");
            assertTrue(studentListPanel.wasRefreshCalled(), "Student list refresh should be called");

            printTestResult("Student data refresh", completed);
        }

        /**
         * Tests the refresh of staff data.
         *
         * @throws InterruptedException if the test is interrupted.
         */
        @Test
        @DisplayName("refreshSpecificData should handle STAFF refresh")
        void testRefreshStaffData() throws InterruptedException
        {
            CountDownLatch latch = new CountDownLatch(1);

            Thread checkThread = new Thread(() ->
            {
                while (!staffListPanel.wasRefreshCalled())
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                }
                latch.countDown();
            });
            checkThread.start();

            dataManager.refreshSpecificData(DataManager.DataType.STAFF);

            boolean completed = latch.await(2, TimeUnit.SECONDS);
            assertTrue(completed, "Staff data refresh should complete");
            assertTrue(progressBar.isProgressVisible(), "Progress bar should be shown");
            assertTrue(staffListPanel.wasRefreshCalled(), "Staff list refresh should be called");

            printTestResult("Staff data refresh", completed);
        }
    }

    /**
     * Nested test class for error handling tests.
     */
    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests
    {

        /**
         * Tests error handling during data refresh.
         *
         * @throws InterruptedException if the test is interrupted.
         */
        @Test
        @DisplayName("Should handle exceptions during data refresh")
        void testRefreshDataWithError() throws InterruptedException
        {
            CountDownLatch latch = new CountDownLatch(1);
            studentListPanel.setExceptionToThrow(new RuntimeException("Test error"));

            Thread checkThread = new Thread(() ->
            {
                while (!progressBar.getCurrentMessage().contains("Error"))
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                }
                latch.countDown();
            });
            checkThread.start();

            dataManager.refreshSpecificData(DataManager.DataType.STUDENTS);

            boolean completed = latch.await(2, TimeUnit.SECONDS);
            assertTrue(completed, "Error handling should complete");
            assertTrue(progressBar.getCurrentMessage().contains("Error"),
                    "Progress bar should show error message");

            printTestResult("Error handling", completed);
        }
    }

    /**
     * Nested test class for progress tracking tests.
     */
    @Nested
    @DisplayName("Progress Tracking Tests")
    class ProgressTrackingTests
    {

        /**
         * Tests progress tracking during operations.
         *
         * @throws InterruptedException if the test is interrupted.
         */
        @Test
        @DisplayName("Should update progress bar during operations")
        void testProgressTracking() throws InterruptedException
        {
            AtomicBoolean messageUpdated = new AtomicBoolean(false);
            CountDownLatch latch = new CountDownLatch(1);

            Thread checkThread = new Thread(() ->
            {
                while (!messageUpdated.get())
                {
                    if (!progressBar.getCurrentMessage().isEmpty())
                    {
                        messageUpdated.set(true);
                        latch.countDown();
                    }
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }
                }
            });
            checkThread.start();

            dataManager.refreshSpecificData(DataManager.DataType.STUDENTS);

            boolean completed = latch.await(2, TimeUnit.SECONDS);
            assertTrue(completed, "Progress tracking should work");
            assertFalse(progressBar.getCurrentMessage().isEmpty(),
                    "Progress bar should show messages");

            printTestResult("Progress tracking", completed);
        }
    }
}