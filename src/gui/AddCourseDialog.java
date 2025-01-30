package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;
import business.DepartmentId;
import gui.templates.ChiUniButton;
import gui.templates.ChiUniDialog;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AddCourseDialog extends ChiUniDialog {
    private final JTextField nameField;
    private final JTextField codeField;
    private final JComboBox<DepartmentId> departmentCombo;

    public AddCourseDialog(Frame owner, GuiMainScreen mainScreen) {
        super(owner, "Add New Course", mainScreen, true);

        // Initialize components
        this.nameField = new JTextField(20);
        this.codeField = new JTextField(10);
        this.departmentCombo = new JComboBox<>(DepartmentId.values());
        departmentCombo.removeItem(DepartmentId.UNKNOWN);
        departmentCombo.setSelectedIndex(0);

        setupUI();
        generateNewCode();
        centerOnOwner();
    }

    private void setupUI() {
        // Form panel setup
        JPanel formPanel = createFormPanel();

        // Add form fields
        addFormField(formPanel, "Course Name:", nameField, createGBC());

        // Code field panel with generate button
        JPanel codePanel = createFieldPanel("", codeField);
        codeField.setEditable(false);
        ChiUniButton generateButton = new ChiUniButton("Generate New Code");
        generateButton.addActionListener(e -> generateNewCode());
        codePanel.add(generateButton);

        GridBagConstraints gbc = createGBC();
        gbc.gridy = 1;
        formPanel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1;
        formPanel.add(codePanel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        addFormField(formPanel, "Department:", departmentCombo, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Add standard save/cancel buttons
        addStandardButtons(this::saveCourse);
    }

    private GridBagConstraints createGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    private void generateNewCode() {
        try {
            String newCode = generateUniqueCode();
            codeField.setText(newCode);
        } catch (IOException e) {
            showError(e.getMessage(), "Error Generating Course Code");
        }
    }

    private String generateUniqueCode() throws IOException {
        Set<String> existingCodes = getExistingCodes();
        Random random = new Random();
        String newCode;

        do {
            newCode = String.format("%06d", random.nextInt(1000000));
        } while (existingCodes.contains(newCode));

        return newCode;
    }

    private Set<String> getExistingCodes() throws IOException {
        Set<String> codes = new HashSet<>();
        JsonProcessor processor = new JsonProcessor(FilePathHandler.COURSES_FILE.getNormalisedPath());
        processor.processFile();
        JsonObject jsonObject = (JsonObject) processor.getJsonContent();
        JsonArray coursesArray = jsonObject.getAsJsonArray("courses");

        for (int i = 0; i < coursesArray.size(); i++) {
            JsonObject course = coursesArray.get(i).getAsJsonObject();
            codes.add(course.get("code").getAsString());
        }
        return codes;
    }

    private void saveCourse() {
        try {
            // Validate input
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            DepartmentId department = (DepartmentId) departmentCombo.getSelectedItem();

            if (!validateRequiredFields(nameField, codeField)) {
                return;
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
            if (department != null) {
                newCourse.addProperty("department", department.getDepartmentName());
            } else {
                newCourse.add("department", null);
            }

            // Add to courses array
            coursesArray.add(newCourse);

            // Write back to file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(FilePathHandler.COURSES_FILE.getNormalisedPath())) {
                gson.toJson(jsonObject, writer);
            }

            // Show success and refresh
            showSuccess("Course saved successfully!");
            mainScreen.refreshSpecificData(DataManager.DataType.COURSES);
            dispose();
        } catch (Exception e) {
            showError("Error saving course: " + e.getMessage(), "Error");
        }
    }
}