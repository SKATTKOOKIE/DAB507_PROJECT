package gui.dialog;

import testframework.*;
import gui.components.dialogs.AddCourseDialog;
import gui.GuiMainScreen;
import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Test class for the AddCourseDialog GUI component.
 * Tests the functionality of the course creation dialog including:
 * - Course information input
 * - Automatic course code generation
 * - Field validation and handling
 */
public class AddCourseDialogTest extends BaseTest
{
    /**
     * Test data for course creation
     */
    private static final String TEST_COURSE_NAME = "Test Course";

    /**
     * Dialog components under test
     */
    private AddCourseDialog dialog;
    private JTextField nameField;
    private JTextField codeField;
    private JComboBox<DepartmentId> departmentCombo;
    private GuiMainScreen mainScreen;

    /**
     * Cleans up resources after each test.
     * Disposes of the dialog and main screen on the EDT.
     */
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

    /**
     * Sets up the test environment before each test.
     * Creates the main screen and dialog, and initializes all fields.
     */
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
                    dialog = new AddCourseDialog(new Frame(), mainScreen);
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

    /**
     * Initializes the dialog fields using reflection.
     * Provides access to private fields for testing purposes.
     *
     * @throws Exception if field access fails
     */
    private void initialiseFields() throws Exception
    {
        Class<AddCourseDialog> dialogClass = AddCourseDialog.class;

        Field nameFieldRef = dialogClass.getDeclaredField("nameField");
        Field codeFieldRef = dialogClass.getDeclaredField("codeField");
        Field deptComboRef = dialogClass.getDeclaredField("departmentCombo");

        nameFieldRef.setAccessible(true);
        codeFieldRef.setAccessible(true);
        deptComboRef.setAccessible(true);

        nameField = (JTextField) nameFieldRef.get(dialog);
        codeField = (JTextField) codeFieldRef.get(dialog);
        departmentCombo = (JComboBox<DepartmentId>) deptComboRef.get(dialog);
    }

    /**
     * Tests the basic course creation functionality.
     * Verifies that the name field and department combo box work correctly.
     *
     * @throws Exception if the test fails on the EDT
     */
    public void testCourseCreation() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            nameField.setText(TEST_COURSE_NAME);
            departmentCombo.setSelectedIndex(0);

            Assert.assertEquals(TEST_COURSE_NAME, nameField.getText(),
                    "Course name field should contain the test name");
            Assert.assertNotNull(departmentCombo.getSelectedItem(),
                    "Department combo box should have a selection");
        });
    }

    /**
     * Tests the automatic course code generation functionality.
     * Verifies that:
     * - Code is automatically generated
     * - Code follows the required format (6 digits)
     * - Code field is not editable
     * - Generate New Code button creates a different valid code
     *
     * @throws Exception if the test fails on the EDT
     */
    public void testCourseCodeGeneration() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            Assert.assertFalse(codeField.getText().isEmpty(),
                    "Course code should be automatically generated");
            Assert.assertEquals(6, codeField.getText().length(),
                    "Course code should be 6 digits long");
            Assert.assertTrue(codeField.getText().matches("\\d{6}"),
                    "Course code should contain only digits");
            Assert.assertFalse(codeField.isEditable(),
                    "Course code field should not be editable");

            String initialCode = codeField.getText();

            for (Component comp : dialog.getContentPane().getComponents())
            {
                if (comp instanceof JPanel)
                {
                    findAndClickGenerateButton((JPanel) comp);
                }
            }

            Assert.assertNotEquals(initialCode, codeField.getText(),
                    "New generated code should be different from initial code");
            Assert.assertTrue(codeField.getText().matches("\\d{6}"),
                    "New code should also be 6 digits");
        });
    }

    /**
     * Helper method to find and click the Generate New Code button.
     * Recursively searches through the component hierarchy.
     *
     * @param panel The panel to search for the button
     */
    private void findAndClickGenerateButton(JPanel panel)
    {
        for (Component comp : panel.getComponents())
        {
            if (comp instanceof JButton && ((JButton) comp).getText().equals("Generate New Code"))
            {
                ((JButton) comp).doClick();
                return;
            }
            else if (comp instanceof JPanel)
            {
                findAndClickGenerateButton((JPanel) comp);
            }
        }
    }

    /**
     * Tests handling of empty fields in the dialog.
     * Verifies that:
     * - Empty name field is handled correctly
     * - Code field remains populated even with empty name
     *
     * @throws Exception if the test fails on the EDT
     */
    public void testEmptyFieldsHandling() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            nameField.setText("");

            Assert.assertTrue(nameField.getText().isEmpty(),
                    "Name field should be empty for this test");
            Assert.assertFalse(codeField.getText().isEmpty(),
                    "Code field should still have generated code");
        });
    }

    /**
     * Main method to run the test suite.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        new AddCourseDialogTest().runTests();
    }
}