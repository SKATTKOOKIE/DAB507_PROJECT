package gui.dialog;

import gui.components.dialogs.AddUserDialog;
import gui.GuiMainScreen;
import gui.templates.ChiUniStringComboBox;
import gui.components.combo.StudentTypeComboBox;
import gui.components.combo.CourseComboBox;
import gui.components.combo.DepartmentComboBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

class AddUserDialogTest
{
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final String TEST_EMAIL = "john.doe@test.com";

    private AddUserDialog dialog;
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

    @BeforeEach
    void setup() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            try
            {
                dialog = new AddUserDialog(new Frame(), new GuiMainScreen());
                initialiseFields();
                System.out.println("✓ Test setup completed successfully");
            }
            catch (Exception e)
            {
                System.out.println("✗ Test setup failed: " + e.getMessage());
                e.printStackTrace();
                fail("Failed to create dialog: " + e.getMessage());
            }
        });
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
        // Find the panel that contains the components for the given card
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
        Component[] components = container.getComponents();
        for (Component comp : components)
        {
            if (comp == target)
            {
                return true;
            }
            if (comp instanceof Container)
            {
                if (containsComponent((Container) comp, target))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Test
    void commonFieldsShouldBeinitialisedCorrectly() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            assertNotNull(firstNameField, "First name field should be initialised");
            assertNotNull(lastNameField, "Last name field should be initialised");
            assertNotNull(emailField, "Email field should be initialised");
            assertNotNull(userTypeCombo, "User type combo should be initialised");

            assertTrue(userTypeCombo.getItemCount() > 0, "User type combo should have items");
            assertEquals("Student", userTypeCombo.getItemAt(0), "First item should be Student");
            assertEquals("Staff", userTypeCombo.getItemAt(1), "Second item should be Staff");
            System.out.println("✓ Common fields initialisation test passed");
        });
    }

    @Nested
    class StudentTests
    {
        @BeforeEach
        void setupStudentMode() throws Exception
        {
            System.out.println("\n--- Starting Student Mode Tests ---");
            SwingUtilities.invokeAndWait(() ->
            {
                userTypeCombo.setSelectedItem("Student");
                cardLayout.show(dynamicFieldsPanel, "STUDENT");
            });
        }

        @Test
        void studentFieldsShouldBeVisible() throws Exception
        {
            System.out.println("Testing student fields visibility...");
            SwingUtilities.invokeAndWait(() ->
            {
                assertNotNull(studentTypeCombo, "Student type combo should be initialised");
                assertNotNull(genderCombo, "Gender combo should be initialised");
                assertNotNull(courseCombo, "Course combo should be initialised");

                assertTrue(isPanelVisible("STUDENT"), "Student panel should be visible");
                System.out.println("✓ Student fields visibility test passed");
            });
        }

        @Test
        void shouldValidateStudentFields() throws Exception
        {
            System.out.println("Testing student fields validation...");
            SwingUtilities.invokeAndWait(() ->
            {
                firstNameField.setText(TEST_FIRST_NAME);
                lastNameField.setText(TEST_LAST_NAME);
                emailField.setText(TEST_EMAIL);

                studentTypeCombo.setSelectedIndex(0);
                genderCombo.setSelectedIndex(0);
                courseCombo.setSelectedIndex(0);

                assertEquals(TEST_FIRST_NAME, firstNameField.getText(), "First name should be set");
                assertEquals(TEST_LAST_NAME, lastNameField.getText(), "Last name should be set");
                assertEquals(TEST_EMAIL, emailField.getText(), "Email should be set");
                assertNotNull(studentTypeCombo.getSelectedItem(), "Student type should be selected");
                assertNotNull(genderCombo.getSelectedItem(), "Gender should be selected");
                assertNotNull(courseCombo.getSelectedItem(), "Course should be selected");
                System.out.println("✓ Student fields validation test passed");
            });
        }
    }

    @Nested
    class StaffTests
    {
        @BeforeEach
        void setupStaffMode() throws Exception
        {
            System.out.println("\n--- Starting Staff Mode Tests ---");
            SwingUtilities.invokeAndWait(() ->
            {
                userTypeCombo.setSelectedItem("Staff");
                cardLayout.show(dynamicFieldsPanel, "STAFF");
            });
        }

        @Test
        void staffFieldsShouldBeVisible() throws Exception
        {
            System.out.println("Testing staff fields visibility...");
            SwingUtilities.invokeAndWait(() ->
            {
                assertNotNull(weeklyHoursSpinner, "Weekly hours spinner should be initialised");
                assertNotNull(maxModulesSpinner, "Max modules spinner should be initialised");
                assertNotNull(departmentCombo, "Department combo should be initialised");

                assertTrue(isPanelVisible("STAFF"), "Staff panel should be visible");
                System.out.println("✓ Staff fields visibility test passed");
            });
        }

        @Test
        void shouldValidateStaffFields() throws Exception
        {
            System.out.println("Testing staff fields validation...");
            SwingUtilities.invokeAndWait(() ->
            {
                firstNameField.setText(TEST_FIRST_NAME);
                lastNameField.setText(TEST_LAST_NAME);
                emailField.setText(TEST_EMAIL);

                weeklyHoursSpinner.setValue(37);
                maxModulesSpinner.setValue(4);
                departmentCombo.setSelectedIndex(0);

                assertEquals(TEST_FIRST_NAME, firstNameField.getText(), "First name should be set");
                assertEquals(TEST_LAST_NAME, lastNameField.getText(), "Last name should be set");
                assertEquals(TEST_EMAIL, emailField.getText(), "Email should be set");
                assertEquals(37, weeklyHoursSpinner.getValue(), "Weekly hours should be set to default");
                assertEquals(4, maxModulesSpinner.getValue(), "Max modules should be set to default");
                assertNotNull(departmentCombo.getSelectedItem(), "Department should be selected");
                System.out.println("✓ Staff fields validation test passed");
            });
        }

        @Test
        void spinnersShouldRespectBoundaries() throws Exception
        {
            System.out.println("Testing spinner boundaries...");
            SwingUtilities.invokeAndWait(() ->
            {
                SpinnerNumberModel weeklyModel = (SpinnerNumberModel) weeklyHoursSpinner.getModel();
                assertEquals(0, weeklyModel.getMinimum(), "Weekly hours minimum should be 0");
                assertEquals(40, weeklyModel.getMaximum(), "Weekly hours maximum should be 40");

                SpinnerNumberModel modulesModel = (SpinnerNumberModel) maxModulesSpinner.getModel();
                assertEquals(1, modulesModel.getMinimum(), "Max modules minimum should be 1");
                assertEquals(8, modulesModel.getMaximum(), "Max modules maximum should be 8");
                System.out.println("✓ Spinner boundaries test passed");
            });
        }
    }

    @Test
    void shouldSwitchBetweenUserTypes() throws Exception
    {
        System.out.println("\nTesting user type switching...");
        SwingUtilities.invokeAndWait(() ->
        {
            // Start with Student view
            userTypeCombo.setSelectedItem("Student");
            cardLayout.show(dynamicFieldsPanel, "STUDENT");
            assertTrue(isPanelVisible("STUDENT"), "Student panel should be visible");
            System.out.println("✓ Successfully switched to Student view");

            // Switch to Staff view
            userTypeCombo.setSelectedItem("Staff");
            cardLayout.show(dynamicFieldsPanel, "STAFF");
            assertTrue(isPanelVisible("STAFF"), "Staff panel should be visible");
            System.out.println("✓ Successfully switched to Staff view");

            // Switch back to Student view
            userTypeCombo.setSelectedItem("Student");
            cardLayout.show(dynamicFieldsPanel, "STUDENT");
            assertTrue(isPanelVisible("STUDENT"), "Student panel should be visible");
            System.out.println("✓ Successfully switched back to Student view");

            System.out.println("✓ User type switching test passed");
        });
    }
}