package gui;

import javax.swing.*;
import java.awt.*;

public class ChiUniTextArea extends JTextArea
{
    public ChiUniTextArea()
    {
        initializeTextArea();
    }

    private void initializeTextArea()
    {
        // Set default properties
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        setBackground(new Color(250, 250, 250));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    public void appendWithTimestamp(String text)
    {
        String timestamp = String.format("[%tT] ", new java.util.Date());
        append(timestamp + text + "\n");
        setCaretPosition(getDocument().getLength());
    }

    public void clear()
    {
        setText("");
    }
}
