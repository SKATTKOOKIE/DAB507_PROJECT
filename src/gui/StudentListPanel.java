package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import users.Student;

public class StudentListPanel extends ChiUniPanel
{
    private final ChiUniPanel studentsContainer;

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
            List<Student> students = Student.getByDepartment("");  // Get all students
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

        card.add(detailsPanel, BorderLayout.CENTER);

        // Set preferred size for consistent card dimensions
        card.setPreferredSize(new Dimension(300, 150));

        return card;
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
