package gui;

import java.awt.*;
import javax.swing.*;

public class GuiMainScreen
{
    private ChiUniFrame mainFrame;
    private ChiUniTextArea outputArea;
    private DepartmentPanel departmentPanel;
    private ChiUniPanel welcomePanel;
    private ChiUniPanel contentPanel;
    private ChiUniPanel loginPanel;
    private StudentListPanel studentListPanel;
    private StaffListPanel staffListPanel;

    // Admin credentials (in practice, these should be stored securely)
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password123";

    public GuiMainScreen()
    {
        initializeGUI();
    }

    private void initializeGUI()
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
        // Add back button to department panel
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

        // Add content panel to frame in the CENTER position
        mainFrame.addComponent(contentPanel, BorderLayout.CENTER);
        mainFrame.setMinimumSize(new Dimension(1000, 700));

        // Show login panel by default
        showLoginPanel();
    }

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

        // Username label and field
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

        // Password label and field
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

        // Add action listener to login button
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

        // Add action listener to password field for Enter key
        passwordField.addActionListener(e -> loginButton.doClick());

        return panel;
    }

    private boolean validateCredentials(String username, String password)
    {
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    private void showLoginPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "LOGIN");
    }

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

        // Departments button
        ChiUniButton departmentsButton = new ChiUniButton("View Departments");
        departmentsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        departmentsButton.addActionListener(e -> showDepartmentsPanel());

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

        // Style the button
        departmentsButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        panel.add(departmentsButton, gbc);

        return panel;
    }

    private void showWelcomePanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "WELCOME");
    }

    private void showDepartmentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "DEPARTMENTS");
    }

    private void showStaffPanel() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STAFF");
    }

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

    private void handleError(String message, Exception ex)
    {
        String errorMessage = message + ": " + ex.getMessage();
        OutputManager.print("ERROR: " + errorMessage);
        JOptionPane.showMessageDialog(mainFrame,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showStudentsPanel()
    {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, "STUDENTS");
    }

    public void show()
    {
        mainFrame.setVisible(true);
    }
}