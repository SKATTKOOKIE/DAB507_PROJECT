import business.Department;
import business.Module;
import com.google.gson.JsonArray;
import file_handling.JsonProcessor;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // Process modules
        JsonProcessor moduleProcessor = new JsonProcessor("data/modules.json");
        moduleProcessor.processFile();
        JsonArray moduleJsonArray = (JsonArray) moduleProcessor.getJsonContent();
        List<Module> modules = Module.fromJsonArray(moduleJsonArray);

        // Process departments
        JsonProcessor departmentProcessor = new JsonProcessor("data/departments.json");
        departmentProcessor.processFile();
        JsonArray departmentJsonArray = (JsonArray) departmentProcessor.getJsonContent();
        List<Department> departments = Department.fromJsonArray(departmentJsonArray);

        // Example: Find Computer Science department and add related modules
        List<Department> computerScienceDept = Department.searchByName(departments, "Computing");
        if (!computerScienceDept.isEmpty()) {
            Department cseDept = computerScienceDept.get(0);

            // Add modules that match computer science criteria
            for (Module module : modules) {
                if (module.getCode().startsWith("CS") ||
                        module.getName().toLowerCase().contains("computer") ||
                        module.getName().toLowerCase().contains("computing")) {
                    cseDept.addModule(module);
                }
            }

            // Now you can work with the department and its modules
            System.out.println(cseDept);
            System.out.println("Current year modules: " + cseDept.getModulesByYear("20"));
            System.out.println("Programming modules: " + cseDept.searchModulesByName("Programming"));
        }
    }
}