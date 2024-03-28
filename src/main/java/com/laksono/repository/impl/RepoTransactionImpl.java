package com.laksono.repository.impl;

import com.laksono.entity.Transaction;
import com.laksono.entity.User;
import com.laksono.repository.RepoTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.math.BigDecimal;
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
        em.createNativeQuery("INSERT INTO userDetails (id, amount, receiver_id, sender_id, timestamp) VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, transaction.getId())
                .setParameter(2, transaction.getAmount())
                .setParameter(3, transaction.getReceiver().getId())
                .setParameter(4, transaction.getSender().getId())
                .setParameter(5, transaction.getTimeStamp())
                .executeUpdate();
        em.getTransaction().commit();
        System.out.println("Data inserted with native query!");
    }

    @Override
    public List<Transaction> getTransactionById(String username) {
        System.out.println(username);
        Query preQuery = em.createNativeQuery("SELECT * FROM users WHERE username = ?",User.class);
        preQuery.setParameter(1, username);

        User user = (User) preQuery.getSingleResult();

        System.out.println(user.getId() + "id");
        Query query = em.createNativeQuery("SELECT * FROM transactions WHERE sender_id = ?", Transaction.class);
        query.setParameter(1, user.getId());

        List<Transaction> transactions = query.getResultList();
        System.out.println(transactions.toString());
//        return transactions;
        return null;
    }
}
