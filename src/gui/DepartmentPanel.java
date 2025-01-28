package gui;

import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DepartmentPanel extends ChiUniPanel
{
    private final CardLayout cardLayout;
    private final ChiUniPanel cardsPanel;

    public DepartmentPanel()
    {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();

        // Create cardsPanel with default constructor and then set its layout
        cardsPanel = new ChiUniPanel();
        cardsPanel.setLayout(cardLayout);

        // Create and add the main departments list panel
        ChiUniPanel departmentsListPanel = createDepartmentsListPanel();
        cardsPanel.add(departmentsListPanel, "DEPARTMENTS_LIST");

        // Add cards panel to main panel
        add(cardsPanel, BorderLayout.CENTER);

        // Create department detail panels
        createDepartmentDetailPanels();
    }

    private void createDepartmentDetailPanels()
    {
        Arrays.stream(DepartmentId.values())
                .filter(dept -> dept != DepartmentId.UNKNOWN)
                .forEach(deptId ->
                {
                    // Create and add department detail panel
                    DepartmentDetailPanel detailPanel = new DepartmentDetailPanel(deptId, this);
                    cardsPanel.add(detailPanel, deptId.toString());
                });
    }

    private ChiUniPanel createDepartmentsListPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add title
        JLabel titleLabel = new JLabel("University Departments", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.insets = new Insets(20, 5, 30, 5);
        panel.add(titleLabel, gbc);

        // Create department buttons panel
        ChiUniPanel buttonsPanel = new ChiUniPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridwidth = GridBagConstraints.REMAINDER;
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.insets = new Insets(5, 20, 5, 20);

        // Create buttons for each department
        Arrays.stream(DepartmentId.values())
                .filter(dept -> dept != DepartmentId.UNKNOWN)
                .forEach(deptId ->
                {
                    ChiUniButton deptButton = createDepartmentButton(deptId);
                    buttonsPanel.add(deptButton, buttonGbc);
                });

        // Add buttons panel with some padding
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 20, 20, 20);
        panel.add(buttonsPanel, gbc);

        return panel;
    }

    private ChiUniButton createDepartmentButton(DepartmentId deptId)
    {
        ChiUniButton button = new ChiUniButton(deptId.getDepartmentName());
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(300, 50));

        // Style the button
        button.setBackground(new Color(0, 48, 87)); // University blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                button.setBackground(new Color(0, 71, 130));
            }

            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                button.setBackground(new Color(0, 48, 87));
            }
        });

        // Add click handler
        button.addActionListener(e -> showDepartmentDetail(deptId));

        return button;
    }

    public void showDepartmentDetail(DepartmentId deptId)
    {
        cardLayout.show(cardsPanel, deptId.toString());
    }

    public void showDepartmentsList()
    {
        cardLayout.show(cardsPanel, "DEPARTMENTS_LIST");
    }

    public void refreshData() {
        // Recreate all department detail panels to refresh their data
        cardsPanel.removeAll();

        // Recreate and add the main departments list
        ChiUniPanel departmentsListPanel = createDepartmentsListPanel();
        cardsPanel.add(departmentsListPanel, "DEPARTMENTS_LIST");

        // Recreate all department detail panels
        createDepartmentDetailPanels();

        // Show the current panel again
        cardLayout.show(cardsPanel, "DEPARTMENTS_LIST");

        // Refresh the UI
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }
}