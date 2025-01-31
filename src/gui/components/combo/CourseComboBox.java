package gui.components.combo;

import business.Course;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file_handling.FilePathHandler;
import file_handling.JsonProcessor;
import gui.templates.ChiUniComboBox;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Specialized combo box for selecting courses.
 * Handles loading course data from JSON and provides convenient access methods.
 */
public class CourseComboBox extends ChiUniComboBox<String>
{
    private static final int PREFERRED_WIDTH = 300; // Increased width for better visibility
    private List<Course> availableCourses;

    public CourseComboBox()
    {
        super();
        this.availableCourses = new ArrayList<>();
        setToolTipText("Select a course");

        // Set preferred size while maintaining the height from ChiUniComboBox
        Dimension currentSize = getPreferredSize();
        setPreferredSize(new Dimension(PREFERRED_WIDTH, currentSize.height));

        // Also set the popup menu width
        setMaximumRowCount(10);
        setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXX"); // This helps size the dropdown part

        loadCourses();
    }

    /**
     * Loads courses from the JSON file and populates the combo box.
     */
    public void loadCourses()
    {
        try
        {
            JsonProcessor processor = new JsonProcessor(FilePathHandler.COURSES_FILE.getNormalisedPath());
            processor.processFile();
            JsonObject jsonObject = (JsonObject) processor.getJsonContent();

            JsonArray coursesArray = jsonObject.getAsJsonArray("courses");
            if (coursesArray != null && coursesArray.size() > 0)
            {
                removeAllItems();
                for (int i = 0; i < coursesArray.size(); i++)
                {
                    JsonObject courseObj = coursesArray.get(i).getAsJsonObject();
                    String courseName = courseObj.get("name").getAsString();
                    if (courseName != null && !courseName.trim().isEmpty())
                    {
                        addItem(courseName);
                    }
                }

                if (getItemCount() > 0)
                {
                    setSelectedIndex(0);
                }
            }
            else
            {
                throw new IOException("No courses found in the courses file");
            }
        }
        catch (IOException e)
        {
            addItem("No courses available");
            setEnabled(false);
        }
    }

    /**
     * Gets the selected course name.
     *
     * @return The selected course name, or null if nothing is selected
     */
    public String getSelectedCourseName()
    {
        return (String) getSelectedItem();
    }

    /**
     * Sets the selected course by name.
     *
     * @param courseName The name of the course to select
     */
    public void setSelectedCourseName(String courseName)
    {
        setSelectedItem(courseName);
    }

    @Override
    public void addItem(String item)
    {
        super.addItem(item);
        // Ensure popup width is updated when new items are added
        revalidatePopupWidth();
    }

    private void revalidatePopupWidth()
    {
        // Force the popup to be at least as wide as the combo box
        if (getItemCount() > 0)
        {
            setPrototypeDisplayValue(getItemAt(0));
            for (int i = 1; i < getItemCount(); i++)
            {
                String item = getItemAt(i);
                if (getFontMetrics(getFont()).stringWidth(item) >
                        getFontMetrics(getFont()).stringWidth(getPrototypeDisplayValue()))
                {
                    setPrototypeDisplayValue(item);
                }
            }
        }
    }
}