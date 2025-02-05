package testframework;

/**
 * Custom assertion class providing basic assertion methods for testing.
 * This class implements common assertion methods used in unit testing
 * to verify expected conditions and values.
 * <p>
 * All methods throw AssertionError with detailed messages when assertions fail.
 */
public class Assert
{

    /**
     * Asserts that two objects are equal using the equals() method.
     *
     * @param expected The expected value
     * @param actual   The actual value to test
     * @param message  The message to display if the assertion fails
     * @throws AssertionError if the objects are not equal
     */
    public static void assertEquals(Object expected, Object actual, String message)
    {
        if (!expected.equals(actual))
        {
            throw new AssertionError(message +
                    " - Expected: " + expected + ", but got: " + actual);
        }
    }

    /**
     * Asserts that a condition is true.
     *
     * @param condition The condition to test
     * @param message   The message to display if the assertion fails
     * @throws AssertionError if the condition is false
     */
    public static void assertTrue(boolean condition, String message)
    {
        if (!condition)
        {
            throw new AssertionError(message);
        }
    }

    /**
     * Asserts that an object is null.
     *
     * @param obj     The object to test
     * @param message The message to display if the assertion fails
     * @throws AssertionError if the object is not null
     */
    public static void assertNull(Object obj, String message)
    {
        if (obj != null)
        {
            throw new AssertionError(message + " - Expected null, but got: " + obj);
        }
    }

    /**
     * Asserts that an object is not null.
     *
     * @param obj     The object to test
     * @param message The message to display if the assertion fails
     * @throws AssertionError if the object is null
     */
    public static void assertNotNull(Object obj, String message)
    {
        if (obj == null)
        {
            throw new AssertionError(message + " - Expected non-null value");
        }
    }

    /**
     * Asserts that a condition is false.
     *
     * @param condition The condition to test
     * @param message   The message to display if the assertion fails
     * @throws AssertionError if the condition is true
     */
    public static void assertFalse(boolean condition, String message)
    {
        if (condition)
        {
            throw new AssertionError(message);
        }
    }

    /**
     * Asserts that two objects are not equal using the equals() method.
     *
     * @param expected The value that actual should not equal
     * @param actual   The actual value to test
     * @param message  The message to display if the assertion fails
     * @throws AssertionError if the objects are equal
     */
    public static void assertNotEquals(Object expected, Object actual, String message)
    {
        if (expected.equals(actual))
        {
            throw new AssertionError(message +
                    " - Expected different value than: " + expected);
        }
    }
}