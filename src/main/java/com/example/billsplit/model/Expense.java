package com.example.billsplit.model;

import jakarta.persistence.*;

@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "paid_by")
    private Person paidBy;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Person getPaidBy() { return paidBy; }
    public void setPaidBy(Person paidBy) { this.paidBy = paidBy; }
}
