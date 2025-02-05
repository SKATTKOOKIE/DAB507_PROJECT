package gui.components.dialogs;

import com.google.gson.*;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;
import business.DepartmentId;
import gui.DataManager;
import gui.GuiMainScreen;
import gui.templates.ChiUniButton;
import gui.templates.ChiUniDialog;
import gui.components.combo.DepartmentComboBox;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Dialog for adding a new course to the system.
 * This dialog provides a form interface for creating new courses with:
 * <ul>
 *   <li>Course name input field</li>
 *   <li>Automatic course code generation</li>
 *   <li>Department selection via dropdown</li>
 *   <li>Save and cancel functionality</li>
 * </ul>
 * The dialog ensures unique course codes and proper data validation
 * before saving to the system.
 */
public class AddCourseDialog extends ChiUniDialog
{
    /**
     * Dialog components:
     * <ul>
     *   <li>{@code nameField} - Text field for entering course name</li>
     *   <li>{@code codeField} - Read-only field displaying generated course code</li>
     *   <li>{@code departmentCombo} - Dropdown for selecting associated department</li>
     * </ul>
     */
    private final JTextField nameField;
    private final JTextField codeField;
    private final DepartmentComboBox departmentCombo;

    /**
     * Creates a new AddCourseDialog.
     *
     * @param owner      The parent frame that owns this dialog
     * @param mainScreen Reference to the main application screen for updates
     */
    public AddCourseDialog(Frame owner, GuiMainScreen mainScreen)
    {
        super(owner, "Add New Course", mainScreen, true);

        // Initialise components
        this.nameField = new JTextField(20);
        this.codeField = new JTextField(10);
        this.departmentCombo = new DepartmentComboBox();

        setupUI();
        generateNewCode();
        centerOnOwner();
    }

    /**
     * Sets up the dialog's user interface.
     * Creates and arranges all UI components including:
     * <ul>
     *   <li>Form fields for course details</li>
     *   <li>Code generation button</li>
     *   <li>Department selection dropdown</li>
     *   <li>Save and cancel buttons</li>
     * </ul>
     */
    private void setupUI()
    {
        JPanel formPanel = createFormPanel();

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

        addStandardButtons(this::saveCourse);
    }

    /**
     * Creates and configures GridBagConstraints for form layout.
     *
     * @return Configured GridBagConstraints object
     */
    private GridBagConstraints createGBC()
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    /**
     * Generates a new unique course code.
     * Handles any errors during code generation by displaying an error message.
     */
    private void generateNewCode()
    {
        try
        {
            String newCode = generateUniqueCode();
            codeField.setText(newCode);
        }
        catch (IOException e)
        {
            showError(e.getMessage(), "Error Generating Course Code");
        }
    }

    /**
     * Generates a unique 6-digit course code.
     * Ensures the generated code doesn't conflict with existing course codes.
     *
     * @return A unique 6-digit course code
     * @throws IOException If there is an error reading existing course data
     */
    private String generateUniqueCode() throws IOException
    {
        Set<String> existingCodes = getExistingCodes();
        Random random = new Random();
        String newCode;

        do
        {
            newCode = String.format("%06d", random.nextInt(1000000));
        }
        while (existingCodes.contains(newCode));

        return newCode;
    }

    /**
     * Retrieves all existing course codes from the system.
     *
     * @return Set of existing course codes
     * @throws IOException If there is an error reading the courses file
     */
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

    /**
     * Saves the new course to the system.
     * Validates input, creates a new course entry, and updates the courses file.
     * Displays appropriate success or error messages and refreshes the main screen
     * on successful save.
     */
    private void saveCourse()
    {
        try
        {
            // Validate input
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            DepartmentId department = (DepartmentId) departmentCombo.getSelectedItem();

            if (!validateRequiredFields(nameField, codeField))
            {
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
            if (department != null)
            {
                newCourse.addProperty("department", department.getDepartmentName());
            }
            else
            {
                newCourse.add("department", null);
            }

            coursesArray.add(newCourse);

            // Write back to file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(FilePathHandler.COURSES_FILE.getNormalisedPath()))
            {
                gson.toJson(jsonObject, writer);
            }

            showSuccess("Course saved successfully!");
            mainScreen.refreshSpecificData(DataManager.DataType.COURSES);
            dispose();
        }
        catch (Exception e)
        {
            showError("Error saving course: " + e.getMessage(), "Error");
        }
    }
}