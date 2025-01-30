package gui;

import business.StaffModuleAssignment;
import business.StudentModuleAssignment;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import users.Staff;
import users.Student;
import users.StudentType;
import business.DepartmentId;
import business.Course;
import file_handling.JsonProcessor;
import file_handling.FilePathHandler;
import file_handling.UserDataManager;
import gui.templates.ChiUniDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AddUserDialog extends ChiUniDialog
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

    public AddUserDialog(Frame owner, GuiMainScreen mainScreen)
    {
        super(owner, "Add New User", mainScreen, true);

        // Initialize fields
        this.availableCourses = new ArrayList<>();
        this.userTypeCombo = new JComboBox<>(new String[]{"Student", "Staff"});
        this.cardLayout = new CardLayout();
        this.dynamicFieldsPanel = new JPanel(cardLayout);

        // Common fields
        this.firstNameField = new JTextField(20);
        this.lastNameField = new JTextField(20);
        this.emailField = new JTextField(20);

        // Student fields
        this.studentTypeCombo = new JComboBox<>(StudentType.values());
        this.genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        this.courseCombo = new JComboBox<>();

        // Staff fields
        this.weeklyHoursSpinner = new JSpinner(new SpinnerNumberModel(37, 0, 40, 1));
        this.maxModulesSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));
        this.departmentCombo = new JComboBox<>(DepartmentId.values());

        loadCourses();
        setupUI();
        centerOnOwner();
        switchUserType();
    }

    private void setupUI()
    {
        // User type selection panel
        JPanel typePanel = createFieldPanel("User Type:", userTypeCombo);
        userTypeCombo.addActionListener(e -> switchUserType());
        mainPanel.add(typePanel, BorderLayout.NORTH);

        // Common fields panel
        JPanel commonPanel = createFormPanel();
        GridBagConstraints gbc = createGBC();

        addFormField(commonPanel, "First Name:", firstNameField, gbc);
        addFormField(commonPanel, "Last Name:", lastNameField, gbc);
        addFormField(commonPanel, "Email:", emailField, gbc);

        // Student-specific fields
        JPanel studentPanel = createFormPanel();
        gbc = createGBC();

        addFormField(studentPanel, "Student Type:", studentTypeCombo, gbc);
        addFormField(studentPanel, "Gender:", genderCombo, gbc);
        addFormField(studentPanel, "Course:", courseCombo, gbc);

        // Staff-specific fields
        JPanel staffPanel = createFormPanel();
        gbc = createGBC();

        addFormField(staffPanel, "Weekly Hours:", weeklyHoursSpinner, gbc);
        addFormField(staffPanel, "Max Modules:", maxModulesSpinner, gbc);
        addFormField(staffPanel, "Department:", departmentCombo, gbc);

        // Add panels to card layout
        dynamicFieldsPanel.add(studentPanel, "STUDENT");
        dynamicFieldsPanel.add(staffPanel, "STAFF");

        // Combine panels
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(commonPanel, BorderLayout.NORTH);
        contentPanel.add(dynamicFieldsPanel, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add standard buttons
        addStandardButtons(this::saveUser);
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
            showError("Error loading courses: " + e.getMessage(), "Error");
            courseCombo.addItem("No courses available");
        }
    }

    private void switchUserType()
    {
        String selectedType = (String) userTypeCombo.getSelectedItem();
        cardLayout.show(dynamicFieldsPanel, selectedType.toUpperCase());
        pack();
    }

    private void saveUser()
    {
        try
        {
            if (!validateRequiredFields(firstNameField, lastNameField, emailField))
            {
                return;
            }

            if (userTypeCombo.getSelectedItem().equals("Student"))
            {
                saveStudent();
                mainScreen.refreshSpecificData(DataManager.DataType.STUDENTS);
            }
            else
            {
                saveStaff();
                mainScreen.refreshSpecificData(DataManager.DataType.STAFF);
            }

            showSuccess("User saved successfully!");
            dispose();
        }
        catch (Exception e)
        {
            showError("Error saving user: " + e.getMessage(), "Error");
        }
    }

    private void saveStudent() throws IOException
    {
        Object selectedCourse = courseCombo.getSelectedItem();
        if (selectedCourse == null)
        {
            throw new IOException("Please select a course. If no courses are available, check the courses file.");
        }

        if (studentTypeCombo.getSelectedItem() == null || genderCombo.getSelectedItem() == null)
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
            // Validate and save the student
            UserDataManager.validateUser(student);
            UserDataManager.addStudent(student);

            // Get course code and generate assignments
            String courseCode = Course.getCourseCodeFromTitle(student.getCourse());
            if (courseCode == null || courseCode.isEmpty())
            {
                throw new IOException("Could not find course code for the selected course");
            }

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
        staff.setFirstName(firstNameField.getText().trim());
        staff.setLastName(lastNameField.getText().trim());
        staff.setEmail(emailField.getText().trim());
        staff.setWeeklyHours((Integer) weeklyHoursSpinner.getValue());
        staff.setMaxModules((Integer) maxModulesSpinner.getValue());
        staff.setDepartmentId((DepartmentId) Objects.requireNonNull(departmentCombo.getSelectedItem()));
        staff.setGuid(UUID.randomUUID().toString());

        try
        {
            // Validate and save the staff member
            UserDataManager.validateUser(staff);
            UserDataManager.addStaff(staff);

            // Generate initial module assignments
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