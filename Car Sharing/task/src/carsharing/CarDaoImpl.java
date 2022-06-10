package carsharing;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static carsharing.DBOperations.*;

public class CarDaoImpl implements CarDao {

    @Override
    public ArrayList<Car> getAllCars(String companyName) {
        int companyId = getIdSql("SELECT ID FROM COMPANY WHERE NAME = '" + companyName + "'", "ID");

        String sql = "SELECT ID, NAME FROM CAR WHERE COMPANY_ID = " + companyId + " ORDER BY ID";
        ArrayList<Car> cars = getAllCarsSQL(sql);

        if (cars.isEmpty()) {
            System.out.println("\nThe car list is empty!");
        } else {
            System.out.println("\nCar list:");
            for (int i = 1; i <= cars.size(); i++) {
                System.out.println(i + ". " + cars.get(i - 1).getName());
            }
        }

        return cars;
    }

    public List<Car> getAllCarsNoPrint(String companyName) {
        int companyId = getIdSql("SELECT ID FROM COMPANY WHERE NAME = '" + companyName + "'", "ID");
        String sql = "SELECT ID, NAME FROM CAR WHERE COMPANY_ID = " + companyId + " ORDER BY ID";
        return getAllCarsSQL(sql);
    }

    @Override
    public void addCar(String carName, String companyName) {
        int companyId = getIdSql("SELECT ID FROM COMPANY WHERE NAME = '" + companyName + "'", "ID");
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + carName + "', " + companyId + ");";
        insertIntoDBSQL(sql);
    }

    private ArrayList<Car> getAllCarsSQL(String sql) {

        ArrayList<Car> list = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(sql);

            // Fetch each row from the result set
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                Car car = new Car(id, name);

                list.add(car);
            }
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
        }
        return list;
    }
}
