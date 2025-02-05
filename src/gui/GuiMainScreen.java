package gui;

import java.awt.*;
import javax.swing.*;

import gui.components.dialogs.AddCourseDialog;
import gui.components.dialogs.AddModuleDialog;
import gui.components.dialogs.AddUserDialog;
import gui.interfaces.UniversityManagementGui;
import gui.templates.*;
import gui.panels.*;

/**
 * Main graphical user interface for the University Management System.
 * This class serves as the primary controller for the GUI, managing navigation
 * between different panels and coordinating user interactions across the application.
 * <p>
 * The GUI consists of several main components:
 * <ul>
 *   <li>A banner panel displaying the university name</li>
 *   <li>A login panel for user authentication</li>
 *   <li>A welcome panel serving as the main navigation hub</li>
 *   <li>Separate panels for managing departments, students, and staff</li>
 *   <li>Dialog windows for adding new users, courses, and modules</li>
 * </ul>
 * <p>
 * The class follows a card layout pattern for panel navigation and uses
 * a DataManager instance to handle data operations.
 */
public class GuiMainScreen implements UniversityManagementGui
{
    private ChiUniFrame mainFrame;
    private DepartmentPanel departmentPanel;
    private ChiUniPanel contentPanel;
    private StudentListPanel studentListPanel;
    private StaffListPanel staffListPanel;
    private final DataManager dataManager;

    /**
     * Constructs a new GuiMainScreen instance.
     * Initialises the GUI components and shows a progress bar while loading data.
     */
    public GuiMainScreen()
    {
        initialiseGUI();
        ChiUniProgressBar progressBar = new ChiUniProgressBar(mainFrame, "Loading", "Initialising system...");
        dataManager = new DataManager(studentListPanel, staffListPanel, departmentPanel, progressBar);
        progressBar.showProgress();
        show();
    }

    public void dispose()
    {
        if (mainFrame != null)
        {
            mainFrame.dispose();
        }
    }

    /**
     * Initialises all GUI components and sets up the main frame layout.
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
        WelcomePanel welcomePanel = new WelcomePanel(this);
        contentPanel.add(welcomePanel, "WELCOME");

        // Initialise other panels
        initialiseOtherPanels();

        mainFrame.addComponent(contentPanel, BorderLayout.CENTER);
        mainFrame.setMinimumSize(new Dimension(1000, 700));

        showLoginPanel();
    }

    /**
     * Initialises secondary panels (students, departments, staff) and adds navigation buttons.
     */
    private void initialiseOtherPanels()
    {
        // Create students panel
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
        panel.setBackground(new Color(0, 48, 87));
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
     * Displays the welcome panel and initialises application data.
     */
    public void showWelcomePanel()
    {
        initialiseData();
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "WELCOME");
    }

    /**
     * Displays the departments management panel.
     */
    public void showDepartmentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DEPARTMENTS");
    }

    /**
     * Displays the student management panel.
     */
    public void showStudentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STUDENTS");
    }

    /**
     * Displays the staff management panel.
     */
    public void showStaffPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STAFF");
    }

    /**
     * Displays the dialog for adding a new user to the system.
     */
    public void showAddUserDialog()
    {
        AddUserDialog dialog = new AddUserDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    /**
     * Displays the dialog for adding a new course to the system.
     */
    public void showAddCourseDialog()
    {
        AddCourseDialog dialog = new AddCourseDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    /**
     * Displays the dialog for adding a new module to the system.
     */
    public void showAddModuleDialog()
    {
        AddModuleDialog dialog = new AddModuleDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    /**
     * Displays the login panel.
     */
    private void showLoginPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "LOGIN");
    }

    /**
     * Initialises the application data through the DataManager.
     */
    private void initialiseData()
    {
        dataManager.initialiseData();
    }

    /**
     * Refreshes a specific type of data in the system.
     *
     * @param dataType The type of data to refresh (e.g., STUDENTS, STAFF, DEPARTMENTS)
     */
    public void refreshSpecificData(DataManager.DataType dataType)
    {
        dataManager.refreshSpecificData(dataType);
    }

    /**
     * Makes the main application window visible.
     */
    public void show()
    {
        mainFrame.setVisible(true);
    }
}