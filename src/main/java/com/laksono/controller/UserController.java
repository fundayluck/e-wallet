package com.laksono.controller;

import com.laksono.Main;
import com.laksono.entity.User;
import com.laksono.entity.UserDetails;
import com.laksono.repository.impl.RepoTransactionImpl;
import com.laksono.repository.impl.RepoUserDetailImpl;
import com.laksono.repository.impl.RepoUserImpl;


import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;
import java.util.UUID;

public class UserController {
    private final RepoTransactionImpl repoTransaction;
    private final RepoUserDetailImpl repoUserDetail;

    public UserController(RepoTransactionImpl repoTransaction, RepoUserDetailImpl repoUserDetail) {
        this.repoTransaction = repoTransaction;
        this.repoUserDetail = repoUserDetail;
    }

    public void registerUser(Scanner scanner, RepoUserImpl repoUser, RepoUserDetailImpl repoUserDetail) {
        try {
            UUID manuallyGeneratedId = UUID.randomUUID();
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter address: ");
            String address = scanner.nextLine();

            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());

            UserDetails userDetails = new UserDetails();
            userDetails.setId(manuallyGeneratedId);
            userDetails.setFirstName(firstName);
            userDetails.setLastName(lastName);
            userDetails.setAddress(address);
            userDetails.setDateOfBirth(dateOfBirth);
            repoUserDetail.insertUserDetail(userDetails);


            User user = new User();
            user.setUsername(username);
            user.setPin(password);
            user.setUserDetails(userDetails);

            repoUser.insertUser(user);

            System.out.println("User registered successfully!");
        } catch (Exception e) {
            // Handle the exception (e.g., log error or display error message)
            e.printStackTrace();
            // Optionally, you can inform the user about the error
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    public void loginUser(Scanner scanner, RepoUserImpl repoUser) {
        if (!Main.isLoggedIn()) {

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            User user = repoUser.loginUser(username, password);
            if (user != null) {
                Main.setLoggedIn(true);
                Main.setUsername(user.getUsername());
                System.out.println("Login successful!");
                showServiceOptions(scanner, repoUser);
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        } else {
            System.out.println("You are already logged in as " + Main.username() + ".");
        }
    }

    public void showServiceOptions(Scanner scanner, RepoUserImpl repoUser) {
        if (Main.isLoggedIn()) {
            // Provide options for services
            System.out.println("Greetings, " + Main.username() + "!");
            System.out.println("Choose an option:");
            System.out.println("1. info saldo");
            System.out.println("2. Top Up");
            System.out.println("3. Transfer");
            System.out.println("4. My Transaction");
            System.out.println("5. Settings");
            System.out.println("6. Logout");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Service 1 logic
                    System.out.println("info saldo");
                    repoUser.getSaldobyUsername(Main.username());
                    break;
                case 2:
                    // Service 2 logic
                    System.out.println("Top Up");
                    System.out.print("Enter amount: ");
                    BigDecimal amount = scanner.nextBigDecimal();
                    repoUser.topUpSaldo(Main.username(), amount);
                    break;
                case 3:
                    // Service 3 logic
                    System.out.println("transfer");
                    System.out.print("Enter amount: ");
                    BigDecimal amountTransfer = scanner.nextBigDecimal();
                    scanner.nextLine();
                    System.out.print("Enter receiver: ");
                    String receiver = scanner.nextLine();
                    repoUser.transferSaldo(Main.username(), receiver, amountTransfer);
                    break;
                case 4:
                    System.out.println("my transaction");
                    repoTransaction.getTransactionById(Main.username());
                    break;
                case 5:
                    System.out.println("settings");
                    System.out.println("1. Info user detail");
                    System.out.println("2. Update user detail");
//                    System.out.println("3. Other option");
                    System.out.print("Enter your choice: ");
                    int settingsChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    switch (settingsChoice) {
                        case 1:
                            System.out.println("Info user detail");
                            // Call a method to display user details
                            repoUserDetail.getUserDetailByUsername(Main.username());
                            break;
                        case 2:
                            System.out.println("Update user detail");
                            System.out.print("Enter first name: ");
                            String firstName = scanner.nextLine();

                            System.out.print("Enter last name: ");
                            String lastName = scanner.nextLine();

                            System.out.print("Enter address: ");
                            String address = scanner.nextLine();

                            System.out.print("Enter date of birth (yyyy-MM-dd): ");
                            LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());

                            UserDetails userDetails = new UserDetails();
                            userDetails.setFirstName(firstName);
                            userDetails.setLastName(lastName);
                            userDetails.setAddress(address);
                            userDetails.setDateOfBirth(dateOfBirth);
                            repoUserDetail.updateUserDetail(Main.username(),userDetails);
                            // Call a method to update user details
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                    break;
                case 6:
                    // Logout logic
                    Main.setLoggedIn(false);
                    Main.setUsername("");
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } else {
            System.out.println("Please login first to access services.");
        }

    }

}
