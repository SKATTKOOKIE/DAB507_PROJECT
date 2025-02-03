package testframework;

/**
 * Custom assertion class providing basic assertion methods for testing.
 */
public class Assert
{
    public static void assertEquals(Object expected, Object actual, String message)
    {
        if (!expected.equals(actual))
        {
            throw new AssertionError(message +
                    " - Expected: " + expected + ", but got: " + actual);
        }
    }

    public static void assertTrue(boolean condition, String message)
    {
        if (!condition)
        {
            throw new AssertionError(message);
        }
    }

    public static void assertNull(Object obj, String message)
    {
        if (obj != null)
        {
            throw new AssertionError(message + " - Expected null, but got: " + obj);
        }
    }

    public static void assertNotNull(Object obj, String message)
    {
        if (obj == null)
        {
            throw new AssertionError(message + " - Expected non-null value");
        }
    }

    public static void assertFalse(boolean condition, String message)
    {
        if (condition)
        {
            throw new AssertionError(message);
        }
    }

    // Add more assertion methods as needed
}