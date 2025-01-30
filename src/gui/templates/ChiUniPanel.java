package gui.templates;

import javax.swing.*;
import java.awt.*;

public class ChiUniPanel extends JPanel
{
    private static final Color DEFAULT_BACKGROUND = new Color(245, 245, 245);
    private int padding = 10;

    public ChiUniPanel()
    {
        initialisePanel();
    }

    private void initialisePanel()
    {
        // Set default properties
        setBackground(DEFAULT_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
        setLayout(new BorderLayout());
    }

    // Setter for custom padding
    public void setPadding(int padding)
    {
        this.padding = padding;
        setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));
    }

    // Utility method to add components with specific constraints
    public void addComponent(Component component, String constraints)
    {
        add(component, constraints);
        revalidate();
        repaint();
    }
}
