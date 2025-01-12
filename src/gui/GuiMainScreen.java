package gui;// GuiMainScreen.java

import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

import business.Department;
import business.Course;
import business.DepartmentId;
import business.Module;
import users.Staff;
import users.Student;

public class GuiMainScreen
{
    private ChiUniFrame mainFrame;
    private Department theatreDept;
    private Department childCareDept;
    private List<Course> allCourses;
    private List<Student> theatreStudents;
    private List<Staff> theatreStaff;
    private ChiUniTextArea outputArea;
    private DepartmentPanel departmentPanel;

    public GuiMainScreen()
    {
        initializeDepartments();
        initializeGUI();
    }

    private void initializeDepartments()
    {
        try
        {
            // Initialize Theatre Department
            theatreDept = new Department("Theatre", DepartmentId.THE);
            theatreStudents = Student.getByDepartment("Theatre");
            theatreStaff = Staff.getByDepartment("Theatre");
//            allCourses = Course.getAll();
            OutputManager.print("Theatre Department initialized successfully!");

            // Initialize Childcare Department
            childCareDept = new Department("Childhood", DepartmentId.CHI);
            OutputManager.print("Childcare Department initialized successfully!");
        }
        catch (IOException ex)
        {
            handleError("Error initializing departments", ex);
        }
    }

    private void initializeGUI()
    {
        mainFrame = new ChiUniFrame("University Management System");

        // Create main panel with grid layout
        ChiUniPanel mainPanel = new ChiUniPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Create and add banner panel
        ChiUniPanel bannerPanel = createBannerPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(bannerPanel, gbc);

        departmentPanel = new DepartmentPanel();
        mainPanel.add(departmentPanel);

        // Create left panel for buttons
        ChiUniPanel leftPanel = new ChiUniPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.weightx = 1.0;
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        leftGbc.insets = new Insets(0, 10, 5, 10);

        // Add department panel to left panel
        ChiUniPanel deptPanel = createDepartmentPanel();
        leftPanel.add(deptPanel, leftGbc);

        // Add courses panel to left panel
        leftGbc.gridy = 1;
        ChiUniPanel coursesPanel = createCoursesPanel();
        leftPanel.add(coursesPanel, leftGbc);

        // Add modules panel to left panel
        leftGbc.gridy = 2;
        ChiUniPanel modulesPanel = createModulesPanel();
        leftPanel.add(modulesPanel, leftGbc);

        // Add left panel to main panel
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(leftPanel, gbc);

        // Create and add output panel
        ChiUniPanel outputPanel = createOutputPanel();
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        mainPanel.add(outputPanel, gbc);

        // Initialize OutputManager with our text area
        OutputManager.initialize(outputArea);

        // Add main panel to frame
        mainFrame.addComponent(mainPanel, BorderLayout.CENTER);
        mainFrame.setMinimumSize(new Dimension(1000, 700));
    }


    private ChiUniPanel createOutputPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Output"));
        panel.setLayout(new BorderLayout(10, 10));

        // Create text area
        outputArea = new ChiUniTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        ChiUniPanel buttonPanel = new ChiUniPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        ChiUniButton clearButton = new ChiUniButton("Clear Output");
        clearButton.addActionListener(e -> OutputManager.clear());
        buttonPanel.add(clearButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private ChiUniPanel createDepartmentPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Department Information"));
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        ChiUniButton printTheaterInfoBtn = new ChiUniButton("View Theatre Department Details");
        printTheaterInfoBtn.addActionListener(e -> printTheatreDepartmentInfo());

        ChiUniButton printSummaryBtn = new ChiUniButton("View Department Summary");
        printSummaryBtn.addActionListener(e -> printDepartmentSummary());

        panel.add(printTheaterInfoBtn);
        panel.add(printSummaryBtn);

        return panel;
    }

    private ChiUniPanel createCoursesPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Course Management"));
        panel.setLayout(new GridLayout(0, 2, 10, 10));

        ChiUniButton displayCoursesBtn = new ChiUniButton("Display All Courses");
//        displayCoursesBtn.addActionListener(e -> displayAllCourses());

        ChiUniButton theaterCoursesBtn = new ChiUniButton("Show Theatre Courses");
//        theaterCoursesBtn.addActionListener(e -> showTheatreCourses());

        ChiUniButton childcareCoursesBtn = new ChiUniButton("Show Childcare Courses");
//        childcareCoursesBtn.addActionListener(e -> showChildcareCourses());

        panel.add(displayCoursesBtn);
        panel.add(theaterCoursesBtn);
        panel.add(childcareCoursesBtn);

        return panel;
    }

    private ChiUniPanel createModulesPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Module Management"));
        panel.setLayout(new GridLayout(1, 1, 10, 10));

        ChiUniButton displayModulesBtn = new ChiUniButton("Display All Modules");
        displayModulesBtn.addActionListener(e -> displayAllModules());

        panel.add(displayModulesBtn);

        return panel;
    }

    private void printTheatreDepartmentInfo()
    {
        if (theatreDept != null)
        {
            String info = theatreDept.getDetailedInfo(theatreStudents, theatreStaff);
            OutputManager.print(info);
        }
        else
        {
            JOptionPane.showMessageDialog(mainFrame, "Please initialise Theatre department first!");
        }
    }

    private void printDepartmentSummary()
    {
        if (theatreDept != null && theatreStudents != null && theatreStaff != null)
        {
            String summary = theatreDept.getSummary(theatreStudents.size(), theatreStaff.size());
            OutputManager.print(summary);
        }
        else
        {
            JOptionPane.showMessageDialog(mainFrame, "Please initialise Theatre department first!");
        }
    }

//    private void displayAllCourses()
//    {
//        try
//        {
//            String courseInfo = Course.getAllCoursesInfo();
//            OutputManager.print(courseInfo);
//        }
//        catch (IOException ex)
//        {
//            handleError("Error displaying courses", ex);
//        }
//    }

//    private void showTheatreCourses()
//    {
//        if (theatreDept != null && allCourses != null)
//        {
//            List<Course> theatreCourses = theatreDept.filterCoursesByDepartment(allCourses);
//            String courseInfo = theatreDept.getCourseInfo(theatreCourses);
//            OutputManager.print(courseInfo);
//        }
//        else
//        {
//            JOptionPane.showMessageDialog(mainFrame, "Please initialize Theatre department first!");
//        }
//    }

//    private void showChildcareCourses()
//    {
//        if (childCareDept != null && allCourses != null)
//        {
//            List<Course> childCareCourses = childCareDept.filterCoursesByDepartment(allCourses);
//            String courseInfo = childCareDept.getCourseInfo(childCareCourses);
//            OutputManager.print(courseInfo);
//        }
//        else
//        {
//            JOptionPane.showMessageDialog(mainFrame, "Please initialize Childcare department first!");
//        }
//    }

    private void displayAllModules()
    {
        try
        {
            String moduleInfo = Module.getAllModulesInfo();
            OutputManager.print(moduleInfo);
        }
        catch (IOException ex)
        {
            handleError("Error displaying modules", ex);
        }
    }

    private ChiUniPanel createBannerPanel()
    {
        ChiUniPanel panel = new ChiUniPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0, 48, 87)); // University blue color
        panel.setPreferredSize(new Dimension(mainFrame.getWidth(), 80));

        JLabel titleLabel = new JLabel("Chichester University Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private void handleError(String message, Exception ex)
    {
        String errorMessage = message + ": " + ex.getMessage();
        OutputManager.print("ERROR: " + errorMessage);
        JOptionPane.showMessageDialog(mainFrame,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void show()
    {
        mainFrame.setVisible(true);
    }
}
