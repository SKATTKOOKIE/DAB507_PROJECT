package gui;

import testframework.*;
import gui.panels.DepartmentPanel;
import gui.panels.StaffListPanel;
import gui.panels.StudentListPanel;
import gui.templates.ChiUniProgressBar;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataManagerTest extends BaseTest
{
    private DataManager dataManager;
    private TestStudentListPanel studentListPanel;
    private TestStaffListPanel staffListPanel;
    private TestDepartmentPanel departmentPanel;
    private TestProgressBar progressBar;

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

    @Override
    protected void setup()
    {
        super.setup();
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
    }

    @Override
    protected void cleanup()
    {
        super.cleanup();
        studentListPanel.reset();
        staffListPanel.reset();
        departmentPanel.reset();
        progressBar.reset();
    }

    public void testRefreshStudentData() throws InterruptedException
    {
        CountDownLatch latch = new CountDownLatch(1);

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
        Assert.assertTrue(completed, "Student data refresh should complete");
        Assert.assertTrue(progressBar.isProgressVisible(), "Progress bar should be shown");
        Assert.assertTrue(studentListPanel.wasRefreshCalled(), "Student list refresh should be called");
    }

    public void testRefreshStaffData() throws InterruptedException
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
        Assert.assertTrue(completed, "Staff data refresh should complete");
        Assert.assertTrue(progressBar.isProgressVisible(), "Progress bar should be shown");
        Assert.assertTrue(staffListPanel.wasRefreshCalled(), "Staff list refresh should be called");
    }

    public void testRefreshDataWithError() throws InterruptedException
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
        Assert.assertTrue(completed, "Error handling should complete");
        Assert.assertTrue(progressBar.getCurrentMessage().contains("Error"),
                "Progress bar should show error message");
    }

    public void testProgressTracking() throws InterruptedException
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
        Assert.assertTrue(completed, "Progress tracking should work");
        Assert.assertFalse(progressBar.getCurrentMessage().isEmpty(),
                "Progress bar should show messages");
    }

    public static void main(String[] args)
    {
        new DataManagerTest().runTests();
    }
}