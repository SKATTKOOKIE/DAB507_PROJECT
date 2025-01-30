package gui;

import java.awt.*;
import javax.swing.*;

import gui.dialogs.AddModuleDialog;
import gui.templates.*;
import gui.panels.*;

/**
 * Main graphical user interface for the University Management System.
 */
public class GuiMainScreen
{
    private ChiUniFrame mainFrame;
    private ChiUniTextArea outputArea;
    private DepartmentPanel departmentPanel;
    private ChiUniPanel contentPanel;
    private StudentListPanel studentListPanel;
    private StaffListPanel staffListPanel;
    private final DataManager dataManager;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";

    public GuiMainScreen()
    {
        initialiseGUI();
        ChiUniProgressBar progressBar = new ChiUniProgressBar(mainFrame, "Loading", "Initialising system...");
        dataManager = new DataManager(studentListPanel, staffListPanel, departmentPanel, progressBar);
        progressBar.showProgress();
        show();
    }

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

        // Initialize other panels
        initializeOtherPanels();

        mainFrame.addComponent(contentPanel, BorderLayout.CENTER);
        mainFrame.setMinimumSize(new Dimension(1000, 700));

        showLoginPanel();
    }

    private void initializeOtherPanels()
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

    // Navigation methods made public for WelcomePanel to access
    public void showWelcomePanel()
    {
        initialiseData();
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "WELCOME");
    }

    public void showDepartmentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DEPARTMENTS");
    }

    public void showStudentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STUDENTS");
    }

    public void showStaffPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STAFF");
    }

    public void showAddUserDialog()
    {
        AddUserDialog dialog = new AddUserDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    public void showAddCourseDialog()
    {
        AddCourseDialog dialog = new AddCourseDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    public void showAddModuleDialog()
    {
        AddModuleDialog dialog = new AddModuleDialog(mainFrame, this);
        dialog.setVisible(true);
    }

    private void showLoginPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "LOGIN");
    }

    private void initialiseData()
    {
        dataManager.initialiseData();
    }

    public void refreshData(String message)
    {
        dataManager.refreshData(message);
    }

    public void refreshSpecificData(DataManager.DataType dataType)
    {
        dataManager.refreshSpecificData(dataType);
    }

    public void show()
    {
        mainFrame.setVisible(true);
    }
}