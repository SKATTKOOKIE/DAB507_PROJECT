package gui.panels;

import gui.GuiMainScreen;
import gui.templates.ChiUniButton;
import gui.templates.ChiUniPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Panel displaying the welcome screen with navigation buttons for the University Management System.
 */
public class WelcomePanel extends ChiUniPanel
{
    private final GuiMainScreen mainScreen;

    /**
     * Constructs a new WelcomePanel with navigation buttons.
     *
     * @param mainScreen Reference to the main screen for handling navigation
     */
    public WelcomePanel(GuiMainScreen mainScreen)
    {
        this.mainScreen = mainScreen;
        initializePanel();
    }

    /**
     * Initializes the panel components and layout.
     */
    private void initializePanel()
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Chichester University Portal");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(welcomeLabel, gbc);

        // Navigation buttons
        addNavigationButton("View Departments", e -> mainScreen.showDepartmentsPanel(), 1, gbc);
        addNavigationButton("View Students", e -> mainScreen.showStudentsPanel(), 2, gbc);
        addNavigationButton("View Staff", e -> mainScreen.showStaffPanel(), 3, gbc);
        addNavigationButton("Add New User", e -> mainScreen.showAddUserDialog(), 4, gbc);
        addNavigationButton("Add New Course", e -> mainScreen.showAddCourseDialog(), 5, gbc);
        addNavigationButton("Add New Module", e -> mainScreen.showAddModuleDialog(), 6, gbc);
    }

    /**
     * Helper method to create and add a navigation button.
     *
     * @param text   Button text
     * @param action Button action
     * @param gridy  Grid y position
     * @param gbc    GridBagConstraints
     */
    private void addNavigationButton(String text, java.awt.event.ActionListener action, int gridy, GridBagConstraints gbc)
    {
        ChiUniButton button = new ChiUniButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(200, 40));
        gbc.gridy = gridy;
        gbc.insets = new Insets(0, 0, 15, 0);
        add(button, gbc);
    }
}