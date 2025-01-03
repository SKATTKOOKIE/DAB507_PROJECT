package gui;

import javax.swing.*;
import java.awt.*;

public class ChiUniFrame extends JFrame
{
    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;

    public ChiUniFrame(String title)
    {
        super(title);
        initializeFrame();
    }

    private void initializeFrame()
    {
        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set default size
        setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        // Center the frame on screen
        setLocationRelativeTo(null);

        // Set layout manager
        setLayout(new BorderLayout());

        // Add custom styling
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Pack and make visible
        pack();
    }

    // Utility method to add components with specific constraints
    public void addComponent(Component component, String constraints)
    {
        add(component, constraints);
        revalidate();
        repaint();
    }
}
