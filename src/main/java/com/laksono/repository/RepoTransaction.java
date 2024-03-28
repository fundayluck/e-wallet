package com.laksono.repository;

import com.laksono.entity.Transaction;
import com.laksono.entity.User;
import com.laksono.entity.UserDetails;

import java.util.List;

public interface RepoTransaction {
    List<User> getAllTransaction();
    void insertTransaction(Transaction transaction);
    List<Transaction> getTransactionById(String username);
}
