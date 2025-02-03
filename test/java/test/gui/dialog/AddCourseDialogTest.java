package gui.dialog;

import testframework.*;
import gui.components.dialogs.AddCourseDialog;
import gui.GuiMainScreen;
import business.DepartmentId;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class AddCourseDialogTest extends BaseTest
{
    private static final String TEST_COURSE_NAME = "Test Course";
    private AddCourseDialog dialog;
    private JTextField nameField;
    private JTextField codeField;
    private JComboBox<DepartmentId> departmentCombo;
    private GuiMainScreen mainScreen;

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

    public static void main(String[] args)
    {
        new AddCourseDialogTest().runTests();
    }
}