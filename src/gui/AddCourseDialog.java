package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;
import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import gui.templates.*;

public class AddCourseDialog extends JDialog
{
    private final JTextField nameField;
    private final JTextField codeField;
    private final JComboBox<DepartmentId> departmentCombo;
    private final GuiMainScreen mainScreen;

    public AddCourseDialog(Frame owner, GuiMainScreen mainScreen)
    {
        super(owner, "Add New Course", true);
        this.mainScreen = mainScreen;
        setLayout(new BorderLayout(10, 10));

        // Initialise components
        this.nameField = new JTextField(20);
        this.codeField = new JTextField(10);
        this.departmentCombo = new JComboBox<>(DepartmentId.values());
        departmentCombo.removeItem(DepartmentId.UNKNOWN); // Remove UNKNOWN from selection
        departmentCombo.setSelectedIndex(0);

        // Setup UI
        setupUI();

        // Generate initial code
        generateNewCode();

        pack();
        setLocationRelativeTo(owner);
    }

    private void setupUI()
    {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addFormField(formPanel, "Course Name:", nameField, gbc);

        // Code field panel with generate button
        JPanel codePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        codeField.setEditable(false); // Make code field read-only
        codePanel.add(codeField);
        ChiUniButton generateButton = new ChiUniButton("Generate New Code");
        generateButton.addActionListener(e -> generateNewCode());
        codePanel.add(generateButton);

        gbc.gridx = 0;
        formPanel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1;
        formPanel.add(codePanel, gbc);
        gbc.gridy++;

        addFormField(formPanel, "Department:", departmentCombo, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ChiUniButton saveButton = new ChiUniButton("Save");
        ChiUniButton cancelButton = new ChiUniButton("Cancel");

        saveButton.addActionListener(e -> saveCourse());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private <T extends JComponent> void addFormField(JPanel panel, String label, T component, GridBagConstraints gbc)
    {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
        gbc.gridy++;
    }

    private void generateNewCode()
    {
        try
        {
            String newCode = generateUniqueCode();
            codeField.setText(newCode);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error generating course code: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateUniqueCode() throws IOException
    {
        // Load existing codes
        Set<String> existingCodes = getExistingCodes();
        Random random = new Random();
        String newCode;

        do
        {
            // Generate a 6-digit number
            newCode = String.format("%06d", random.nextInt(1000000));
        } while (existingCodes.contains(newCode));

        return newCode;
    }

    private Set<String> getExistingCodes() throws IOException
    {
        Set<String> codes = new HashSet<>();
        JsonProcessor processor = new JsonProcessor(FilePathHandler.COURSES_FILE.getNormalisedPath());
        processor.processFile();
        JsonObject jsonObject = (JsonObject) processor.getJsonContent();
        JsonArray coursesArray = jsonObject.getAsJsonArray("courses");

        for (int i = 0; i < coursesArray.size(); i++)
        {
            JsonObject course = coursesArray.get(i).getAsJsonObject();
            codes.add(course.get("code").getAsString());
        }
        return codes;
    }

    private void saveCourse()
    {
        try
        {
            // Validate input
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            DepartmentId department = (DepartmentId) departmentCombo.getSelectedItem();

            if (name.isEmpty() || code.isEmpty())
            {
                throw new IllegalArgumentException("Course name and code are required.");
            }

            // Load existing courses
            JsonProcessor processor = new JsonProcessor(FilePathHandler.COURSES_FILE.getNormalisedPath());
            processor.processFile();
            JsonObject jsonObject = (JsonObject) processor.getJsonContent();
            JsonArray coursesArray = jsonObject.getAsJsonArray("courses");

            // Create new course object
            JsonObject newCourse = new JsonObject();
            newCourse.addProperty("name", name);
            newCourse.addProperty("code", code);
            if (department != null)
            {
                newCourse.addProperty("department", department.getDepartmentName());
            }
            else
            {
                newCourse.add("department", null);
            }

            // Add to courses array
            coursesArray.add(newCourse);

            // Write back to file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(FilePathHandler.COURSES_FILE.getNormalisedPath()))
            {
                gson.toJson(jsonObject, writer);
            }

            dispose();
            JOptionPane.showMessageDialog(this,
                    "Course saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh only course data
            mainScreen.refreshSpecificData(DataManager.DataType.COURSES);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error saving course: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}