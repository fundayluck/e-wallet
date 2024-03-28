package com.laksono.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String pin;
    @Column(nullable = false)
    private BigDecimal balance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_details_id", referencedColumnName = "id", nullable = false)
    private UserDetails userDetails;

    public User(String username, String pin, BigDecimal balance,UserDetails userDetails) {
        this.username = username;
        this.pin = pin;
        if (balance == null) {
            this.balance = BigDecimal.ZERO;
        } else {
            this.balance = balance;
        }
        this.userDetails = userDetails;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", pin=" + pin +
                ", balance=" + balance +
                ", userDetails=" + userDetails +
                '}';
    }
}
