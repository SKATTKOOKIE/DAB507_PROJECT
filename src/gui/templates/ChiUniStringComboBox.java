package gui.templates;

/**
 * A specialized combo box for simple string selections.
 * Provides consistent styling for string-based dropdowns.
 */
public class ChiUniStringComboBox extends ChiUniComboBox<String>
{

    public ChiUniStringComboBox(String[] items)
    {
        super(items);
        setToolTipText("Select an option");
    }

    public ChiUniStringComboBox()
    {
        super();
        setToolTipText("Select an option");
    }

    /**
     * Gets the selected string, or null if nothing is selected.
     */
    public String getSelectedString()
    {
        return (String) getSelectedItem();
    }

    /**
     * Sets the selected string if it exists in the combo box.
     */
    public void setSelectedString(String value)
    {
        setSelectedItem(value);
    }
}