package gui;

import javax.swing.*;
import java.awt.*;

public class ChiUniButton extends JButton
{
    private static final Color DEFAULT_BACKGROUND = new Color(70, 130, 180);
    private static final Color HOVER_BACKGROUND = new Color(100, 149, 237);
    private static final Color DEFAULT_TEXT_COLOR = Color.WHITE;

    public ChiUniButton(String text)
    {
        super(text);
        initialiseButton();
    }

    private void initialiseButton()
    {
        // Set default styling
        setBackground(DEFAULT_BACKGROUND);
        setForeground(DEFAULT_TEXT_COLOR);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);

        // Add hover effect
        addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                setBackground(HOVER_BACKGROUND);
            }

            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                setBackground(DEFAULT_BACKGROUND);
            }
        });
    }
}
