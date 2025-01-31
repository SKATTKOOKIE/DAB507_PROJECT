package gui.components.combo;

import gui.templates.ChiUniComboBox;
import users.StudentType;

/**
 * Specialized combo box for selecting student types.
 */
public class StudentTypeComboBox extends ChiUniComboBox<StudentType>
{

    public StudentTypeComboBox()
    {
        super(StudentType.values());
        initialise();
    }

    private void initialise()
    {
        setItemRenderer(StudentType::toString);
        setToolTipText("Select student type");
    }

    /**
     * Gets the selected student type.
     *
     * @return The selected StudentType, or null if nothing is selected
     */
    public StudentType getSelectedStudentType()
    {
        return (StudentType) getSelectedItem();
    }

    /**
     * Sets the selected student type.
     *
     * @param type The student type to select
     */
    public void setSelectedStudentType(StudentType type)
    {
        setSelectedItem(type);
    }
}