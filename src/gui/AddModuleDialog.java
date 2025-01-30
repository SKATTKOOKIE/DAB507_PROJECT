package gui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;
import business.DepartmentId;
import business.Course;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import gui.templates.*;

public class AddModuleDialog extends JDialog
{
    private final JTextField nameField;
    private final JTextField codeField;
    private final JTextField acYearField;
    private final JComboBox<DepartmentId> departmentCombo;
    private final GuiMainScreen mainScreen;
    private List<String> selectedCourses;

    public AddModuleDialog(Frame owner, GuiMainScreen mainScreen)
    {
        super(owner, "Add New Module", true);
        this.mainScreen = mainScreen;
        setLayout(new BorderLayout(10, 10));

        // Initialize components
        this.nameField = new JTextField(30);
        this.codeField = new JTextField(10);
        this.acYearField = new JTextField(4);
        this.departmentCombo = new JComboBox<>(DepartmentId.values());
        departmentCombo.removeItem(DepartmentId.UNKNOWN);
        departmentCombo.setSelectedIndex(0);
        this.selectedCourses = new ArrayList<>();

        // Set current year as default
        Calendar cal = Calendar.getInstance();
        String currentYear = String.format("%02d", cal.get(Calendar.YEAR) % 100);
        acYearField.setText(currentYear);

        // Make code field read-only
        codeField.setEditable(false);

        // Setup UI
        setupUI();

        // Generate initial code
        generateNewCode();

        // Add department selection listener
        departmentCombo.addActionListener(e -> updateSelectedCourses());

        pack();
        setLocationRelativeTo(owner);
    }

    private void setupUI()
    {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add form fields
        addFormField(formPanel, "Module Name:", nameField, gbc);

        // Code field panel with generate button
        JPanel codePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        codePanel.add(codeField);
        ChiUniButton generateButton = new ChiUniButton("Generate New Code");
        generateButton.addActionListener(e -> generateNewCode());
        codePanel.add(generateButton);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Module Code:"), gbc);
        gbc.gridx = 1;
        formPanel.add(codePanel, gbc);

        addFormField(formPanel, "Academic Year:", acYearField, gbc);
        addFormField(formPanel, "Department:", departmentCombo, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ChiUniButton saveButton = new ChiUniButton("Save");
        ChiUniButton cancelButton = new ChiUniButton("Cancel");

        saveButton.addActionListener(e -> saveModule());
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
                    "Error generating module code: " + e.getMessage(),
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
            // Generate a 3-character hex string
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < 3; i++)
            {
                code.append(Integer.toHexString(random.nextInt(16)));
            }
            // Add year suffix
            newCode = code.toString() + "-" + acYearField.getText();
        } while (existingCodes.contains(newCode));

        return newCode;
    }

    private Set<String> getExistingCodes() throws IOException
    {
        Set<String> codes = new HashSet<>();
        JsonProcessor processor = new JsonProcessor(FilePathHandler.MODULES_FILE.getNormalisedPath());
        processor.processFile();
        JsonObject jsonObject = (JsonObject) processor.getJsonContent();
        JsonArray modulesArray = jsonObject.getAsJsonArray("modules");

        for (int i = 0; i < modulesArray.size(); i++)
        {
            JsonObject module = modulesArray.get(i).getAsJsonObject();
            codes.add(module.get("module_code").getAsString());
        }
        return codes;
    }

    private void updateSelectedCourses()
    {
        try
        {
            DepartmentId selectedDept = (DepartmentId) departmentCombo.getSelectedItem();
            if (selectedDept != null)
            {
                List<Course> allCourses = Course.getAll();
                List<Course> departmentCourses = allCourses.stream()
                        .filter(course ->
                        {
                            DepartmentId deptId = course.getDepartmentId();
                            return deptId != null && deptId.getDepartmentName().equals(selectedDept.getDepartmentName());
                        })
                        .collect(Collectors.toList());

                // Randomly select up to 4 courses
                Collections.shuffle(departmentCourses);
                selectedCourses = departmentCourses.stream()
                        .limit(4)
                        .map(Course::getCourseId)
                        .collect(Collectors.toList());
            }
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error loading courses: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveModule()
    {
        try
        {
            // Validate input
            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            String acYear = acYearField.getText().trim();

            if (name.isEmpty() || code.isEmpty() || acYear.isEmpty())
            {
                throw new IllegalArgumentException("All fields are required.");
            }

            if (selectedCourses.isEmpty())
            {
                throw new IllegalArgumentException("No courses available for selected department.");
            }

            // Load existing modules
            JsonProcessor processor = new JsonProcessor(FilePathHandler.MODULES_FILE.getNormalisedPath());
            processor.processFile();
            JsonObject jsonObject = (JsonObject) processor.getJsonContent();
            JsonArray modulesArray = jsonObject.getAsJsonArray("modules");

            // Create new module object
            JsonObject newModule = new JsonObject();
            newModule.addProperty("module_name", name);
            newModule.addProperty("module_code", code);
            newModule.addProperty("ac_year", acYear);

            // Add associated courses
            JsonArray coursesArray = new JsonArray();
            selectedCourses.forEach(coursesArray::add);
            newModule.add("associated_courses", coursesArray);

            // Add to modules array
            modulesArray.add(newModule);

            // Write back to file
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(FilePathHandler.MODULES_FILE.getNormalisedPath()))
            {
                gson.toJson(jsonObject, writer);
            }

            dispose();
            JOptionPane.showMessageDialog(this,
                    "Module saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh only module data
            mainScreen.refreshSpecificData(DataManager.DataType.MODULES);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this,
                    "Error saving module: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}