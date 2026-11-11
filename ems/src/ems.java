import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class Employee {
    private int id;
    private String name;
    private String department;
    private double salary;

    public Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}

 class EmployeeService {
    private final List<Employee> employees = new ArrayList<>();

    public boolean addEmployee(Employee employee) {
        if(findById(employee.getId())!= null) return false;
        employees.add(employee);
        return true;
    }

    public List<Employee> getAllEmployees() {
        return employees;
    }

    public Employee findById(int id) {
        for (Employee employee : employees) {
            if(employee.getId() == id) return employee;
        }
        return null;
    }

    public boolean updateEmployee(int id, String name, String department, double salary) {
        Employee employee = findById(id);
        if(employee == null) return false;

        employee.setName(name);
        employee.setDepartment(department);
        employee.setSalary(salary);
        return true;
    }

    public boolean deleteEmployee(int id) {
        Employee employee = findById(id);
        if(employee == null) return false;
        return employees.remove(employee);
    }
 }

public class ems {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeService service = new EmployeeService();

    void main() {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: {
                    int id = readInt("Enter ID: ");
                    String name = readString("Enter Name: ");
                    String department = readString("Enter Department: ");
                    double salary = readDouble("Enter Salary: ");

                    if (service.addEmployee(new Employee(id, name, department, salary))) {
                        System.out.println("Employee added successfully!");
                    } else {
                        System.out.println("Employee already exists!");
                    }
                    break;
                }

                case 2: {
                    List<Employee> list = service.getAllEmployees();

                    if(list.isEmpty()) {
                        System.out.println("No employees records found!");
                        return;
                    }
                    System.out.println("-------------Employee Records-------------");
                    for(Employee employee : list) {
                        System.out.println(employee);
                    }
                    break;
                }

                case 3: {
                    int id = readInt("Enter id to update:");
                    if(service.findById(id) == null) {
                        System.out.println("Employee not found!");
                        return;
                    }
                    String name = readString("Enter Name to update:");
                    String department = readString("Enter Department to update:");
                    double salary = readDouble("Enter Salary to update:");
                    service.updateEmployee(id, name, department, salary);
                    System.out.println("Employee updated successfully!");
                    break;
                }

                case 4: {
                    int id = readInt("Enter emp ID to delete:");
                    if(service.deleteEmployee(id)) {
                        System.out.println("Employee deleted successfully!");
                    } else {
                        System.out.println("Employee not found!");
                    }
                    break;
                }

                case 5: {
                    System.out.println("----------Exiting------------");
                    break;
                }

                default: {
                    System.out.println("Invalid choice, Try again!");
                    break;
                }
            }
        } while (choice != 5);
    }

    private void printMenu() {
        System.out.println("---------------------Employee Management System----------------------");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Update Employee");
        System.out.println("4. Delete Employee");
        System.out.println("5. Exit");
    }

    private int readInt(String prompt) {
       while (true) {
           try {
               System.out.print(prompt);
               int value = scanner.nextInt();
               scanner.nextLine();
               return value;
           } catch (InputMismatchException e) {
                System.out.println("Invalid Input / Enter a whole number");
                scanner.nextLine();
           }
       }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input / Enter valid Number");
                scanner.nextLine();
            }
        }
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
