package gui.templates;

import javax.swing.*;
import java.awt.*;

/**
 * Base class for combo boxes in the University Management System.
 * Provides consistent styling and behavior across the application.
 *
 * @param <T> The type of elements in this combo box
 */
public class ChiUniComboBox<T> extends JComboBox<T>
{
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final int DEFAULT_HEIGHT = 25;

    public ChiUniComboBox()
    {
        initialiseComboBox();
    }

    public ChiUniComboBox(T[] items)
    {
        super(items);
        initialiseComboBox();
    }

    public ChiUniComboBox(ComboBoxModel<T> model)
    {
        super(model);
        initialiseComboBox();
    }

    private void initialiseComboBox()
    {
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        setPreferredSize(new Dimension(getPreferredSize().width, DEFAULT_HEIGHT));
        setMaximumRowCount(10);
    }

    /**
     * Sets a custom renderer for the combo box items.
     *
     * @param renderFunction Function to convert item to display string
     */
    protected void setItemRenderer(ItemDisplayFunction<T> renderFunction)
    {
        setRenderer(new DefaultListCellRenderer()
        {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus)
            {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null)
                {
                    @SuppressWarnings("unchecked")
                    T item = (T) value;
                    setText(renderFunction.getDisplayText(item));
                }
                return this;
            }
        });
    }

    /**
     * Functional interface for converting combo box items to display text.
     *
     * @param <T> The type of item to be displayed
     */
    @FunctionalInterface
    protected interface ItemDisplayFunction<T>
    {
        String getDisplayText(T item);
    }
}