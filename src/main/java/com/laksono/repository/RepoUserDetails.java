package com.laksono.repository;

import com.laksono.entity.User;
import com.laksono.entity.UserDetails;

import java.util.List;

public interface RepoUserDetails {
    List<User> getAll();
    void insertUserDetail(UserDetails userDetails);
    void updateUserDetail(String username, UserDetails userDetails);
    void deletedUserDetail(int id);
    void getUserDetailByUsername(String username);

}
