package com.laksono.repository;

import com.laksono.entity.User;
import com.laksono.utils.EPaymentAccount;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface RepoUser {
    List<User> getAll();

    void insertUser(User user);

    void updateUser(User user);

    void deletedUser(int id);

    User getUserByUsername(String username);

    User loginUser(String username, String password);

    void getSaldobyUsername(String username);

    void topUpSaldo(String username, BigDecimal amount);

    void transferSaldo(String sender, String receiver, BigDecimal amount);
    void makePayment(String username, BigInteger recipientPayment, BigDecimal paymentAmount, EPaymentAccount choosePayment);
}
