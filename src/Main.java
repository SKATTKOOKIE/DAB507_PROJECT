import java.io.IOException;
import business.Department;

public class Main
{
    public static void main(String[] args)
    {
        String filePath = "data/departments.json";

        System.out.println("All Departments:");
        Department.displayAll(filePath);

        System.out.println("\nLooking for specific department:");
        Department.displayById(filePath, "ENG");


    }
}