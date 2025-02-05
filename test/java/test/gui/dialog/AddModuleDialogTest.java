package gui.dialog;

import testframework.*;
import gui.components.dialogs.AddModuleDialog;
import gui.GuiMainScreen;
import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * Test class for the AddModuleDialog GUI component.
 * Tests the functionality of the module creation dialog including field validation,
 * module code generation, and overall dialog behavior.
 */
public class AddModuleDialogTest extends BaseTest
{
    /**
     * Test data for module creation
     */
    private static final String TEST_MODULE_NAME = "Test Module";
    private static final String TEST_YEAR = "25";

    /**
     * Dialog components under test
     */
    private AddModuleDialog dialog;
    private GuiMainScreen mainScreen;
    private JTextField nameField;
    private JTextField codeField;
    private JTextField acYearField;
    private JComboBox<DepartmentId> departmentCombo;

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
     * Creates the main screen and dialog, and initialises all fields.
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
                    dialog = new AddModuleDialog(new Frame(), mainScreen);
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
     * Initialises the dialog fields using reflection.
     * Provides access to private fields for testing purposes.
     *
     * @throws Exception if field access fails
     */
    private void initialiseFields() throws Exception
    {
        Class<AddModuleDialog> dialogClass = AddModuleDialog.class;

        Field nameFieldRef = dialogClass.getDeclaredField("nameField");
        Field codeFieldRef = dialogClass.getDeclaredField("codeField");
        Field yearFieldRef = dialogClass.getDeclaredField("acYearField");
        Field deptComboRef = dialogClass.getDeclaredField("departmentCombo");

        nameFieldRef.setAccessible(true);
        codeFieldRef.setAccessible(true);
        yearFieldRef.setAccessible(true);
        deptComboRef.setAccessible(true);

        nameField = (JTextField) nameFieldRef.get(dialog);
        codeField = (JTextField) codeFieldRef.get(dialog);
        acYearField = (JTextField) yearFieldRef.get(dialog);
        departmentCombo = (JComboBox<DepartmentId>) deptComboRef.get(dialog);
    }

    /**
     * Tests the academic year field validation.
     * Verifies that the field:
     * - Accepts valid years (18-99)
     * - Rejects invalid input (letters, symbols)
     * - Handles single digits correctly
     *
     * @throws Exception if the test fails
     */
    public void testAcademicYearValidation() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            acYearField.setText("");
            Assert.assertTrue(acYearField.getText().isEmpty(), "Field should start empty");

            acYearField.setText("ab");
            Assert.assertTrue(acYearField.getText().isEmpty(), "Field should reject letters");

            acYearField.setText("@#");
            Assert.assertTrue(acYearField.getText().isEmpty(), "Field should reject symbols");

            acYearField.setText("17");
            Assert.assertNotEquals("17", acYearField.getText(), "Field should reject years below 18");

            acYearField.setText("25");
            Assert.assertEquals("25", acYearField.getText(), "Field should accept valid year 25");

            acYearField.setText("99");
            Assert.assertEquals("99", acYearField.getText(), "Field should accept valid year 99");

            acYearField.setText("");
            acYearField.setText("2");
            Assert.assertEquals("2", acYearField.getText(), "Field should accept single digit");
        });
    }

    /**
     * Tests the module creation functionality.
     * Verifies that all fields can be set correctly and retain their values.
     *
     * @throws Exception if the test fails
     */
    public void testModuleCreation() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            nameField.setText(TEST_MODULE_NAME);
            acYearField.setText(TEST_YEAR);
            departmentCombo.setSelectedIndex(0);

            Assert.assertEquals(TEST_MODULE_NAME, nameField.getText(),
                    "Module name field should contain the test name");
            Assert.assertEquals(TEST_YEAR, acYearField.getText(),
                    "Academic year field should contain the test year");
            Assert.assertNotNull(departmentCombo.getSelectedItem(),
                    "Department combo box should have a selection");
        });
    }

    /**
     * Tests the automatic module code generation.
     * Verifies that the code field:
     * - Is automatically populated
     * - Contains expected formatting
     * - Is not editable
     *
     * @throws Exception if the test fails
     */
    public void testModuleCodeGeneration() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            Assert.assertFalse(codeField.getText().isEmpty(),
                    "Module code should be automatically generated");
            Assert.assertTrue(codeField.getText().contains("-"),
                    "Module code should contain a hyphen");
            Assert.assertFalse(codeField.isEditable(),
                    "Module code field should not be editable");
        });
    }

    /**
     * Main method to run the test suite.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        new AddModuleDialogTest().runTests();
    }
}