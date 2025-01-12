package gui;

import business.Course;
import business.Department;
import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class DepartmentDetailPanel extends ChiUniPanel
{
    private final DepartmentId departmentId;
    private final DepartmentPanel parentPanel;
    private final ChiUniTextArea coursesArea;

    public DepartmentDetailPanel(DepartmentId departmentId, DepartmentPanel parentPanel)
    {
        this.departmentId = departmentId;
        this.parentPanel = parentPanel;
        this.coursesArea = new ChiUniTextArea();

        initializeUI();
        loadDepartmentCourses();
    }

    private void initializeUI()
    {
        setLayout(new BorderLayout(10, 10));

        // Create header panel
        ChiUniPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create courses display area
        coursesArea.setEditable(false);
        coursesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(coursesArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

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
        ChiUniButton refreshButton = new ChiUniButton("Refresh Courses");
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

            if (courses.isEmpty())
            {
                coursesArea.setText("No courses found for " + departmentId.getDepartmentName() + " department.");
                return;
            }

            StringBuilder content = new StringBuilder();
            content.append("Courses in ").append(departmentId.getDepartmentName()).append(" Department:\n");
            content.append("=".repeat(50)).append("\n\n");

            courses.forEach(course ->
            {
                content.append(String.format("Course: %s\n", course.getCourseTitle()));
                content.append(String.format("ID: %s\n", course.getCourseId()));
                content.append("-".repeat(50)).append("\n\n");
            });

            content.append(String.format("\nTotal Courses: %d", courses.size()));

            coursesArea.setText(content.toString());

        }
        catch (IOException ex)
        {
            coursesArea.setText("Error loading courses: " + ex.getMessage());
        }
    }
}
