package carsharing;

import java.util.List;

public interface CarDao {
    public List<Car> getAllCars(String companyName);
    public void addCar(String carName, String companyName);
}
