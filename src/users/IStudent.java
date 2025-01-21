package users;

/**
 * Interface defining the contract for student-specific functionality.
 * This interface extends the base functionality provided by IUser with
 * methods specific to student management, such as gender and student type.
 * Classes implementing this interface must provide both the student-specific
 * functionality defined here and the base user functionality from IUser.
 */
public interface IStudent
{

    /**
     * Gets the student's gender.
     *
     * @return The gender of the student
     */
    String getGender();

    /**
     * Sets the student's gender.
     *
     * @param gender The gender to set for the student
     */
    void setGender(String gender);

    /**
     * Gets the student's type (e.g., full-time, part-time, international).
     *
     * @return The type of the student
     * @see StudentType for valid student types
     */
    String getType();

    /**
     * Sets the student's type.
     *
     * @param type The type to set for the student
     * @see StudentType for valid student types
     */
    void setType(String type);
}