package carsharing;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static carsharing.DBOperations.*;

public class CustomerDaoImpl implements CustomerDao {

    static Scanner scanner = new Scanner(System.in);

    @Override
    public ArrayList<Customer> getAllCustomers() {
        String sql = "SELECT NAME FROM CUSTOMER ORDER BY ID";
        ArrayList<Customer> customers = getAllCustomersSQL(sql);

        if (customers.isEmpty()) {
            System.out.println("\nThe customer list is empty!");
        } else {
            System.out.println("\nChoose the customer:");
            int i;
            for (i = 1; i <= customers.size(); i++) {
                System.out.println(i + ". " + customers.get(i - 1).getName());
            }
            System.out.println("0. Back");
        }

        return customers;
    }

    @Override
    public void addCustomer(String customerName) {
        String sql = "INSERT INTO CUSTOMER (NAME) VALUES ('" + customerName + "')";
        insertIntoDBSQL(sql);
    }

    @Override
    public void rentACar(String customerName) {
        CompanyDaoImpl companyDao = new CompanyDaoImpl();
        CarDaoImpl carDao = new CarDaoImpl();

        int carId = getIdSql("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE NAME = '" + customerName + "'", "RENTED_CAR_ID");

        if (carId != 0) {
            System.out.println("\nYou've already rented a car!");
        } else {
            ArrayList<Company> companies = companyDao.getAllCompanies();

            if (companies.isEmpty()) {
                System.out.println("\nThe company list is empty!");
            } else {
                int companyIdPlusOne = Integer.parseInt(scanner.nextLine());
                String companyName = companies.get(companyIdPlusOne - 1).getName();

                List<Car> cars = carDao.getAllCarsNoPrint(companyName);

                int userId;
                String carName;
                boolean flag = false;
                int i = 1;
                ArrayList<Car> availableCars = new ArrayList<>();
                for (Car car : cars) {
                    userId = getIdSql("SELECT ID FROM CUSTOMER WHERE RENTED_CAR_ID = " + car.getId(), "ID");

                    if (userId == 0) {
                        availableCars.add(car);

                        if (!flag) {
                            System.out.println("\nChoose a car:");
                            flag = true;
                        }
                        carName = getNameSql("SELECT NAME FROM CAR WHERE ID = " + car.getId());

                        System.out.println(i + ". " + carName);
                        i++;
                    }
                }

                if (flag) {
                    System.out.println("0. Back");
                } else {
                    System.out.println("\nNo available cars in the" + companyName + "company.");
                }

                int chosenCarIdPlusOne = Integer.parseInt(scanner.nextLine());

                if (chosenCarIdPlusOne != 0) {
                    String chosenCarName = availableCars.get(chosenCarIdPlusOne - 1).getName();
                    int chosenId = getIdSql("SELECT ID FROM CAR WHERE NAME = '" + chosenCarName + "'", "ID");

                    insertIntoDBSQL("UPDATE CUSTOMER SET RENTED_CAR_ID = " + chosenId + " WHERE NAME = '" + customerName + "'");

                    System.out.println("\nYou rented '" + chosenCarName + "'");
                }

            }
        }
    }

    @Override
    public void getRentedCarInfo(String customerName) {
        int carId = getIdSql("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE NAME = '" + customerName + "'", "RENTED_CAR_ID");

        if (carId == 0) {
            System.out.println("\nYou didn't rent a car!");
        } else {
            String carName = getNameSql("SELECT NAME FROM CAR WHERE ID = " + carId);
            int companyId = getIdSql("SELECT COMPANY_ID FROM CAR WHERE ID = " + carId, "COMPANY_ID");
            String companyName = getNameSql("SELECT NAME FROM COMPANY WHERE ID = " + companyId);

            System.out.println("\nYour rented car:");
            System.out.println(carName);
            System.out.println("Company:");
            System.out.println(companyName);
        }
    }

    @Override
    public void returnCar(String customerName) {
        int carId = getIdSql("SELECT RENTED_CAR_ID FROM CUSTOMER WHERE NAME = '" + customerName + "'", "RENTED_CAR_ID");

        if (carId == 0) {
            System.out.println("\nYou didn't rent a car!");
        }

        insertIntoDBSQL("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = " + carId);
        System.out.println("\nYou've returned a rented car!");
    }

    private ArrayList<Customer> getAllCustomersSQL(String sql) {

        ArrayList<Customer> list = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(sql);

            // Fetch each row from the result set
            while (rs.next()) {
                String str = rs.getString("NAME");
                Customer customer = new Customer(str);

                list.add(customer);
            }
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
        }
        return list;
    }
}
