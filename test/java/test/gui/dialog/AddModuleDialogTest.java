package gui.dialog;

import gui.components.dialogs.AddModuleDialog;
import gui.GuiMainScreen;
import business.DepartmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

class AddModuleDialogTest
{
    private static final String TEST_MODULE_NAME = "Test Module";
    private static final String TEST_YEAR = "25";
    private AddModuleDialog dialog;
    private JTextField nameField;
    private JTextField codeField;
    private JTextField acYearField;
    private JComboBox<DepartmentId> departmentCombo;

    @BeforeEach
    void setup() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            try
            {
                dialog = new AddModuleDialog(new Frame(), new GuiMainScreen());
                initialiseFields();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                fail("Failed to create dialog: " + e.getMessage());
            }
        });
    }

    private void initialiseFields() throws Exception
    {
        // Access private fields using reflection
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

    @Test
    void academicYearShouldRejectInvalidInputs() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            // Clear the field first
            acYearField.setText("");
            assertEquals("", acYearField.getText(), "Field should start empty");

            // Test non-numeric input
            acYearField.setText("ab");
            assertEquals("", acYearField.getText(), "Field should reject letters");

            // Test symbols
            acYearField.setText("@#");
            assertEquals("", acYearField.getText(), "Field should reject symbols");

            // Test year below minimum
            acYearField.setText("17");
            assertNotEquals("17", acYearField.getText(), "Field should reject years below 18");

            // Test valid years
            acYearField.setText("25");
            assertEquals("25", acYearField.getText(), "Field should accept valid year 25");

            acYearField.setText("99");
            assertEquals("99", acYearField.getText(), "Field should accept valid year 99");

            // Test partial input
            acYearField.setText("");
            acYearField.setText("2");
            assertEquals("2", acYearField.getText(), "Field should accept single digit");

            System.out.println("✓ Academic year validation test passed");
        });
    }

    @Test
    void moduleCreationShouldSetFieldsCorrectly() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            nameField.setText(TEST_MODULE_NAME);
            acYearField.setText(TEST_YEAR);
            departmentCombo.setSelectedIndex(0);

            assertEquals(TEST_MODULE_NAME, nameField.getText(),
                    "Module name field should contain the test name");
            assertEquals(TEST_YEAR, acYearField.getText(),
                    "Academic year field should contain the test year");
            assertNotNull(departmentCombo.getSelectedItem(),
                    "Department combo box should have a selection");

            System.out.println("✓ Module creation test passed");
        });
    }

    @Test
    void moduleCodeShouldBeGenerated() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            assertFalse(codeField.getText().isEmpty(),
                    "Module code should be automatically generated");
            assertTrue(codeField.getText().contains("-"),
                    "Module code should contain a hyphen");
            assertFalse(codeField.isEditable(),
                    "Module code field should not be editable");

            System.out.println("✓ Module code generation test passed");
        });
    }
}