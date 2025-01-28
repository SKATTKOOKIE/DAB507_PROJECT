package gui;

import business.StaffModuleAssignment;
import business.StudentModuleAssignment;
import com.google.gson.JsonObject;
import users.Staff;
import users.Student;
import users.StudentType;
import business.DepartmentId;
import business.Course;
import file_handling.JsonProcessor;
import file_handling.FilePathHandler;
import file_handling.UserDataManager;
import com.google.gson.JsonArray;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class AddUserDialog extends JDialog
{
    private final JComboBox<String> userTypeCombo;
    private final JPanel dynamicFieldsPanel;
    private final CardLayout cardLayout;

    // Common fields
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField emailField;

    // Student-specific fields
    private final JComboBox<StudentType> studentTypeCombo;
    private final JComboBox<String> genderCombo;
    private final JComboBox<String> courseCombo;
    private List<Course> availableCourses;

    // Staff-specific fields
    private final JSpinner weeklyHoursSpinner;
    private final JSpinner maxModulesSpinner;
    private final JComboBox<DepartmentId> departmentCombo;
    private final JTextField guidField;

    private final GuiMainScreen mainScreen;

    public AddUserDialog(Frame owner, GuiMainScreen mainScreen)
    {
        super(owner, "Add New User", true);
        this.mainScreen = mainScreen;  // Initialize the field
        setLayout(new BorderLayout(10, 10));

        this.availableCourses = new ArrayList<>();
        this.courseCombo = new JComboBox<>();
        this.userTypeCombo = new JComboBox<>(new String[]{"Student", "Staff"});
        this.cardLayout = new CardLayout();
        this.dynamicFieldsPanel = new JPanel(cardLayout);

        // Initialize common fields
        this.firstNameField = new JTextField(20);
        this.lastNameField = new JTextField(20);
        this.emailField = new JTextField(20);

        // Initialize student fields
        this.studentTypeCombo = new JComboBox<>(StudentType.values());
        this.genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});

        // Initialize staff fields
        this.weeklyHoursSpinner = new JSpinner(new SpinnerNumberModel(37, 0, 40, 1));
        this.maxModulesSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));
        this.departmentCombo = new JComboBox<>(DepartmentId.values());
        this.guidField = new JTextField(20);

        // Load courses before creating the UI
        loadCourses();

        // Setup UI
        setupUI();

        pack();
        setLocationRelativeTo(owner);
        switchUserType(); // Initialize with default selection
    }

    private void setupUI()
    {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // User type selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userTypeCombo.addActionListener(e -> switchUserType());
        typePanel.add(new JLabel("User Type:"));
        typePanel.add(userTypeCombo);
        mainPanel.add(typePanel, BorderLayout.NORTH);

        // Common fields
        JPanel commonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addFormField(commonPanel, "First Name:", firstNameField, gbc);
        addFormField(commonPanel, "Last Name:", lastNameField, gbc);
        addFormField(commonPanel, "Email:", emailField, gbc);

        // Student-specific fields
        JPanel studentPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addFormField(studentPanel, "Student Type:", studentTypeCombo, gbc);
        addFormField(studentPanel, "Gender:", genderCombo, gbc);
        addFormField(studentPanel, "Course:", courseCombo, gbc);

        // Staff-specific fields
        JPanel staffPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addFormField(staffPanel, "Weekly Hours:", weeklyHoursSpinner, gbc);
        addFormField(staffPanel, "Max Modules:", maxModulesSpinner, gbc);
        addFormField(staffPanel, "Department:", departmentCombo, gbc);
        addFormField(staffPanel, "GUID:", guidField, gbc);

        dynamicFieldsPanel.add(studentPanel, "STUDENT");
        dynamicFieldsPanel.add(staffPanel, "STAFF");

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(commonPanel, BorderLayout.NORTH);
        contentPanel.add(dynamicFieldsPanel, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ChiUniButton saveButton = new ChiUniButton("Save");
        ChiUniButton cancelButton = new ChiUniButton("Cancel");

        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void loadCourses()
    {
        try
        {
            JsonProcessor processor = new JsonProcessor(FilePathHandler.COURSES_FILE.getNormalisedPath());
            processor.processFile();
            JsonObject jsonObject = (JsonObject) processor.getJsonContent();

            JsonArray coursesArray = jsonObject.getAsJsonArray("courses");
            if (coursesArray != null && coursesArray.size() > 0)
            {
                courseCombo.removeAllItems();
                for (int i = 0; i < coursesArray.size(); i++)
                {
                    JsonObject courseObj = coursesArray.get(i).getAsJsonObject();
                    String courseName = courseObj.get("name").getAsString();
                    if (courseName != null && !courseName.trim().isEmpty())
                    {
                        courseCombo.addItem(courseName);
                    }
                }

                if (courseCombo.getItemCount() > 0)
                {
                    courseCombo.setSelectedIndex(0);
                }
            }
            else
            {
                throw new IOException("No courses found in the courses file");
            }
        }
        catch (IOException e)
        {
            String errorMsg = "Error loading courses: " + e.getMessage();
            JOptionPane.showMessageDialog(this,
                    errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            courseCombo.addItem("No courses available");
        }
    }

    private void switchUserType()
    {
        String selectedType = (String) userTypeCombo.getSelectedItem();
        cardLayout.show(dynamicFieldsPanel, selectedType.toUpperCase());
        pack();
    }

    private <T extends JComponent> T addFormField(JPanel panel, String label, T component, GridBagConstraints gbc)
    {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
        gbc.gridy++;
        return component;
    }

    private void saveUser()
    {
        try
        {
            if (userTypeCombo.getSelectedItem().equals("Student"))
            {
                saveStudent();
            }
            else
            {
                saveStaff();
            }
            dispose();

            // Show success message
            JOptionPane.showMessageDialog(this, "User saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh the main screen data
            mainScreen.refreshData("Refreshing data after adding new user...");
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Error saving user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveStudent() throws IOException
    {
        // Check if course is selected
        Object selectedCourse = courseCombo.getSelectedItem();
        if (selectedCourse == null)
        {
            throw new IOException("Please select a course. If no courses are available, check the courses file.");
        }

        // Validate other required fields before creating student object
        if (firstNameField.getText().trim().isEmpty() ||
                lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                studentTypeCombo.getSelectedItem() == null ||
                genderCombo.getSelectedItem() == null)
        {
            throw new IOException("All fields are required. Please fill in all information.");
        }

        Student student = new Student();
        student.setId(getNextStudentId());
        student.setFirstName(firstNameField.getText().trim());
        student.setLastName(lastNameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setType(studentTypeCombo.getSelectedItem().toString());
        student.setGender(genderCombo.getSelectedItem().toString());
        student.setCourse(selectedCourse.toString());

        try
        {
            // First validate and save the student
            UserDataManager.validateUser(student);
            UserDataManager.addStudent(student);

            // Get the course code for the selected course
            String courseCode = Course.getCourseCodeFromTitle(student.getCourse());
            if (courseCode == null || courseCode.isEmpty())
            {
                throw new IOException("Could not find course code for the selected course");
            }

            // Generate initial module assignments for the student
            StudentModuleAssignment.generateInitialAssignments(student.getId(), courseCode);
        }
        catch (IllegalArgumentException e)
        {
            throw new IOException("Validation failed: " + e.getMessage());
        }
    }

    private void saveStaff() throws IOException
    {
        Staff staff = new Staff();
        staff.setId(getNextStaffId());
        staff.setFirstName(firstNameField.getText());
        staff.setLastName(lastNameField.getText());
        staff.setEmail(emailField.getText());
        staff.setWeeklyHours((Integer) weeklyHoursSpinner.getValue());
        staff.setMaxModules((Integer) maxModulesSpinner.getValue());
        staff.setDepartmentId((DepartmentId) departmentCombo.getSelectedItem());
        staff.setGuid(guidField.getText());

        try
        {
            // First validate and save the staff member
            UserDataManager.validateUser(staff);
            UserDataManager.addStaff(staff);

            // Generate initial module assignments for the staff member
            Map<Integer, StaffModuleAssignment> currentAssignments = StaffModuleAssignment.loadAssignments();
            if (!currentAssignments.containsKey(staff.getId()))
            {
                StaffModuleAssignment.generateInitialAssignments();
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new IOException("Validation failed: " + e.getMessage());
        }
    }

    private int getNextStudentId() throws IOException
    {
        List<Student> students = Student.getByCourse("");
        return students.stream()
                .mapToInt(Student::getId)
                .max()
                .orElse(0) + 1;
    }

    private int getNextStaffId() throws IOException
    {
        List<Staff> staff = Staff.getByDepartment("");
        return staff.stream()
                .mapToInt(Staff::getId)
                .max()
                .orElse(0) + 1;
    }
}