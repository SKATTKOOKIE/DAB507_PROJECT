package gui;

import business.Course;
import business.Module;
import business.StaffModuleAssignment;
import business.StudentModuleAssignment;
import file_handling.FilePathHandler;
import gui.panels.DepartmentPanel;
import gui.panels.StaffListPanel;
import gui.panels.StudentListPanel;
import gui.templates.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages data initialisation and refresh operations for the University Management System.
 */
public class DataManager
{
    /**
     * Enum defining the different types of data that can be refreshed
     */
    public enum DataType
    {
        STUDENTS,
        STAFF,
        COURSES,
        MODULES,
        ASSIGNMENTS,
        ALL
    }

    private final StudentListPanel studentListPanel;
    private final StaffListPanel staffListPanel;
    private final DepartmentPanel departmentPanel;
    private final ChiUniProgressBar progressBar;
    private final AtomicInteger completedTasks;
    private static final int TOTAL_OPERATIONS = 5; // Updated to include modules

    public DataManager(StudentListPanel studentListPanel,
                       StaffListPanel staffListPanel,
                       DepartmentPanel departmentPanel,
                       ChiUniProgressBar progressBar)
    {
        this.studentListPanel = studentListPanel;
        this.staffListPanel = staffListPanel;
        this.departmentPanel = departmentPanel;
        this.progressBar = progressBar;
        this.completedTasks = new AtomicInteger(0);
    }

    public void refreshSpecificData(DataType dataType)
    {
        progressBar.showProgress();
        String message = "Refreshing " + dataType.toString().toLowerCase() + " data...";
        progressBar.updateMessage(message);

        SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                try
                {
                    publish(message);
                    executeSpecificRefresh(dataType);
                    publish(dataType.toString().toLowerCase() + " refresh complete!");
                    Thread.sleep(500);
                }
                catch (Exception e)
                {
                    publish("Error refreshing " + dataType.toString().toLowerCase() + ": " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks)
            {
                if (!chunks.isEmpty())
                {
                    progressBar.updateMessage(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done()
            {
                progressBar.hideProgress();
            }
        };
        worker.execute();
    }

    private void executeSpecificRefresh(DataType dataType) throws Exception
    {
        switch (dataType)
        {
            case STUDENTS:
                studentListPanel.refreshData();
                break;
            case STAFF:
                staffListPanel.refreshData();
                break;
            case COURSES:
                refreshCourseData();
                break;
            case MODULES:
                refreshModuleData();
                break;
            case ASSIGNMENTS:
                refreshAssignments();
                break;
            case ALL:
                refreshAll();
                break;
        }
    }

    public void refreshData(String message)
    {
        refreshSpecificData(DataType.ALL);
    }

    private void refreshAll() throws Exception
    {
        CountDownLatch latch = new CountDownLatch(TOTAL_OPERATIONS);
        completedTasks.set(0);

        executeWorker("Refreshing student data...",
                studentListPanel::refreshData,
                latch,
                "student");

        executeWorker("Refreshing staff data...",
                staffListPanel::refreshData,
                latch,
                "staff");

        executeWorker("Refreshing course data...",
                this::refreshCourseData,
                latch,
                "course");

        executeWorker("Refreshing module data...",
                this::refreshModuleData,
                latch,
                "module");

        executeWorker("Checking assignments...",
                this::refreshAssignments,
                latch,
                "assignments");

        latch.await();
    }

    private void refreshCourseData() throws IOException
    {
        Course.getAll(); // Force reload course data
        SwingUtilities.invokeLater(() -> departmentPanel.refreshData());
    }

    private void refreshModuleData() throws IOException
    {
        Module.getAll(); // Force reload module data
        SwingUtilities.invokeLater(() -> departmentPanel.refreshData());
    }

    private void refreshAssignments() throws IOException
    {
        File staffAssignmentsFile = new File(FilePathHandler.ASSIGNED_STAFF_FILE.getNormalisedPath());
        File studentAssignmentsFile = new File(FilePathHandler.ASSIGNED_STUDENTS_FILE.getNormalisedPath());

        if (!staffAssignmentsFile.exists() || !studentAssignmentsFile.exists())
        {
            StaffModuleAssignment.generateInitialAssignments();
            StudentModuleAssignment.generateInitialAssignments();
        }
    }

    private void executeWorker(String startMessage, WorkerOperation operation, CountDownLatch latch, String workerName)
    {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                try
                {
                    publish(startMessage);
                    operation.execute();
                    publish(workerName + " data refresh complete!");
                }
                catch (Exception e)
                {
                    publish("Error refreshing " + workerName + " data: " + e.getMessage());
                }
                finally
                {
                    latch.countDown();
                    updateProgress();
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks)
            {
                if (!chunks.isEmpty())
                {
                    progressBar.updateMessage(chunks.get(chunks.size() - 1));
                }
            }
        };
        worker.execute();
    }

    private void updateProgress()
    {
        int completed = completedTasks.incrementAndGet();
        progressBar.updateMessage(String.format("Completed %d of %d operations...",
                completed, TOTAL_OPERATIONS));
    }

    @FunctionalInterface
    private interface WorkerOperation
    {
        void execute() throws Exception;
    }

    // Add this method to DataManager class:

    /**
     * Initializes application data in a background thread.
     * Checks for existing assignments files and generates initial assignments if needed.
     */
    public void initialiseData()
    {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                try
                {
                    publish("Checking file system...");
                    File staffAssignmentsFile = new File(FilePathHandler.ASSIGNED_STAFF_FILE.getNormalisedPath());
                    File studentAssignmentsFile = new File(FilePathHandler.ASSIGNED_STUDENTS_FILE.getNormalisedPath());

                    if (!staffAssignmentsFile.exists() || !studentAssignmentsFile.exists())
                    {
                        publish("Generating initial assignments...");
                        StaffModuleAssignment.generateInitialAssignments();
                        StudentModuleAssignment.generateInitialAssignments();
                    }

                    // Force reload of all data
                    publish("Loading course data...");
                    Course.getAll();
                    publish("Loading module data...");
                    Module.getAll();

                    publish("Loading complete!");
                    Thread.sleep(500); // Brief pause to show completion message
                }
                catch (IOException e)
                {
                    publish("Error: " + e.getMessage());
                    System.err.println("Error checking/generating assignments: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks)
            {
                if (chunks != null && !chunks.isEmpty())
                {
                    progressBar.updateMessage(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done()
            {
                progressBar.hideProgress();
            }
        };
        worker.execute();
    }
}