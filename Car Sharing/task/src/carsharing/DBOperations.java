package carsharing;

import java.sql.*;

public class DBOperations {

    private String dbUrl = "jdbc:h2:./src/carsharing/db/";
    private String myDb = "my_DB";

    static Connection conn = null;
    static Statement stmt = null;

    public DBOperations() {
        this.dbUrl += myDb;
    }

    public DBOperations(String dbName) {
        this.dbUrl += dbName;
    }

    public void makeConnectionAndCreateTables() {

        try {
            //STEP 1: Open a connection
            conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(true);

            //STEP 2: Execute a query
            stmt = conn.createStatement();

            // check if table exists
            stmt.execute("CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(ID INT AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "PRIMARY KEY (ID))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CAR " +
                    "(ID INT AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "COMPANY_ID INT NOT NULL, " +
                    "PRIMARY KEY (ID), " +
                    "FOREIGN KEY(COMPANY_ID) REFERENCES COMPANY(ID))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(ID INT AUTO_INCREMENT, " +
                    "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    "RENTED_CAR_ID INT DEFAULT NULL, " +
                    "PRIMARY KEY (ID), " +
                    "FOREIGN KEY(RENTED_CAR_ID) REFERENCES CAR(ID))");


        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
        }
    }

    static void insertIntoDBSQL(String sql) {

        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
        }
    }

    static int getIdSql(String sql, String column) {

        int id = 0;
        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                id = rs.getInt(column);
            }
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
        }
        return id;
    }

    static String getNameSql(String sql) {

        String name = "";
        try {
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                name = rs.getString("NAME");
            }
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
        }
        return name;
    }


    void closeResources() throws SQLException {
        // STEP 3: Clean-up environment
        if (stmt != null) {
            stmt.close();
        }

        if (conn != null) {
            conn.close();
        }
    }
}
