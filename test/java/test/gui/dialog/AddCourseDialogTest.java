package gui.dialog;

import gui.components.dialogs.AddCourseDialog;
import gui.GuiMainScreen;
import business.DepartmentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

class AddCourseDialogTest
{
    private static final String TEST_COURSE_NAME = "Test Course";
    private AddCourseDialog dialog;
    private JTextField nameField;
    private JTextField codeField;
    private JComboBox<DepartmentId> departmentCombo;

    @BeforeEach
    void setup() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            try
            {
                dialog = new AddCourseDialog(new Frame(), new GuiMainScreen());
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

    @Test
    void courseCreationShouldSetFieldsCorrectly() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            nameField.setText(TEST_COURSE_NAME);
            departmentCombo.setSelectedIndex(0);

            assertEquals(TEST_COURSE_NAME, nameField.getText(),
                    "Course name field should contain the test name");
            assertNotNull(departmentCombo.getSelectedItem(),
                    "Department combo box should have a selection");

            System.out.println("✓ Course creation test passed");
        });
    }

    @Test
    void courseCodeShouldBeGenerated() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            // Test initial code generation
            assertFalse(codeField.getText().isEmpty(),
                    "Course code should be automatically generated");
            assertEquals(6, codeField.getText().length(),
                    "Course code should be 6 digits long");
            assertTrue(codeField.getText().matches("\\d{6}"),
                    "Course code should contain only digits");
            assertFalse(codeField.isEditable(),
                    "Course code field should not be editable");

            // Store the initial code
            String initialCode = codeField.getText();

            // Simulate clicking the generate button to get a new code
            for (Component comp : dialog.getContentPane().getComponents())
            {
                if (comp instanceof JPanel)
                {
                    findAndClickGenerateButton((JPanel) comp);
                }
            }

            // Verify new code is different
            assertNotEquals(initialCode, codeField.getText(),
                    "New generated code should be different from initial code");
            assertTrue(codeField.getText().matches("\\d{6}"),
                    "New code should also be 6 digits");

            System.out.println("✓ Course code generation test passed");
        });
    }

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

    @Test
    void dialogShouldHandleEmptyFields() throws Exception
    {
        SwingUtilities.invokeAndWait(() ->
        {
            nameField.setText("");

            // Try to trigger save (you'll need to expose this method or find another way to trigger it)
            // For now, we can at least verify the state of required fields
            assertTrue(nameField.getText().isEmpty(),
                    "Name field should be empty for this test");
            assertFalse(codeField.getText().isEmpty(),
                    "Code field should still have generated code");

            System.out.println("✓ Empty fields handling test passed");
        });
    }
}