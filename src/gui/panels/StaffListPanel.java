package gui.panels;

import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import business.StaffModuleAssignment;
import users.Staff;
import business.Module;
import business.Course;

import gui.templates.*;

/**
 * A panel that displays a filterable grid of staff cards.
 * Each card shows staff information and their avatar image.
 * Uses DepartmentId enum for department filtering and ChiUni components for consistent styling.
 */
public class StaffListPanel extends ChiUniPanel
{
    private ChiUniPanel staffContainer;
    private List<Staff> allStaff;
    private JComboBox<DepartmentId> departmentFilter;
    private boolean dataLoaded = false;

    @Override
    public void addNotify()
    {
        super.addNotify();
        if (!dataLoaded)
        {
            loadStaffData();
        }
    }

    public StaffListPanel()
    {
        setLayout(new BorderLayout(10, 10));
        initialiseUI(); // initialise UI components without loading data
    }

    private void initialiseUI()
    {
        // Create header panel
        ChiUniPanel headerPanel = new ChiUniPanel();
        headerPanel.setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Staff Directory", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Filter panel
        ChiUniPanel filterPanel = new ChiUniPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel filterLabel = new JLabel("Filter by Department: ");
        filterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(filterLabel);

        // Create department filter dropdown
        DepartmentId[] departments = DepartmentId.values();
        departmentFilter = new JComboBox<>(departments);
        departmentFilter.setSelectedItem(DepartmentId.UNKNOWN);
        departmentFilter.addActionListener(e ->
        {
            if (dataLoaded)
            {
                filterStaff();
            }
        });
        filterPanel.add(departmentFilter);

        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Create scrollable container for staff cards
        staffContainer = new ChiUniPanel();
        staffContainer.setLayout(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(staffContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadStaffData()
    {
        SwingWorker<List<Staff>, Void> worker = new SwingWorker<>()
        {
            @Override
            protected List<Staff> doInBackground() throws Exception
            {
                // Force a fresh load from JSON
                return Staff.getByDepartment("");  // This will read fresh from the file
            }

            @Override
            protected void done()
            {
                try
                {
                    allStaff = get();
                    dataLoaded = true;
                    filterStaff(); // Initial display
                }
                catch (Exception e)
                {
                    handleError("Error loading staff", e);
                }
            }
        };
        worker.execute();
    }

    private void loadStaff()
    {
        try
        {
            allStaff = Staff.getByDepartment("");
            filterStaff();
        }
        catch (IOException e)
        {
            handleError("Error loading staff", e);
        }
    }

    private void filterStaff()
    {
        DepartmentId selectedDepartment = (DepartmentId) departmentFilter.getSelectedItem();
        List<Staff> filteredStaff;

        if (selectedDepartment == DepartmentId.UNKNOWN)
        {
            filteredStaff = allStaff;
        }
        else
        {
            String departmentName = selectedDepartment.getDepartmentName();
            filteredStaff = allStaff.stream()
                    .filter(staff ->
                    {
                        String staffDept = staff.getDepartment();
                        if (staffDept == null || staffDept.isEmpty())
                        {
                            return selectedDepartment == DepartmentId.UNKNOWN;
                        }
                        return departmentName.equals(staffDept);
                    })
                    .collect(Collectors.toList());
        }

        displayStaff(filteredStaff);
    }

    private void displayStaff(List<Staff> staffList)
    {
        staffContainer.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;
        int col = 0;
        int maxCols = 3;  // Number of cards per row

        for (Staff staff : staffList)
        {
            ChiUniPanel card = createStaffCard(staff);
            gbc.gridx = col;
            gbc.gridy = row;
            staffContainer.add(card, gbc);

            col++;
            if (col >= maxCols)
            {
                col = 0;
                row++;
            }
        }

        staffContainer.revalidate();
        staffContainer.repaint();
    }

// In StaffListPanel.java, update the createStaffCard method:

    private ChiUniPanel createStaffCard(Staff staff)
    {
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
        infoPanel.setPadding(0);

        JLabel nameLabel = new JLabel(staff.getFirstName() + " " + staff.getLastName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));

        // Additional staff details
        addInfoLabel(infoPanel, "ID: " + staff.getId());

        String departmentDisplay = staff.getDepartment();
        if (departmentDisplay == null || departmentDisplay.isEmpty())
        {
            departmentDisplay = "No Department";
        }
        addInfoLabel(infoPanel, "Department: " + departmentDisplay);

        addInfoLabel(infoPanel, "Weekly Hours: " + staff.getWeeklyHours());
        addInfoLabel(infoPanel, "Max Modules: " + staff.getMaxModules());
        addInfoLabel(infoPanel, "Email: " + staff.getEmail());

        // Button panel for multiple buttons
        ChiUniPanel buttonPanel = new ChiUniPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setPadding(0);

        // Details button
        ChiUniButton detailsButton = new ChiUniButton("View Details");
        detailsButton.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsButton.addActionListener(e -> showStaffDetails(staff));
        buttonPanel.add(detailsButton);

        // Modules button
        ChiUniButton modulesButton = new ChiUniButton("Manage Modules");
        modulesButton.setFont(new Font("Arial", Font.PLAIN, 12));
        modulesButton.addActionListener(e -> showModuleManagement(staff));
        buttonPanel.add(modulesButton);

        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(buttonPanel);

        card.add(infoPanel, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(300, 350));

        return card;
    }

    private static class ModuleInfo
    {
        private String code;
        private String name;
        private String description;

        public ModuleInfo(String code, String name, String description)
        {
            this.code = code;
            this.name = name;
            this.description = description;
        }

        @Override
        public String toString()
        {
            return code + " - " + name;
        }
    }


    private void showModuleManagement(Staff staff)
    {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Module Management - " + staff.getFirstName() + " " + staff.getLastName(), true);

        ChiUniPanel contentPanel = new ChiUniPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with module count and capacity
        ChiUniPanel headerPanel = new ChiUniPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel countLabel = new JLabel(String.format("Module Capacity: 0/%d modules", staff.getMaxModules()));
        countLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(countLabel, BorderLayout.WEST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Create split pane for available and assigned modules
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);

        // Available modules panel
        ChiUniPanel availablePanel = new ChiUniPanel();
        availablePanel.setLayout(new BorderLayout());
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available Modules"));

        DefaultListModel<ModuleDisplay> availableModel = new DefaultListModel<>();
        JList<ModuleDisplay> availableList = new JList<>(availableModel);
        availableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Assigned modules panel
        ChiUniPanel assignedPanel = new ChiUniPanel();
        assignedPanel.setLayout(new BorderLayout());
        assignedPanel.setBorder(BorderFactory.createTitledBorder("Assigned Modules"));

        DefaultListModel<ModuleDisplay> assignedModel = new DefaultListModel<>();
        JList<ModuleDisplay> assignedList = new JList<>(assignedModel);
        assignedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load modules based on department
        try
        {
            // Get all department modules
            List<Course> departmentCourses = Course.getAll().stream()
                    .filter(course ->
                    {
                        DepartmentId deptId = course.getDepartmentId();
                        String deptName = deptId != null ? deptId.getDepartmentName() : "";
                        return deptName.equals(staff.getDepartment());
                    })
                    .collect(Collectors.toList());

            Map<String, Module> allModules = new HashMap<>();
            for (Course course : departmentCourses)
            {
                List<Module> courseModules = Module.getModulesForCourse(course.getCourseCode());
                for (Module module : courseModules)
                {
                    allModules.put(module.getCode(), module);
                }
            }

            // Load current assignments
            List<String> assignedModuleIds = StaffModuleAssignment.getStaffAssignments(staff.getId());

            // Add modules to appropriate lists
            allModules.forEach((code, module) ->
            {
                ModuleDisplay display = new ModuleDisplay(module);
                if (assignedModuleIds.contains(code))
                {
                    assignedModel.addElement(display);
                }
                else
                {
                    availableModel.addElement(display);
                }
            });

            // Update count label
            countLabel.setText(String.format("Module Capacity: %d/%d modules",
                    assignedModel.getSize(), staff.getMaxModules()));

        }
        catch (IOException e)
        {
            handleError("Error loading modules", e);
        }

        // Add lists to scroll panes
        availablePanel.add(new JScrollPane(availableList), BorderLayout.CENTER);
        assignedPanel.add(new JScrollPane(assignedList), BorderLayout.CENTER);

        // Add assign/unassign buttons
        ChiUniPanel buttonPanel = new ChiUniPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        ChiUniButton assignButton = new ChiUniButton(">>");
        ChiUniButton unassignButton = new ChiUniButton("<<");

        assignButton.addActionListener(e ->
        {
            ModuleDisplay selected = availableList.getSelectedValue();
            if (selected != null && assignedModel.size() < staff.getMaxModules())
            {
                assignedModel.addElement(selected);
                availableModel.removeElement(selected);
                updateAssignments(staff.getId(), assignedModel);
                countLabel.setText(String.format("Module Capacity: %d/%d modules",
                        assignedModel.getSize(), staff.getMaxModules()));
            }
        });

        unassignButton.addActionListener(e ->
        {
            ModuleDisplay selected = assignedList.getSelectedValue();
            if (selected != null)
            {
                availableModel.addElement(selected);
                assignedModel.removeElement(selected);
                updateAssignments(staff.getId(), assignedModel);
                countLabel.setText(String.format("Module Capacity: %d/%d modules",
                        assignedModel.getSize(), staff.getMaxModules()));
            }
        });

        // Selection listeners
        availableList.addListSelectionListener(e ->
                assignButton.setEnabled(availableList.getSelectedValue() != null &&
                        assignedModel.getSize() < staff.getMaxModules()));

        assignedList.addListSelectionListener(e ->
                unassignButton.setEnabled(assignedList.getSelectedValue() != null));

        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(assignButton);
        buttonPanel.add(Box.createVerticalStrut(5));
        buttonPanel.add(unassignButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Add components to split pane
        splitPane.setLeftComponent(availablePanel);
        splitPane.setRightComponent(assignedPanel);

        // Add button panel between lists
        ChiUniPanel centerPanel = new ChiUniPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(splitPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Add close button
        ChiUniButton closeButton = new ChiUniButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());

        ChiUniPanel bottomPanel = new ChiUniPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(closeButton);

        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Helper class for displaying modules in JList
    private static class ModuleDisplay
    {
        private final Module module;

        public ModuleDisplay(Module module)
        {
            this.module = module;
        }

        @Override
        public String toString()
        {
            return String.format("%s - %s (Year %s)",
                    module.getCode(), module.getName(), module.getAcYear());
        }

        public String getModuleCode()
        {
            return module.getCode();
        }
    }

    // Helper method to update assignments in storage
    private void updateAssignments(int staffId, DefaultListModel<ModuleDisplay> assignedModel)
    {
        try
        {
            List<String> moduleIds = new ArrayList<>();
            for (int i = 0; i < assignedModel.size(); i++)
            {
                moduleIds.add(assignedModel.getElementAt(i).getModuleCode());
            }
            StaffModuleAssignment.updateStaffAssignments(staffId, moduleIds);
        }
        catch (IOException e)
        {
            handleError("Error saving module assignments", e);
        }
    }

    private void updateButtonStates()
    {
    }

    private void updateButtonStates(JList<String> availableList, JList<String> assignedList,
                                    JButton assignButton, JButton unassignButton, Staff staff)
    {
        assignButton.setEnabled(availableList.getSelectedValue() != null &&
                assignedList.getModel().getSize() < staff.getMaxModules());
        unassignButton.setEnabled(assignedList.getSelectedValue() != null);
    }


    private JLabel createAvatarLabel(Staff staff)
    {
        ImageIcon avatarIcon;
        if (staff.getAvatar() != null && !staff.getAvatar().isEmpty())
        {
            try
            {
                avatarIcon = new ImageIcon(new URL(staff.getAvatar()));
                Image img = avatarIcon.getImage();
                Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                avatarIcon = new ImageIcon(scaledImg);
            }
            catch (Exception e)
            {
                avatarIcon = createDefaultAvatar();
            }
        }
        else
        {
            avatarIcon = createDefaultAvatar();
        }

        JLabel avatarLabel = new JLabel(avatarIcon);
        avatarLabel.setPreferredSize(new Dimension(80, 80));
        return avatarLabel;
    }

    private ImageIcon createDefaultAvatar()
    {
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

    private void addInfoLabel(JPanel panel, String text)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(3));
    }

    private void showStaffDetails(Staff staff)
    {
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

    private void addDetailField(JPanel panel, String label, String value)
    {
        JLabel labelComponent = new JLabel(label + ":", SwingConstants.RIGHT);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel valueComponent = new JLabel(value, SwingConstants.LEFT);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    private void handleError(String message, Exception ex)
    {
        String errorMessage = message + ": " + ex.getMessage();
        JOptionPane.showMessageDialog(this,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void refreshData() throws IOException
    {
        // First regenerate module assignments
        StaffModuleAssignment.generateInitialAssignments();

        // Force reload of staff data from JSON
        this.allStaff = null;    // Clear cached data
        dataLoaded = false;      // Reset the data loaded flag

        // Load fresh data
        loadStaffData();
    }

}