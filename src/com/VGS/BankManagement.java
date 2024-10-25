package com.VGS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class BankManagement {
    public static final int NULL = 0;
    static Connection con = Connect.getConnection();
    static String sql = "";

    public static boolean createAccount(String name, int pass) {
        try {
            if (name.isEmpty() || pass == NULL) {
                System.out.println("All fields are required!!");
                return false;
            }

            sql = "INSERT INTO customer(cname, balance, pass_code) VALUES(?, 1000, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, pass);

            if (ps.executeUpdate() == 1) {
                System.out.println(name + " You may Login Now!!");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("ERR: Username not available or insertion failed!!");
            e.printStackTrace();
        }
        return false; // Return false if account creation failed
    }

    public static boolean loginAccount(String name, int pass) {
        try {
            if (name.isEmpty() || pass == NULL) {
                System.out.println("All fields are needed!");
                return false;
            }

            sql = "SELECT * FROM customer WHERE cname = ? AND pass_code = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, pass);
            ResultSet rs = ps.executeQuery();

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            if (rs.next()) {
                int ch;
                int amt;
                int senderAc = rs.getInt("ac_no");
                int recieveAc;

                while (true) {
                    try {
                        System.out.println("Hello, " + rs.getString("cname"));
                        System.out.println("1) Transfer Money");
                        System.out.println("2) Check Balance");
                        System.out.println("5) Log Out");
                        ch = Integer.parseInt(br.readLine());

                        if (ch == 1) {
                            System.out.print("Enter Receiver A/c No: ");
                            recieveAc = Integer.parseInt(br.readLine());
                            System.out.print("Enter Amount: ");
                            amt = Integer.parseInt(br.readLine());

                            if (transferMoney(senderAc, recieveAc, amt)) {
                                System.out.println("MSG: Money Sent Successfully!!\n");
                            } else {
                                System.out.println("ERR: Transaction Failed !! ");
                            }
                        } else if (ch == 2) {
                            getBalance(senderAc);
                        } else if (ch == 5) {
                            break;
                        } else {
                            System.out.println("ERR: Invalid option! Please try again.\n");
                        }
                    } catch (Exception e) {
                        System.out.println("ERR: Enter valid input!!\n");
                    }
                }
                return true; // Login successful
            } else {
                System.out.println("ERR: Invalid username or password!");
            }
        } catch (Exception e) {
            System.out.println("ERR: An unexpected error occurred during login.");
            e.printStackTrace();
        }
        return false; // Return false if login failed
    }

    public static boolean transferMoney(int senderAc, int recieveAc, int amt) {
        if (recieveAc == NULL || amt == NULL) {
            System.out.println("All fields are required!");
            return false;
        }

        try {
            con.setAutoCommit(false); // Start transaction
            sql = "SELECT * FROM customer WHERE ac_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, senderAc);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("balance") < amt) {
                    System.out.println("Insufficient Funds!");
                    return false;
                }
            }

            // Debit
            sql = "UPDATE customer SET balance = balance - ? WHERE ac_no = ?";
            PreparedStatement debitStmt = con.prepareStatement(sql);
            debitStmt.setInt(1, amt);
            debitStmt.setInt(2, senderAc);
            debitStmt.executeUpdate();

            // Credit
            sql = "UPDATE customer SET balance = balance + ? WHERE ac_no = ?";
            PreparedStatement creditStmt = con.prepareStatement(sql);
            creditStmt.setInt(1, amt);
            creditStmt.setInt(2, recieveAc);
            creditStmt.executeUpdate();

            con.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            try {
                con.rollback(); // Rollback transaction in case of error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
        return false; // Return false if transfer failed
    }

    public static void getBalance(int acno) {
        try {
            sql = "SELECT * FROM customer WHERE ac_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, acno);
            ResultSet rs = ps.executeQuery();

            System.out.println("----------------------------------------------------------------------------------");
            System.out.printf("%12s %10s %10s\n", "Account No", "Name", "Balance");

            while (rs.next()) {
                System.out.printf("%12d %10s %10d.00\n",
                        rs.getInt("ac_no"),
                        rs.getString("cname"),
                        rs.getInt("balance"));
            }
            System.out.println("----------------------------------------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
