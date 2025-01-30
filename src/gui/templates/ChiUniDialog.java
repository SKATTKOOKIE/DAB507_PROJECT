package gui.templates;

import gui.GuiMainScreen;

import javax.swing.*;
import java.awt.*;

public class ChiUniDialog extends JDialog
{
    private static final int DEFAULT_PADDING = 10;
    protected final GuiMainScreen mainScreen;
    protected final JPanel mainPanel;
    protected final JPanel buttonPanel;

    public ChiUniDialog(Frame owner, String title, GuiMainScreen mainScreen, boolean modal)
    {
        super(owner, title, modal);
        this.mainScreen = mainScreen;

        // Set dialog properties
        setLayout(new BorderLayout(DEFAULT_PADDING, DEFAULT_PADDING));

        // Initialize main panel with border layout and padding
        mainPanel = new JPanel(new BorderLayout(DEFAULT_PADDING, DEFAULT_PADDING));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING));

        // Initialize button panel with right-aligned flow layout
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Add panels to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Utility method to add form fields with consistent layout
     */
    protected <T extends JComponent> void addFormField(JPanel panel, String label, T component,
                                                       GridBagConstraints gbc)
    {
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
        gbc.gridy++;
    }

    /**
     * Creates and adds standard dialog buttons
     */
    protected void addStandardButtons(Runnable saveAction)
    {
        ChiUniButton saveButton = new ChiUniButton("Save");
        ChiUniButton cancelButton = new ChiUniButton("Cancel");

        saveButton.addActionListener(e -> saveAction.run());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
    }

    /**
     * Shows an error message dialog with custom message and title
     */
    protected void showError(String message, String title)
    {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a validation error message dialog
     */
    protected void showValidationError()
    {
        showError("All required fields must be filled.", "Validation Error");
    }

    /**
     * Shows a success message dialog
     */
    protected void showSuccess(String message)
    {
        JOptionPane.showMessageDialog(this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Centers the dialog on the owner window
     */
    protected void centerOnOwner()
    {
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Validates that required text fields are not empty
     */
    protected boolean validateRequiredFields(JTextField... fields)
    {
        for (JTextField field : fields)
        {
            if (field.getText().trim().isEmpty())
            {
                showValidationError();
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a form panel with GridBagLayout and standard constraints
     */
    protected JPanel createFormPanel()
    {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        return formPanel;
    }

    /**
     * Creates a field panel with label and component in FlowLayout
     */
    protected JPanel createFieldPanel(String label, JComponent component)
    {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.add(new JLabel(label));
        panel.add(component);
        return panel;
    }
}