package gui.interfaces;

import gui.DataManager;

/**
 * Interface defining the core functionality for the University Management System GUI.
 * Provides contract for navigation between different views and management of UI components.
 */
public interface UniversityManagementGui
{
    /**
     * Displays the welcome panel and initializes application data.
     */
    void showWelcomePanel();

    /**
     * Displays the departments management panel.
     */
    void showDepartmentsPanel();

    /**
     * Displays the student management panel.
     */
    void showStudentsPanel();

    /**
     * Displays the staff management panel.
     */
    void showStaffPanel();

    /**
     * Displays the dialog for adding a new user to the system.
     */
    void showAddUserDialog();

    /**
     * Displays the dialog for adding a new course to the system.
     */
    void showAddCourseDialog();

    /**
     * Displays the dialog for adding a new module to the system.
     */
    void showAddModuleDialog();

    /**
     * Refreshes a specific type of data in the system.
     *
     * @param dataType The type of data to refresh
     */
    void refreshSpecificData(DataManager.DataType dataType);

    /**
     * Makes the main application window visible.
     */
    void show();
}