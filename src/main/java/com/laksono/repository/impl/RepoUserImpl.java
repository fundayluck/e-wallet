package com.laksono.repository.impl;

import com.laksono.entity.Transaction;
import com.laksono.entity.User;
import com.laksono.repository.RepoUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RepoUserImpl implements RepoUser {
    private final EntityManager em;


    public RepoUserImpl(EntityManager em){
        this.em = em;
    }
    @Override
    public List<User> getAll() {
        List<User> read = em.createQuery("select c from users c", User.class).getResultList();
        return read;
    }

    @Override
    public void insertUser(User user) {
        em.getTransaction().begin();
        em.createNativeQuery("INSERT INTO users (username, pin, balance, user_details_id) VALUES (?, ?, ?, ?)")
                .setParameter(1, user.getUsername())
                .setParameter(2, user.getPin())
                .setParameter(3, 0)
                .setParameter(4, user.getUserDetails().getId())
                .executeUpdate();
        em.getTransaction().commit();
        String[] headers = {"Username", "PIN", "Balance", "User Details ID"};
        String[] userData = {user.getUsername(), user.getPin(), "0", String.valueOf(user.getUserDetails().getId())};

        String table = Stream.of(headers, userData)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            int[] columnWidths = new int[headers.length];
                            list.forEach(data -> {
                                for (int i = 0; i < data.length; i++) {
                                    columnWidths[i] = Math.max(columnWidths[i], data[i].length());
                                }
                            });

                            StringBuilder sb = new StringBuilder();
                            printRow(sb, headers, columnWidths);
                            printSeparator(sb, columnWidths);
                            printRow(sb, userData, columnWidths);
                            printSeparator(sb, columnWidths);
                            return sb.toString();
                        }));

        System.out.println("Data User inserted successfully:");
        System.out.println(table);
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deletedUser(int id) {

    }

    @Override
    public User getUserByUsername(String username ) {
        Query query = em.createNativeQuery("SELECT * FROM users WHERE username = ?", User.class);
        query.setParameter(1, username);
        return (User) query.getSingleResult();
    }

    @Override
    public User loginUser(String username, String password) {
        Query query = em.createNativeQuery("SELECT * FROM users WHERE username = ? AND pin = ?", User.class);
        query.setParameter(1, username);
        query.setParameter(2, password);

        List userList = query.getResultList();
        if (!userList.isEmpty()) {
            return (User) userList.get(0); // Assuming there's only one user with the given username and password
        } else {
            return null; // No user found with the given username and password
        }
    }

    @Override
    public void getSaldobyUsername(String username) {
        Query query = em.createNativeQuery("SELECT balance FROM users WHERE username = :username");
        query.setParameter("username", username);

        BigDecimal balance = null;
        try {
            balance = (BigDecimal) query.getSingleResult();
        } catch (NoResultException e) {
            // Handle case when no user found with the given username
            System.out.println("User with username '" + username + "' not found.");
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

        String[] headers = {"Saldo"};
        String[] userData = {String.valueOf(balance)};

        String table = Stream.of(headers, userData)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            int[] columnWidths = new int[headers.length];
                            list.forEach(data -> {
                                for (int i = 0; i < data.length; i++) {
                                    columnWidths[i] = Math.max(columnWidths[i], data[i].length());
                                }
                            });

                            StringBuilder sb = new StringBuilder();
                            printRow(sb, headers, columnWidths);
                            printSeparator(sb, columnWidths);
                            printRow(sb, userData, columnWidths);
                            printSeparator(sb, columnWidths);
                            return sb.toString();
                        }));
        System.out.println(table);
    }

    @Override
    public void topUpSaldo(String username, BigDecimal amount) {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            // Create a native SQL query to update the balance
            Query query = em.createNativeQuery(
                    "UPDATE users SET balance = balance + :amount WHERE username = :username");
            query.setParameter("amount", amount);
            query.setParameter("username", username);

            // Execute the update query
            int updatedRows = query.executeUpdate();

            // Check if the update was successful
            if (updatedRows > 0) {
                User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult();

                Transaction topUpTrans = new Transaction();
                topUpTrans.setAmount(amount);
                topUpTrans.setReceiver(user);
                topUpTrans.setSender(user);
                topUpTrans.setTimeStamp(LocalDateTime.now());

                em.persist(topUpTrans);

                transaction.commit();
                System.out.println();
                System.out.printf("Balance updated successfully for user '%s'.%n", username);
                System.out.println();
            } else {
                transaction.rollback();
                System.err.println("Failed to update balance for user: " + username);
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            // Handle exception appropriately (e.g., log error, throw custom exception)
        }
    }

    @Override
    public void transferSaldo(String sender, String receiver, BigDecimal amount) {
        EntityTransaction transaction = null;
        try {
            // Begin transaction
            transaction = em.getTransaction();
            transaction.begin();

            // Query to check sender's balance
            Query balanceQuery = em.createNativeQuery(
                    "SELECT balance FROM users WHERE username = :username");
            balanceQuery.setParameter("username", sender);
            BigDecimal senderBalance = (BigDecimal) balanceQuery.getSingleResult();

            // Check if sender has sufficient balance for transfer
            if (senderBalance.compareTo(amount) < 0) {
                System.err.println("Insufficient balance for transfer from " + sender + " to " + receiver);
                return;
            }

            // Deduct the transferred amount from the sender's balance
            Query deductQuery = em.createNativeQuery(
                    "UPDATE users SET balance = balance - :amount WHERE username = :username");
            deductQuery.setParameter("amount", amount);
            deductQuery.setParameter("username", sender);
            int senderUpdatedRows = deductQuery.executeUpdate();

            // Check if the deduction was successful for the sender
            if (senderUpdatedRows <= 0) {
                transaction.rollback();
                System.err.println("Failed to deduct balance from sender: " + sender);
                return;
            }

            // Add the transferred amount to the receiver's balance
            Query addQuery = em.createNativeQuery(
                    "UPDATE users SET balance = balance + :amount WHERE username = :username");
            addQuery.setParameter("amount", amount);
            addQuery.setParameter("username", receiver);
            int receiverUpdatedRows = addQuery.executeUpdate();

            // Check if the addition was successful for the receiver
            if (receiverUpdatedRows <= 0) {
                transaction.rollback();
                System.err.println("Failed to add balance to receiver: " + receiver);
                return;
            }

            // Record the transfer transaction for both sender and receiver
            User senderQuery = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", sender)
                    .getSingleResult();
            User receiverQuery = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", receiver)
                    .getSingleResult();

            Transaction transactionUpdated = new Transaction();
            transactionUpdated.setAmount(amount);
            transactionUpdated.setSender(senderQuery);
            transactionUpdated.setReceiver(receiverQuery);
            transactionUpdated.setTimeStamp(LocalDateTime.now());
            em.persist(transactionUpdated);

            // Commit transaction
            transaction.commit();
            System.out.println();
            System.out.printf("Balance transfer successful from %s to %s%n", sender, receiver);
            System.out.println();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            // Handle exception appropriately (e.g., log error, throw custom exception)
        }
    }



    private static void printRow(StringBuilder sb, String[] rowData, int[] columnWidths) {
        sb.append("|");
        for (int i = 0; i < rowData.length; i++) {
            sb.append(" ");
            sb.append(String.format("%-" + columnWidths[i] + "s", rowData[i]));
            sb.append(" |");
        }
        sb.append("\n");
    }

    private static void printSeparator(StringBuilder sb, int[] columnWidths) {
        sb.append("+");
        for (int width : columnWidths) {
            sb.append("-".repeat(width + 2)).append("+");
        }
        sb.append("\n");
    }



}
