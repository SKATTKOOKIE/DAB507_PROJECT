package gui.components.combo;

import business.DepartmentId;
import gui.templates.ChiUniComboBox;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Specialized combo box for selecting departments.
 * Provides consistent department selection behavior across the application.
 * Engineering departments are placed at the bottom of the selection list.
 */
public class DepartmentComboBox extends ChiUniComboBox<DepartmentId>
{
    private static final int PREFERRED_WIDTH = 300; // Consistent width with other combo boxes

    /**
     * Creates a new department combo box with all departments except UNKNOWN.
     */
    public DepartmentComboBox()
    {
        super(getFilteredDepartments());
        initialise();
    }

    /**
     * Creates a new department combo box with specific departments.
     *
     * @param departments List of departments to include
     */
    public DepartmentComboBox(List<DepartmentId> departments)
    {
        super(sortDepartments(departments).toArray(new DepartmentId[0]));
        initialise();
    }

    private void initialise()
    {
        setItemRenderer(DepartmentId::getDepartmentName);

        // Set tooltip to show both name and code
        setToolTipText("Select a department");
        addItemListener(e -> updateTooltip());

        // Set preferred size while maintaining the height
        Dimension currentSize = getPreferredSize();
        setPreferredSize(new Dimension(PREFERRED_WIDTH, currentSize.height));

        // Set the popup menu width
        setMaximumRowCount(10);

        // Ensure the combo box is wide enough for the longest department name
        revalidatePopupWidth();
    }

    private void updateTooltip()
    {
        DepartmentId selected = (DepartmentId) getSelectedItem();
        if (selected != null)
        {
            setToolTipText(String.format("%s (%s)",
                    selected.getDepartmentName(),
                    selected.name()));
        }
    }

    private static DepartmentId[] getFilteredDepartments()
    {
        return sortDepartments(
                Arrays.stream(DepartmentId.values())
                        .filter(dept -> dept != DepartmentId.UNKNOWN)
                        .collect(Collectors.toList())
        ).toArray(new DepartmentId[0]);
    }

    /**
     * Sorts departments with engineering departments at the bottom.
     * Non-engineering departments are sorted alphabetically.
     */
    private static List<DepartmentId> sortDepartments(List<DepartmentId> departments)
    {
        return departments.stream()
                .sorted(Comparator
                        .<DepartmentId, Boolean>comparing(dept ->
                                dept.getDepartmentName().toLowerCase().contains("engineering"))
                        .thenComparing(DepartmentId::getDepartmentName))
                .collect(Collectors.toList());
    }

    @Override
    public void addItem(DepartmentId item)
    {
        super.addItem(item);
        revalidatePopupWidth();
    }

    private void revalidatePopupWidth()
    {
        // Find the longest department name and use its DepartmentId as prototype
        if (getItemCount() > 0)
        {
            DepartmentId longestDept = null;
            int maxWidth = 0;

            for (int i = 0; i < getItemCount(); i++)
            {
                DepartmentId dept = getItemAt(i);
                String displayText = dept.getDepartmentName();
                int width = getFontMetrics(getFont()).stringWidth(displayText);

                if (width > maxWidth)
                {
                    maxWidth = width;
                    longestDept = dept;
                }
            }

            if (longestDept != null)
            {
                setPrototypeDisplayValue(longestDept);
            }
        }
    }

    /**
     * Gets the currently selected department.
     *
     * @return The selected DepartmentId, or null if nothing is selected
     */
    public DepartmentId getSelectedDepartment()
    {
        return (DepartmentId) getSelectedItem();
    }

    /**
     * Sets the selected department.
     *
     * @param department The department to select
     */
    public void setSelectedDepartment(DepartmentId department)
    {
        setSelectedItem(department);
    }
}