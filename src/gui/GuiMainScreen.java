package gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

import business.StaffModuleAssignment;
import business.StudentModuleAssignment;
import file_handling.FilePathHandler;

/**
 * Main graphical user interface for the University Management System.
 * This class manages the primary window of the application, including
 * the login screen, navigation, and various content panels for departments,
 * students, and staff management.
 */
public class GuiMainScreen
{
    /**
     * Main application window
     */
    private ChiUniFrame mainFrame;

    /**
     * Text area for displaying output messages
     */
    private ChiUniTextArea outputArea;

    /**
     * Panel for displaying and managing departments
     */
    private DepartmentPanel departmentPanel;

    /**
     * Welcome screen panel
     */
    private ChiUniPanel welcomePanel;

    /**
     * Main content panel that holds all other panels
     */
    private ChiUniPanel contentPanel;

    /**
     * Login screen panel
     */
    private ChiUniPanel loginPanel;

    /**
     * Panel for displaying and managing student lists
     */
    private StudentListPanel studentListPanel;

    /**
     * Panel for displaying and managing staff lists
     */
    private StaffListPanel staffListPanel;

    private JDialog loadingDialog;

    private ChiUniProgressBar progressBar;

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

        // Create login panel
        loginPanel = createLoginPanel();
        contentPanel.add(loginPanel, "LOGIN");

        // Create welcome panel
        welcomePanel = createWelcomePanel();
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
     * Initialises application data in a background thread.
     * Checks for existing assignments files and generates initial assignments if needed.
     */
    private void initialiseData()
    {
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>()
        {
            @Override
            protected Void doInBackground() throws Exception
            {
                try
                {
                    publish("Checking file system...");
                    // Use FilePathHandler enum to get the file paths
                    File staffAssignmentsFile = new File(FilePathHandler.ASSIGNED_STAFF_FILE.getNormalisedPath());
                    File studentAssignmentsFile = new File(FilePathHandler.ASSIGNED_STUDENTS_FILE.getNormalisedPath());

                    if (!staffAssignmentsFile.exists() || !studentAssignmentsFile.exists())
                    {
                        publish("Generating initial assignments...");
                        StaffModuleAssignment.generateInitialAssignments();
                        StudentModuleAssignment.generateInitialAssignments();
                    }

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
            protected void process(java.util.List<String> chunks)
            {
                // Update progress message
                if (chunks != null && !chunks.isEmpty())
                {
                    progressBar.updateMessage(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done()
            {
                // Hide progress bar when complete
                progressBar.hideProgress();
            }
        };
        worker.execute();
    }

    /**
     * Creates and configures the login panel with username and password fields.
     *
     * @return Configured login panel
     */
    private ChiUniPanel createLoginPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Login title
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, gbc);

        // Username components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        // Password components
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Login button
        ChiUniButton loginButton = new ChiUniButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(loginButton, gbc);

        // Add action listeners
        loginButton.addActionListener(e ->
        {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateCredentials(username, password))
            {
                showWelcomePanel();
            }
            else
            {
                JOptionPane.showMessageDialog(mainFrame,
                        "Invalid credentials. Please try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        passwordField.addActionListener(e -> loginButton.doClick());

        return panel;
    }

    /**
     * Validates the provided login credentials.
     *
     * @param username The entered username
     * @param password The entered password
     * @return true if credentials are valid, false otherwise
     */
    private boolean validateCredentials(String username, String password)
    {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
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
        loadingDialog = new JDialog(mainFrame, "Loading", false);
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
     * Handles and displays error messages to the user.
     *
     * @param message The error message to display
     * @param ex      The exception that occurred
     */
    private void handleError(String message, Exception ex)
    {
        String errorMessage = message + ": " + ex.getMessage();
        OutputManager.print("ERROR: " + errorMessage);
        JOptionPane.showMessageDialog(mainFrame,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows the students panel in the main content area.
     */
    private void showStudentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STUDENTS");
    }

    /**
     * Makes the main application window visible.
     */
    public void show()
    {
        mainFrame.setVisible(true);
    }
}