package gui;

import javax.swing.*;
import java.awt.*;

/**
 * A reusable progress bar dialog component that can be used across the application
 * to show loading states and progress updates.
 */
public class ChiUniProgressBar extends JDialog
{
    private final JLabel messageLabel;
    private final JProgressBar progressBar;

    /**
     * Creates a new progress bar dialog.
     *
     * @param parent  The parent frame
     * @param title   The dialog title
     * @param message The initial message to display
     */
    public ChiUniProgressBar(Frame parent, String title, String message)
    {
        super(parent, title, false);
        setUndecorated(true);

        // Create main panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Message label
        messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(messageLabel, BorderLayout.NORTH);

        // Progress bar
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(progressBar, BorderLayout.CENTER);

        add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Updates the message shown in the progress bar dialog.
     *
     * @param message The new message to display
     */
    public void updateMessage(String message)
    {
        messageLabel.setText(message);
    }

    /**
     * Sets whether the progress bar is indeterminate.
     *
     * @param indeterminate true for an indeterminate progress bar
     */
    public void setIndeterminate(boolean indeterminate)
    {
        progressBar.setIndeterminate(indeterminate);
    }

    /**
     * Sets the current progress value (0-100).
     *
     * @param value The progress value
     */
    public void setProgress(int value)
    {
        progressBar.setValue(value);
    }

    /**
     * Shows the progress bar dialog.
     */
    public void showProgress()
    {
        setVisible(true);
    }

    /**
     * Hides the progress bar dialog.
     */
    public void hideProgress()
    {
        setVisible(false);
        dispose();
    }
}
