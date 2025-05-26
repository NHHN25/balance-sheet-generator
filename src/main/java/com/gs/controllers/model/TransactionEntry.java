package com.gs.controllers.model;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionEntry {
    private int id;
    private int accountId;
    private BigDecimal amount;
    private LocalDate entryDate;
    private String description;

    public TransactionEntry(int id, int accountId, BigDecimal amount, LocalDate entryDate, String description) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.entryDate = entryDate;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
