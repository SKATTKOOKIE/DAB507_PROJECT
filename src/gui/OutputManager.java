package gui;

public class OutputManager
{
    private static ChiUniTextArea textArea;

    public static void initialise(ChiUniTextArea area)
    {
        textArea = area;
    }

    public static void print(String message)
    {
        if (textArea != null)
        {
            textArea.appendWithTimestamp(message);
        }
        else
        {
            System.out.println(message);
        }
    }

    public static void clear()
    {
        if (textArea != null)
        {
            textArea.clear();
        }
    }
}
