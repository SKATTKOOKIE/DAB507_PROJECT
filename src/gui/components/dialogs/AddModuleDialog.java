package gui.components.dialogs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;
import business.DepartmentId;
import business.Course;
import file_handling.validation.AcademicYearFilter;
import gui.DataManager;
import gui.GuiMainScreen;
import gui.templates.ChiUniButton;
import gui.templates.ChiUniDialog;
import gui.components.combo.DepartmentComboBox;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class AddModuleDialog extends ChiUniDialog
{
    private final JTextField nameField;
    private final JTextField codeField;
    private final JTextField acYearField;
    private final DepartmentComboBox departmentCombo;
    private List<String> selectedCourses;

    public AddModuleDialog(Frame owner, GuiMainScreen mainScreen)
    {
        super(owner, "Add New Module", mainScreen, true);

        // Initialise components
        this.nameField = new JTextField(30);
        this.codeField = new JTextField(10);
        this.acYearField = new JTextField(4);

        // Initialise department combo with custom renderer
        this.departmentCombo = new DepartmentComboBox();
        codeField.setEditable(false);

        // Set current year as default and add input filter
        Calendar cal = Calendar.getInstance();
        String currentYear = String.format("%02d", cal.get(Calendar.YEAR) % 100);
        acYearField.setText(currentYear);

        // Add academic year filter
        ((AbstractDocument) acYearField.getDocument()).setDocumentFilter(new AcademicYearFilter());

        setupUI();
        generateNewCode();
        departmentCombo.addActionListener(e -> updateSelectedCourses());
        centerOnOwner();
    }

    private void setupUI()
    {
        // Form panel setup
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = createGBC();

        // Module Name field
        JLabel nameLabel = new JLabel("Module Name: ");
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        // Module Code field with generate button
        JLabel codeLabel = new JLabel("Module Code: ");
        codeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(codeLabel, gbc);

        JPanel codePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        codePanel.add(codeField);
        ChiUniButton generateButton = new ChiUniButton("Generate New Code");
        generateButton.addActionListener(e -> generateNewCode());
        codePanel.add(generateButton);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(codePanel, gbc);

        // Academic Year field
        JLabel yearLabel = new JLabel("Academic Year: ");
        yearLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(yearLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(acYearField, gbc);

        // Department field
        JLabel deptLabel = new JLabel("Department: ");
        deptLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(deptLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(departmentCombo, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        addStandardButtons(this::saveModule);
    }

    private GridBagConstraints createGBC()
    {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
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
            showError("Error generating module code: " + e.getMessage(), "Error");
        }
    }

    private String generateUniqueCode() throws IOException
    {
        Set<String> existingCodes = getExistingCodes();
        Random random = new Random();
        String newCode;

        do
        {
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < 3; i++)
            {
                code.append(Integer.toHexString(random.nextInt(16)));
            }
            newCode = code.toString() + "-" + acYearField.getText();
        }
        while (existingCodes.contains(newCode));

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

                Collections.shuffle(departmentCourses);
                selectedCourses = departmentCourses.stream()
                        .limit(4)
                        .map(Course::getCourseId)
                        .collect(Collectors.toList());
            }
        }
        catch (IOException e)
        {
            showError("Error loading courses: " + e.getMessage(), "Error");
        }
    }

    private void saveModule()
    {
        try
        {
            if (!validateRequiredFields(nameField, codeField, acYearField))
            {
                return;
            }

            if (selectedCourses.isEmpty())
            {
                throw new IllegalArgumentException("No courses available for selected department.");
            }

            String name = nameField.getText().trim();
            String code = codeField.getText().trim();
            String acYear = acYearField.getText().trim();

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

            // Add to modules array and save
            modulesArray.add(newModule);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(FilePathHandler.MODULES_FILE.getNormalisedPath()))
            {
                gson.toJson(jsonObject, writer);
            }

            showSuccess("Module saved successfully!");
            mainScreen.refreshSpecificData(DataManager.DataType.MODULES);
            dispose();
        }
        catch (Exception e)
        {
            showError("Error saving module: " + e.getMessage(), "Error");
        }
    }
}