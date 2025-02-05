package gui.dialog;

import testframework.*;
import gui.components.dialogs.AddUserDialog;
import gui.GuiMainScreen;
import gui.templates.ChiUniStringComboBox;
import gui.components.combo.StudentTypeComboBox;
import gui.components.combo.CourseComboBox;
import gui.components.combo.DepartmentComboBox;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class AddUserDialogTest extends BaseTest
{
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@test.com";

    private AddUserDialog dialog;
    private GuiMainScreen mainScreen;
    private JPanel dynamicFieldsPanel;
    private CardLayout cardLayout;

    // Common fields
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private ChiUniStringComboBox userTypeCombo;

    // Student fields
    private StudentTypeComboBox studentTypeCombo;
    private ChiUniStringComboBox genderCombo;
    private CourseComboBox courseCombo;

    // Staff fields
    private JSpinner weeklyHoursSpinner;
    private JSpinner maxModulesSpinner;
    private DepartmentComboBox departmentCombo;

    @Override
    protected void cleanup()
    {
        super.cleanup();
        if (dialog != null)
        {
            SwingUtilities.invokeLater(() ->
            {
                dialog.dispose();
            });
        }
        if (mainScreen != null)
        {
            SwingUtilities.invokeLater(() ->
            {
                mainScreen.dispose();
            });
        }
    }

    @Override
    protected void setup()
    {
        super.setup();
        try
        {
            SwingUtilities.invokeAndWait(() ->
            {
                try
                {
                    mainScreen = new GuiMainScreen();
                    dialog = new AddUserDialog(new Frame(), mainScreen);
                    initialiseFields();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Assert.assertTrue(false, "Failed to create dialog: " + e.getMessage());
                }
            });
        }
        catch (Exception e)
        {
            Assert.assertTrue(false, "Setup failed: " + e.getMessage());
        }
    }

    private void initialiseFields() throws Exception
    {
        Class<AddUserDialog> dialogClass = AddUserDialog.class;

        // Common fields
        firstNameField = getField(dialogClass, "firstNameField");
        lastNameField = getField(dialogClass, "lastNameField");
        emailField = getField(dialogClass, "emailField");
        userTypeCombo = getField(dialogClass, "userTypeCombo");
        dynamicFieldsPanel = getField(dialogClass, "dynamicFieldsPanel");
        cardLayout = (CardLayout) dynamicFieldsPanel.getLayout();

        // Student fields
        studentTypeCombo = getField(dialogClass, "studentTypeCombo");
        genderCombo = getField(dialogClass, "genderCombo");
        courseCombo = getField(dialogClass, "courseCombo");

        // Staff fields
        weeklyHoursSpinner = getField(dialogClass, "weeklyHoursSpinner");
        maxModulesSpinner = getField(dialogClass, "maxModulesSpinner");
        departmentCombo = getField(dialogClass, "departmentCombo");
    }

    private <T> T getField(Class<?> clazz, String fieldName) throws Exception
    {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(dialog);
    }

    private boolean isPanelVisible(String cardName)
    {
        for (Component comp : dynamicFieldsPanel.getComponents())
        {
            if (comp instanceof JPanel)
            {
                JPanel panel = (JPanel) comp;
                if (cardName.equals("STUDENT") && containsComponent(panel, studentTypeCombo))
                {
                    return true;
                }
                else if (cardName.equals("STAFF") && containsComponent(panel, weeklyHoursSpinner))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsComponent(Container container, Component target)
    {
        for (Component comp : container.getComponents())
        {
            if (comp == target)
            {
                return true;
            }
            if (comp instanceof Container && containsComponent((Container) comp, target))
            {
                return true;
            }
        }
        return false;
    }

    public void testCommonFields() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            Assert.assertNotNull(firstNameField, "First name field should be initialised");
            Assert.assertNotNull(lastNameField, "Last name field should be initialised");
            Assert.assertNotNull(emailField, "Email field should be initialised");
            Assert.assertNotNull(userTypeCombo, "User type combo should be initialised");
            Assert.assertTrue(userTypeCombo.getItemCount() > 0, "User type combo should have items");
            Assert.assertEquals("Student", userTypeCombo.getItemAt(0), "First item should be Student");
            Assert.assertEquals("Staff", userTypeCombo.getItemAt(1), "Second item should be Staff");
        });
    }

    public void testStudentMode() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            userTypeCombo.setSelectedItem("Student");
            cardLayout.show(dynamicFieldsPanel, "STUDENT");

            Assert.assertNotNull(studentTypeCombo, "Student type combo should be initialised");
            Assert.assertNotNull(genderCombo, "Gender combo should be initialised");
            Assert.assertNotNull(courseCombo, "Course combo should be initialised");
            Assert.assertTrue(isPanelVisible("STUDENT"), "Student panel should be visible");

            firstNameField.setText(TEST_FIRST_NAME);
            lastNameField.setText(TEST_LAST_NAME);
            emailField.setText(TEST_EMAIL);
            studentTypeCombo.setSelectedIndex(0);
            genderCombo.setSelectedIndex(0);
            courseCombo.setSelectedIndex(0);

            Assert.assertEquals(TEST_FIRST_NAME, firstNameField.getText(), "First name should be set");
            Assert.assertEquals(TEST_LAST_NAME, lastNameField.getText(), "Last name should be set");
            Assert.assertEquals(TEST_EMAIL, emailField.getText(), "Email should be set");
            Assert.assertNotNull(studentTypeCombo.getSelectedItem(), "Student type should be selected");
            Assert.assertNotNull(genderCombo.getSelectedItem(), "Gender should be selected");
            Assert.assertNotNull(courseCombo.getSelectedItem(), "Course should be selected");
        });
    }

    public void testStaffMode() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            userTypeCombo.setSelectedItem("Staff");
            cardLayout.show(dynamicFieldsPanel, "STAFF");

            Assert.assertNotNull(weeklyHoursSpinner, "Weekly hours spinner should be initialised");
            Assert.assertNotNull(maxModulesSpinner, "Max modules spinner should be initialised");
            Assert.assertNotNull(departmentCombo, "Department combo should be initialised");
            Assert.assertTrue(isPanelVisible("STAFF"), "Staff panel should be visible");

            firstNameField.setText(TEST_FIRST_NAME);
            lastNameField.setText(TEST_LAST_NAME);
            emailField.setText(TEST_EMAIL);
            weeklyHoursSpinner.setValue(37);
            maxModulesSpinner.setValue(4);
            departmentCombo.setSelectedIndex(0);

            Assert.assertEquals(TEST_FIRST_NAME, firstNameField.getText(), "First name should be set");
            Assert.assertEquals(TEST_LAST_NAME, lastNameField.getText(), "Last name should be set");
            Assert.assertEquals(TEST_EMAIL, emailField.getText(), "Email should be set");
            Assert.assertEquals(37, weeklyHoursSpinner.getValue(), "Weekly hours should be set to default");
            Assert.assertEquals(4, maxModulesSpinner.getValue(), "Max modules should be set to default");
            Assert.assertNotNull(departmentCombo.getSelectedItem(), "Department should be selected");

            SpinnerNumberModel weeklyModel = (SpinnerNumberModel) weeklyHoursSpinner.getModel();
            SpinnerNumberModel modulesModel = (SpinnerNumberModel) maxModulesSpinner.getModel();
            Assert.assertEquals(0, weeklyModel.getMinimum(), "Weekly hours minimum should be 0");
            Assert.assertEquals(40, weeklyModel.getMaximum(), "Weekly hours maximum should be 40");
            Assert.assertEquals(1, modulesModel.getMinimum(), "Max modules minimum should be 1");
            Assert.assertEquals(8, modulesModel.getMaximum(), "Max modules maximum should be 8");
        });
    }

    public void testUserTypeSwitching() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            userTypeCombo.setSelectedItem("Student");
            cardLayout.show(dynamicFieldsPanel, "STUDENT");
            Assert.assertTrue(isPanelVisible("STUDENT"), "Student panel should be visible");

            userTypeCombo.setSelectedItem("Staff");
            cardLayout.show(dynamicFieldsPanel, "STAFF");
            Assert.assertTrue(isPanelVisible("STAFF"), "Staff panel should be visible");

            userTypeCombo.setSelectedItem("Student");
            cardLayout.show(dynamicFieldsPanel, "STUDENT");
            Assert.assertTrue(isPanelVisible("STUDENT"), "Student panel should be visible again");
        });
    }

    public static void main(String[] args)
    {
        new AddUserDialogTest().runTests();
    }
}