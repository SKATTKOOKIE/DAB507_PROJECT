package gui;

import javax.swing.*;
import java.awt.*;

import gui.templates.*;

/**
 * Login panel for the University Management System.
 * Handles user authentication and login interface.
 */
public class LoginPanel extends ChiUniPanel
{
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";

    private final ChiUniFrame mainFrame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final Runnable onLoginSuccess;

    /**
     * Constructs a new LoginPanel.
     *
     * @param mainFrame      The main application frame
     * @param onLoginSuccess Callback to execute when login is successful
     */
    public LoginPanel(ChiUniFrame mainFrame, Runnable onLoginSuccess)
    {
        this.mainFrame = mainFrame;
        this.onLoginSuccess = onLoginSuccess;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        // Build the panel
        createLoginComponents(gbc);
    }

    /**
     * Creates and arranges all login components.
     *
     * @param gbc GridBagConstraints for component layout
     */
    private void createLoginComponents(GridBagConstraints gbc)
    {
        // Login title
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(titleLabel, gbc);

        // Username components
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        // Password components
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        // Login button
        ChiUniButton loginButton = new ChiUniButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        add(loginButton, gbc);

        // Hint Section
        JPanel hintPanel = new JPanel();
        hintPanel.setLayout(new BoxLayout(hintPanel, BoxLayout.Y_AXIS));
        hintPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Hint"));

        JLabel credentialsHint = new JLabel("Username: admin | Password: password");
        credentialsHint.setFont(new Font("Arial", Font.ITALIC, 12));
        credentialsHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel progressHint = new JLabel("Please wait for the progress bar to start moving before logging in.");
        progressHint.setFont(new Font("Arial", Font.ITALIC, 12));
        progressHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintPanel.add(credentialsHint);
        hintPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        hintPanel.add(progressHint);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        add(hintPanel, gbc);

        // Add action listeners
        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> loginButton.doClick());
    }

    /**
     * Handles the login attempt and validates credentials.
     */
    private void handleLogin()
    {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateCredentials(username, password))
        {
            onLoginSuccess.run();
        }
        else
        {
            JOptionPane.showMessageDialog(mainFrame,
                    "Invalid credentials. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
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
}