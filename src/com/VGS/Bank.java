package com.VGS;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Bank {
    public static void main(String[] args) {
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        String name = "";
        int pass_code;
        int ch;

        while (true) {
            System.out.println("\n -> || Welcome VGS Bank ||<-\n");
            System.out.println("1) Create New Account");
            System.out.println("2) Login Account");

            try {
                System.out.println("\n Enter Input:"); // User Input of Options
                ch = Integer.parseInt(sc.readLine());

                switch (ch) {
                    case 1:
                        try {
                            System.out.println("\n -> || Welcome VGS Bank ||<-\n");
                            System.out.println("Enter the Unique Username: ");
                            name = sc.readLine();
                            System.out.println("Enter New Rememberable Password: ");
                            pass_code = Integer.parseInt(sc.readLine());

                            if (BankManagement.createAccount(name, pass_code)) {
                                System.out.println("MSG: Account Created Successfully!! \n");
                            } else {
                                System.out.println("ERR: Account Not Created! \n");
                            }
                        } catch (Exception e) {
                            System.out.println("ERR: Enter valid Data :: Insertion Failed!! \n");
                        }
                        break;

                    case 2:
                        try {
                            System.out.println("Enter Username: ");
                            name = sc.readLine();
                            System.out.println("Enter Your Password: ");
                            pass_code = Integer.parseInt(sc.readLine());

                            if (BankManagement.loginAccount(name, pass_code)) {
                                System.out.println("MSG: Login Successful!\n");
                            } else {
                                System.out.println("ERR: Login Failed!\n");
                            }
                        } catch (Exception e) {
                            System.out.println("ERR: Enter Valid Data:: Login Failed!\n");
                        }
                        break;

                    default:
                        System.out.println("ERR: Invalid option! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("ERR: Invalid input!");
            }
        }
    }
}
