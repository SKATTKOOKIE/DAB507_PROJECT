package file_handling.validation;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

public class AcademicYearFilter extends DocumentFilter
{
    private static final Pattern DIGIT_PATTERN = Pattern.compile("^\\d*$");
    private static final int MIN_YEAR = 18;
    private static final int MAX_LENGTH = 2;

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException
    {
        // If the string is null or empty, allow it
        if (string == null || string.isEmpty())
        {
            super.insertString(fb, offset, string, attr);
            return;
        }

        // Get the proposed new text
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String proposedText = currentText.substring(0, offset) + string + currentText.substring(offset);

        if (isValidInput(proposedText))
        {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException
    {
        // If replacing with null or empty string, allow it
        if (text == null || text.isEmpty())
        {
            super.replace(fb, offset, length, text, attrs);
            return;
        }

        // Get the proposed new text
        String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
        String beforeOffset = currentText.substring(0, offset);
        String afterOffset = currentText.substring(Math.min(offset + length, currentText.length()));
        String proposedText = beforeOffset + text + afterOffset;

        if (isValidInput(proposedText))
        {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException
    {
        // Always allow removal of text
        super.remove(fb, offset, length);
    }

    private boolean isValidInput(String proposedText)
    {
        // Allow empty text
        if (proposedText.isEmpty())
        {
            return true;
        }

        // Check if input contains only digits
        if (!DIGIT_PATTERN.matcher(proposedText).matches())
        {
            return false;
        }

        // Check length
        if (proposedText.length() > MAX_LENGTH)
        {
            return false;
        }

        // If we have a complete year, check the minimum value
        if (proposedText.length() == MAX_LENGTH)
        {
            try
            {
                int year = Integer.parseInt(proposedText);
                return year >= MIN_YEAR;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }

        return true;
    }
}