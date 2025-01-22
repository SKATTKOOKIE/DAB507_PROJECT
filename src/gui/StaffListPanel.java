package gui;

import business.DepartmentId;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import users.Staff;

/**
 * A panel that displays a filterable grid of staff cards.
 * Each card shows staff information and their avatar image.
 * Uses DepartmentId enum for department filtering and ChiUni components for consistent styling.
 */
public class StaffListPanel extends ChiUniPanel {
    private final ChiUniPanel staffContainer;
    private List<Staff> allStaff;
    private JComboBox<DepartmentId> departmentFilter;

    public StaffListPanel() {
        setLayout(new BorderLayout(10, 10));

        // Create header panel with ChiUniPanel
        ChiUniPanel headerPanel = new ChiUniPanel();
        headerPanel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Staff Directory", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Filter panel using ChiUniPanel
        ChiUniPanel filterPanel = new ChiUniPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel filterLabel = new JLabel("Filter by Department: ");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(filterLabel);

        // Create department filter dropdown using DepartmentId enum
        DepartmentId[] departments = DepartmentId.values();
        departmentFilter = new JComboBox<>(departments);
        departmentFilter.setFont(new Font("Arial", Font.PLAIN, 14));
        departmentFilter.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DepartmentId) {
                    setText(((DepartmentId) value).getDepartmentName());
                }
                setFont(new Font("Arial", Font.PLAIN, 14));
                return this;
            }
        });
        departmentFilter.setSelectedItem(DepartmentId.UNKNOWN);
        departmentFilter.addActionListener(e -> filterStaff());
        filterPanel.add(departmentFilter);

        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Create scrollable container for staff cards using ChiUniPanel
        staffContainer = new ChiUniPanel();
        staffContainer.setLayout(new GridBagLayout());
        staffContainer.setPadding(5); // Smaller padding for grid cells

        JScrollPane scrollPane = new JScrollPane(staffContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        add(scrollPane, BorderLayout.CENTER);

        loadStaff();
    }

    private void loadStaff() {
        try {
            allStaff = Staff.getByDepartment("");
            filterStaff();
        } catch (IOException e) {
            handleError("Error loading staff", e);
        }
    }

    private void filterStaff() {
        DepartmentId selectedDepartment = (DepartmentId) departmentFilter.getSelectedItem();
        List<Staff> filteredStaff;

        if (selectedDepartment == DepartmentId.UNKNOWN) {
            filteredStaff = allStaff;
        } else {
            String departmentName = selectedDepartment.getDepartmentName();
            filteredStaff = allStaff.stream()
                    .filter(staff -> {
                        String staffDept = staff.getDepartment();
                        if (staffDept == null || staffDept.isEmpty()) {
                            return selectedDepartment == DepartmentId.UNKNOWN;
                        }
                        return departmentName.equals(staffDept);
                    })
                    .collect(Collectors.toList());
        }

        displayStaff(filteredStaff);
    }

    private void displayStaff(List<Staff> staffList) {
        staffContainer.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;
        int col = 0;
        int maxCols = 3;  // Number of cards per row

        for (Staff staff : staffList) {
            ChiUniPanel card = createStaffCard(staff);
            gbc.gridx = col;
            gbc.gridy = row;
            staffContainer.add(card, gbc);

            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }

        staffContainer.revalidate();
        staffContainer.repaint();
    }

    private ChiUniPanel createStaffCard(Staff staff) {
        ChiUniPanel card = new ChiUniPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Add avatar panel at the top
        ChiUniPanel avatarPanel = new ChiUniPanel();
        avatarPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.add(createAvatarLabel(staff));
        card.add(avatarPanel, BorderLayout.NORTH);

        // Staff name and basic info using ChiUniPanel
        ChiUniPanel infoPanel = new ChiUniPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPadding(0); // Remove padding for better layout

        JLabel nameLabel = new JLabel(staff.getFirstName() + " " + staff.getLastName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));

        // Additional staff details
        addInfoLabel(infoPanel, "ID: " + staff.getId());

        String departmentDisplay = staff.getDepartment();
        if (departmentDisplay == null || departmentDisplay.isEmpty()) {
            departmentDisplay = "No Department";
        }
        addInfoLabel(infoPanel, "Department: " + departmentDisplay);

        addInfoLabel(infoPanel, "Weekly Hours: " + staff.getWeeklyHours());
        addInfoLabel(infoPanel, "Email: " + staff.getEmail());

        // Add view details button using ChiUniButton
        ChiUniButton detailsButton = new ChiUniButton("View Details");
        detailsButton.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsButton.addActionListener(e -> showStaffDetails(staff));

        ChiUniPanel buttonPanel = new ChiUniPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setPadding(0);
        buttonPanel.add(detailsButton);

        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(buttonPanel);

        card.add(infoPanel, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(300, 220));

        return card;
    }

    private JLabel createAvatarLabel(Staff staff) {
        ImageIcon avatarIcon;
        if (staff.getAvatar() != null && !staff.getAvatar().isEmpty()) {
            try {
                avatarIcon = new ImageIcon(new URL(staff.getAvatar()));
                Image img = avatarIcon.getImage();
                Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                avatarIcon = new ImageIcon(scaledImg);
            } catch (Exception e) {
                avatarIcon = createDefaultAvatar();
            }
        } else {
            avatarIcon = createDefaultAvatar();
        }

        JLabel avatarLabel = new JLabel(avatarIcon);
        avatarLabel.setPreferredSize(new Dimension(80, 80));
        return avatarLabel;
    }

    private ImageIcon createDefaultAvatar() {
        BufferedImage defaultImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = defaultImage.createGraphics();

        g2d.setColor(new Color(70, 130, 180)); // Match ChiUniButton color
        g2d.fillRect(0, 0, 80, 80);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("?", 33, 50);

        g2d.dispose();
        return new ImageIcon(defaultImage);
    }

    private void addInfoLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(3));
    }

    private void showStaffDetails(Staff staff) {
        // Create a modal dialog using ChiUni components
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Staff Details", true);

        ChiUniPanel contentPanel = new ChiUniPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));

        // Create details panel
        ChiUniPanel detailsPanel = new ChiUniPanel();
        detailsPanel.setLayout(new GridLayout(0, 2, 10, 5));

        // Add all staff details
        addDetailField(detailsPanel, "Name", staff.getFirstName() + " " + staff.getLastName());
        addDetailField(detailsPanel, "ID", String.valueOf(staff.getId()));
        addDetailField(detailsPanel, "Department", staff.getDepartment());
        addDetailField(detailsPanel, "Email", staff.getEmail());
        addDetailField(detailsPanel, "Weekly Hours", String.valueOf(staff.getWeeklyHours()));
        addDetailField(detailsPanel, "Max Modules", String.valueOf(staff.getMaxModules()));
        addDetailField(detailsPanel, "GUID", staff.getGuid());

        contentPanel.add(detailsPanel, BorderLayout.CENTER);

        // Add close button
        ChiUniPanel buttonPanel = new ChiUniPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        ChiUniButton closeButton = new ChiUniButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(closeButton);

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void addDetailField(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label + ":", SwingConstants.RIGHT);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel valueComponent = new JLabel(value, SwingConstants.LEFT);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    private void handleError(String message, Exception ex) {
        String errorMessage = message + ": " + ex.getMessage();
        JOptionPane.showMessageDialog(this,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}