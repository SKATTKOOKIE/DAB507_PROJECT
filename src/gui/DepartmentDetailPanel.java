package gui;

import business.Course;
import business.Department;
import business.DepartmentId;
import business.Module;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDetailPanel extends ChiUniPanel
{
    private final DepartmentId departmentId;
    private final DepartmentPanel parentPanel;
    private final CardLayout cardLayout;
    private final ChiUniPanel contentPanel;
    private final ChiUniPanel coursesPanel;
    private final ChiUniPanel modulePanel;

    public DepartmentDetailPanel(DepartmentId departmentId, DepartmentPanel parentPanel)
    {
        this.departmentId = departmentId;
        this.parentPanel = parentPanel;
        this.cardLayout = new CardLayout();
        this.contentPanel = new ChiUniPanel();
        this.coursesPanel = new ChiUniPanel();
        this.modulePanel = new ChiUniPanel();

        initializeUI();
        loadDepartmentCourses();
    }

    private void initializeUI()
    {
        setLayout(new BorderLayout(10, 10));

        // Create header panel
        ChiUniPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Setup content panel with card layout
        contentPanel.setLayout(cardLayout);

        // Setup courses panel
        coursesPanel.setLayout(new GridBagLayout());
        JScrollPane coursesScrollPane = new JScrollPane(coursesPanel);
        coursesScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        coursesScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Setup modules panel
        modulePanel.setLayout(new GridBagLayout());
        JScrollPane moduleScrollPane = new JScrollPane(modulePanel);
        moduleScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        moduleScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add panels to card layout
        contentPanel.add(coursesScrollPane, "COURSES");
        contentPanel.add(moduleScrollPane, "MODULES");

        add(contentPanel, BorderLayout.CENTER);

        // Create buttons panel
        ChiUniPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private ChiUniPanel createHeaderPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Add department title
        JLabel titleLabel = new JLabel(departmentId.getDepartmentName() + " Department", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.CENTER);

        // Add back button
        ChiUniButton backButton = new ChiUniButton("← Back to Departments");
        backButton.addActionListener(e -> parentPanel.showDepartmentsList());
        panel.add(backButton, BorderLayout.WEST);

        return panel;
    }

    private ChiUniPanel createButtonsPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Add refresh button
        ChiUniButton refreshButton = new ChiUniButton("Refresh");
        refreshButton.addActionListener(e -> loadDepartmentCourses());
        panel.add(refreshButton);

        return panel;
    }

    private void loadDepartmentCourses()
    {
        try
        {
            Department department = new Department(departmentId.getDepartmentName(), departmentId);
            List<Course> courses = Course.getCoursesByDepartment(department);

            // Clear existing content
            coursesPanel.removeAll();

            if (courses.isEmpty())
            {
                JLabel noCoursesLabel = new JLabel("No courses found for " + departmentId.getDepartmentName() + " department.");
                noCoursesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                coursesPanel.add(noCoursesLabel);
            }
            else
            {
                // Setup GridBagLayout constraints
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 20, 5, 20);

                // Add title
                JLabel titleLabel = new JLabel("Available Courses", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
                gbc.insets = new Insets(20, 20, 30, 20);
                coursesPanel.add(titleLabel, gbc);

                // Reset insets for buttons
                gbc.insets = new Insets(5, 20, 5, 20);

                // Add course buttons
                for (Course course : courses)
                {
                    ChiUniButton courseButton = createCourseButton(course);
                    coursesPanel.add(courseButton, gbc);
                }

                // Add empty space at the bottom
                gbc.weighty = 1.0;
                coursesPanel.add(Box.createVerticalGlue(), gbc);
            }

            // Show courses panel
            cardLayout.show(contentPanel, "COURSES");

            // Refresh the panel
            coursesPanel.revalidate();
            coursesPanel.repaint();

        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Error loading courses: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private ChiUniButton createCourseButton(Course course)
    {
        ChiUniButton button = new ChiUniButton(course.getCourseTitle());
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(300, 50));

        // Add click handler to show modules
        button.addActionListener(e -> showModulesForCourse(course));

        return button;
    }

    private void showModulesForCourse(Course course) {
        modulePanel.removeAll();

        try {
            // Get modules specifically for this course
            List<Module> modules = new ArrayList<>();
            String courseCode = course.getCourseCode();
            if (course.hasValidCourseCode()) {
                modules = Module.getModulesForCourse(courseCode);
            }

            // Setup layout
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 20, 5, 20);

            // Add course title
            JLabel courseTitle = new JLabel(course.getCourseTitle(), SwingConstants.CENTER);
            courseTitle.setFont(new Font("Arial", Font.BOLD, 20));
            gbc.insets = new Insets(20, 20, 30, 20);
            modulePanel.add(courseTitle, gbc);

            // Add back to courses button
            ChiUniButton backButton = new ChiUniButton("← Back to Courses");
            backButton.addActionListener(e -> cardLayout.show(contentPanel, "COURSES"));
            gbc.insets = new Insets(5, 20, 20, 20);
            modulePanel.add(backButton, gbc);

            // Reset insets for module list
            gbc.insets = new Insets(5, 20, 5, 20);

            // Add modules list
            if (!modules.isEmpty()) {
                JLabel modulesHeader = new JLabel("Course Modules:", SwingConstants.LEFT);
                modulesHeader.setFont(new Font("Arial", Font.BOLD, 16));
                modulePanel.add(modulesHeader, gbc);

                for (Module module : modules) {
                    // Create panel for module info
                    JPanel moduleInfoPanel = new JPanel(new BorderLayout(10, 5));
                    moduleInfoPanel.setBackground(Color.WHITE);
                    moduleInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(200, 200, 200)),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));

                    // Add module name
                    JLabel nameLabel = new JLabel(module.getName());
                    nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    moduleInfoPanel.add(nameLabel, BorderLayout.CENTER);

                    // Add module code and year
                    JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                    detailsPanel.setBackground(Color.WHITE);

                    JLabel codeLabel = new JLabel("Code: " + module.getCode());
                    codeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                    detailsPanel.add(codeLabel);

                    JLabel yearLabel = new JLabel("Year: 20" + module.getAcYear());
                    yearLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                    detailsPanel.add(yearLabel);

                    moduleInfoPanel.add(detailsPanel, BorderLayout.SOUTH);

                    modulePanel.add(moduleInfoPanel, gbc);
                }
            } else {
                JLabel noModulesLabel = new JLabel("No modules found for this course");
                noModulesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                modulePanel.add(noModulesLabel, gbc);
            }

            // Add empty space at bottom
            gbc.weighty = 1.0;
            modulePanel.add(Box.createVerticalGlue(), gbc);

            // Show the modules panel
            cardLayout.show(contentPanel, "MODULES");

            // Refresh the panel
            modulePanel.revalidate();
            modulePanel.repaint();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading modules: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}