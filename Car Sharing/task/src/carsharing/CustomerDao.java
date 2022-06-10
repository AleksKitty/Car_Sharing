package carsharing;

import java.util.List;

public interface CustomerDao {
    public List<Customer> getAllCustomers();
    public void addCustomer(String customerName);
    public void rentACar(String customerName);
    public void getRentedCarInfo(String customerName);
    public void returnCar(String customerName);
}
