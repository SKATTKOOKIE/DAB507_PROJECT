package gui;// GuiMainScreen.java

import java.awt.*;
import javax.swing.*;

public class GuiMainScreen
{
    private ChiUniFrame mainFrame;
    private ChiUniTextArea outputArea;
    private DepartmentPanel departmentPanel;

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
        ChiUniPanel contentPanel = new ChiUniPanel();
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        departmentPanel = new DepartmentPanel();
        contentPanel.add(departmentPanel);

        // Create left panel for buttons
        ChiUniPanel leftPanel = new ChiUniPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.weightx = 1.0;
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        leftGbc.insets = new Insets(0, 10, 5, 10);

        // Add left panel to content panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(leftPanel, gbc);

        // Add content panel to frame in the CENTER position
        mainFrame.addComponent(contentPanel, BorderLayout.CENTER);
        mainFrame.setMinimumSize(new Dimension(1000, 700));
    }

    private ChiUniPanel createOutputPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Output"));
        panel.setLayout(new BorderLayout(10, 10));

        // Create text area
        outputArea = new ChiUniTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        ChiUniPanel buttonPanel = new ChiUniPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        ChiUniButton clearButton = new ChiUniButton("Clear Output");
        clearButton.addActionListener(e -> OutputManager.clear());
        buttonPanel.add(clearButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private ChiUniPanel createModulesPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Module Management"));
        panel.setLayout(new GridLayout(1, 1, 10, 10));

        ChiUniButton displayModulesBtn = new ChiUniButton("Display All Modules");
//        displayModulesBtn.addActionListener(e -> displayAllModules());

        panel.add(displayModulesBtn);

        return panel;
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

    public void show()
    {
        mainFrame.setVisible(true);
    }
}
