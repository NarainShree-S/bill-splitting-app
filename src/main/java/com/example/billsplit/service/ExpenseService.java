package com.example.billsplit.service;

import com.example.billsplit.model.Expense;
import com.example.billsplit.model.Person;
import com.example.billsplit.repository.ExpenseRepository;
import com.example.billsplit.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PersonRepository personRepository;

    // Get all expenses
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Add an expense
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    // Delete an expense
    public void deleteExpense(int id) {
        expenseRepository.deleteById(id);
    }

    // Calculate how much each person owes
    public Map<String, Double> calculateSplit() {
        List<Expense> expenses = expenseRepository.findAll();
        List<Person> persons = personRepository.findAll();

        if (persons.isEmpty()) return new HashMap<>();

        // Total amount spent
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }

        // Equal share per person
        double share = total / persons.size();

        // How much each person paid
        Map<String, Double> paid = new HashMap<>();
        for (Person p : persons) {
            paid.put(p.getName(), 0.0);
        }
        for (Expense e : expenses) {
            String name = e.getPaidBy().getName();
            paid.put(name, paid.getOrDefault(name, 0.0) + e.getAmount());
        }

        // Balance = paid - share (positive means others owe them, negative means they owe)
        Map<String, Double> balance = new HashMap<>();
        for (Person p : persons) {
            double bal = paid.getOrDefault(p.getName(), 0.0) - share;
            balance.put(p.getName(), Math.round(bal * 100.0) / 100.0);
        }

        return balance;
    }
}
