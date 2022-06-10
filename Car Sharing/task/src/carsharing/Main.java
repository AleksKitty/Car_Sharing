package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        DBOperations dbOperations;
        if (args.length > 1 && !args[1].isEmpty()) {
            dbOperations = new DBOperations(args[1]);
        } else {
            dbOperations = new DBOperations();
        }

        dbOperations.makeConnectionAndCreateTables();

        boolean exit = false;

        int command;

        while (!exit) {
            printMainMenu();
            command = Integer.parseInt(scanner.nextLine());

            if (command == 1) {
                runAsAManager();
            } else if (command == 2) {
                runAsACustomer();
            } else if (command == 3) {
                createCustomer();
            } else {
                exit = true;
            }
        }

        dbOperations.closeResources();
    }

    private static void printMainMenu() {
        System.out.println("\n1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }

    private static void printCompanyMenu() {
        System.out.println("\n1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    private static void printCarMenu(String companyName, boolean flag) {
        if (flag) {
            System.out.println("\n'" + companyName + "' company");
            System.out.println("1. Car list");
        } else {
            System.out.println("\n1. Car list");
        }
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    private static void printCarMenu() {
        System.out.println("\n1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
    }

    private static void runAsAManager() {
        boolean exitCompanyMenu = false;
        boolean exitCarMenu;
        boolean flag;

        int companyIdPlusOne = 0;
        ArrayList<Company> companies;

        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        CarDaoImpl carDao = new CarDaoImpl();

        while (!exitCompanyMenu) {
            printCompanyMenu();

            int command = Integer.parseInt(scanner.nextLine());

            if (command == 1) {
                companies = companyDao.getAllCompanies();

                exitCarMenu = false;
                if (!companies.isEmpty()) {
                    companyIdPlusOne = Integer.parseInt(scanner.nextLine());
                } else{
                    exitCarMenu = true;
                }

                flag = true;
                while (!exitCarMenu) {
                    if (companyIdPlusOne == 0 || command == 0) {
                        exitCarMenu = true;
                    } else {
                        printCarMenu(companies.get(companyIdPlusOne - 1).getName(), flag);
                        flag = false;

                        command = Integer.parseInt(scanner.nextLine());
                        if (command == 1) {
                            carDao.getAllCars(companies.get(companyIdPlusOne - 1).getName());
                        } else if (command == 2) {
                            System.out.println("\nEnter the car name:");
                            carDao.addCar(scanner.nextLine(), companies.get(companyIdPlusOne - 1).getName());
                            System.out.println("The car was added!");
                        }
                    }
                }
            } else if (command == 2) {
                System.out.println("\nEnter the company name:");
                companyDao.addCompany(scanner.nextLine());
                System.out.println("The company was created!");
            } else {
                exitCompanyMenu = true;
            }
        }
    }

    private static void runAsACustomer(){
        CustomerDaoImpl customerDao = new CustomerDaoImpl();
        ArrayList<Customer> customers = customerDao.getAllCustomers();

        int customerIdPlusOne;
        int command = -1;
        if (!customers.isEmpty()) {

            customerIdPlusOne = Integer.parseInt(scanner.nextLine());

            String customerName = customers.get(customerIdPlusOne - 1).getName();
            while (command != 0) {
                printCarMenu();
                command = Integer.parseInt(scanner.nextLine());

                if (command == 1) {
                    customerDao.rentACar(customerName);
                } else if (command == 2) {
                    customerDao.returnCar(customerName);
                } else if (command == 3) {
                    customerDao.getRentedCarInfo(customerName);
                }
            }
        }
    }

    private static void createCustomer(){
        CustomerDaoImpl customerDao = new CustomerDaoImpl();
        System.out.println("\nEnter the customer name:");
        customerDao.addCustomer(scanner.nextLine());
        System.out.println("The customer was added!");
    }
}