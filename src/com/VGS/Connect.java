package com.VGS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    private static Connection con;  // Global connection object

    public static Connection getConnection() {
        if (con == null) {
            try {
                String mysqlJDBCDriver = "com.mysql.cj.jdbc.Driver"; // JDBC Driver
                String url = "jdbc:mysql://localhost:3306/VGS_BANK"; // MySQL URL
                String user = "root"; // MySQL Username
                String pass = "root"; // MySQL Password

                Class.forName(mysqlJDBCDriver);
                con =  DriverManager.getConnection(url, user, pass);
            } catch (ClassNotFoundException e) {
                System.out.println("JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("Connection Failed!!");
                e.printStackTrace();
            }
        }
        return con;
    }
}
