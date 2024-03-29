package com.laksono.repository.impl;

import com.laksono.entity.Transaction;
import com.laksono.entity.User;
import com.laksono.repository.RepoTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class RepoTransactionImpl implements RepoTransaction {
    private final EntityManager em;
    public RepoTransactionImpl(EntityManager em){
        this.em = em;
    }
    @Override
    public List<User> getAllTransaction() {
        return null;
    }

    @Override
    public void insertTransaction(Transaction transaction) {
        em.getTransaction().begin();
        em.createNativeQuery("INSERT INTO userDetails (id, amount, receiver_id, sender_id, category, timestamp) VALUES (?, ?, ?, ?, ?, ?)")
                .setParameter(1, transaction.getId())
                .setParameter(2, transaction.getAmount())
                .setParameter(3, transaction.getReceiver().getId())
                .setParameter(4, transaction.getSender().getId())
                .setParameter(5, transaction.getCategory().name())
                .setParameter(6, transaction.getTimeStamp())
                .executeUpdate();
        em.getTransaction().commit();
        System.out.println("Data inserted with native query!");
    }

    @Override
    public List<Transaction> getTransactionById(String username) {
        System.out.println("Transaksi untuk pengguna: " + username);

        Query preQuery = em.createNativeQuery("SELECT * FROM users WHERE username = ?", User.class);
        preQuery.setParameter(1, username);

        User user = (User) preQuery.getSingleResult();

        Query query = em.createNativeQuery("SELECT * FROM transactions WHERE sender_id = ?", Transaction.class);
        query.setParameter(1, user.getId());

        List<Transaction> transactions = query.getResultList();

        if (transactions.isEmpty()) {
            System.out.println("Tidak ada transaksi untuk pengguna: " + username);
//            return;
        }

        String[] headers = {"Pengirim", "Penerima", "Jumlah", "category", "payment", "PLN No. / PAM No. / no.telp", "Waktu"};

        List<List<String>> data = transactions.stream()
                .map(t ->{
                    String senderUsername = t.getSender() != null ? t.getSender().getUsername() : "N/A";
                    String receiverUsername = t.getReceiver() != null ? t.getReceiver().getUsername() : "N/A";
                    String amount = t.getAmount() != null ? t.getAmount().toString() : "N/A";
                    String category = t.getCategory() != null ? t.getCategory().name() : "N/A";
                    String paymentaccount = t.getPaymentAccount() != null ? t.getPaymentAccount().name() : "N/A";
                    String paymentTo = t.getReferencePaymentAccount() != null ? t.getReferencePaymentAccount().toString() : "N/A";
                    String timeStamp = t.getTimeStamp() != null ? t.getTimeStamp().toString() : "N/A";
                    return List.of(senderUsername, receiverUsername, amount, category, paymentaccount, paymentTo, timeStamp);
                })
                .toList();

        printTable(headers, data);
        return null;
    }

    private void printTable(String[] headers, List<List<String>> data) {
        int[] columnWidths = calculateColumnWidths(headers, data);

        printRow(headers, columnWidths);
        printSeparator(columnWidths);

        for (List<String> rowData : data) {
            printRow(rowData.toArray(new String[0]), columnWidths);
        }

        printSeparator(columnWidths);
    }

    private int[] calculateColumnWidths(String[] headers, List<List<String>> data) {
        int[] columnWidths = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }

        for (List<String> rowData : data) {
            for (int i = 0; i < rowData.size(); i++) {
                columnWidths[i] = Math.max(columnWidths[i], rowData.get(i).length());
            }
        }

        return columnWidths;
    }

    private void printRow(String[] rowData, int[] columnWidths) {
        StringBuilder rowBuilder = new StringBuilder("|");

        for (int i = 0; i < rowData.length; i++) {
            rowBuilder.append(String.format(" %-" + (columnWidths[i] + 1) + "s|", rowData[i]));
        }

        System.out.println(rowBuilder);
    }

    private void printSeparator(int[] columnWidths) {
        StringBuilder separatorBuilder = new StringBuilder("+");

        for (int width : columnWidths) {
            separatorBuilder.append("-".repeat(width + 2)).append("+");
        }

        System.out.println(separatorBuilder);
    }
}
