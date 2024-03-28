package com.laksono;

import com.laksono.controller.UserController;
import com.laksono.repository.impl.RepoTransactionImpl;
import com.laksono.repository.impl.RepoUserDetailImpl;
import com.laksono.repository.impl.RepoUserImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Objects;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static boolean isLoggedIn = false;
    private static String username = "";
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("learn-jpa-hibernate");
        EntityManager em = emf.createEntityManager();
        RepoUserImpl repoUser = new RepoUserImpl(em);
        RepoUserDetailImpl repoUserDetail = new RepoUserDetailImpl(em,repoUser);
        RepoTransactionImpl repoTransaction = new RepoTransactionImpl(em);

        UserController userController = new UserController(repoTransaction,repoUserDetail);

        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        if(!Objects.equals(Main.username(), "")) {
            userController.showServiceOptions(scanner,repoUser);
        } else {
            while (!exit) {
                if(!Objects.equals(Main.username(), "")) {
                    userController.showServiceOptions(scanner,repoUser);
                } else {
                    System.out.println("Welcome to e-wallet!");
                    System.out.println("+------------------+");
                    System.out.println("| Choose an option |");
                    System.out.println("+------------------+");
                    System.out.println("| 1. Register      |");
                    System.out.println("| 2. Login         |");
                    System.out.println("| 3. Exit          |");
                    System.out.println("+------------------+");

                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline


                    switch (choice) {
                        case 1:
                            userController.registerUser(scanner,repoUser, repoUserDetail);
                            break;
                        case 2:
                            userController.loginUser(scanner,repoUser);
                            break;
                        case 3:
                            exit = true;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
            }
        }


        em.close();
        emf.close();
        scanner.close();
    }
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
    public static String username() {
        return username;
    }
    public static void setUsername(String name) {
        username = name;
    }

}