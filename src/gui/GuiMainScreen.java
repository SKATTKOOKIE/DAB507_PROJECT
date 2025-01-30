package gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;
import java.util.List;

import business.Course;
import business.StaffModuleAssignment;
import business.StudentModuleAssignment;
import file_handling.FilePathHandler;
import gui.templates.*;


/**
 * Main graphical user interface for the University Management System.
 * This class manages the primary window of the application, including
 * the login screen, navigation, and various content panels for departments,
 * students, and staff management.
 */
public class GuiMainScreen
{
    private ChiUniFrame mainFrame;
    private ChiUniTextArea outputArea;
    private DepartmentPanel departmentPanel;
    private ChiUniPanel contentPanel;
    private StudentListPanel studentListPanel;
    private StaffListPanel staffListPanel;
    private final ChiUniProgressBar progressBar;
    private AtomicInteger completedTasks;
    private final DataManager dataManager;

    /**
     * Administrator username for login
     */
    private static final String ADMIN_USERNAME = "admin";

    /**
     * Administrator password for login
     */
    private static final String ADMIN_PASSWORD = "password";

    /**
     * Constructs a new GuiMainScreen and initialises all components.
     * Sets up the main frame, creates all panels, and initialises data.
     */
    public GuiMainScreen()
    {
        initialiseGUI();
        progressBar = new ChiUniProgressBar(mainFrame, "Loading", "Initialising system...");
        dataManager = new DataManager(studentListPanel, staffListPanel, departmentPanel, progressBar);
        progressBar.showProgress();
        show();
    }

    /**
     * initialises all GUI components and layouts.
     * Sets up the main frame, banner, content panels, and navigation buttons.
     */
    private void initialiseGUI()
    {
        mainFrame = new ChiUniFrame("University Management System");

        // Create banner panel and add it to the NORTH position
        ChiUniPanel bannerPanel = createBannerPanel();
        mainFrame.addComponent(bannerPanel, BorderLayout.NORTH);

        // Create main content panel
        contentPanel = new ChiUniPanel();
        contentPanel.setLayout(new CardLayout());

        // Create login panel with success callback
        ChiUniPanel loginPanel = new LoginPanel(mainFrame, this::showWelcomePanel);
        contentPanel.add(loginPanel, "LOGIN");

        // Create welcome panel
        ChiUniPanel welcomePanel = createWelcomePanel();
        contentPanel.add(welcomePanel, "WELCOME");

        studentListPanel = new StudentListPanel();
        ChiUniButton studentBackButton = new ChiUniButton("Back to Welcome");
        studentBackButton.addActionListener(e -> showWelcomePanel());
        studentListPanel.add(studentBackButton, BorderLayout.SOUTH);

        // Create departments panel
        departmentPanel = new DepartmentPanel();
        ChiUniButton backButton = new ChiUniButton("Back to Welcome");
        backButton.addActionListener(e -> showWelcomePanel());
        departmentPanel.add(backButton, BorderLayout.SOUTH);

        // Create staff panel
        staffListPanel = new StaffListPanel();
        ChiUniButton staffBackButton = new ChiUniButton("Back to Welcome");
        staffBackButton.addActionListener(e -> showWelcomePanel());
        staffListPanel.add(staffBackButton, BorderLayout.SOUTH);

        contentPanel.add(departmentPanel, "DEPARTMENTS");
        contentPanel.add(studentListPanel, "STUDENTS");
        contentPanel.add(staffListPanel, "STAFF");

        mainFrame.addComponent(contentPanel, BorderLayout.CENTER);
        mainFrame.setMinimumSize(new Dimension(1000, 700));

        showLoginPanel();
    }

    /**
     * Shows the login panel in the main content area.
     */
    private void showLoginPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "LOGIN");
    }

    private void showLoadingDialog()
    {
        JDialog loadingDialog = new JDialog(mainFrame, "Loading", false);
        loadingDialog.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel loadingLabel = new JLabel("Initializing data...");
        loadingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(loadingLabel, BorderLayout.CENTER);

        // Add a simple progress indicator
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.SOUTH);

        loadingDialog.add(panel);
        loadingDialog.pack();
        loadingDialog.setLocationRelativeTo(mainFrame);
        loadingDialog.setVisible(true);
    }


    /**
     * Creates and configures the welcome panel with navigation buttons.
     *
     * @return Configured welcome panel
     */
    private ChiUniPanel createWelcomePanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Chichester University Portal");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(welcomeLabel, gbc);

        // Navigation buttons
        ChiUniButton departmentsButton = new ChiUniButton("View Departments");
        departmentsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        departmentsButton.addActionListener(e -> showDepartmentsPanel());
        departmentsButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(departmentsButton, gbc);

        ChiUniButton studentsButton = new ChiUniButton("View Students");
        studentsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        studentsButton.addActionListener(e -> showStudentsPanel());
        studentsButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 2;
        panel.add(studentsButton, gbc);

        ChiUniButton staffButton = new ChiUniButton("View Staff");
        staffButton.setFont(new Font("Arial", Font.PLAIN, 16));
        staffButton.addActionListener(e -> showStaffPanel());
        staffButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(staffButton, gbc);

        ChiUniButton addUserButton = new ChiUniButton("Add New User");
        addUserButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addUserButton.addActionListener(e -> showAddUserDialog());
        addUserButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 4; // Adjust based on your existing buttons
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(addUserButton, gbc);

        // Add Course Button
        ChiUniButton addCourseButton = new ChiUniButton("Add New Course");
        addCourseButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addCourseButton.addActionListener(e -> showAddCourseDialog());
        addCourseButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(addCourseButton, gbc);

        // Add Module button
        ChiUniButton addModuleButton = new ChiUniButton("Add New Module");
        addModuleButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addModuleButton.addActionListener(e -> showAddModuleDialog());
        addModuleButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(addModuleButton, gbc);

        return panel;
    }



    /**
     * Shows the welcome panel in the main content area.
     */
    private void showWelcomePanel()
    {
        initialiseData();
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "WELCOME");
    }

    /**
     * Shows the departments panel in the main content area.
     */
    private void showDepartmentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DEPARTMENTS");
    }

    /**
     * Shows the staff panel in the main content area.
     */
    private void showStaffPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STAFF");
    }

    private void showAddUserDialog()
    {
        AddUserDialog dialog = new AddUserDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    /**
     * Refreshes all data in the application with a progress indicator.
     *
     * @param message The message to display in the progress bar
     */
    public void refreshData(String message)
    {
        dataManager.refreshData(message);
    }

    // Helper method to update progress message with completion status
    private void updateProgressMessage()
    {
        int completed = completedTasks.get();
        int total = 4; // Total number of tasks
        progressBar.updateMessage(String.format("Completed %d of %d operations...", completed, total));
    }

    /**
     * Creates and configures the banner panel with university title.
     *
     * @return Configured banner panel
     */
    private ChiUniPanel createBannerPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0, 48, 87)); // University blue color
        panel.setPreferredSize(new Dimension(mainFrame.getWidth(), 80));

        JLabel titleLabel = new JLabel("Chichester University Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Refreshes a specific type of data in the application.
     *
     * @param dataType The type of data to refresh
     */
    public void refreshSpecificData(DataManager.DataType dataType) {
        dataManager.refreshSpecificData(dataType);
    }

    /**
     * Initialises application data in a background thread.
     * Checks for existing assignments files and generates initial assignments if needed.
     */
    private void initialiseData()
    {
        dataManager.initialiseData();
    }

    /**
     * Shows the students panel in the main content area.
     */
    private void showStudentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STUDENTS");
    }

    private void showAddCourseDialog()
    {
        AddCourseDialog dialog = new AddCourseDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    private void showAddModuleDialog()
    {
        AddModuleDialog dialog = new AddModuleDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    /**
     * Makes the main application window visible.
     */
    public void show()
    {
        mainFrame.setVisible(true);
    }
}