package gui.dialog;

import testframework.*;
import gui.components.dialogs.AddModuleDialog;
import gui.GuiMainScreen;
import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class AddModuleDialogTest extends BaseTest
{
    private static final String TEST_MODULE_NAME = "Test Module";
    private static final String TEST_YEAR = "25";
    private AddModuleDialog dialog;
    private GuiMainScreen mainScreen;
    private JTextField nameField;
    private JTextField codeField;
    private JTextField acYearField;
    private JComboBox<DepartmentId> departmentCombo;

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

    public static void main(String[] args)
    {
        new AddModuleDialogTest().runTests();
    }
}