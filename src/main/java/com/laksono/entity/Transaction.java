package com.laksono.entity;

import com.laksono.utils.ECategory;
import com.laksono.utils.EPaymentAccount;
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
    @Enumerated(EnumType.STRING)
    private ECategory category;
    @Enumerated(EnumType.STRING)
    private EPaymentAccount paymentAccount;
    private Number referencePaymentAccount;

    public Transaction(
            String id,
            User sender,
            User receiver,
            BigDecimal amount,
            LocalDateTime timeStamp,
            ECategory category,
            EPaymentAccount ePaymentAccount,
            Number referencePaymentAccount
    ) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.timeStamp = timeStamp;
        this.category = category;
        this.paymentAccount = ePaymentAccount;
        this.referencePaymentAccount = referencePaymentAccount;
    }

    public Transaction() {

    }
    public ECategory getCategory() {
        return category;
    }

    public void setCategory(ECategory category) {
        this.category = category;
    }
    public String getId() {
        return id;
    }

    public EPaymentAccount getPaymentAccount() {
        return paymentAccount;
    }

    public Number getReferencePaymentAccount() {
        return referencePaymentAccount;
    }

    public void setReferencePaymentAccount(Number referencePaymentAccount) {
        this.referencePaymentAccount = referencePaymentAccount;
    }

    public void setPaymentAccount(EPaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
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
