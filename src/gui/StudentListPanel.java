package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import business.StudentModuleAssignment;
import users.Student;
import users.StudentType;
import business.Module;
import business.Course;

/**
 * A panel that displays a filterable grid of student cards.
 * Each card shows student information and provides access to their module details.
 * Students can be filtered by their type (Full-time, Part-time, International, or DA).
 */
public class StudentListPanel extends ChiUniPanel
{
    /**
     * Container panel for the student cards grid
     */
    private final ChiUniPanel studentsContainer;

    /**
     * Dialog for displaying module information
     */
    private JDialog moduleDialog;

    /**
     * Cached list of all students for filtering
     */
    private List<Student> allStudents;

    /**
     * Dropdown component for filtering students by type
     */
    private JComboBox<StudentType> typeFilter;

    /**
     * Constructs a new StudentListPanel.
     * Initializes the UI components including the header, filter dropdown,
     * and scrollable container for student cards.
     */
    public StudentListPanel()
    {
        setLayout(new BorderLayout(10, 10));

        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Student Directory", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel filterLabel = new JLabel("Filter by Type: ");
        filterPanel.add(filterLabel);

        // Create and populate type filter dropdown
        typeFilter = new JComboBox<>(StudentType.values());
        typeFilter.insertItemAt(null, 0); // Add "All" option
        typeFilter.setSelectedIndex(0); // Select "All" by default
        typeFilter.setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(value == null ? "All" : value.toString());
                return this;
            }
        });

        // Add filter change listener
        typeFilter.addActionListener(e -> filterStudents());
        filterPanel.add(typeFilter);

        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Create scrollable container for student cards
        studentsContainer = new ChiUniPanel();
        studentsContainer.setLayout(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(studentsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        loadStudents();
    }

    /**
     * Loads all students from the data source and applies the initial filter.
     * If an error occurs while loading students, displays an error message.
     */
    private void loadStudents()
    {
        try
        {
            allStudents = Student.getByCourse("");  // Get all students
            filterStudents(); // Apply initial filter
        }
        catch (IOException e)
        {
            handleError("Error loading students", e);
        }
    }

    /**
     * Filters the displayed students based on the selected student type in the dropdown.
     * If no type is selected (All), shows all students.
     * Otherwise, filters to show only students matching the selected type.
     */
    private void filterStudents()
    {
        StudentType selectedType = (StudentType) typeFilter.getSelectedItem();
        List<Student> filteredStudents;

        if (selectedType == null)
        {
            filteredStudents = allStudents; // Show all students
        }
        else
        {
            // Filter students by selected type
            filteredStudents = allStudents.stream()
                    .filter(student ->
                    {
                        try
                        {
                            return StudentType.fromString(student.getType()) == selectedType;
                        }
                        catch (IllegalArgumentException e)
                        {
                            return false; // Skip students with invalid types
                        }
                    })
                    .collect(Collectors.toList());
        }

        displayStudents(filteredStudents);
    }

    /**
     * Displays the given list of students in a grid layout.
     * Creates a card for each student and arranges them in rows and columns.
     *
     * @param students The list of students to display
     */
    private void displayStudents(List<Student> students)
    {
        studentsContainer.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;
        int col = 0;
        int maxCols = 3;  // Number of cards per row

        for (Student student : students)
        {
            ChiUniPanel card = createStudentCard(student);
            gbc.gridx = col;
            gbc.gridy = row;
            studentsContainer.add(card, gbc);

            col++;
            if (col >= maxCols)
            {
                col = 0;
                row++;
            }
        }

        studentsContainer.revalidate();
        studentsContainer.repaint();
    }

    /**
     * Creates a card panel displaying information for a single student.
     * The card includes the student's name, ID, type, email, and course information.
     * If the student is enrolled in a course, also includes a button to view modules.
     *
     * @param student The student whose information should be displayed
     * @return A panel containing the student's information
     */
    private ChiUniPanel createStudentCard(Student student)
    {
        ChiUniPanel card = new ChiUniPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Student name and basic info
        JLabel nameLabel = new JLabel(student.getFirstName() + " " + student.getLastName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        card.add(nameLabel, BorderLayout.NORTH);

        // Additional student details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.add(new JLabel("ID: " + student.getId()));
        detailsPanel.add(new JLabel("Type: " + student.getType()));
        detailsPanel.add(new JLabel("Email: " + student.getEmail()));

        // Add course information if available
        String course = student.getCourse();
        if (course != null && !course.trim().isEmpty() && !course.equals("No course available"))
        {
            detailsPanel.add(Box.createVerticalStrut(5));
            JLabel courseLabel = new JLabel("Course: " + course);
            courseLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            detailsPanel.add(courseLabel);

            // Add view modules button
            ChiUniButton viewModulesBtn = new ChiUniButton("View Modules");
            viewModulesBtn.addActionListener(e -> showModulesDialog(student));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            buttonPanel.add(viewModulesBtn);
            detailsPanel.add(buttonPanel);
        }

        card.add(detailsPanel, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(300, 180));

        return card;
    }

    /**
     * Shows a dialog displaying all modules for the given student's course.
     * Modules are grouped by academic year and displayed in a scrollable list.
     *
     * @param student The student whose course modules should be displayed
     */
    private void showModulesDialog(Student student) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Module Management - " + student.getFirstName() + " " + student.getLastName(), true);

        ChiUniPanel contentPanel = new ChiUniPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with module count
        ChiUniPanel headerPanel = new ChiUniPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel countLabel = new JLabel("Selected Modules: 0");
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
        assignedPanel.setBorder(BorderFactory.createTitledBorder("Selected Modules"));

        DefaultListModel<ModuleDisplay> assignedModel = new DefaultListModel<>();
        JList<ModuleDisplay> assignedList = new JList<>(assignedModel);
        assignedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        try {
            // Get student's course code
            String courseCode = Course.getCourseCodeFromTitle(student.getCourse());

            // Get all available modules for the course
            List<Module> courseModules = Module.getModulesForCourse(courseCode);
            Map<String, Module> moduleMap = new HashMap<>();
            for (Module module : courseModules) {
                moduleMap.put(module.getCode(), module);
            }

            // Load current assignments
            List<String> assignedModuleIds = StudentModuleAssignment.getStudentAssignments(student.getId());
            if (assignedModuleIds.isEmpty()) {
                // Generate initial assignments if none exist
                StudentModuleAssignment.generateInitialAssignments(student.getId(), courseCode);
                assignedModuleIds = StudentModuleAssignment.getStudentAssignments(student.getId());
            }

            // Add modules to appropriate lists
            List<String> finalAssignedModuleIds = assignedModuleIds;
            moduleMap.forEach((code, module) -> {
                ModuleDisplay display = new ModuleDisplay(module);
                if (finalAssignedModuleIds.contains(code)) {
                    assignedModel.addElement(display);
                } else {
                    availableModel.addElement(display);
                }
            });

            // Update count label
            countLabel.setText(String.format("Selected Modules: %d", assignedModel.getSize()));

        } catch (IOException e) {
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

        assignButton.addActionListener(e -> {
            ModuleDisplay selected = availableList.getSelectedValue();
            if (selected != null) {
                assignedModel.addElement(selected);
                availableModel.removeElement(selected);
                updateAssignments(student.getId(), assignedModel);
                countLabel.setText(String.format("Selected Modules: %d", assignedModel.getSize()));
            }
        });

        unassignButton.addActionListener(e -> {
            ModuleDisplay selected = assignedList.getSelectedValue();
            if (selected != null)  {
                availableModel.addElement(selected);
                assignedModel.removeElement(selected);
                updateAssignments(student.getId(), assignedModel);
                countLabel.setText(String.format("Selected Modules: %d", assignedModel.getSize()));
            } else if (selected != null) {
                JOptionPane.showMessageDialog(dialog,
                        "Cannot remove core module: " + selected.getModule().getName(),
                        "Core Module",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add buttons to panel
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
    private static class ModuleDisplay {
        private final Module module;

        public ModuleDisplay(Object module) {
            this.module = (Module) module;
        }

//        @Override
//        public String toString() {
//            String coreIndicator = module.isCore() ? " (Core)" : "";
//            return String.format("%s - %s%s", module.getCode(), module.getName(), coreIndicator);
//        }

        public Module getModule() {
            return module;
        }

        public String getModuleCode() {
            return module.getCode();
        }
    }

    // Helper method to update assignments in storage
    private void updateAssignments(int studentId, DefaultListModel<ModuleDisplay> assignedModel) {
        try {
            List<String> moduleIds = new ArrayList<>();
            for (int i = 0; i < assignedModel.size(); i++) {
                moduleIds.add(assignedModel.getElementAt(i).getModuleCode());
            }
            StudentModuleAssignment.updateStudentAssignments(studentId, moduleIds);
        } catch (IOException e) {
            handleError("Error saving module assignments", e);
        }
    }

    /**
     * Displays the given modules in the provided panel, grouped by academic year.
     *
     * @param modules The list of modules to display
     * @param panel   The panel in which to display the modules
     */
    private void displayModulesInPanel(List<Module> modules, JPanel panel)
    {
        modules.sort((m1, m2) -> m1.getAcYear().compareTo(m2.getAcYear()));

        String currentYear = "";
        for (Module module : modules)
        {
            if (!currentYear.equals(module.getAcYear()))
            {
                currentYear = module.getAcYear();
                JLabel yearLabel = new JLabel("Year 20" + currentYear);
                yearLabel.setFont(new Font("Arial", Font.BOLD, 14));
                yearLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
                panel.add(yearLabel);
            }

            JPanel modulePanel = new JPanel();
            modulePanel.setLayout(new BoxLayout(modulePanel, BoxLayout.Y_AXIS));
            modulePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            modulePanel.add(new JLabel(module.getName()));
            modulePanel.add(new JLabel("Code: " + module.getCode()));

            modulePanel.setMaximumSize(new Dimension(400, 60));
            panel.add(modulePanel);
            panel.add(Box.createVerticalStrut(5));
        }
    }

    /**
     * Adds a close button to the module dialog.
     */
    private void addCloseButtonToDialog()
    {
        ChiUniButton closeButton = new ChiUniButton("Close");
        closeButton.addActionListener(e -> moduleDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        moduleDialog.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Displays an error message dialog with the given message and exception details.
     *
     * @param message The error message to display
     * @param ex      The exception that caused the error
     */
    private void handleError(String message, Exception ex)
    {
        String errorMessage = message + ": " + ex.getMessage();
        JOptionPane.showMessageDialog(this,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}