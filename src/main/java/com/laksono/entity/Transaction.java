package com.laksono.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    public Transaction(String id, User sender, User receiver, BigDecimal amount, LocalDateTime timeStamp) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timeStamp = timeStamp;
    }

    public Transaction() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return String.format("Transaction {\n" +
                        "  %-10s %s\n" +
                        "  %-10s %s\n" +
                        "  %-10s %s\n" +
                        "  %-10s %s\n" +
                        "  %-10s %s\n" +
                        "}",
                "id:", id,
                "sender:", sender != null ? sender.getUsername() : "null",
                "receiver:", receiver != null ? receiver.getUsername() : "null",
                "amount:", amount,
                "timeStamp:", timeStamp);
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
