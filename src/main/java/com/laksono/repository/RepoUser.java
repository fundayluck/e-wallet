package com.laksono.repository;

import com.laksono.entity.User;

import java.math.BigDecimal;
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
}
