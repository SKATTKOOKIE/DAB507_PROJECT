package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import business.Module;
import users.Student;
import business.Course;

public class StudentListPanel extends ChiUniPanel
{
    private final ChiUniPanel studentsContainer;
    private JDialog moduleDialog;

    public StudentListPanel()
    {
        setLayout(new BorderLayout(10, 10));

        // Create header
        JLabel titleLabel = new JLabel("Student Directory", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Create scrollable container for student cards
        studentsContainer = new ChiUniPanel();
        studentsContainer.setLayout(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(studentsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        loadStudents();
    }

    private void loadStudents()
    {
        try
        {
            List<Student> students = Student.getByCourse("");  // Get all students
            displayStudents(students);
        }
        catch (IOException e)
        {
            handleError("Error loading students", e);
        }
    }

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

        // Add course information
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

        // Increase card height to accommodate new content
        card.setPreferredSize(new Dimension(300, 180));

        return card;
    }

    private void showModulesDialog(Student student)
    {
        moduleDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Modules for " + student.getFirstName() + " " + student.getLastName(), true);
        moduleDialog.setLayout(new BorderLayout());

        // Create panel for modules
        JPanel modulesPanel = new JPanel();
        modulesPanel.setLayout(new BoxLayout(modulesPanel, BoxLayout.Y_AXIS));

        try
        {
            // Get the proper course code from the course title
            String courseCode = Course.getCourseCodeFromTitle(student.getCourse());
            List<Module> modules = Module.getModulesForCourse(courseCode);

            if (modules.isEmpty())
            {
                modulesPanel.add(new JLabel("No modules found for this course: " + student.getCourse() + " (" + courseCode + ")"));
            }
            else
            {
                // Sort modules by academic year
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
                        modulesPanel.add(yearLabel);
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
                    modulesPanel.add(modulePanel);
                    modulesPanel.add(Box.createVerticalStrut(5));
                }
            }
        }
        catch (IOException e)
        {
            modulesPanel.add(new JLabel("Error loading modules: " + e.getMessage()));
        }

        JScrollPane scrollPane = new JScrollPane(modulesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        moduleDialog.add(scrollPane, BorderLayout.CENTER);

        // Add close button
        ChiUniButton closeButton = new ChiUniButton("Close");
        closeButton.addActionListener(e -> moduleDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        moduleDialog.add(buttonPanel, BorderLayout.SOUTH);

        moduleDialog.setSize(450, 500);
        moduleDialog.setLocationRelativeTo(this);
        moduleDialog.setVisible(true);
    }

    private void handleError(String message, Exception ex)
    {
        String errorMessage = message + ": " + ex.getMessage();
        JOptionPane.showMessageDialog(this,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}