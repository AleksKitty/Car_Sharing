package carsharing;

import java.sql.*;
import java.util.ArrayList;

import static carsharing.DBOperations.insertIntoDBSQL;
import static carsharing.DBOperations.stmt;


public class CompanyDaoImpl implements CompanyDao{

    @Override
    public ArrayList<Company> getAllCompanies() {
        String sql = "SELECT NAME FROM COMPANY ORDER BY ID";
        ArrayList<Company> companies = getAllCompaniesSQL(sql);

        if (companies.isEmpty()) {
            System.out.println("\nThe company list is empty!");
        } else {
            System.out.println("\nChoose the company:");
            int i;
            for (i = 1; i <= companies.size(); i++) {
                System.out.println(i + ". " + companies.get(i - 1).getName());
            }
            System.out.println("0. Back");
        }
        return companies;
    }

    @Override
    public void addCompany(String nameOfCompany) {
        String sql = "INSERT INTO COMPANY (NAME) VALUES ('" + nameOfCompany + "');";
        insertIntoDBSQL(sql);
    }

    private ArrayList<Company> getAllCompaniesSQL(String sql) {

        ArrayList<Company> list = new ArrayList<>();
        try {
            ResultSet rs = stmt.executeQuery(sql);

            // Fetch each row from the result set
            while (rs.next()) {
                String str = rs.getString("NAME");
                Company company = new Company(str);

                list.add(company);
            }
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
        }
        return list;
    }
}
